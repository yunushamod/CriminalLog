package com.yunushamod.criminallog.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yunushamod.criminallog.models.Crime
import java.util.*

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime ORDER BY date DESC;")
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>
    
    @Update
    fun updateCrime(crime: Crime)
    
    @Insert
    fun addCrime(crime: Crime)

    @Delete
    fun deleteCrime(crime: Crime)
}