package com.github.mjaremczuk.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDao
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApiService
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import com.github.mjaremczuk.politicalpreparedness.network.models.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ElectionRepository(
        private val database: ElectionDao,
        private val apiService: CivicsApiService,
        private val defaultDispatcher: CoroutineDispatcher,
        private val ioDispatcher: CoroutineDispatcher,
) : ElectionDataSource {

//    save all to db and add new parameter: saved: true/false to split between
    private val _upcomingElections: MutableLiveData<List<Election>> = MediatorLiveData()

    override val upcomingElections: LiveData<List<ElectionModel>> =
            Transformations.map(_upcomingElections) {
                it.toDomainModel()
            }

    override val savedElections: LiveData<List<ElectionModel>>
        get() = Transformations.map(database.getAll()) {
            it.toDomainModel()
        }

    override suspend fun refreshElections() {
        withContext(ioDispatcher) {
            try {
                val elections = getElections()
                _upcomingElections.postValue(elections)
            } catch (e: Exception) {
                _upcomingElections.postValue(emptyList())
            }
        }
    }

    private suspend fun getElections() = apiService.getElections().elections
}