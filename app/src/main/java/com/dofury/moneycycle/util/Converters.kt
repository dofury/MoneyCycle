package com.dofury.moneycycle.util

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }
}
