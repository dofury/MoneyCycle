package com.dofury.moneycycle.util

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.dofury.moneycycle.dto.MoneyLog
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.OutputStream


object CSVWriter {
    fun writeToFile(moneyLogs: List<MoneyLog>, fileName: String,context: Context) {
        val header = listOf("UID", "Charge", "Sign", "Category", "Date", "Memo", "Budget", "Server")
        val csvData = moneyLogs.joinToString("\n") { "${it.uid},${it.charge},${it.sign},${it.category},${it.date},${it.memo},${it.budget},${it.server}" }
        val csvString = "${header.joinToString(",")}\n$csvData"
/*        val filePath: String =
            context.filesDir.path
                .toString() + "/$fileName"*/
        val fileDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val getDownload = fileDownload.path
        val filePath: String = "$getDownload/$fileName"

        try {
            File(filePath).printWriter().use { out ->
                out.write(csvString)
            }
        }catch (e: Exception){
            Toast.makeText(context,"파일을 저장할 수 없습니다",Toast.LENGTH_SHORT).show()
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
