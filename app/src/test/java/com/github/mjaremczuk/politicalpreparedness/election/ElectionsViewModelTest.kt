package com.github.mjaremczuk.politicalpreparedness.election

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mjaremczuk.politicalpreparedness.data.FakeTestRepository
import com.github.mjaremczuk.politicalpreparedness.MainCoroutineRule
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.getOrAwaitValue
import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import org.hamcrest.Matchers.`is` as matches

@ExperimentalCoroutinesApi
class ElectionsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var tasksRepository: FakeTestRepository
    private lateinit var electionsViewModel: ElectionsViewModel

    @Before
    fun setupViewModel() {
        tasksRepository = FakeTestRepository()
        val election1 = Election(1, "Title1", Date(), Division("1", "us", "al"))
        val election2 = Election(2, "Title2", Date(), Division("2", "us", "ga"))
        val election3 = Election(3, "Title3", Date(), Division("3", "us", "cl"))
        tasksRepository.addElections(election1, election2, election3)

        electionsViewModel = ElectionsViewModel(tasksRepository)
    }

    @Test
    fun refresh_ShowDataLoading() {
        mainCoroutineRule.pauseDispatcher()

        electionsViewModel.refresh()

        assertThat(electionsViewModel.dataLoading.getOrAwaitValue(), matches(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(electionsViewModel.dataLoading.getOrAwaitValue(), matches(false))
    }

    @Test
    fun refresh_LoadOnlyUpcomingElectionsExists() {
        electionsViewModel.refresh()

        assertThat(electionsViewModel.upcomingElections.getOrAwaitValue().isEmpty(), matches(false))

        assertThat(electionsViewModel.upcomingElections.getOrAwaitValue().size, matches(3))
        assertThat(electionsViewModel.savedElections.getOrAwaitValue(), matches(IsEmptyCollection()))
    }

    @Test
    fun refresh_LoadUpcomingAndSavedElections() {
        tasksRepository.addElections(Election(1, "Title1", Date(), Division("1", "us", "al")).copy(saved = true))
        electionsViewModel.refresh()

        assertThat(electionsViewModel.upcomingElections.getOrAwaitValue().isEmpty(), matches(false))

        assertThat(electionsViewModel.upcomingElections.getOrAwaitValue().size, matches(3))
        assertThat(electionsViewModel.savedElections.getOrAwaitValue().size, matches(1))
    }

    @Test
    fun upcomingElectionClicked() {
        electionsViewModel.onUpcomingClicked(ElectionModel(1, "Title1", Date(), Division("1", "us", "al"), false))

        assertThat(electionsViewModel.navigateTo.getOrAwaitValue(), IsInstanceOf(ElectionsFragmentDirections.ActionElectionsFragmentToVoterInfoFragment::class.java))
    }

    @Test
    fun savedElectionClicked() {
        electionsViewModel.onSavedClicked(ElectionModel(1, "Title1", Date(), Division("1", "us", "al"), false))

        assertThat(electionsViewModel.navigateTo.getOrAwaitValue(), IsInstanceOf(ElectionsFragmentDirections.ActionElectionsFragmentToVoterInfoFragment::class.java))
    }

    @Test
    fun navigationCompleted_ClearNavigateTo() {
        electionsViewModel.navigateCompleted()

        assertThat(electionsViewModel.navigateTo.getOrAwaitValue(), IsNull())
    }
}