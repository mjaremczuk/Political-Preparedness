package com.github.mjaremczuk.politicalpreparedness.representative

import androidx.lifecycle.*
import com.github.mjaremczuk.politicalpreparedness.R
import com.github.mjaremczuk.politicalpreparedness.network.models.Address
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.repository.Result
import com.github.mjaremczuk.politicalpreparedness.representative.model.Representative
import com.github.mjaremczuk.politicalpreparedness.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: ElectionsRepository) : ViewModel() {

    private val _line1 = MutableLiveData("")
    val line1: MutableLiveData<String> = _line1
    private val _line2 = MutableLiveData("")
    val line2: MutableLiveData<String> = _line2
    private val _city = MutableLiveData("")
    val city: MutableLiveData<String> = _city
    private val _state = MutableLiveData("")
    val state: MutableLiveData<String> = _state
    private val _zip = MutableLiveData("")
    val zip: MutableLiveData<String> = _zip

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _message: MutableLiveData<Int?> = SingleLiveEvent()
    val message: LiveData<Int?>
        get() = _message

    private val _representatives: MutableLiveData<Result<List<Representative>>> = MutableLiveData()
    val representatives: LiveData<List<Representative>> = Transformations.map(_representatives) {
        when (it) {
            is Result.Failure -> emptyList()
            is Result.Success -> it.data
            is Result.Loading -> emptyList()
        }
    }

    fun searchForRepresentatives(address: Address) {
        _line1.value = address.line1
        _line2.value = address.line2.orEmpty()
        _city.value = address.city
        _state.value = address.state
        _zip.value = address.zip
        println("My location: $address")
        searchRepresentativesIfFormValid(address)
    }

    fun searchForMyRepresentatives() {
        searchRepresentativesIfFormValid(Address(
                requireNotNull(line1.value),
                line2.value,
                requireNotNull(city.value),
                requireNotNull(state.value),
                requireNotNull(zip.value)
        ))
    }

    private fun searchRepresentativesIfFormValid(address: Address) {
        println("My location searchRepresentativesIfFormValid: $address")
        viewModelScope.launch {
            if (address.line1.isBlank()) {
                _message.value = R.string.error_missing_first_line_address
                return@launch
            }
            if (address.city.isBlank()) {
                _message.value = R.string.error_missing_city
                return@launch
            }
            if (address.state.isBlank()) {
                _message.value = R.string.error_missing_state
                return@launch
            }
            if (address.zip.isBlank()) {
                _message.value = R.string.error_missing_zip
                return@launch
            }
            search(address)
        }
    }

    private suspend fun search(address: Address) {
        _dataLoading.value = true
        _representatives.value = repository.searchRepresentatives(address)
        _dataLoading.value = false
    }

    fun setState(state: String?) {
        _state.value = state
    }

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
