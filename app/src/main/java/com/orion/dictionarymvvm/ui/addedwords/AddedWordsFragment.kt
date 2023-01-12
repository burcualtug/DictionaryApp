package com.orion.dictionarymvvm.ui.addedwords

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.orion.dictionarymvvm.R
import com.orion.dictionarymvvm.data.firebase.Words
import com.orion.dictionarymvvm.databinding.FragmentAddWordBinding
import com.orion.dictionarymvvm.databinding.FragmentAddedWordsBinding
import com.orion.dictionarymvvm.databinding.FragmentFavBinding
import com.orion.dictionarymvvm.ui.dictionary.CustomAdapter
import com.orion.dictionarymvvm.ui.fav.CustomAdapterFav
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


var _bindingAdded: FragmentFavBinding? = null

val binding get() = _bindingAdded!!

class AddedWordsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()
    private val factory : AddedWordsViewModelFactory by instance()

    private lateinit var viewModel: AddedWordsViewModel

    var Wordlist = arrayListOf<Words>()

    private val firebaseAuthAdded: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val customAdapterAddedWords = CustomAdapterAddedWords(this, Wordlist,this)
    private lateinit var binding: FragmentAddedWordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_added_words, container, false)
        viewModel = ViewModelProviders.of(this, factory)[AddedWordsViewModel::class.java]
        if (binding != null) {
            binding.viewmodel = viewModel
        }
        binding.jsonListAdded.removeAllViewsInLayout()
        val layout = LinearLayoutManager(activity)
        binding.jsonListAdded.layoutManager = layout
        binding.jsonListAdded.adapter = customAdapterAddedWords
        binding.jsonListAdded.setHasFixedSize(true)
        Wordlist.clear()
        viewModel.wordDataAdded.observe(viewLifecycleOwner) { it ->
            Wordlist.addAll(it)
            customAdapterAddedWords.notifyDataSetChanged()
        }

        customAdapterAddedWords.setItemClickListener(object : CustomAdapterAddedWords.ItemClickListener {
            override fun onItemClick(position: Int) {
                }

            override fun onItemClicked(item: Words) {
                TODO("Not yet implemented")
            }
        })

        binding.searchViewAdded.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(charSequence: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                customAdapterAddedWords.filter.filter(s)
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

}