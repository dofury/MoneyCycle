package com.dofury.moneycycle

import android.app.Application
import com.dofury.moneycycle.dto.DBHelper
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.util.PreferenceUtil

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var db: DBHelper
    }

    override fun onCreate() {
        prefs= PreferenceUtil(applicationContext)
        db = DBHelper(this)
        MoneyLogList.list= db.allLogs
        super.onCreate()
    }
}