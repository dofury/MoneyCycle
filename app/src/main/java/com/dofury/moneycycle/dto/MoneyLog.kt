package com.dofury.moneycycle.dto

data class MoneyLog(var uid: Int,var charge: Long, var sign: Boolean, var category: String, var date: String,
                    var memo: String,var is_budget: Boolean,var is_server: Boolean)