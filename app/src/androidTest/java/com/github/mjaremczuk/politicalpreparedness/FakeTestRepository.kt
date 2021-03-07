package com.github.mjaremczuk.politicalpreparedness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.repository.Result
import kotlinx.coroutines.runBlocking

class FakeTestRepository : ElectionsRepository {

    private var shouldReturnError = false
    var electionsServiceData: LinkedHashMap<Int, Election> = LinkedHashMap()

    private val observableElections = MutableLiveData<Result<List<Election>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getElections(force: Boolean): Result<List<Election>> {
        if (shouldReturnError) {
            return Result.Failure(Exception("Test exception"))
        }
        return Result.Success(electionsServiceData.values.toList())
    }

    override suspend fun refreshElections() {
        observableElections.value = getElections(true)
    }

    override fun observeElections(): LiveData<Result<List<Election>>> {
        runBlocking { refreshElections() }
        return observableElections
    }

    override suspend fun markAsSaved(election: Election, saved: Boolean) {
        electionsServiceData[election.id] = election.copy(saved = saved)
    }

    fun addElections(vararg election: Election) {
        election.forEach {
            electionsServiceData[it.id] = it
        }
    }

    fun removeAllElections() {
        electionsServiceData = linkedMapOf()
    }

}