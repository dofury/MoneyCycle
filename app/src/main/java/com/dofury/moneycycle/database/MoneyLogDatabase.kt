package com.dofury.moneycycle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dofury.moneycycle.dao.MoneyLogDao
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.Converters

@Database(entities = [MoneyLog::class], version = 1)
@TypeConverters(Converters::class)
abstract class MoneyLogDatabase: RoomDatabase() {
    abstract fun moneyLogDao(): MoneyLogDao

    companion object {
        const val DATABASE_NAME = "moneyLog-database"

        @Volatile
        private var INSTANCE: MoneyLogDatabase? = null

        fun getInstance(context: Context): MoneyLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoneyLogDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}