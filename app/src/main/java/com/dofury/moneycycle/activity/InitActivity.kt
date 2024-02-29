package com.dofury.moneycycle.activity

import android.content.Intent

import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R

import com.dofury.moneycycle.databinding.ActivityInitBinding
import com.dofury.moneycycle.dialog.InputDialog
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.DataUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitBinding
    private var moneyBuffer: String= ""
    private var goalBuffer: String= ""
    private var budgetBuffer: String= ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonEvent()
    }

    private fun buttonEvent() {
        binding.btnInitSubmit.setOnClickListener(View.OnClickListener {
            if((DataUtil.isNumber(moneyBuffer) && moneyBuffer.isNotBlank())&&
                (DataUtil.isNumber(goalBuffer) && goalBuffer.isNotBlank()) &&
                (DataUtil.isNumber(budgetBuffer) && budgetBuffer.isNotBlank())&&
                (binding.tvInitBudgetCycle.text.isNotBlank() &&
                        DataUtil.isBudgetCycle(binding.tvInitBudgetCycle.text.toString().toInt()))){

                MyApplication.prefs.setString("targetAmount",goalBuffer)
                MyApplication.prefs.setString("budgetAmount",budgetBuffer)

                MyApplication.prefs.setString("remainBudgetAmount",budgetBuffer)
                MyApplication.prefs.setString("budget_cycle",binding.tvInitBudgetCycle.text.toString())
                MyApplication.prefs.setBoolean("is_init",false)

                createLog()//자산 로그 설정

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

                }else{
                    Snackbar.make(binding.root,"제대로 입력해주세요",Snackbar.LENGTH_SHORT).show()
            }

        })
        val buttons = mapOf("init_money" to binding.llMoney, "init_goal" to binding.llGoal, "init_budget" to binding.llBudget, "init_budget_cycle" to binding.llBudgetCycle)
        buttons.forEach { it
            val map = it
            it.value.setOnClickListener(View.OnClickListener {
                val dialog = InputDialog(this)
                dialog.show(map.key)
            })

        }

    }
    fun setMoney(value: String){
        binding.tvInitMoneyValue.text = DataUtil.parseMoney(value.toLong())
        moneyBuffer = value
    }
    fun setBudget(value: String){
        binding.tvInitBudgetValue.text = DataUtil.parseMoney(value.toLong())
        budgetBuffer = value
    }
    fun setGoal(value: String){
        binding.tvInitGoalValue.text = DataUtil.parseMoney(value.toLong())
        goalBuffer = value
    }
    fun setBudgetCycle(value: String){
        binding.tvInitBudgetCycle.text = value
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createLog(){
        val moneyLog = MoneyLog(
            moneyBuffer.toLong(),
            sign = true,
            getString(R.string.change),
            "",
            "기본 자산",
            isBudget = false,
            isServer = false
        )


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        val money = MyApplication.prefs.getString("currentAmount","0").toLong()

        moneyLog.date = formatted

        MyApplication.prefs.setString("currentAmount",money.toString())//자산 돈 반영

        GlobalScope.launch(Dispatchers.IO) {
            MyApplication.db.moneyLogDao().insert(moneyLog)//db 추가
        }

    }

}