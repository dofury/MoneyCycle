package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.adapter.ListAdapter
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.databinding.DialogLogPageBinding
import com.dofury.moneycycle.dto.DBHelper
import com.dofury.moneycycle.util.DataUtil
import java.text.SimpleDateFormat

class LogPageDialog(private val context: AppCompatActivity) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialogLogPageBinding
    @RequiresApi(Build.VERSION_CODES.O)
    fun show(moneyLog: MoneyLog, adapter: ListAdapter, position: Int){
        binding = DialogLogPageBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)

        if(moneyLog.sign){
            binding.tvMoneySign.text="+"
        }else{
            binding.tvMoneySign.text="-"
        }
        binding.tvMoneyValue.text = DataUtil().parseMoney(moneyLog.charge)
        binding.tvCategory.text = moneyLog.category
        binding.tvDate.text = parseDate(moneyLog.date)
        binding.evMemo.setText(moneyLog.memo)
        if(moneyLog.sign){//예산 포함
            binding.isBudget.text=context.getString(R.string.yes_budget)
            binding.isBudget.isChecked = moneyLog.is_budget
        }else{//예산 제외
            binding.isBudget.text=context.getString(R.string.no_budget)
            binding.isBudget.isChecked = !moneyLog.is_budget
        }
        binding.isBudget.setOnClickListener(View.OnClickListener {
            if(moneyLog.sign){//예산 포함
                when(binding.isBudget.isChecked){
                    true -> moneyLog.is_budget = true
                    false -> moneyLog.is_budget = false
                }
            }else{//예산 제외
                when(binding.isBudget.isChecked){
                    true -> moneyLog.is_budget = false
                    false -> moneyLog.is_budget = true
                }
            }
            MyApplication.db.updateLog(moneyLog)//db 반영
        })

        binding.ibClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.ibDelete.setOnClickListener(View.OnClickListener {
            adapter.notifyItemRemoved(position)
            adapter.notifyDataSetChanged()
            MoneyLogList.list.removeAt(position)//지워도 됨
            MyApplication.db.deleteLog(moneyLog)

            DataUtil().updateValue()//자산, 예산 최신화

            dialog.dismiss()

        })

        //크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDate(date: String): String {
        val beforeDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm:ss")
        return formatter.format(beforeDate).replace("PM", "오후").replace("AM", "오전")
    }

}