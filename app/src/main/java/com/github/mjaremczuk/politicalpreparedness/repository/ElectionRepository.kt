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
import retrofit2.HttpException

class ElectionRepository(
        private val database: ElectionDao,
        private val apiService: CivicsApiService,
        private val defaultDispatcher: CoroutineDispatcher,
        private val ioDispatcher: CoroutineDispatcher,
) : ElectionDataSource {

    //    save all to db and add new parameter: saved: true/false to split between
    private val _upcomingElections: MutableLiveData<Result<Election>> = MediatorLiveData()

    override val upcomingElections: LiveData<Result<ElectionModel>> =
            Transformations.map(_upcomingElections) {
                when (it) {
                    is Result.Failure -> it
                    is Result.Success -> Result.Success(it.elections.toDomainModel())
                    is Result.Loading -> it
                } as Result<ElectionModel>

            }

    override val savedElections: LiveData<List<ElectionModel>>
        get() = Transformations.map(database.getAll()) {
            it.toDomainModel()
        }

    override suspend fun refreshElections() {
        withContext(ioDispatcher) {
            _upcomingElections.postValue(Result.Loading())
            try {
                val elections = getElections()
                database.save(elections.first())
                _upcomingElections.postValue(Result.Success(elections))
            } catch (e: Exception) {
                e.printStackTrace()
                _upcomingElections.postValue(Result.Failure(e))
            }
        }
    }

    override suspend fun refreshSavedElections() {
        withContext(ioDispatcher) {

        }
    }

    private suspend fun getElections() = apiService.getElections().elections
}

sealed class Result<out T> {
    data class Failure<T>(val exception: Exception) : Result<T>()
    data class Success<T>(val elections: List<T>) : Result<T>()
    class Loading<T>() : Result<T>()
}