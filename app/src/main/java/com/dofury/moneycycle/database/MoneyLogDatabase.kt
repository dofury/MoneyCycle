package com.dofury.moneycycle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dofury.moneycycle.dao.MoneyLogDao
import com.dofury.moneycycle.dto.MoneyLog

@Database(entities = [MoneyLog::class], version = 1)
abstract class MoneyLogDatabase: RoomDatabase() {
    abstract fun moneyLogDao(): MoneyLogDao

    companion object {
        private var instance: MoneyLogDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MoneyLogDatabase? {
            if (instance == null){
               synchronized(MoneyLogDatabase::class){
                   instance = Room.databaseBuilder(
                       context.applicationContext,
                       MoneyLogDatabase::class.java,
                       "moneyLog-database"
                   ).build()
               }
            }
            return instance
        }
    }
}