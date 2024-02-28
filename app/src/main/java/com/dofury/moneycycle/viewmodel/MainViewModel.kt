package com.dofury.moneycycle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.DataUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth

class MainViewModel: ViewModel() {
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
    fun BudgetPlusAmountUpdate(){
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

    @OptIn(DelicateCoroutinesApi::class)
    fun moneyLogListLoad(){
        val month = YearMonth.from(LocalDateTime.now())
        val firstDate = month.atDay(1)
        val lastDate = month.atEndOfMonth()
        GlobalScope.launch(Dispatchers.IO) {
            _moneyLogList.postValue(MyApplication.db.moneyLogDao().getDateLog(firstDate.toString(),lastDate.toString()).toMutableList())
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

    fun addData() {
        _targetAmount.value = (_targetAmount.value ?: "0") + 1//null이면 0에서 더하고 null이 아니면 기존 값에서 더함
    }
}