package com.dofury.moneycycle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.repository.MoneyLogRepository
import com.dofury.moneycycle.util.DataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val repository: MoneyLogRepository
): ViewModel() {

    fun insertMoneyLog(moneyLog: MoneyLog){
        viewModelScope.launch {
            repository.insert(moneyLog)
        }
    }






}