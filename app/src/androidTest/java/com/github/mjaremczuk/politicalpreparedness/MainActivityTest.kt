package com.github.mjaremczuk.politicalpreparedness

import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDao
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDatabase
import com.github.mjaremczuk.politicalpreparedness.database.LocalDataSource
import com.github.mjaremczuk.politicalpreparedness.election.ElectionsViewModel
import com.github.mjaremczuk.politicalpreparedness.election.VoterInfoViewModel
import com.github.mjaremczuk.politicalpreparedness.election.adapter.ElectionListAdapter
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApi
import com.github.mjaremczuk.politicalpreparedness.network.CivicsApiService
import com.github.mjaremczuk.politicalpreparedness.network.NetworkDataSource
import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionDataSource
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.representative.RepresentativeViewModel
import com.github.mjaremczuk.politicalpreparedness.util.DataBindingIdlingResource
import com.github.mjaremczuk.politicalpreparedness.util.KoinTestRule
import com.github.mjaremczuk.politicalpreparedness.util.RecyclerViewItemCountAssertion
import com.github.mjaremczuk.politicalpreparedness.util.monitorActivity
import com.github.mjaremczuk.politicalpreparedness.utils.EspressoIdlingResource
import com.github.mjaremczuk.politicalpreparedness.utils.GeocoderHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()
    private val repository: FakeTestRepository = FakeTestRepository()

    val appContext: Application = getApplicationContext()
    val module = module {
        viewModel { (election: ElectionModel) ->
            VoterInfoViewModel(get(), election)
        }
        viewModel { ElectionsViewModel(get()) }
        viewModel { RepresentativeViewModel(get()) }
        factory { GeocoderHelper.Factory(Dispatchers.IO) }
        single { repository as ElectionsRepository }
        single { ElectionDatabase.getInstance(appContext).electionDao as ElectionDao }
        single { CivicsApi.create() as CivicsApiService }
        single(qualifier = named("local")) {
            LocalDataSource(
                get(),
                Dispatchers.IO
            ) as ElectionDataSource
        }
        single(qualifier = named("remote")) {
            NetworkDataSource(
                get(),
                Dispatchers.IO
            ) as ElectionDataSource
        }
        single { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) as DateFormat }
    }

    @Rule
    @JvmField
    val mRuntimePermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(module)
    )

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun upcomingElections_AddAndRemoveFromSaved() = runTest {
        val election1 = Election(1, "Title1", Date(), Division("1", "us", "al"))
        val election2 = Election(2, "Title2", Date(), Division("2", "us", "ga"))
        val election3 = Election(3, "Title3", Date(), Division("3", "us", "cl"))
        repository.addElections(election1, election2, election3)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.upcoming_button)).perform(click())

        addElectionToSaved()

        onView(withId(R.id.saved_elections_recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ElectionListAdapter.ElectionViewHolder>(
                    0,
                    click()
                )
            )
        onView(withId(R.id.voter_action_button)).check(matches(withText("Unfollow election")))
        onView(withId(R.id.voter_action_button)).perform(click())

        onView(withId(R.id.saved_elections_header)).check(matches(not(isDisplayed())))

        activityScenario.close()
    }

    @Test
    fun myRepresentative_SearchForMyRepresentativesUsingLocationAddress() = runTest {
        val election1 = Election(1, "Title1", Date(), Division("1", "us", "al"))
        val election2 = Election(2, "Title2", Date(), Division("2", "us", "ga"))
        val election3 = Election(3, "Title3", Date(), Division("3", "us", "cl"))
        repository.addElections(election1, election2, election3)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.upcoming_button)).perform(click())

        addElectionToSaved()
        Espresso.pressBack()

        onView(withId(R.id.representative_button)).perform(click())
        onView(withId(R.id.button_location)).perform(click())
        onView(withId(R.id.representative_recycler))
            .check(RecyclerViewItemCountAssertion.withItemCount(not(0)))

        Espresso.pressBack()

        onView(withId(R.id.upcoming_button)).perform(click())
        onView(withId(R.id.saved_elections_header)).check(matches(withText("Saved elections")))
        onView(withId(R.id.saved_elections_recycler)).check(matches(isDisplayed()))
        onView(withId(R.id.saved_elections_recycler))
            .check(RecyclerViewItemCountAssertion.withItemCount(1))

        activityScenario.close()
    }

    private fun addElectionToSaved() {
        onView(withId(R.id.upcoming_elections_header)).check(matches(isDisplayed()))
        onView(withId(R.id.upcoming_elections_header)).check(matches(withText("Upcoming elections")))
        onView(withId(R.id.saved_elections_header)).check(matches(not(isDisplayed())))
        onView(withId(R.id.upcoming_elections_recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ElectionListAdapter.ElectionViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.voter_action_button)).check(matches(withText("Follow election")))
        onView(withId(R.id.voter_action_button)).perform(click())

        onView(withId(R.id.saved_elections_header)).check(matches(withText("Saved elections")))
        onView(withId(R.id.saved_elections_recycler)).check(matches(isDisplayed()))
        onView(withId(R.id.saved_elections_recycler))
            .check(RecyclerViewItemCountAssertion.withItemCount(1))
    }
}