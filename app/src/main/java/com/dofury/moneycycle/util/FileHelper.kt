package com.dofury.moneycycle.util

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.fragment.ListFragment
import com.opencsv.CSVWriter
import java.io.*


class FileHelper(private val context: Context) {
    private val db = MoneyLogDatabase.getInstance(context)
    fun writeCSV(activity: AppCompatActivity, moneyLogs: List<MoneyLog>, uri: Uri) {
        activity.contentResolver.openOutputStream(uri).use { outputStream ->
            val writer = OutputStreamWriter(outputStream)
            val csvWriter = CSVWriter(writer)

            // Write Header
            val header =
                arrayOf("UID", "Charge", "Sign", "Category", "Date", "Memo", "Budget", "Server")
            csvWriter.writeNext(header)
            /*            val csvData = moneyLogs.joinToString("\n") { "${it.uid},${it.charge},${it.sign},${it.category},${it.date},${it.memo},${it.budget},${it.server}" }
                        val csvString = "${header.joinToString(",")}\n$csvData"*/
            // Write Data
            moneyLogs.forEach { item ->
                csvWriter.writeNext(item.toString().split(",").toTypedArray())
            }
            csvWriter.close()
        }

    }

    fun readCSV(activity: AppCompatActivity, uri: Uri) {
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
                db!!.moneyLogDao().insertAll(context,logs)
                reader.close()
            }

        }
    }

}
