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
class MainViewModel @Inject constructor(
    private val repository: MoneyLogRepository
): ViewModel() {
    private val _targetAmount = MutableLiveData<String>()
    private val _budgetAmount = MutableLiveData<String>()
    private val _budgetPlusAmount = MutableLiveData<String>()
    private val _budgetSumAmount = MutableLiveData<String>()
    private val _currentAmount = MutableLiveData<String>()
    private val _remainBudgetAmount = MutableLiveData<String>()
    private val _remainDays = MutableLiveData<String>()
    private val _moneyLogList = MutableLiveData<MutableList<MoneyLog>>()

    val targetAmount: MutableLiveData<String>
        get() = _targetAmount

    val budgetPlusAmount: MutableLiveData<String>
        get() = _budgetPlusAmount

    val budgetSumAmount: MutableLiveData<String>
        get() = _budgetSumAmount

    val currentAmount: MutableLiveData<String>
        get() = _currentAmount

    val remainBudgetAmount: MutableLiveData<String>
        get() = _remainBudgetAmount

    val moneyLogList: MutableLiveData<MutableList<MoneyLog>>
        get() = _moneyLogList
    init {
        dataInit()
    }


    override fun onCleared() {
        super.onCleared()
    }
    private fun dataInit(){
        moneyLogListLoad()
        _targetAmount.value = MyApplication.prefs.getString("targetAmount","0")
        _budgetAmount.value = MyApplication.prefs.getString("budgetAmount","0")
        _budgetPlusAmount.value = MyApplication.prefs.getString("budgetPlusAmount","0")
        _currentAmount.value = MyApplication.prefs.getString("currentAmount","2")
        _remainBudgetAmount.value = MyApplication.prefs.getString("remainBudgetAmount","0")

        _budgetSumAmount.value = (_budgetAmount.value?.toLong()
            ?.plus(budgetPlusAmount.value!!.toLong())).toString()
        _remainDays.value = (DataUtil.getNowLastDate().dayOfMonth- DataUtil.getNowDate().dayOfMonth+1).toString()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun currentAmountUpdate(){
        GlobalScope.launch(Dispatchers.IO) {
            _currentAmount.postValue(DataUtil.getMoney().toString())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun budgetPlusAmountUpdate(){
        GlobalScope.launch(Dispatchers.IO) {
            val data = DataUtil.getBudgetPlus().toString()
            _budgetPlusAmount.postValue(data)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun remainBudgetAmountUpdate(){
        GlobalScope.launch(Dispatchers.IO) {
            val data = DataUtil.getRemainBudget().toString()
            _remainBudgetAmount.postValue(data)
        }
    }

    fun moneyLogListLoad(){
        val month = YearMonth.from(LocalDateTime.now())
        val firstDate = month.atDay(1)
        val lastDate = month.atEndOfMonth()
        viewModelScope.launch {
            val moneyLogs = repository.getDateBetweenLog(firstDate.toString(),lastDate.toString()).toMutableList()
            Log.d("test","길이" + moneyLogs.size)
            _moneyLogList.postValue(moneyLogs)
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

    fun moneyLogListDateLoad(date: LocalDateTime){
        viewModelScope.launch {
            val dateFormat = date.format(DateTimeFormatter.ofPattern("yyyy-MM"))
            val moneyLogs = repository.getDateLog(dateFormat).toMutableList()
            _moneyLogList.postValue(moneyLogs)
        }
    }

    fun addData() {
        _targetAmount.value = (_targetAmount.value ?: "0") + 1//null이면 0에서 더하고 null이 아니면 기존 값에서 더함
    }
}