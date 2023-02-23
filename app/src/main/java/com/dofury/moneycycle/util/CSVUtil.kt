package com.dofury.moneycycle.util

import com.dofury.moneycycle.BuildConfig
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class CSVUtil(private val filePath: String) {
    fun writeAllData(fileName: String, dataList: ArrayList<Array<String>>) {
        try {
            FileWriter(File("$filePath/$fileName")).use { fw ->
                //writeAll()을 이용한 리스트 데이터 등록
                CSVWriter(fw).use {
                    it.writeAll(dataList)
                }
            }
        } catch (e: IOException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }
}