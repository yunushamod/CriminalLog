package com.yunushamod.criminallog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yunushamod.criminallog.models.Crime
import com.yunushamod.criminallog.repository.CrimeRepository
import java.util.concurrent.Executors

class CrimeListViewModel : ViewModel() {
    private val crimeRepository: CrimeRepository = CrimeRepository.get()
    val crimeListLiveData: LiveData<List<Crime>> = crimeRepository.getCrimes()
    fun addCrime(crime: Crime){
        crimeRepository.addCrime(crime)
    }
}