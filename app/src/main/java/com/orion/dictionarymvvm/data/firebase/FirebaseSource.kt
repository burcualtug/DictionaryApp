package com.orion.dictionarymvvm.data.firebase

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
//    val repository: DictionaryRepository = TODO()
    var Wordlist = arrayListOf<Words>()
 //   lateinit var activity: Activity
    /*lateinit var fragment: Fragment

    init {
        fragment = DictionaryFragment()
    }*/

    //val customAdapter = CustomAdapter(this, Wordlist,this)
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

    fun addWord(english: String, turkish: String) = Completable.create{ emitter ->
            var db = Firebase.firestore
            val word = hashMapOf(
                "wordid" to uuidGenerator(),
                "english" to english,
                "turkish" to turkish
            )
            db.collection("dictionary")
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


    /*fun getData() {
        /*binding.jsonList.removeAllViewsInLayout()
        //val layout = LinearLayoutManager(activity)
        binding.jsonList.adapter = customAdapter
        //binding.jsonList.layoutManager = layout
        binding.jsonList.setHasFixedSize(true)
        Wordlist.clear() */
        db.collection("dictionary")
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    Wordlist.add(
                        Words(
                            document.data["id"].toString(), //burada normalde sadece id yaziyodu
                            document.data["english"].toString(),
                            document.data["turkish"].toString()
                        )
                    )
                    Log.d(TAG, "${document.id} => ${document.data.get("english")}")
                }
            }.addOnFailureListener { Exception ->
                Log.e(TAG, "get failed with", Exception);
                //Toast.makeText(activity, "loading chamber", Toast.LENGTH_SHORT).show()
            }



    } */


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

    fun addFav(wordid: String, english: String, turkish: String) {
        val word = hashMapOf(
            "wordid" to wordid,
            "english" to english,
            "turkish" to turkish
        )

        var flag: Int = 0
        db.collection("fav")
            .whereEqualTo("english", english)
            .whereEqualTo("turkish", turkish)
            .get()
            .addOnCompleteListener { task1 ->
                for (document in task1.result) {
                    //Fetch from database as Map
                    if (document == task1.result) {
                    } else {
                        flag = 1
                        //Toast.makeText(activity, "Already in list", Toast.LENGTH_SHORT).show()
                    }
                }
                if (flag != 1) {
                    db.collection("fav")
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




}