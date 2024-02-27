package com.dofury.moneycycle

import android.app.Application
import android.content.Context
import androidx.room.RoomDatabase
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.dto.DBHelper
import com.dofury.moneycycle.util.PreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var db: MoneyLogDatabase
        fun dataReset(context: Context){//데이터 초기화 하는 함수
            context.deleteDatabase("MoneyLog")
            prefs.clear()// prefs 전부삭제
        }
    }

    override fun onCreate() {
        prefs= PreferenceUtil(applicationContext)
        db = MoneyLogDatabase.getInstance(applicationContext)!!
        super.onCreate()
    }
}