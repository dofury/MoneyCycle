package com.dofury.moneycycle.util

import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.dto.MoneyLog
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.text.DecimalFormat

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
            if(log.isBudget && log.sign)
                sum += log.charge
        }
        return sum
    }
    private fun getRemainBudget(): Long{//실험실
        var sum: Long = 0
        for(log in MyApplication.db.allLogs){
            if(log.isBudget)
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

    fun logToCSV(){

    }



}