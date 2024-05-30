package com.dofury.moneycycle.dao

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
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

    @Query("SELECT * FROM MoneyLog WHERE id = :id")
    fun getLog(id: Int): MoneyLog

    @Query("SELECT * FROM MoneyLog WHERE isBudget = true AND sign = true")
    fun getBudgetPlus(): List<MoneyLog>

    @Query("SELECT * FROM MoneyLog WHERE isBudget = true AND sign = false")
    fun getRemainBudget(): List<MoneyLog>

    //날짜, 카테고리, 예산, 메모 4가지 조건이 모두 일치하는 로그를 찾는다.
    //조건은 on, off 를 통해 여러가지 경우를 나누어 검색한다.

    @RawQuery
    fun getConditionLogs(query: SimpleSQLiteQuery): List<MoneyLog>


}