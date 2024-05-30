package com.dofury.moneycycle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.dao.QueryCondition
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.repository.MoneyLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val repository: MoneyLogRepository
): ViewModel() {

    private val _moneyLogList = MutableLiveData<List<MoneyLog>>()

    val moneyLogList: MutableLiveData<List<MoneyLog>>
        get() = _moneyLogList

    init {
        _moneyLogList.value = mutableListOf()
    }
    fun moneyLogListLoad(query: QueryCondition){
        viewModelScope.launch {
            val logs = repository.getConditionLog(query)
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
            //_moneyLogList.value?.remove(moneyLog)
            MyApplication.db.moneyLogDao().delete(moneyLog)
        }
    }

}