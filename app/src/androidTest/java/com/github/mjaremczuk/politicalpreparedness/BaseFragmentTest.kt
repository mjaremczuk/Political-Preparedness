package com.github.mjaremczuk.politicalpreparedness

import androidx.navigation.NavController
import com.github.mjaremczuk.politicalpreparedness.election.ElectionsViewModel
import com.github.mjaremczuk.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mockito

abstract class BaseFragmentTest : AutoCloseKoinTest() {

    protected lateinit var repository: FakeTestRepository

    protected val navController = Mockito.mock(NavController::class.java)

    @Before
    fun init() {
        stopKoin()
        val myModule = module {
            viewModel {
                ElectionsViewModel(
                        get(),
                )
            }
            single { FakeTestRepository() as ElectionsRepository }
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