package com.github.mjaremczuk.politicalpreparedness

import android.app.Application
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDao
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDatabase
import com.github.mjaremczuk.politicalpreparedness.database.LocalDataSource
import com.github.mjaremczuk.politicalpreparedness.election.ElectionsViewModel
import com.github.mjaremczuk.politicalpreparedness.election.VoterInfoViewModel
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApi
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApiService
import com.github.mjaremczuk.politicalpreparedness.network.NetworkDataSource
import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionRepository
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class PoliticalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {
            viewModel { (electionId: Int, division: Division) ->
                VoterInfoViewModel(get(), electionId, division)
            }
            viewModel { ElectionsViewModel(get()) }

            single { ElectionDatabase.getInstance(this@PoliticalApplication).electionDao as ElectionDao }
            single { CivicsApi.create() as CivicsApiService }
            single(qualifier = named("local")) { LocalDataSource(get(), Dispatchers.IO) as ElectionDataSource }
            single(qualifier = named("remote")) { NetworkDataSource(get(), Dispatchers.IO) as ElectionDataSource }
            single {
                ElectionRepository(
                        get<ElectionDataSource>(qualifier = named("local")),
                        get<ElectionDataSource>(qualifier = named("remote")),
                        Dispatchers.IO,
                ) as ElectionsRepository
            }
        }
        startKoin {
            androidContext(this@PoliticalApplication)
            modules(listOf(module))
        }
    }
}