package com.orion.dictionarymvvm.ui.dictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.R
import com.orion.dictionarymvvm.databinding.FragmentAddWordBinding
import org.kodein.di.KodeinAware

import org.kodein.di.KodeinContext
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.math.BigInteger
import java.util.*
import org.kodein.di.generic.kcontext
import org.kodein.di.android.x.kodein

var _binding2: FragmentAddWordBinding? = null
class AddWordFragment() : DialogFragment(),KodeinAware {




  //  private val viewModelFactory: ViewModelProvider.Factory by instance()

    override val kodein by kodein()
    private val factory : DictionaryViewModelFactory by instance()


    private lateinit var viewModel: DictionaryViewModel

    private lateinit var binding: FragmentAddWordBinding

    val english : String = ""
    val turkish : String = ""

    fun add(){
            addWord(
                uuidGenerator(),
                english,
                turkish
            )
    }

     fun uuidGenerator(): String {
        return String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
    }

    fun addWord(wordid: String, english: String, turkish: String) {
        var db = Firebase.firestore
        val word = hashMapOf(
            "wordid" to wordid,
            "english" to english,
            "turkish" to turkish
        )
        db.collection("dictionary")
            .add(word)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(activity, "New Word Added!", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "CANNOT ADDED", Toast.LENGTH_SHORT).show()
                Log.w("TAG", "Error adding document", e)
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentAddWordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val binding: FragmentAddWordBinding? = activity?.let { DataBindingUtil.setContentView(it, R.layout.fragment_add_word) }
        viewModel = activity?.let { ViewModelProviders.of(it, factory)[DictionaryViewModel::class.java] }!!
        if (binding != null) {
            binding.viewmodel = viewModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding2 = FragmentAddWordBinding.inflate(inflater,container,false)
        return binding.root
    }

}