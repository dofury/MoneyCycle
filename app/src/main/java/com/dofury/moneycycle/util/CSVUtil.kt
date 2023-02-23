package com.dofury.moneycycle.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.dofury.moneycycle.dto.MoneyLog
import java.io.File


object CsvWriter {
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
}
