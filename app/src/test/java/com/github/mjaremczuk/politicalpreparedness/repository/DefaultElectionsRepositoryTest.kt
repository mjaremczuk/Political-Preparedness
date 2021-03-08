package com.github.mjaremczuk.politicalpreparedness.repository

import com.github.mjaremczuk.politicalpreparedness.MainCoroutineRule
import com.github.mjaremczuk.politicalpreparedness.data.FakeDataSource
import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class DefaultElectionsRepositoryTest {

    val election1 = Election(1, "Title1", Date(), Division("1", "us", "al"))
    val election2 = Election(2, "Title2", Date(), Division("2", "us", "ga"))
    val election3 = Election(3, "Title3", Date(), Division("3", "us", "cl"))
    val remoteElections = listOf(election1, election2, election3)
    val localElections = listOf<Election>()
    private lateinit var electionsRemoteDataSource: FakeDataSource
    private lateinit var electionsLocalDataSource: FakeDataSource

    private lateinit var electionsRepository: DefaultElectionsRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        electionsRemoteDataSource = FakeDataSource(remoteElections.toMutableList())
        electionsLocalDataSource = FakeDataSource(localElections.toMutableList())
        electionsRepository = DefaultElectionsRepository(
                electionsLocalDataSource,
                electionsRemoteDataSource,
                Dispatchers.Main
        )
    }

    @Test
    fun getElections_GetElectionsFromRemote() = mainCoroutineRule.runBlockingTest {
        val result = electionsRepository.getElections(true) as Result.Success

        assertThat(result.data, IsEqual(remoteElections))
    }

    @Test
    fun getElections_GetElectionsFromLocal() = mainCoroutineRule.runBlockingTest {
        val result = electionsRepository.getElections(true) as Result.Success

        assertThat(result.data, IsEqual(remoteElections))
    }

    @Test
    fun markAsSaved_GetElectionsAndMarkOneAsSaved() = mainCoroutineRule.runBlockingTest {
        val result = electionsRepository.getElections(true) as Result.Success

        val election = result.data.first()
        assertThat(election.saved, `is`(false))

        electionsRepository.markAsSaved(result.data.first(), true)

        val newResult = electionsRepository.getElections(false) as Result.Success
        assertThat(newResult.data.first { it.id == election.id }.saved, `is`(true))
    }

    @Test
    fun refreshElections_GetRemoteElectionDoesNotOverrideSavedStatusForLocalElections() = mainCoroutineRule.runBlockingTest {
        val result = electionsRepository.getElections(true) as Result.Success

        val election = result.data.first()

        electionsRepository.markAsSaved(result.data.first(), true)
        electionsRepository.refreshElections()

        val newResult = electionsRepository.getElections(true) as Result.Success

        assertThat(newResult.data.first { it.id == election.id }.saved, `is`(true))
    }

    @Test
    fun getElectionDetails_ReturnError() = mainCoroutineRule.runBlockingTest {
        electionsRemoteDataSource.showDetailsError = true
        val result = electionsRepository.getElectionDetails(1, "address")

        assertThat(result, IsInstanceOf(Result.Failure::class.java))
    }

    @Test
    fun getElectionDetails_GetState() = mainCoroutineRule.runBlockingTest {
        val result = electionsRepository.getElectionDetails(1, "address")

        assertThat(result, IsInstanceOf(Result.Success::class.java))
        assertThat((result as Result.Success).data, not(IsNull()))
    }
}