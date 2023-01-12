package com.orion.dictionarymvvm.ui.fav

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
import com.orion.dictionarymvvm.databinding.FragmentFavBinding
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

var _bindingFav: FragmentFavBinding? = null

val binding get() = _bindingFav!!
class FavFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()
    private val factory : FavViewModelFactory by instance()

    private lateinit var viewModel: FavViewModel

    var Favlist = arrayListOf<Words>()

    private val firebaseAuthFav: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val customAdapterFav = CustomAdapterFav(this, Favlist,this)
    private lateinit var binding: FragmentFavBinding

    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fav, container, false)
        viewModel = ViewModelProviders.of(this, factory)[FavViewModel::class.java]
        if (binding != null) {
            binding.viewmodel = viewModel
        }

        viewModel.getDataFav(firebaseAuthFav.currentUser?.email.toString())

        binding.jsonListFav.removeAllViewsInLayout()
        val layout = LinearLayoutManager(activity)
        binding.jsonListFav.layoutManager = layout
        binding.jsonListFav.adapter = customAdapterFav
        binding.jsonListFav.setHasFixedSize(true)
        Favlist.clear()
        viewModel.wordDataFav.observe(viewLifecycleOwner) { it ->
            Favlist.addAll(it)
            customAdapterFav.notifyDataSetChanged()
        }

        customAdapterFav.setItemClickListener(object : CustomAdapterFav.ItemClickListener {
            override fun onItemClick(position: Int) {
                //viewModel.addFav(firebaseAuthFav.currentUser?.email.toString(), customAdapter.getItem(position)!!.wordid,customAdapter.getItem(position)!!.english,customAdapter.getItem(position)!!.turkish)
            }

            override fun onItemClicked(item: Words) {
                TODO("Not yet implemented")
            }
        })

        binding.searchViewFav.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(charSequence: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                customAdapterFav.filter.filter(s)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}