package com.dofury.moneycycle.repository

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dofury.moneycycle.dao.MoneyLogDao
import com.dofury.moneycycle.dao.QueryCondition
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

    suspend fun getConditionLog(query: QueryCondition): List<MoneyLog> {
        val simpleQuery = buildQuery(query.startDate, query.endDate, query.categories, query.isBudget, query.memo)
        return withContext(Dispatchers.IO) {

            moneyLogDao.getConditionLogs(simpleQuery)
        }
    }

    private fun buildQuery(
        startDate: String?, endDate: String?,
        categories: List<String>?, isBudget: Boolean?, memo: String?
    ): SimpleSQLiteQuery {
        val queryBuilder = StringBuilder("SELECT * FROM MoneyLog WHERE 1=1")
        val args = mutableListOf<Any?>()

        startDate?.let {
            queryBuilder.append(" AND (strftime('%Y-%m-%d', date) >= ?)")
            args.add(it)
        }

        endDate?.let {
            queryBuilder.append(" AND (strftime('%Y-%m-%d', date) <= ?)")
            args.add(it)
        }

        categories?.takeIf { it.isNotEmpty() }?.let {
            queryBuilder.append(" AND (category IN (${it.joinToString(",") { "?" }}))")
            args.addAll(it)
        }

        isBudget?.let {
            queryBuilder.append(" AND (isBudget = ?)")
            args.add(it)
        }

        memo?.let {
            queryBuilder.append(" AND (memo = ?)")
            args.add(it)
        }

        return SimpleSQLiteQuery(queryBuilder.toString(), args.toTypedArray())
    }

    suspend fun delete(moneyLog: MoneyLog) {
        moneyLogDao.delete(moneyLog)
    }

    suspend fun update(moneyLog: MoneyLog) {
        moneyLogDao.update(moneyLog)
    }
}