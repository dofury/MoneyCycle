package com.dofury.moneycycle.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoneyLog(var charge: Long,
                     var sign: Boolean,
                     var category: String,
                     var date: String,
                     var memo: String,
                     var isBudget: Boolean,
                     var isServer: Boolean){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    constructor() : this (0,false,"","","",false,false)
    override fun toString(): String {
        return "$id,$charge,$sign,$category,$date,$memo,$isBudget,$isServer"
    }
}