package com.github.mjaremczuk.politicalpreparedness.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import com.github.mjaremczuk.politicalpreparedness.network.models.State
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NetworkDataSource(
        private val apiService: CivicsApiService,
        private val ioDispatcher: CoroutineDispatcher,
) : ElectionDataSource {

    private val _upcomingElections: MutableLiveData<Result<List<Election>>> = MutableLiveData()

    override suspend fun getElections(): Result<List<Election>> {
        return withContext(ioDispatcher) {
            val result = try {
                Result.Success(apiService.getElections().elections)
            } catch (ex: Exception) {
                Result.Failure(ex)
            }
            _upcomingElections.postValue(result)
            result
        }
    }

    override suspend fun getDetails(electionId: Int, address: String): Result<State?> {
        return withContext(ioDispatcher) {
            val result = try {
                Result.Success(apiService.getVoterInfo(address, electionId).state?.first())
            } catch (ex: Exception) {
                ex.printStackTrace()
                Result.Failure(ex)
            }
            result
        }
    }

    override fun observerElections(): LiveData<Result<List<Election>>> {
        return _upcomingElections
    }

    override suspend fun saveElections(elections: List<Election>) {
//        do nothing
    }

    override suspend fun markAsSaved(election: Election) {
//        do nothing
    }

    override suspend fun deleteAll() {
//        do nothing
    }
}