package com.dofury.moneycycle

import android.app.Application
import android.content.Context
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.util.PreferenceUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs= PreferenceUtil(applicationContext)
        db = MoneyLogDatabase.getInstance(applicationContext)!!
    }
    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var db: MoneyLogDatabase
        fun dataReset(context: Context){//데이터 초기화 하는 함수
            context.deleteDatabase("MoneyLog")
            prefs.clear()// prefs 전부삭제
        }
    }


}