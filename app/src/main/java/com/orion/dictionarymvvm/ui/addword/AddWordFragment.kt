package com.orion.dictionarymvvm.ui.addword

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.R
import com.orion.dictionarymvvm.data.firebase.Words
import com.orion.dictionarymvvm.databinding.FragmentAddWordBinding
import com.orion.dictionarymvvm.databinding.FragmentDictionaryBinding
import com.orion.dictionarymvvm.ui.dictionary.CustomAdapter
import com.orion.dictionarymvvm.ui.dictionary.DictionaryViewModel
import com.orion.dictionarymvvm.ui.dictionary.DictionaryViewModelFactory
import com.orion.dictionarymvvm.ui.fav.FavViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.math.BigInteger
import java.util.*
import org.kodein.di.android.x.kodein

var _bindingAddWord: FragmentAddWordBinding? = null

val binding get() = _bindingAddWord!!

class AddWordFragment() : DialogFragment(),KodeinAware {

    override val kodein: Kodein by kodein()
    private val factory: AddWordViewModelFactory by instance()


    private val firebaseAuthAddWord: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    var db = Firebase.firestore

    private lateinit var viewModel: AddWordViewModel

    private lateinit var binding: FragmentAddWordBinding

    val english: String = ""
    val turkish: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_word, container, false)
        viewModel = ViewModelProviders.of(this, factory)[AddWordViewModel::class.java]
        if (binding != null) {
            binding.viewmodel = viewModel
        }

        fun uuidGenerator(): String {
            return String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
        }

        binding.cancel.setOnClickListener { Log.d("TAG", "dismissed")
            dismiss() }
        viewModel.addWord(firebaseAuthAddWord.currentUser?.email.toString(),uuidGenerator(),binding.addEng.toString(), binding.addTr.toString())

        return binding.root
    }




}