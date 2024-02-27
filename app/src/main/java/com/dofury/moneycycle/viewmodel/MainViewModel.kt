package com.dofury.moneycycle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.util.DataUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _targetAmount = MutableLiveData<String>()
    private val _budgetAmount = MutableLiveData<String>()
    private val _budgetPlusAmount = MutableLiveData<String>()
    private val _budgetSumAmount = MutableLiveData<String>()
    private val _currentAmount = MutableLiveData<String>()
    private val _remainBudgetAmount = MutableLiveData<String>()
    private val _remainDays = MutableLiveData<String>()

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
    init {
        dataInit()
    }

    override fun onCleared() {
        super.onCleared()
    }
    private fun dataInit(){
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

    fun addData() {
        _targetAmount.value = (_targetAmount.value ?: "0") + 1//null이면 0에서 더하고 null이 아니면 기존 값에서 더함
    }
}