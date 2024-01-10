package com.github.mjaremczuk.politicalpreparedness

import androidx.navigation.NavController
import androidx.test.rule.GrantPermissionRule
import com.github.mjaremczuk.politicalpreparedness.election.ElectionsViewModel
import com.github.mjaremczuk.politicalpreparedness.election.VoterInfoViewModel
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import com.github.mjaremczuk.politicalpreparedness.representative.RepresentativeViewModel
import com.github.mjaremczuk.politicalpreparedness.util.KoinTestRule
import com.github.mjaremczuk.politicalpreparedness.utils.GeocoderHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mockito.Mockito
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseFragmentTest {

    protected val repository: FakeTestRepository = FakeTestRepository()

    protected val navController = Mockito.mock(NavController::class.java)

    val myModule = module {
        viewModel {
            ElectionsViewModel(
                    get(),
            )
        }
        viewModel { (election: ElectionModel) ->
            VoterInfoViewModel(get(), election)
        }
        viewModel { RepresentativeViewModel(get()) }
        factory { GeocoderHelper.Factory(Dispatchers.IO) }
        single { repository as ElectionsRepository }
        single { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) as DateFormat }
    }


    @get:Rule
    val koinTestRule = KoinTestRule(
            modules = listOf(myModule)
    )

    @Rule
    @JvmField
    val mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    @Before
    fun init() {
        runBlocking {
            repository.removeAllElections()
        }
    }
}