package com.github.mjaremczuk.politicalpreparedness

import androidx.navigation.NavController
import androidx.test.rule.GrantPermissionRule
import com.github.mjaremczuk.politicalpreparedness.election.ElectionsViewModel
import com.github.mjaremczuk.politicalpreparedness.election.VoterInfoViewModel
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mockito
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseFragmentTest : AutoCloseKoinTest() {

    protected lateinit var repository: FakeTestRepository

    protected val navController = Mockito.mock(NavController::class.java)

    @Rule
    @JvmField
    val mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    @Before
    fun init() {
        stopKoin()
        val myModule = module {
            viewModel {
                ElectionsViewModel(
                        get(),
                )
            }
            viewModel { (election: ElectionModel) ->
                VoterInfoViewModel(get(), election)
            }
            single { FakeTestRepository() as ElectionsRepository }
            single { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) as DateFormat }
        }
        startKoin {
            modules(listOf(myModule))
        }
        val fake: ElectionsRepository = get()
        repository = fake as FakeTestRepository

        runBlocking {
            repository.removeAllElections()
        }
    }
}