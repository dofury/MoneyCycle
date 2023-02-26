package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.os.Build
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.adapter.ListAdapter
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.InitActivity
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.adapter.BudgetPlusAdapter
import com.dofury.moneycycle.databinding.DialogInputBinding
import com.dofury.moneycycle.databinding.DialogLogPageBinding
import com.dofury.moneycycle.databinding.DialogLogSetBinding
import com.dofury.moneycycle.databinding.DialogYesNoBinding
import com.dofury.moneycycle.util.DataUtil
import java.text.SimpleDateFormat

class InputDialog(private val context: AppCompatActivity) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialogInputBinding
    @RequiresApi(Build.VERSION_CODES.O)
    fun show(tag: String){
        binding =DialogInputBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)

        init(tag)
        buttonEvent(tag)
        //크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun buttonEvent(tag: String){

        binding.btnNo.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.btnYes.setOnClickListener(View.OnClickListener {

            try {
                when(tag){
                    "init_money" ->{
                        context as InitActivity
                        context.setMoney(
                            binding.etValue.text.toString())
                    }
                    "init_goal" -> {
                        context as InitActivity
                        context.setGoal(
                            binding.etValue.text.toString()
                        )
                    }
                    "init_budget" -> {
                        context as InitActivity
                        context.setBudget(
                            binding.etValue.text.toString()
                        )
                    }
                    "init_budget_cycle" -> {
                        context as InitActivity
                        context.setBudgetCycle(
                            binding.etValue.text.toString()
                        )
                    }
                    "budget_cycle" -> {
                        if(DataUtil.isBudgetCycle(binding.etValue.text.toString().toInt())){
                            MyApplication.prefs.setString(tag,binding.etValue.text.toString())
                        }else{
                            throw Exception("오버플로우")
                        }
                    }
                    "budget" -> {
                        MyApplication.prefs.setString(tag,binding.etValue.text.toString())
                    }
                    "goal" -> {
                        MyApplication.prefs.setString(tag,binding.etValue.text.toString())
                    }

                }
                if(DataUtil.isNumber(binding.etValue.text.toString())){
                    Toast.makeText(context,"설정 완료", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }else{
                    Toast.makeText(context,"숫자를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(context,"제대로 적어주세요", Toast.LENGTH_SHORT).show()
            }


        })
    }
    private fun init(tag: String){
        when(tag){
            "init_money" -> binding.tvTitle.text = context.getString(R.string.money_setting)
            "init_goal" -> binding.tvTitle.text = context.getString(R.string.goal_setting)
            "init_budget" -> binding.tvTitle.text = context.getString(R.string.budget_setting)
            "init_budget_cycle" -> binding.tvTitle.text = context.getString(R.string.budget_cycle_setting_title)
            "budget_cycle" -> {
                binding.etValue.hint = MyApplication.prefs.getString(tag,"0")
                binding.tvTitle.text = context.getString(R.string.budget_cycle_setting_title)
                binding.etValue.inputType = InputType.TYPE_CLASS_NUMBER//숫자 패드
            }
            "budget" -> {
                binding.etValue.hint = DataUtil.parseMoney(MyApplication.prefs.getString(tag,"0").toLong())
                binding.tvTitle.text = context.getString(R.string.budget_charge)
            }
            "goal" -> {
                binding.etValue.hint = DataUtil.parseMoney(MyApplication.prefs.getString(tag,"0").toLong())
                binding.tvTitle.text = context.getString(R.string.goal_charge)
            }
        }

    }

}