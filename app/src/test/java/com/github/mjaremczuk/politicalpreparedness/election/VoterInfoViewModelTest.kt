package com.github.mjaremczuk.politicalpreparedness.election

import android.location.Address
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mjaremczuk.politicalpreparedness.MainCoroutineRule
import com.github.mjaremczuk.politicalpreparedness.R
import com.github.mjaremczuk.politicalpreparedness.data.FakeTestRepository
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.getOrAwaitValue
import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import com.github.mjaremczuk.politicalpreparedness.network.models.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class VoterInfoViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var electionsRepository: FakeTestRepository
    private lateinit var voterInfoViewModel: VoterInfoViewModel

    private val address: Address = Address(Locale.getDefault())

    @Before
    fun setupViewModel() {
        val election = ElectionModel(1, "Title1", Date(), Division("1", "us", "al"), false)
        electionsRepository = FakeTestRepository()
        voterInfoViewModel = VoterInfoViewModel(electionsRepository, election)
    }

    @Test
    fun loadDetails_SuccessfullyLoadDetails() {
        voterInfoViewModel.loadDetails(address)

        assertThat(voterInfoViewModel.errorMessage.getOrAwaitValue(), IsNull())
        assertThat(voterInfoViewModel.electionDetails.getOrAwaitValue(), not(IsNull()))
        assertThat(voterInfoViewModel.electionDetails.getOrAwaitValue(), IsInstanceOf(State::class.java))
    }

    @Test
    fun loadDetails_ErrorMessage() {
        electionsRepository.setReturnError(true)

        voterInfoViewModel.loadDetails(address)

        assertThat(voterInfoViewModel.electionDetails.getOrAwaitValue(), IsNull())
        assertThat(voterInfoViewModel.errorMessage.getOrAwaitValue(), `is`(R.string.error_failed_load_voter_info))
    }

    @Test
    fun actionClick_FollowElection() {
        voterInfoViewModel.onActionClick()

        assertThat(voterInfoViewModel.navigateBack.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun navigateCompleted_SetNavigateBackToFalse() {
        voterInfoViewModel.navigateCompleted()

        assertThat(voterInfoViewModel.navigateBack.getOrAwaitValue(), `is`(false))
    }
}