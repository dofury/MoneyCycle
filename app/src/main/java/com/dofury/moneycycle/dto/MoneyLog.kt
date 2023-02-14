package com.dofury.moneycycle.dto

data class MoneyLog(var uid: Int,var charge: Int, var sign: Boolean, var category: String, var date: String,var is_server: Boolean)