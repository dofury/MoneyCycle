package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModel
import com.dofury.moneycycle.R
import com.dofury.moneycycle.adapter.ListAdapter
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.databinding.DialogLogPageBinding
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.DataUtil
import com.dofury.moneycycle.viewmodel.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class LogPageDialog(private val context: Context,private val moneyLogList: List<MoneyLog>, private val listener: LogPageDialogListener) {
    interface LogPageDialogListener {
        fun onLogUpdated(position: Int)
        fun onLogDeleted(position: Int)
    }

    private val dialog = Dialog(context)

    private lateinit var binding: DialogLogPageBinding
    @OptIn(DelicateCoroutinesApi::class)
    fun show(position: Int){
        var isDelete = false
        binding = DialogLogPageBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)

        if(moneyLogList?.get(position)!!.sign){
            binding.tvMoneySign.text="+"
        }else{
            binding.tvMoneySign.text="-"
        }
        binding.tvMoneyValue.text = DataUtil.parseMoney(moneyLogList[position].charge)
        binding.tvCategory.text = moneyLogList[position].category
        binding.tvDate.text = parseDate(moneyLogList[position].date)
        binding.evMemo.setText(moneyLogList[position].memo)
        if(moneyLogList[position].sign){//예산 포함
            binding.isBudget.text=context.getString(R.string.yes_budget)
            binding.isBudget.isChecked = moneyLogList[position].isBudget
        }else{//예산 제외
            binding.isBudget.text=context.getString(R.string.no_budget)
            binding.isBudget.isChecked = !moneyLogList[position].isBudget
        }
        binding.isBudget.setOnClickListener(View.OnClickListener {
            if(moneyLogList[position].sign){//예산 포함
                when(binding.isBudget.isChecked){
                    true -> moneyLogList[position].isBudget = true
                    false -> moneyLogList[position].isBudget = false
                }
            }else{//예산 제외
                when(binding.isBudget.isChecked){
                    true -> moneyLogList[position].isBudget = false
                    false -> moneyLogList[position].isBudget = true
                }
            }
            listener.onLogUpdated(position)

        })

        binding.ibClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.ibDelete.setOnClickListener(View.OnClickListener {
            listener.onLogDeleted(position)
            isDelete = true
            dialog.dismiss()

        })
        dialog.setOnDismissListener {
            if(!isDelete){//삭제된 로그가 아니라면 메모 업데이트
                moneyLogList[position].memo = binding.evMemo.text.toString()
                listener.onLogUpdated(position)
            }
        }

        //크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    private fun parseDate(date: String): String {
        val beforeDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm:ss")
        return formatter.format(beforeDate).replace("PM", "오후").replace("AM", "오전")
    }

}