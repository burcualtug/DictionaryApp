package com.orion.dictionarymvvm.ui.addedwords

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.orion.dictionarymvvm.data.firebase.Words
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository
import com.orion.dictionarymvvm.ui.auth.AuthListener
import kotlinx.coroutines.launch

class AddedWordsViewModel (
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _wordDataAdded  = MutableLiveData<List<Words>>()
    val wordDataAdded : LiveData<List<Words>>
        get() = _wordDataAdded

    var authListener: AuthListener? = null

    private val firebaseAuthAdded: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    init {
        getDataAdded(firebaseAuthAdded.currentUser?.email.toString())
    }
    fun getDataAdded(mail:String) {
        viewModelScope.launch {
            _wordDataAdded.value = repository.getDataAdded(mail)
        }

    }
}