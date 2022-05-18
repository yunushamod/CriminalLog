package com.yunushamod.criminallog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yunushamod.criminallog.models.Crime
import com.yunushamod.criminallog.repository.CrimeRepository
import java.util.*

class CrimeDetailViewModel: ViewModel() {
    private val crimeRepository: CrimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()
    val crimeLiveData: LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData){
            crimeRepository.getCrime(it)
        }
    fun loadCrime(crimeId: UUID){
        crimeIdLiveData.value = crimeId
    }
    fun saveCrime(crime: Crime) = crimeRepository.updateCrime(crime)

}