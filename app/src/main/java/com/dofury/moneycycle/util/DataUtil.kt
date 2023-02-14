package com.dofury.moneycycle.util

import java.text.DecimalFormat

class DataUtil {

    fun parseMoney(money: Long): String {
        val dec = DecimalFormat("#,###")
        return dec.format(money)
    }

    fun isNumber(s: String): Boolean {
        return try {
            s.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }


}