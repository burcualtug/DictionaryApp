package com.orion.dictionarymvvm.ui.addword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.data.firebase.Words
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository
import com.orion.dictionarymvvm.ui.auth.AuthListener
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.util.*


class AddWordViewModel(
    private val repository: DictionaryRepository
) : ViewModel() {
    private val _wordDataAdded = MutableLiveData<Words>()
    val wordDataAdded : LiveData<Words>
        get() = _wordDataAdded

    var english: String? = null
    var turkish: String? = null

    var authListener: AuthListener? = null

    private val firebaseAuthAddWord: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun uuidGenerator(): String {
        return String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
    }

 fun addWord(){
     english?.let { Log.d("english", it) }
     turkish?.let { Log.d("turkish", it) }

     english?.let {
         turkish?.let { it1 ->
             repository.addWord(
                 firebaseAuthAddWord.currentUser?.email.toString(),uuidGenerator(),
                 it, it1
             )
         }
     }
     Log.d("TAG", "ADDED THE WORD")
 }
}