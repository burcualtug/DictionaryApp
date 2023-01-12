package com.orion.dictionarymvvm.ui.dictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.R
import com.orion.dictionarymvvm.data.firebase.Words
import com.orion.dictionarymvvm.databinding.FragmentDictionaryBinding
import com.orion.dictionarymvvm.ui.addword.AddWordFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


var _binding: FragmentDictionaryBinding? = null

val binding get() = _binding!!
class DictionaryFragment : Fragment(), KodeinAware {


    override val kodein: Kodein by kodein()
    private val factory : DictionaryViewModelFactory by instance()


    private lateinit var viewModel: DictionaryViewModel

    val TAG: String = "TAG"

    var Wordlist = arrayListOf<Words>()

    private val firebaseAuth2: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val customAdapter = CustomAdapter(this, Wordlist,this)
    private lateinit var binding: FragmentDictionaryBinding

    var db = Firebase.firestore

    fun addWord(){
        var dialog = AddWordFragment()
        dialog.show(parentFragmentManager, "AddWordFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d("ADEM", "onCreate: ")
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ADEM", "onCreateView: ")


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dictionary, container, false)
        viewModel = ViewModelProviders.of(this, factory)[DictionaryViewModel::class.java]
        if (binding != null) {
            binding.viewmodel = viewModel
        }
        binding.jsonList.removeAllViewsInLayout()
        val layout = LinearLayoutManager(activity)
        binding.jsonList.layoutManager = layout
        binding.jsonList.adapter = customAdapter
        binding.jsonList.setHasFixedSize(true)
        Wordlist.clear()
        viewModel.wordData.observe(viewLifecycleOwner) { it ->
            Wordlist.addAll(it)
            customAdapter.notifyDataSetChanged()
        }

        customAdapter.setItemClickListener(object : CustomAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                viewModel.addFav(firebaseAuth2.currentUser?.email.toString(), customAdapter.getItem(position)!!.wordid,customAdapter.getItem(position)!!.english,customAdapter.getItem(position)!!.turkish)
            }

            override fun onItemClicked(item: Words) {
                TODO("Not yet implemented")
            }
        })

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(charSequence: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                customAdapter.filter.filter(s)
            }
        })
        binding.addWord.setOnClickListener(){
            addWord()
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ADEM", "onCreateViewCreated: ")
    }

    override fun onResume() {
        super.onResume()
    }

}