package com.orion.dictionarymvvm.data.repositories
import com.orion.dictionarymvvm.data.firebase.FirebaseSource
import com.orion.dictionarymvvm.data.firebase.Words

class DictionaryRepository (
    private val firebase: FirebaseSource
){

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    fun addWord(mail: String, wordid: String, english: String, turkish: String) = firebase.addWord(mail,wordid,english,turkish)

    suspend fun getData() : List<Words> = firebase.getData()

    suspend fun getDataFav(mail: String) : List<Words> = firebase.getDataFav(mail)

    suspend fun getDataAdded(mail: String) : List<Words> = firebase.getDataAdded(mail)

    fun addFav(mail: String, wordid: String, english: String, turkish: String) = firebase.addFav(mail, wordid, english, turkish)

    fun setLevel(mail: String, wordid: String, level: String) = firebase.setLevel(mail,wordid,level)

    suspend fun getLevel(mail: String, level: String): Int = firebase.getLevel(mail, level)

    suspend fun getLevelID0() : String = firebase.getLevelID0()

    suspend fun getLevelID1(mail: String, size: Int) : String = firebase.getLevelID1(mail,size)

    suspend fun getLevelID2(mail: String, size: Int) : String = firebase.getLevelID2(mail,size)
}