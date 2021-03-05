package com.github.mjaremczuk.politicalpreparedness

import android.app.Application
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDao
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDatabase
import com.github.mjaremczuk.politicalpreparedness.election.ElectionsViewModel
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApi
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApiService
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PoliticalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {
            viewModel {
                ElectionsViewModel(get())
            }

            single { ElectionDatabase.getInstance(this@PoliticalApplication).electionDao as ElectionDao }
            single { CivicsApi.create() as CivicsApiService }
            single {
                ElectionRepository(
                        get(),
                        get(),
                        Dispatchers.Default,
                        Dispatchers.IO,
                ) as ElectionDataSource
            }
        }
        startKoin {
            androidContext(this@PoliticalApplication)
            modules(listOf(module))
        }
    }
}