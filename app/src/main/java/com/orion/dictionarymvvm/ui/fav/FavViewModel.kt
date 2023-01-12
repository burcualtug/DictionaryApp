package com.orion.dictionarymvvm.ui.fav

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


class FavViewModel(
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _wordDataFav  = MutableLiveData<List<Words>>()
    val wordDataFav : LiveData<List<Words>>
        get() = _wordDataFav

    var db = Firebase.firestore

    val TAG: String = "TAG"

    var Wordlist = arrayListOf<Words>()

    var english: String? = null
    var turkish: String? = null

    var authListener: AuthListener? = null

    private val firebaseAuthFav2: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val user by lazy {
        repository.currentUser()
    }

    init {
        getDataFav(firebaseAuthFav2.currentUser?.email.toString())
    }
    fun getDataFav(mail:String) {
        viewModelScope.launch {
            _wordDataFav.value = repository.getDataFav(mail)
        }

    }
}