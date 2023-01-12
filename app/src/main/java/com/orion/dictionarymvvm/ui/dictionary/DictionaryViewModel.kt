package com.orion.dictionarymvvm.ui.dictionary

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.data.firebase.Words
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository
import com.orion.dictionarymvvm.ui.addword.AddWordFragment
import com.orion.dictionarymvvm.ui.auth.AuthListener
import com.orion.dictionarymvvm.utils.startLoginActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.util.*

class DictionaryViewModel(
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _wordData  = MutableLiveData<List<Words>>()
    val wordData : LiveData<List<Words>>
        get() = _wordData

    var authListener: AuthListener? = null
    //val connectionManager : ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()
    var db = Firebase.firestore

    val TAG: String = "TAG"

    var Wordlist = arrayListOf<Words>()
    var english: String? = null
    var turkish: String? = null

    val user by lazy {
        repository.currentUser()
    }
    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }


    fun uuidGenerator(): String {
        return String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
    }

    init {
        getData()
    }
    fun getData() {
        viewModelScope.launch {
            _wordData.value = repository.getData()
        }
    }

    fun addFav(mail: String, wordid: String, english: String, turkish: String) {
        repository.addFav(mail,wordid,english,turkish)
    }



}