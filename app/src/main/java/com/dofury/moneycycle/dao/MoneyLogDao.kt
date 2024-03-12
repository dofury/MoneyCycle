package com.dofury.moneycycle.dao

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dofury.moneycycle.dto.MoneyLog

@Dao
interface MoneyLogDao {
    @Insert
    fun insert(moneyLog: MoneyLog)

    fun insertAll(logs: List<MoneyLog>){
        for(log in logs){
            insert(log)
        }
    }

    @Update
    fun update(moneyLog: MoneyLog)

    @Delete
    fun delete(moneyLog: MoneyLog)

    @Query("SELECT * FROM MoneyLog")
    fun getAll(): List<MoneyLog>

    @Query("SELECT * FROM MoneyLog WHERE isBudget = true AND sign = true")
    fun getBudgetLogs(): List<MoneyLog>
    @Query("SELECT * FROM MoneyLog WHERE isBudget = true")
    fun getResetBudgetLogs(): List<MoneyLog>

    @Query("SELECT * FROM MoneyLog WHERE strftime('%Y-%m-%d', date) BETWEEN :startDate AND :endDate")
    fun getDateBetweenLog(startDate: String, endDate: String): List<MoneyLog>

    @Query("SELECT * FROM MoneyLog WHERE strftime('%Y-%m', date) = :monthDate")
    fun getDateLog(monthDate: String): List<MoneyLog>

    @Query("SELECT * FROM MoneyLog WHERE :sql")
    fun getQueryLog(sql: String): List<MoneyLog>

}