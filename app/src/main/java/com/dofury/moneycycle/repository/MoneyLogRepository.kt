package com.dofury.moneycycle.repository

import android.content.Context
import com.dofury.moneycycle.dao.MoneyLogDao
import com.dofury.moneycycle.dto.MoneyLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoneyLogRepository(private val moneyLogDao: MoneyLogDao) {

    suspend fun getAllMoneyLog(): List<MoneyLog> {
        return withContext(Dispatchers.IO) {
            moneyLogDao.getAll()
        }
    }

    suspend fun getDateBetweenLog(startDate: String, endDate: String): List<MoneyLog> {
        return withContext(Dispatchers.IO) {
            moneyLogDao.getDateBetweenLog(startDate, endDate)
        }
    }

    suspend fun getDateLog(monthDate: String): List<MoneyLog>{
        return withContext(Dispatchers.IO) {
            moneyLogDao.getDateLog(monthDate)
        }
    }
    suspend fun insert(moneyLog: MoneyLog) {
        withContext(Dispatchers.IO) {
            moneyLogDao.insert(moneyLog)
        }
    }

    suspend fun insertAll(moneyLogs: List<MoneyLog>,context: Context) {
        withContext(Dispatchers.IO) {
            context.deleteDatabase("MoneyLog")
            moneyLogDao.insertAll(moneyLogs)
        }
    }

    suspend fun delete(moneyLog: MoneyLog) {
        moneyLogDao.delete(moneyLog)
    }

    suspend fun update(moneyLog: MoneyLog) {
        moneyLogDao.update(moneyLog)
    }
}