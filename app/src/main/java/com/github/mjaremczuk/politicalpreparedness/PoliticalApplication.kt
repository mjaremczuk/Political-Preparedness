package com.github.mjaremczuk.politicalpreparedness

import android.app.Application
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDao
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDatabase
import com.github.mjaremczuk.politicalpreparedness.database.LocalDataSource
import com.github.mjaremczuk.politicalpreparedness.election.ElectionsViewModel
import com.github.mjaremczuk.politicalpreparedness.election.VoterInfoViewModel
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApi
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApiService
import com.github.mjaremczuk.politicalpreparedness.network.NetworkDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.DefaultElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.representative.RepresentativeViewModel
import com.github.mjaremczuk.politicalpreparedness.utils.GeocoderHelper
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PoliticalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val module = module {
            viewModel { (election: ElectionModel) ->
                VoterInfoViewModel(get(), election)
            }
            viewModel { ElectionsViewModel(get()) }
            viewModel { RepresentativeViewModel(get()) }
            factory { GeocoderHelper.Factory(Dispatchers.IO) }
            single { ElectionDatabase.getInstance(this@PoliticalApplication).electionDao as ElectionDao }
            single { CivicsApi.create() as CivicsApiService }
            single(qualifier = named("local")) {
                LocalDataSource(
                    get(),
                    Dispatchers.IO
                ) as ElectionDataSource
            }
            single(qualifier = named("remote")) {
                NetworkDataSource(
                    get(),
                    Dispatchers.IO
                ) as ElectionDataSource
            }
            single { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) as DateFormat }
            single {
                DefaultElectionsRepository(
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