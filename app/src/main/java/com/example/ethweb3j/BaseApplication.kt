package com.example.ethweb3j

import android.app.Application
import com.example.ethweb3j.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        startKoin {

            androidContext(this@BaseApplication)
            modules(listOf(mainModule))
        }
    }
}