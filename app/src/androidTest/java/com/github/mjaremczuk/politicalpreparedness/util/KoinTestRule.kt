package com.github.mjaremczuk.politicalpreparedness.util

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.module.Module

class KoinTestRule(
    private val modules: List<Module>
) : TestWatcher() {

    override fun starting(description: Description) {
        stopKoin()
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
            modules(modules)
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}