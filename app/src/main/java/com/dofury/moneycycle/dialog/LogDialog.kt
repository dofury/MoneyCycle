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
import com.dofury.moneycycle.databinding.DialogLogPageBinding
import java.text.SimpleDateFormat

class LogDialog(private val context: AppCompatActivity) {
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
        binding.tvMoneyValue.text = moneyLog.charge.toString()
        binding.tvCategory.text = moneyLog.category
        binding.tvDate.text = parseDate(moneyLog.date)

        binding.ibClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.ibDelete.setOnClickListener(View.OnClickListener {
            adapter.notifyItemRemoved(position)
            adapter.notifyDataSetChanged()
            MoneyLogList.list.removeAt(position)
            MyApplication.prefs.setList("moneyLogList", MoneyLogList.list)
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