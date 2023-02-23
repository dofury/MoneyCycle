package com.dofury.moneycycle

import android.app.Application
import com.dofury.moneycycle.dto.DBHelper
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.util.DataUtil
import com.dofury.moneycycle.util.PreferenceUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import java.io.File

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var db: DBHelper
        fun dataReset(){//데이터 초기화 하는 함수
            db.drop()//db 삭제
            prefs.clear()// prefs 전부삭제
        }
    }

    override fun onCreate() {
        prefs= PreferenceUtil(applicationContext)
        db = DBHelper(this)
        //MoneyLogList.list= db.allLogs

        super.onCreate()
    }
}