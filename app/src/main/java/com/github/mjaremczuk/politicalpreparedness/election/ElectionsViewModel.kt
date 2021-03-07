package com.github.mjaremczuk.politicalpreparedness.election

import androidx.lifecycle.*
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.network.models.toDomainModel
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.repository.Result
import kotlinx.coroutines.launch

class ElectionsViewModel(private val repository: ElectionsRepository) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val elections: LiveData<List<ElectionModel>> = Transformations.map(repository.observeElections()) {
        when (it) {
            is Result.Failure -> {
                emptyList()
            }
            is Result.Success -> {
                it.elections.toDomainModel()
            }
            is Result.Loading -> {
                upcomingElections.value
            }
        }
    }

    val upcomingElections: LiveData<List<ElectionModel>> = elections

    val savedElections: LiveData<List<ElectionModel>> = Transformations.map(elections) {
        it.filter { it.saved }
    }

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _dataLoading.value = true
            refreshElections()
            _dataLoading.value = false
        }
    }

    private suspend fun refreshElections() {
        repository.refreshElections()
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
}