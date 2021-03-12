package com.github.mjaremczuk.politicalpreparedness.repository

import com.github.mjaremczuk.politicalpreparedness.network.models.*
import com.github.mjaremczuk.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class DefaultElectionsRepository(
        private val localDataSource: ElectionDataSource,
        private val networkDataSource: ElectionDataSource,
        private val ioDispatcher: CoroutineDispatcher,
) : ElectionsRepository {

    override fun observeElections() = localDataSource.observerElections()

    override suspend fun getElections(force: Boolean): Result<List<Election>> {
        if (force) {
            try {
                getElectionsFromNetwork()
            } catch (exception: java.lang.Exception) {
                return Result.Failure(exception)
            }
        }
        return localDataSource.getElections()
    }

    private suspend fun getElectionsFromNetwork() {
        try {
            val elections = networkDataSource.getElections()
            if (elections is Result.Success) {
                val localElections = localDataSource.getElections()
                localDataSource.deleteAll()
                if (localElections is Result.Success) {
                    val saved = localElections.data.filter { it.saved }
                    elections.data.map { online ->
                        online.copy(saved = saved.firstOrNull { it.id == online.id }?.saved == true)
                    }
                } else {
                    elections.data
                }.run { localDataSource.saveElections(this) }
            } else if (elections is Result.Failure) {
                throw elections.exception
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun refreshElections() {
        try {
            getElectionsFromNetwork()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override suspend fun markAsSaved(election: Election, saved: Boolean) {
        withContext(ioDispatcher) {
            coroutineScope {
                localDataSource.markAsSaved(election.copy(saved = saved))
            }
        }
    }

    override suspend fun getElectionDetails(electionId: Int, address: String): Result<State?> {
        return withContext(ioDispatcher) {
            networkDataSource.getDetails(electionId, address)
        }
    }

    override suspend fun searchRepresentatives(address: Address): Result<List<Representative>> {
        return withContext(ioDispatcher) {
            Thread.sleep(5_000)
            return@withContext Result.Success(listOf(
                    dummyRepresentative("Official address"),
                    dummyRepresentative("Official address2"),
                    dummyRepresentative("Official address3"),
                    dummyRepresentative("Official address4"),
                    dummyRepresentative("Official address5"),
                    dummyRepresentative("Official address6"),
                    dummyRepresentative("Official address7"),
            ))
        }
    }

    private fun dummyRepresentative(officialName: String) = Representative(
            Official(officialName, emptyList(), "party?", listOf("111 332 543", "423125523"), listOf("https://www.google.com"), null, null),
            Office("Test office", Division("division-id", "US", "Alabama"), emptyList())
    )
}