package com.github.mjaremczuk.politicalpreparedness.repository

import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class ElectionRepository(
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
                    val saved = localElections.elections.filter { it.saved }
//                    elections.elections.map { networkElection ->
//                        saved.find { it.id == networkElection.id }?.copy(saved = true)
//                                ?: networkElection
//                    }
                    elections.elections.map { online ->
                        online.copy(saved = saved.firstOrNull { it.id == online.id }?.saved == true)
                    }
                } else {
                    elections.elections
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
}

sealed class Result<out T> {
    data class Failure<T>(val exception: Exception) : Result<T>()
    data class Success<T>(val elections: T) : Result<T>()
    class Loading<T>() : Result<T>()
}