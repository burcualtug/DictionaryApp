package com.orion.dictionarymvvm.ui.auth

interface AuthListener {
    fun onStarted()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}