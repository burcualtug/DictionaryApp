package com.orion.dictionarymvvm.ui.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository
import com.orion.dictionarymvvm.data.repositories.UserRepository
import com.orion.dictionarymvvm.ui.dictionary.DictionaryViewModel

@Suppress("UNCHECKED_CAST")
class DictionaryViewModelFactory(
    private val repository: DictionaryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DictionaryViewModel(repository) as T
    }
}