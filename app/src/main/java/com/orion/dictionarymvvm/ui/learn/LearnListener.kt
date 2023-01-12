package com.orion.dictionarymvvm.ui.learn

interface LearnListener {

    fun onStarted()
    fun onSuccess(message1: String, message2: String)
    fun onSuccess0(message: String)
    fun onSuccess1(message: String)
    fun onSuccess2(message: String)
    fun onFailure(message: String)
}