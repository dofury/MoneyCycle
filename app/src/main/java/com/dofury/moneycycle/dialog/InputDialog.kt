package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputBinding
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

class InputDialog(private val context: InitActivity) {
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
            when(tag){
                "money" -> context.setMoney(
                    binding.etValue.text.toString())
                "goal" -> context.setGoal(
                    binding.etValue.text.toString())
                "budget" -> context.setBudget(
                    binding.etValue.text.toString())
            }
            dialog.dismiss()
        })
    }
    private fun init(tag: String){

        when(tag){
            "money" -> binding.tvTitle.text = context.getString(R.string.money_setting)
            "goal" -> binding.tvTitle.text = context.getString(R.string.goal_setting)
            "budget" -> binding.tvTitle.text = context.getString(R.string.budget_setting)
        }

    }

}