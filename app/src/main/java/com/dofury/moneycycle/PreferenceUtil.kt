package com.dofury.moneycycle

import android.content.Context
import android.content.SharedPreferences
import com.dofury.moneycycle.dto.MoneyLog
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name",Context.MODE_PRIVATE)
    private val makeGson = GsonBuilder().create()

    private var list : MutableList<MoneyLog> = mutableListOf()
    private var listType : TypeToken<MutableList<MoneyLog>> = object : TypeToken<MutableList<MoneyLog>>() {}

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }
    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }
    fun getList(key: String): MutableList<MoneyLog>? {
        val json = prefs.getString(key,null)
        return if(json!=null)
            makeGson.fromJson(json, listType.type) else null
    }
    fun setList(key: String, list: MutableList<MoneyLog>) {
        var str = makeGson.toJson(list,listType.type)
        prefs.edit().putString(key, str).apply()
    }
    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
    fun setBoolean(key: String, bool: Boolean) {
        prefs.edit().putBoolean(key, bool).apply()
    }
}