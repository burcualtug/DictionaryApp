package com.orion.dictionarymvvm.ui.home

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orion.dictionarymvvm.data.repositories.UserRepository
import com.orion.dictionarymvvm.utils.startLoginActivity

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var status = MutableLiveData<Boolean?>()
    val user by lazy {
        repository.currentUser()
    }

    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }

    fun viewToast(){
//In your network successfull response
        status.value = true
    }



}