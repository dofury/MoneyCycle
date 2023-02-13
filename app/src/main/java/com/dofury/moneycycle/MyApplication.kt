package com.dofury.moneycycle

import android.app.Application
import com.dofury.moneycycle.dto.MoneyLogList

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs= PreferenceUtil(applicationContext)
        if(MyApplication.prefs.getList("moneyLogList")!=null)
            MoneyLogList.list = MyApplication.prefs.getList("moneyLogList")!!
        super.onCreate()
    }
}