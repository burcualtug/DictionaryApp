package com.orion.dictionarymvvm.data.firebase

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository
import com.orion.dictionarymvvm.databinding.FragmentDictionaryBinding
import com.orion.dictionarymvvm.ui.dictionary.CustomAdapter
//import com.orion.dictionarymvvm.ui.dictionary.binding
import io.reactivex.Completable
import java.math.BigInteger
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

var _binding: FragmentDictionaryBinding? = null

val binding get() = _binding!!
class FirebaseSource {

    val TAG: String = "TAG"

    private lateinit var binding: FragmentDictionaryBinding
    //val repository: DictionaryRepository = TODO()
    var Wordlist = arrayListOf<Words>()
    var Favlist = arrayListOf<Words>()

    var db = Firebase.firestore
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }


    fun register(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    emitter.onComplete()
                }
                else
                    emitter.onError(it.exception!!)
            }
        }
        var db = Firebase.firestore
        val user = hashMapOf(
            "email" to email,
            "password" to password
        )
        db.collection("users2").document(email)
            .collection("userinfos").add(user)

    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    @SuppressLint("DefaultLocale")
    fun uuidGenerator(): String {
        return String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))
    }


    suspend fun getLevel(mail: String, level: String)=
        suspendCoroutine<Int> {
            var count = 0
            db.collection("users2")
                .document(mail)
                .collection("learning")
                .whereEqualTo("level",level)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        count = result!!.size()
                        it.resume(count)
                        Log.d("Firestore", "Collection size: $count")
                    } else {
                        Log.e("Firestore", "Error getting documents: ${task.exception}")
                    }
                }
        }

    suspend fun getLevelID0()=
        suspendCoroutine <String> {
                val rnd2 = (0 until 2000).random()
                db.collection("dictionary")
                    .get()
                    .addOnCompleteListener { task1 ->
                        if (task1.isSuccessful) {
                            val oneWordId = task1.result.documents[rnd2].get("wordid").toString()
                            it.resume(oneWordId)
                        }
                    }
        }

    suspend fun getLevelID1(mail: String, size: Int)=

        suspendCoroutine <String> {
            if (size>0){
                val rnd2 = (0 until size).random()
                db.collection("users2")
                    .document(mail)
                    .collection("learning")
                    .whereEqualTo("level","1")
                    .get()
                    .addOnCompleteListener { task1 ->
                        if (task1.isSuccessful) {
                            val oneWordId = task1.result.documents[rnd2].get("wordid").toString()
                            it.resume(oneWordId)
                        }
                    }
            }
            else if(size==0){
                it.resume("0131820956425567970354070760026269803851")
            }
        }

    suspend fun getLevelID2(mail: String, size: Int)=
        suspendCoroutine <String> {
            if (size>0){
                val rnd2 = (0 until size).random()
                db.collection("users2")
                    .document(mail)
                    .collection("learning")
                    .whereEqualTo("level","2")
                    .get()
                    .addOnCompleteListener { task1 ->
                        if (task1.isSuccessful) {
                            val oneWordId = task1.result.documents[rnd2].get("wordid").toString()
                            it.resume(oneWordId)

                        }
                    }
            }
            else if(size==0){
                it.resume("0338996114965402549244662037962371997874")
            }
        }
    suspend fun getData() =
        suspendCoroutine<List<Words>> {
            db.collection("dictionary")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val word = Words(
                            document.data["wordid"].toString(),
                            document.data["english"].toString(),
                            document.data["turkish"].toString()
                        )
                        Wordlist.add(word)
                    }
                    it.resume(Wordlist)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                    it.resume(Wordlist)
                }
        }

    suspend fun getDataFav(mail:String) =
        suspendCoroutine<List<Words>> {
            db.collection("users2").document(mail).collection("favwords")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val word = Words(
                            document.data["wordid"].toString(),
                            document.data["english"].toString(),
                            document.data["turkish"].toString()
                        )
                        Favlist.add(word)
                    }
                    it.resume(Favlist)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                    it.resume(Favlist)
                }
        }

    fun addFav(mail: String, wordid: String, english: String, turkish: String){
        val word = hashMapOf(
            "wordid" to wordid,
            "english" to english,
            "turkish" to turkish
        )

        var flag: Int = 0
        db.collection("users2").document(mail).collection("favwords")
            .whereEqualTo("english", english)
            .whereEqualTo("turkish", turkish)
            .get()
            .addOnCompleteListener { task1 ->
                for (document in task1.result) {
                    if (document == task1.result) {
                    } else {
                        flag = 1
                        //Toast.makeText(activity, "Already in list", Toast.LENGTH_SHORT).show()
                    }
                }
                if (flag != 1) {
                    db.collection("users2").document(mail).collection("favwords")
                        .add(word)
                        .addOnSuccessListener {

                            //Toast.makeText(activity, "Added to Fav!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Error adding document", e)
                            //Toast.makeText(activity, "Error adding document", Toast.LENGTH_SHORT).show()
                        }
                    //Toast.makeText(activity, "Added to Fav!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun addWord(mail:String, wordid: String, english: String, turkish: String) = Completable.create{ emitter ->
        Log.d("TAG", "ADDED THE WORD2")
        var db = Firebase.firestore
        val word = hashMapOf(
            "wordid" to wordid,
            "english" to english,
            "turkish" to turkish
        )
        db.collection("users2").document(mail).collection("addedwords")
            .add(word)
            .addOnSuccessListener { documentReference ->
                //Toast.makeText(this, "New Word Added!", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                //Toast.makeText(activity, "CANNOT ADDED", Toast.LENGTH_SHORT).show()
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun setLevel(mail:String, wordid: String, level: String){
        Log.d("TAG","Level setting is successful")
        var db = Firebase.firestore
        val wordLevel = hashMapOf(
            "wordid" to wordid,
            "level" to level
        )
        db.collection("users2").document(mail).collection("learning")
            .add(wordLevel)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }



}