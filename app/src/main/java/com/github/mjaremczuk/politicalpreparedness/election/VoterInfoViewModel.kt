package com.github.mjaremczuk.politicalpreparedness.election

import android.location.Address
import androidx.lifecycle.*
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.election.model.toDataModel
import com.github.mjaremczuk.politicalpreparedness.network.models.State
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.repository.Result
import kotlinx.coroutines.launch

class VoterInfoViewModel(
        private val repository: ElectionsRepository,
        val election: ElectionModel,
) : ViewModel() {

    private val _electionDetails: MutableLiveData<Result<State?>> = MutableLiveData()
    val electionDetails: LiveData<State> = Transformations.map(_electionDetails) {
        when (it) {
            is Result.Success -> it.data
            is Result.Failure,
            is Result.Loading -> null
        }
    }

    private val _navigateBack: MutableLiveData<Boolean> = MutableLiveData()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack


    fun loadDetails(address: Address?) {
        viewModelScope.launch {
            address?.postalCode
            val exactAddress = "${address?.getAddressLine(0)}"
            println("downloaded adress: $address")
            val response = repository.getElectionDetails(election.id, exactAddress)
            println("downloaded details: $response")
            _electionDetails.value = response
        }
    }

    fun onActionClick() {
        viewModelScope.launch {
            repository.markAsSaved(election.toDataModel(), election.saved.not())
            _navigateBack.value = true
        }
    }

    fun navigateCompleted() {
        _navigateBack.value = false
    }

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}