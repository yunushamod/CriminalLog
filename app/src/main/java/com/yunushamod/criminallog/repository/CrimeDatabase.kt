package com.yunushamod.criminallog.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yunushamod.criminallog.models.Crime

@Database(entities=[Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase: RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}