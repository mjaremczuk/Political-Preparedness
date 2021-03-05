package com.github.mjaremczuk.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel

interface ElectionDataSource {
    val upcomingElections: LiveData<List<ElectionModel>>
    val savedElections: LiveData<List<ElectionModel>>

    suspend fun refreshElections()
}