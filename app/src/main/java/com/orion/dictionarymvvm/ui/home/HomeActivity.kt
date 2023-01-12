package com.orion.dictionarymvvm.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.R
import com.orion.dictionarymvvm.databinding.ActivityHomeBinding
import com.orion.dictionarymvvm.ui.auth.AuthListener
import org.json.JSONArray
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.util.*

class HomeActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory : HomeViewModelFactory by instance()

    private lateinit var viewModel: HomeViewModel

    var db = Firebase.firestore

    var authListener: AuthListener? = null

    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        binding.viewmodel = viewModel

        //readJson()

        replaceFragment(com.orion.dictionarymvvm.ui.dictionary.DictionaryFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.dictionary -> replaceFragment(com.orion.dictionarymvvm.ui.dictionary.DictionaryFragment())
                R.id.fav -> replaceFragment(com.orion.dictionarymvvm.ui.fav.FavFragment())
                R.id.learn -> replaceFragment(com.orion.dictionarymvvm.ui.learn.LearnFragment())
                R.id.addWord -> replaceFragment(com.orion.dictionarymvvm.ui.addedwords.AddedWordsFragment())
                else -> {

                }
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("DefaultLocale")
    fun uuidGenerator(): String {
        val lUUID =
            String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))

        //val intUUID = lUUID.dropLast(32).toInt()
        return lUUID.toString()
    }

   /* fun readJson(){
        var json : String?=null
        try{
            val inputStream: InputStream? =assets?.open("dict.json")
            json=inputStream?.bufferedReader()?.readText()

            var jsonarr= JSONArray(json)
            for(i in 0 until jsonarr.length()){
                var jsonobj=jsonarr.getJSONObject(i)
                fillData(uuidGenerator(),jsonobj.getString("word"),jsonobj.getString("tr"))
            }


        }catch (e: IOException){

        }
    }
    fun fillData(wordid: String, english: String, turkish: String) {
        val word = hashMapOf(
            "wordid" to wordid,
            "english" to english,
            "turkish" to turkish
        )
        db.collection("dictionary").add(word)
    } */



}