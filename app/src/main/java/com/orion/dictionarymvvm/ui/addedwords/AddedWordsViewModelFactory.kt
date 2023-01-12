package com.orion.dictionarymvvm.ui.addedwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository

@Suppress("UNCHECKED_CAST")
class AddedWordsViewModelFactory(
    private val repository: DictionaryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddedWordsViewModel(repository) as T
    }
}