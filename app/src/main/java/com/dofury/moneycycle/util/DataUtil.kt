package com.dofury.moneycycle.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.dto.MoneyLog
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DataUtil {

    private val makeGson = GsonBuilder().create()
    private var listType : TypeToken<MutableList<MoneyLog>> = object : TypeToken<MutableList<MoneyLog>>() {}
    fun parseMoney(money: Long): String {
        val dec = DecimalFormat("#,###")
        return dec.format(money)
    }

    fun isNumber(s: String): Boolean {
        return try {
            s.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }


    private fun getBudgetPlus(): Long{//실험실
        var sum: Long = 0
        for(log in MyApplication.db.allLogs){
            if(log.budget && log.sign)
                sum += log.charge
        }
        return sum
    }
    private fun getRemainBudget(): Long{//실험실
        var sum: Long = 0
        for(log in MyApplication.db.allLogs){
            if(log.budget)
                sum += if(!log.sign){
                    (0-log.charge)
                }else{
                    log.charge
                }
        }
        return MyApplication.prefs.getString("budget","0").toLong() + sum
    }
    private fun getMoney(): Long{//실험실
        var sum: Long = 0
        for(log in MyApplication.db.allLogs){
                sum += if(!log.sign){
                    (0-log.charge)
                }else{
                    log.charge
                }
        }
        return sum
    }

    private fun updateBudgetPlus(){
        MyApplication.prefs.setString("budget_plus",getBudgetPlus().toString())
    }
    private fun updateRemainBudget(){
        MyApplication.prefs.setString("remain_budget",getRemainBudget().toString())
    }
    private fun updateMoney(){
        MyApplication.prefs.setString("money",getMoney().toString())
    }

    fun updateValue(){
        updateRemainBudget()
        updateBudgetPlus()
        updateMoney()
    }

    fun logToJson(): String {
        val log = MyApplication.db.allLogs
        return makeGson.toJson(log, listType.type)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowDate(): LocalDateTime {
        return LocalDateTime.now()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowFirstDate(): LocalDateTime {
        val now = getNowDate()
        return LocalDateTime.of(now.year, now.month, 1, now.hour, now.minute)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowLastDate(): LocalDateTime {
        val now = getNowDate()
        val cal = Calendar.getInstance()
        cal.set(LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth)

        return LocalDateTime.of(
            now.year,
            now.month,
            cal.getActualMaximum(Calendar.DAY_OF_MONTH),
            now.hour,
            now.hour
        )
    }




}