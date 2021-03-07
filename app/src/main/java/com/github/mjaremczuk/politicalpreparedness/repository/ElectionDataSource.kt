package com.github.mjaremczuk.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import com.github.mjaremczuk.politicalpreparedness.network.models.State

interface ElectionDataSource {
    fun observerElections(): LiveData<Result<List<Election>>>
    suspend fun getElections(): Result<List<Election>>
    suspend fun saveElections(elections: List<Election>)
    suspend fun markAsSaved(election: Election)
    suspend fun deleteAll()
    suspend fun getDetails(electionId: Int, address: String): Result<State?>
}