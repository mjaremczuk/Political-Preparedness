package com.github.mjaremczuk.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.github.mjaremczuk.politicalpreparedness.network.models.Election

interface ElectionDataSource {
    fun observerElections(): LiveData<Result<List<Election>>>
    suspend fun getElections(): Result<List<Election>>
    suspend fun saveElections(elections: List<Election>)
    suspend fun markAsSaved(election: Election)
    suspend fun deleteAll()
}