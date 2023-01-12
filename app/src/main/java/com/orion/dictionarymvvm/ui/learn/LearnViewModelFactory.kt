package com.orion.dictionarymvvm.ui.learn


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository
import com.orion.dictionarymvvm.data.repositories.UserRepository
import com.orion.dictionarymvvm.ui.dictionary.DictionaryViewModel

@Suppress("UNCHECKED_CAST")
class LearnViewModelFactory(
    private val repository: DictionaryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LearnViewModel(repository) as T
    }
}