package com.github.mjaremczuk.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.github.mjaremczuk.politicalpreparedness.network.models.Election

interface ElectionsRepository {
    suspend fun getElections(force: Boolean): Result<List<Election>>
    suspend fun refreshElections()
    fun observeElections(): LiveData<Result<List<Election>>>
    suspend fun markAsSaved(election: Election, saved: Boolean)
}