package com.yunushamod.criminallog

import android.app.Application
import com.yunushamod.criminallog.repository.CrimeRepository

class CriminalLogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}