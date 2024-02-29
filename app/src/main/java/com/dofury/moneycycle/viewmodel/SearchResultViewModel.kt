package com.dofury.moneycycle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.dto.MoneyLog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchResultViewModel: ViewModel() {

    private val _moneyLogList = MutableLiveData<MutableList<MoneyLog>>()

    val moneyLogList: MutableLiveData<MutableList<MoneyLog>>
        get() = _moneyLogList

    init {
        _moneyLogList.value = mutableListOf()
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun moneyLogListLoad(sql: String){
        GlobalScope.launch(Dispatchers.IO) {
            val logs = MyApplication.db.moneyLogDao().getQueryLog(sql).toMutableList()
            Log.d("test","data" + logs.toString())
            _moneyLogList.postValue(logs)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun moneyLogListUpdate(moneyLog: MoneyLog){
        GlobalScope.launch(Dispatchers.IO) {
            MyApplication.db.moneyLogDao().update(moneyLog)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun moneyLogListDelete(moneyLog: MoneyLog){
        GlobalScope.launch(Dispatchers.IO) {
            _moneyLogList.value?.remove(moneyLog)
            MyApplication.db.moneyLogDao().delete(moneyLog)
        }
    }

}