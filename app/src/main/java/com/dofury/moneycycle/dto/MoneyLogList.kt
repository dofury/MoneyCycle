package com.dofury.moneycycle.dto

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.activity.MainActivity
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.DataUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MoneyLogList {
    var list: MutableList<MoneyLog> = mutableListOf()
}
