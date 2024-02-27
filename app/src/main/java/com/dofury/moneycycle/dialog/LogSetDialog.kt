package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.databinding.DialogLogSetBinding
import com.dofury.moneycycle.dto.MoneyLog

class LogSetDialog(private val context: LogActivity) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialogLogSetBinding
    fun show(moneyLog: MoneyLog){
        binding = DialogLogSetBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)


        buttonEvent(moneyLog)
        //크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    fun buttonEvent(moneyLog: MoneyLog){
        if(moneyLog.sign){//예산 포함
            binding.isBudget.text=context.getString(R.string.yes_budget)
        }else{//예산 제외
            binding.isBudget.text=context.getString(R.string.no_budget)
        }
        binding.ibClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.btnOk.setOnClickListener(View.OnClickListener {

            if(moneyLog.sign){//예산 포함
                moneyLog.isBudget = binding.isBudget.isChecked
            }else{//예산 제외
                moneyLog.isBudget = !binding.isBudget.isChecked
            }
            moneyLog.memo = binding.etMemo.text.toString()
            context.submitLog()
            dialog.dismiss()
        })
    }

}