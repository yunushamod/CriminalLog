package com.yunushamod.criminallog.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.yunushamod.criminallog.models.Crime
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class CrimeRepository private constructor(context: Context) {
    private val database: CrimeDatabase = Room.
        databaseBuilder(context, CrimeDatabase::class.java, DATABASE_NAME)
        .build()
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val crimeDao = database.crimeDao()
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)
    fun updateCrime(crime: Crime){
        executor.execute{
            crimeDao.updateCrime(crime)
        }
    }
    fun addCrime(crime: Crime){
        executor.execute{
            crimeDao.addCrime(crime)
        }
    }
    companion object{
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = CrimeRepository(context)
            }
        }
        fun get(): CrimeRepository{
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
        private const val DATABASE_NAME: String = "crime-database"
    }
}