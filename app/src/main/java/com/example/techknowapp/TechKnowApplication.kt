package com.example.techknowapp

import android.app.Application
import timber.log.Timber

class TechKnowApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}