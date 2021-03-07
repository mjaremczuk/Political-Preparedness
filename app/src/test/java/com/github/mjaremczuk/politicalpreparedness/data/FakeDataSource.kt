package com.github.mjaremczuk.politicalpreparedness.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.Result

class FakeDataSource(val elections: MutableList<Election>? = mutableListOf()) : ElectionDataSource {

    override fun observerElections(): LiveData<Result<List<Election>>> {
        return MutableLiveData(elections).map {
            Result.Success(it) as Result<List<Election>>
        }
    }

    override suspend fun getElections(): Result<List<Election>> {
        elections?.let { return Result.Success(ArrayList(it)) }
        return Result.Failure(
                Exception("Tasks not found")
        )
    }

    override suspend fun saveElections(elections: List<Election>) {
        this.elections?.addAll(elections)
    }

    override suspend fun markAsSaved(election: Election) {
        elections?.removeIf { it.id == election.id }
        elections?.add(election)
    }

    override suspend fun deleteAll() {
        this.elections?.clear()
    }
}