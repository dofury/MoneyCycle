package com.dofury.moneycycle.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.dto.MoneyLog
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

object DataUtil {

    private val makeGson = GsonBuilder().create()
    private var listType: TypeToken<MutableList<MoneyLog>> =
        object : TypeToken<MutableList<MoneyLog>>() {}
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

    fun isBudgetCycle(i: Int): Boolean {
        return i in 1..31
    }


   fun getBudgetPlus(): Long {//실험실
        var sum: Long = 0
            for (log in MyApplication.db.moneyLogDao().getAll()) {
                if (log.isBudget && log.sign)
                    sum += log.charge
            }
        return sum
    }

    fun getPercent(all: String, now: String): Int {
        //val df = DecimalFormat("#.##")
        var percent : Int = 0
        val value = now.toDouble()/all.toDouble() *100
        percent = value.toInt()

        return percent
    }

    fun getRemainBudget(): Long {//실험실
        var sum: Long = 0
            for (log in MyApplication.db.moneyLogDao().getAll()) {
                if (log.isBudget)
                    sum += if (!log.sign) {
                        (0 - log.charge)
                    } else {
                        log.charge
                    }
            }

        return MyApplication.prefs.getString("budgetAmount", "0").toLong() + sum
    }

    fun getMoney(): Long{
        var sum: Long = 0
        for (log in MyApplication.db.moneyLogDao().getAll()) {
            sum += if (!log.sign) {
                (0 - log.charge)
            } else {
                log.charge
            }
        }
        return sum
    }




    private fun updateBudgetPlus() {
        MyApplication.prefs.setString("budgetPlusAmount", getBudgetPlus().toString())
    }

    private fun updateRemainBudget() {
        MyApplication.prefs.setString("remainBudgetAmount", getRemainBudget().toString())
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateMoney() {
        GlobalScope.launch(Dispatchers.Main) {
            //val money = getMoney().await().toString()
            //MyApplication.prefs.setString("currentAmount", money)
        }
    }

    fun updateValue() {
        updateRemainBudget()
        updateBudgetPlus()
        //updateMoney()
    }

    fun budgetCheck(context: Context) {
        val lastMonth = MyApplication.prefs.getString(
            "lastMonth",
            Calendar.getInstance().get(Calendar.MONTH).toString()
        ).toInt()
        val lastYear = MyApplication.prefs.getString(
            "lastYear",
            Calendar.getInstance().get(Calendar.YEAR).toString()
        ).toInt()

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        if (lastYear < currentYear || (lastYear == currentYear && lastMonth < currentMonth)) {

            // 달이 변경되었으므로 초기화 작업 수행

            for (log in MyApplication.db.moneyLogDao().getResetBudgetLogs()) {//예산 초기화 진행
                log.isBudget = false
                MyApplication.db.moneyLogDao().update(log)
            }

            // 현재 달을 저장
            MyApplication.prefs.setString("lastMonth", currentMonth.toString())
            MyApplication.prefs.setString("lastYear", currentYear.toString())

            Toast.makeText(context, "남은 예산 초기화 작업 실행", Toast.LENGTH_SHORT).show()
        }

    }

    fun logToJson(): String {
        val log = MyApplication.db.moneyLogDao().getAll()
        return makeGson.toJson(log, listType.type)
    }

    fun jsonToLog(json: String): MutableList<MoneyLog>? {
        return makeGson.fromJson(json, listType.type)
    }

    fun parseDate(date: String): String {
        val beforeDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm")
        return formatter.format(beforeDate).replace("PM", "오후").replace("AM", "오전")
    }

    fun getNowDate(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun getNowFirstDate(): LocalDateTime {
        val now = getNowDate()
        return LocalDateTime.of(now.year, now.month, 1, now.hour, now.minute)
    }

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

    @SuppressLint("Range")
    fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
        var displayName: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }

        return displayName
    }


}