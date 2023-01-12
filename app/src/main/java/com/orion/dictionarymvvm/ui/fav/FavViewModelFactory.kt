package com.orion.dictionarymvvm.ui.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository

@Suppress("UNCHECKED_CAST")
class FavViewModelFactory(
    private val repository: DictionaryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavViewModel(repository) as T
    }
}