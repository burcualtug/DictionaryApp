package com.orion.dictionarymvvm.ui.learn

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orion.dictionarymvvm.R
import com.orion.dictionarymvvm.data.firebase.Words
import com.orion.dictionarymvvm.databinding.FragmentFavBinding
import com.orion.dictionarymvvm.databinding.FragmentLearnBinding
import com.orion.dictionarymvvm.ui.auth.AuthListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_learn.*
import kotlinx.coroutines.tasks.await
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.resume


var _bindingLearn: FragmentLearnBinding? = null

val binding get() = _bindingLearn!!
class LearnFragment : Fragment(), KodeinAware, LearnListener {


    private var wordidTemp:String=""
    override val kodein: Kodein by kodein()
    private val factory : LearnViewModelFactory by instance()
    private lateinit var binding: FragmentLearnBinding
    private lateinit var viewModel: LearnViewModel
    private val firebaseAuthLearn: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    var isFront = true
    var db = Firebase.firestore

    var mapZero: HashMap<String, String> = hashMapOf("" to "", "" to "", "" to "")
    var mapOne: HashMap<String, String> = hashMapOf("" to "", "" to "", "" to "")
    var mapTwo: HashMap<String, String> = hashMapOf("" to "", "" to "", "" to "")

    var localCount : Int = 0
    var publicCount1 : Int = 0
    var publicCount2 : Int = 0
    var publicID0 : String = ""
    var publicID1 : String = ""
    var publicID2 : String = ""

    var Wordlist = arrayListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_learn, container, false)
        viewModel = ViewModelProviders.of(this, factory)[LearnViewModel::class.java]
        if (binding != null) {
            binding.viewmodel = viewModel
        }

        random2()
        viewModel.learnListener = this
        var scale = requireContext().applicationContext.resources.displayMetrics.density
        binding.front.cameraDistance = 8000 * scale
        binding.back.cameraDistance = 8000 * scale


        var front_animation = AnimatorInflater.loadAnimator(
            requireContext().applicationContext,
            R.animator.front_animator
        ) as AnimatorSet
        var back_animation = AnimatorInflater.loadAnimator(
            requireContext().applicationContext,
            R.animator.back_animator
        ) as AnimatorSet

        binding.show.setOnClickListener {
            makeVisible()
            front_animation.setTarget(binding.front);
            back_animation.setTarget(binding.back);
            front_animation.start()
            back_animation.start()
            isFront = false
        }
        binding.skip.setOnClickListener {
            uuidGenerator()
            viewModel.getLevelID0()
            viewModel.getLevelID1()
            viewModel.getLevelID2()
            random2()
            front_animation.setTarget(binding.back)
            back_animation.setTarget(binding.front)
            back_animation.start()
            front_animation.start()
            isFront = true
        }

        binding.zero.setOnClickListener {
            //Log.d("getc onclick " ," llocal count  $localCount")
            viewModel.setLevel(firebaseAuthLearn.currentUser?.email.toString(),wordidTemp,"0")
        }
        binding.one.setOnClickListener {
            viewModel.setLevel(firebaseAuthLearn.currentUser?.email.toString(),wordidTemp,"1")
        }
        binding.two.setOnClickListener {
            viewModel.setLevel(firebaseAuthLearn.currentUser?.email.toString(),wordidTemp,"2")
        }

        return binding.root
    }


    @SuppressLint("DefaultLocale")
    fun uuidGenerator(): Int {
        val lUUID =
            String.format("%040d", BigInteger(UUID.randomUUID().toString().replace("-", ""), 16))

        val intUUID = lUUID.dropLast(32).toInt()
        return intUUID
    }
    fun makeVisible() {
        binding.meaning.visibility = View.VISIBLE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _bindingLearn = null
    }
    fun random2() {
        binding.meaning.visibility = View.GONE

        val weightedCollection = mapOf( publicID0 to 3, publicID1 to 2, publicID2 to 1)
        val picked = pickRandomElement(weightedCollection)

        db.collection("dictionary")
            .whereEqualTo("wordid", picked )
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    binding.question2.text = document.data["english"].toString()
                    binding.question.text = document.data["english"].toString()
                    binding.meaning.text = document.data["turkish"].toString()
                    wordidTemp = document.data["wordid"].toString()
                }
            }
    }

    fun <T> pickRandomElement(map: Map<T, Int>): T {
        val sum = map.values.sum()
        val rnd = Random().nextInt(sum)

        var sumSoFar = 0
        for ((element, weight) in map) {
            sumSoFar += weight
            if (rnd < sumSoFar) {
                return element
            }
        }
        throw IllegalArgumentException("This should never be reached")
    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
        viewModel.levelData.observe(viewLifecycleOwner) {
            localCount= it
            Log.d("getCountLog in observe",it.toString())
        }
    }

    override fun onSuccess(message1: String, message2: String) {
        Log.d("onSuccess",message1)
        Log.d("onSuccess",message2)
        publicCount1=message1.toInt()
        publicCount2=message2.toInt()

        Log.d("onSuccessP",publicCount1.toString())
        Log.d("onSuccessP",publicCount2.toString())
    }

    override fun onSuccess0(id0: String) {
        publicID0=id0
        Log.d("onSuccess0",publicID0)
    }
    override fun onSuccess1(id1: String) {
        publicID1=id1
        Log.d("onSuccess1",publicID1)
    }

    override fun onSuccess2(id2: String) {
        publicID2=id2
        Log.d("onSuccess2",publicID2)

    }

    override fun onFailure(message: String) {
        TODO()
    }



}