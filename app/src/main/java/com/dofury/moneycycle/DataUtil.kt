package com.dofury.moneycycle

import java.text.DecimalFormat

class DataUtil {

    fun parseMoney(money: Int): String {
        val dec = DecimalFormat("#,###")
        return dec.format(money)
    }

}