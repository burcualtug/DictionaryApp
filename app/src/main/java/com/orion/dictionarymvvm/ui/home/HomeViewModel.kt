package com.orion.dictionarymvvm.ui.home

import android.view.View
import androidx.lifecycle.ViewModel
import com.orion.dictionarymvvm.data.repositories.UserRepository
import com.orion.dictionarymvvm.utils.startLoginActivity

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }
}