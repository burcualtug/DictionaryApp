package com.orion.dictionarymvvm.ui.dictionary

import android.annotation.SuppressLint
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
import com.orion.dictionarymvvm.ui.auth.AuthListener
import com.orion.dictionarymvvm.utils.startLoginActivity
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.util.*

class DictionaryViewModel(
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _wordData  = MutableLiveData<List<Words>>()
    val wordData : LiveData<List<Words>>
        get() = _wordData

    var db = Firebase.firestore

    val TAG: String = "TAG"

    var Wordlist = arrayListOf<Words>()

    //val customAdapter = CustomAdapter(this, Wordlist,this)

    var english: String? = null
    var turkish: String? = null

    var authListener: AuthListener? = null

    val user by lazy {
        repository.currentUser()
    }
    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }


    fun cancel(){
        //dismiss()
    }

    fun uuidGenerator(): String {
        return String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
    }

    init {
        getData()
    }
    fun getData() {
        viewModelScope.launch {
            Log.d("ADEM", "getData: ")
            _wordData.value = repository.getData()
        }

    }


    fun addFav(mail: String, wordid: String, english: String, turkish: String) {
        repository.addFav(mail,wordid,english,turkish)
    }
    fun addWord() {
        if(english.isNullOrEmpty() || turkish.isNullOrEmpty()){
            authListener?.onFailure("Please fill the blanks")
            return
        }
        //authentication started
        authListener?.onStarted()

        //val addResult =
        repository.addWord(english!!, turkish!!)

        authListener?.onSuccess("Succesfully added!")
        /*addResult.addOnCompleteListener(getActivity(this)) {task->
            if (task.isSuccessful) {
                authListener?.onSuccess()
            } else {
                authListener?.onFailure(task.exception!!.message.toString())
            }
        }*/
    }



}