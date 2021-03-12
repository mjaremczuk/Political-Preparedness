package com.github.mjaremczuk.politicalpreparedness.representative

import androidx.lifecycle.*
import com.github.mjaremczuk.politicalpreparedness.network.models.Address
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.repository.Result
import com.github.mjaremczuk.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: ElectionsRepository) : ViewModel() {

    private val _line1 = MutableLiveData("")
    val line1: LiveData<String> = _line1
    private val _line2 = MutableLiveData("")
    val line2: LiveData<String> = _line2
    private val _city = MutableLiveData("")
    val city: LiveData<String> = _city
    private val _state = MutableLiveData("")
    val state: LiveData<String> = _state
    private val _zip = MutableLiveData("")
    val zip: LiveData<String> = _zip

    private val _message: MutableLiveData<String?> = MutableLiveData()
    val message: LiveData<String?>
        get() = _message

    private val _representatives: MutableLiveData<Result<List<Representative>>> = MutableLiveData()
    val representatives: LiveData<List<Representative>> = Transformations.map(_representatives) {
        when (it) {
            is Result.Failure -> emptyList()
            is Result.Success -> it.data
            is Result.Loading -> emptyList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("Clearing representatives")
    }

    fun searchForRepresentatives(address: Address) {
        _line1.value = address.line1
        _line2.value = address.line2.orEmpty()
        _city.value = address.city
        _state.value = address.state
        _zip.value = address.zip
        println("My location: $address")
        viewModelScope.launch {
            _representatives.value = repository.searchRepresentatives(address)
        }
    }

    fun searchForMyRepresentatives() {
        viewModelScope.launch {
            val formFilled = listOf(
                    line1.value.orEmpty(),
                    city.value.orEmpty(),
                    state.value.orEmpty(),
                    zip.value.orEmpty()
            )
                    .none { it.isEmpty() }

            val address = Address(
                    requireNotNull(line1.value),
                    line2.value,
                    requireNotNull(city.value),
                    requireNotNull(state.value),
                    requireNotNull(zip.value)
            )

            println("Manual My location: $address")
            _representatives.value = repository.searchRepresentatives(address)
        }
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
