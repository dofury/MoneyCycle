package com.dofury.moneycycle.util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.repository.MoneyLogRepository
import com.dofury.moneycycle.viewmodel.MainViewModel
import com.opencsv.CSVWriter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*


class FileHelper(private val context: Context) {
    fun writeCSV(activity: AppCompatActivity, moneyLogs: List<MoneyLog>, uri: Uri,resultCallback: ()-> Unit) {
        activity.contentResolver.openOutputStream(uri).use { outputStream ->
            val writer = OutputStreamWriter(outputStream)
            val csvWriter = CSVWriter(writer)

            // Write Header
            val header =
                arrayOf("ID", "Charge", "Sign", "Category", "Date", "Memo", "IsBudget", "IsServer")
            csvWriter.writeNext(header)

            // Write Data
            moneyLogs.forEach { item ->
                csvWriter.writeNext(item.toString().split(",").toTypedArray())
            }
            csvWriter.close()
        }
        resultCallback()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun readCSV(activity: AppCompatActivity,viewModel: MainViewModel, uri: Uri,resultCallback: ()-> Unit) {
        val displayName = DataUtil.getFileName(activity.contentResolver, uri)
        if (displayName.toString().contains(".csv")) {//csv 파일이 아니라면
            activity.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val logs: MutableList<MoneyLog> = mutableListOf()
                var line: String? = reader.readLine()
                var count = 0
                while (line != null) {
                    val tokens = line.split(",")
                    if (count > 0) {
                        val log = MoneyLog()
                        log.id = tokens[0].replace("\"", "").toInt()
                        log.charge = tokens[1].replace("\"", "").toLong()
                        log.sign = tokens[2].replace("\"", "").toBoolean()
                        log.category = tokens[3].replace("\"", "")
                        log.date = tokens[4].replace("\"", "")
                        log.memo = tokens[5].replace("\"", "")
                        log.isBudget = tokens[6].replace("\"", "").toBoolean()
                        log.isServer = tokens[7].replace("\"", "").toBoolean()
                        logs.add(log)
                    }
                    line = reader.readLine()
                    count++
                }
                GlobalScope.launch(Dispatchers.IO){
                    viewModel.addCSVLog(logs,activity)
                }
                reader.close()
            }

        }
        resultCallback()
    }

}
