package com.orion.dictionarymvvm.ui.learn

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class LearnViewModel(
    private val repository: DictionaryRepository
) : ViewModel() {
    var db = Firebase.firestore
    val TAG: String = "TAG"
    var Wordlist = arrayListOf<Words>()
    var levelPublic: String = ""

    private val _levelData  = MutableLiveData<Int>()
    val levelData : LiveData<Int>
        get() = _levelData

    private val _levelData2  = MutableLiveData<Int>()
    val levelData2 : LiveData<Int>
        get() = _levelData2

    private val _idData1  = MutableLiveData<String>()
    val idData1 : LiveData<String>
        get() = _idData1

    private val _idData2  = MutableLiveData<String>()
    val idData2 : LiveData<String>
        get() = _idData2

    private val firebaseAuthLearn: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val user by lazy {
        repository.currentUser()
    }

    var learnListener: LearnListener? = null

    fun setLevel(mail: String, wordid: String, level: String){
        repository.setLevel(mail,wordid,level)
        levelPublic=level
    }


    init {
        getLevel()
    }
    fun getLevel() {
        viewModelScope.launch {
            _levelData.value = repository.getLevel(firebaseAuthLearn.currentUser?.email.toString(), "1")

            _levelData2.value = repository.getLevel(firebaseAuthLearn.currentUser?.email.toString(), "2")

            learnListener?.onSuccess(_levelData.value.toString(),_levelData2.value.toString())

            getLevelID0()
            getLevelID1()
            getLevelID2()
        }
    }

    fun getLevelID0(){
        viewModelScope.launch {
            _idData1.value = repository.getLevelID0()
            learnListener?.onSuccess0(_idData1.value.toString())
        }
    }
    fun getLevelID1(){
        viewModelScope.launch {
            val ints = _levelData.value?.toInt()
            Log.d("ints",ints.toString())
            _idData1.value = repository.getLevelID1(firebaseAuthLearn.currentUser?.email.toString(),_levelData.value!!.toInt())
            learnListener?.onSuccess1(_idData1.value.toString())
        }
    }

    fun getLevelID2(){
        viewModelScope.launch {
            _idData1.value = repository.getLevelID2(firebaseAuthLearn.currentUser?.email.toString(),_levelData2.value!!.toInt())
            learnListener?.onSuccess2(_idData1.value.toString())
        }
    }


}