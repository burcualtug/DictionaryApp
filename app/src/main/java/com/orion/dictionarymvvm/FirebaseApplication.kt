package com.orion.dictionarymvvm


import android.app.Application
import com.orion.dictionarymvvm.data.firebase.FirebaseSource
import com.orion.dictionarymvvm.data.repositories.DictionaryRepository
import com.orion.dictionarymvvm.data.repositories.UserRepository
import com.orion.dictionarymvvm.ui.addword.AddWordViewModel
import com.orion.dictionarymvvm.ui.addword.AddWordViewModelFactory
import com.orion.dictionarymvvm.ui.auth.AuthViewModelFactory
import com.orion.dictionarymvvm.ui.addword.AddWordFragment
import com.orion.dictionarymvvm.ui.dictionary.DictionaryViewModelFactory
import com.orion.dictionarymvvm.ui.fav.FavViewModelFactory
import com.orion.dictionarymvvm.ui.home.HomeViewModelFactory
import com.orion.dictionarymvvm.ui.learn.LearnViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class FirebaseApplication : Application(), KodeinAware{

    override val kodein = Kodein.lazy {
        import(androidXModule(this@FirebaseApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { DictionaryRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from provider { DictionaryViewModelFactory(instance()) }
        bind() from provider { FavViewModelFactory(instance())}
        bind() from provider { AddWordViewModelFactory(instance()) }
        bind() from provider { LearnViewModelFactory(instance()) }

    }
}