package com.github.mjaremczuk.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(private val electionDataSource: ElectionDataSource) : ViewModel() {

    val upcomingElections: LiveData<List<ElectionModel>> =
            electionDataSource.upcomingElections

    val savedElections: LiveData<List<ElectionModel>> = electionDataSource.savedElections

    init {
        viewModelScope.launch {
            getSavedElections()
            getUpcomingElections()
//            electionDataSource.upcomingElections
//                    .catch { it.printStackTrace() }
//                    .collect {
//                println("DOWNLOADED list of elections: $it")
//            }
//            electionDataSource.refreshElections()
        }
    }

    private suspend fun getUpcomingElections() {
        electionDataSource.refreshElections()
    }

    private suspend fun getSavedElections() {

    }
    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}