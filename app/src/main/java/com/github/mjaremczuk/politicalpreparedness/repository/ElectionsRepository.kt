package com.github.mjaremczuk.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.github.mjaremczuk.politicalpreparedness.network.models.Address
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import com.github.mjaremczuk.politicalpreparedness.network.models.State
import com.github.mjaremczuk.politicalpreparedness.representative.model.Representative

interface ElectionsRepository {
    suspend fun getElections(force: Boolean): Result<List<Election>>
    suspend fun refreshElections()
    fun observeElections(): LiveData<Result<List<Election>>>
    suspend fun markAsSaved(election: Election, saved: Boolean)
    suspend fun getElectionDetails(electionId: Int, address: String): Result<State?>
    suspend fun searchRepresentatives(address: Address): Result<List<Representative>>
}