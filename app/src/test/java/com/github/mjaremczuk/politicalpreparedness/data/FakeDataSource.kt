package com.github.mjaremczuk.politicalpreparedness.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.github.mjaremczuk.politicalpreparedness.network.models.*
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.Result

class FakeDataSource(val elections: MutableList<Election>? = mutableListOf()) : ElectionDataSource {

    var showDetailsError = false

    override fun observerElections(): LiveData<Result<List<Election>>> {
        return MutableLiveData(elections).map {
            Result.Success(it) as Result<List<Election>>
        }
    }

    override suspend fun getElections(): Result<List<Election>> {
        elections?.let { return Result.Success(ArrayList(it)) }
        return Result.Failure(
                Exception("Tasks not found")
        )
    }

    override suspend fun saveElections(elections: List<Election>) {
        this.elections?.addAll(elections)
    }

    override suspend fun markAsSaved(election: Election) {
        elections?.removeIf { it.id == election.id }
        elections?.add(election)
    }

    override suspend fun deleteAll() {
        this.elections?.clear()
    }

    override suspend fun getDetails(electionId: Int, address: String): Result<State?> {
        return if (showDetailsError) {
            Result.Failure(IllegalStateException("Failed to get details!"))
        } else {
            Result.Success(fakeStateData())
        }
    }

    override suspend fun getRepresentatives(address: Address): Result<RepresentativeResponse> {
        return if (showDetailsError) {
            Result.Failure(IllegalStateException("Failed to get details!"))
        } else {
            Result.Failure(IllegalStateException("Failed to get details!"))//
        }
    }

    private fun fakeStateData() = State(
            name = "Alabama",
            electionAdministrationBody = AdministrationBody(
                    name = "Administration name",
                    electionInfoUrl = "https://www.google.com/info_url",
                    votingLocationFinderUrl = "https://www.google.com/votin_location_url",
                    ballotInfoUrl = "https://www.google.com/ballon_info_url",
                    correspondenceAddress = Address(
                            line1 = "Line one address",
                            line2 = "Line 2 address",
                            city = "City",
                            state = "State",
                            zip = "471283"
                    )
            )
    )
}