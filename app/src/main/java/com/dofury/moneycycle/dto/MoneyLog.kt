package com.dofury.moneycycle.dto

data class MoneyLog(var uid: Int, var charge: Long, var sign: Boolean, var category: String, var date: String,
                    var memo: String, var budget: Boolean, var server: Boolean){
    constructor() : this (0,0,false,"","","",false,false)
}