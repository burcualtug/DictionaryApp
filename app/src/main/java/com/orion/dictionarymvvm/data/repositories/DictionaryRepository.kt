package com.orion.dictionarymvvm.data.repositories
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.data.firebase.FirebaseSource
import com.orion.dictionarymvvm.data.firebase.Words

class DictionaryRepository (
    private val firebase: FirebaseSource
){
    /*var wordList = arrayListOf<Words>()
    init {
        getData()
    }
    fun getWords() = wordList
    var db = Firebase.firestore

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    fun addWord( english: String, turkish: String) = firebase.addWord(english,turkish)

    fun getData() = firebase.getData() */

    var wordList = arrayListOf<Words>()

    fun getWords() = wordList
    var db = Firebase.firestore

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    fun addWord( english: String, turkish: String) = firebase.addWord(english,turkish)

    suspend fun getData() : List<Words> = firebase.getData()

    fun addFav(mail: String, wordid: String, english: String, turkish: String) = firebase.addFav(mail, wordid, english, turkish)

}