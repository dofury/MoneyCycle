package com.dofury.moneycycle.util

import android.content.Context
import android.os.Build
import android.os.Environment
import com.dofury.moneycycle.dto.MoneyLog
import com.opencsv.CSVReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.IOException


object CSVWriter {
    fun writeToFile(moneyLogs: List<MoneyLog>, fileName: String,context: Context) {
        val header = listOf("UID", "Charge", "Sign", "Category", "Date", "Memo", "Budget", "Server")
        val csvData = moneyLogs.joinToString("\n") { "${it.uid},${it.charge},${it.sign},${it.category},${it.date},${it.memo},${it.budget},${it.server}" }
        val csvString = "${header.joinToString(",")}\n$csvData"
        val filePath: String =
            context.filesDir.path
                .toString() + "/$fileName"

        File(filePath).printWriter().use { out ->
            out.write(csvString)
        }
    }
    fun isExternalStorageWritable() : Boolean{
        when{
            //외부저장장치 상태가 media-mounted면 사용가능
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> return true
            else -> return false
        }
    }

}
