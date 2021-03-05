package com.github.mjaremczuk.politicalpreparedness.election

import androidx.lifecycle.*
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.Result
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(private val electionDataSource: ElectionDataSource) : ViewModel() {

    private val _upcomingStatus: MutableLiveData<Status> = MutableLiveData()
    val upcomingStatus: LiveData<Status>
        get() = _upcomingStatus

    private val elections: LiveData<List<ElectionModel>> =  Transformations.map(electionDataSource.upcomingElections) {
        when (it) {
            is Result.Failure -> {
                _upcomingStatus.postValue(Status.FAILURE)
                emptyList()
            }
            is Result.Success -> {
                _upcomingStatus.postValue(Status.SUCCESS)
                it.elections
            }
            is Result.Loading -> {
                _upcomingStatus.postValue(Status.LOADING)
                upcomingElections.value
            }
        }
    }

    val upcomingElections: LiveData<List<ElectionModel>> = elections

    val savedElections: LiveData<List<ElectionModel>> = electionDataSource.savedElections

    init {
        viewModelScope.launch {
            getSavedElections()
            getUpcomingElections()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            getUpcomingElections()
            getSavedElections()
        }
    }

    private suspend fun getUpcomingElections() {
        electionDataSource.refreshElections()
    }

    private suspend fun getSavedElections() {
        electionDataSource.refreshSavedElections()
    }

    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

    enum class Status {
        LOADING,
        FAILURE,
        SUCCESS
    }
}