package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.Adapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.adapter.ListAdapter
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.databinding.DialogLogPageBinding
import com.dofury.moneycycle.fragment.ListFragment
import com.dofury.moneycycle.fragment.mainActivity
import com.dofury.moneycycle.util.DataUtil
import java.text.SimpleDateFormat

class LogPageDialog(private val context: AppCompatActivity) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialogLogPageBinding
    @RequiresApi(Build.VERSION_CODES.O)
    fun show(moneyLogList: MutableList<MoneyLog>, adapter: ListAdapter, position: Int){
        binding = DialogLogPageBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)

        if(moneyLogList[position].sign){
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
            binding.isBudget.isChecked = moneyLogList[position].budget
        }else{//예산 제외
            binding.isBudget.text=context.getString(R.string.no_budget)
            binding.isBudget.isChecked = !moneyLogList[position].budget
        }
        binding.isBudget.setOnClickListener(View.OnClickListener {
            if(moneyLogList[position].sign){//예산 포함
                when(binding.isBudget.isChecked){
                    true -> moneyLogList[position].budget = true
                    false -> moneyLogList[position].budget = false
                }
            }else{//예산 제외
                when(binding.isBudget.isChecked){
                    true -> moneyLogList[position].budget = false
                    false -> moneyLogList[position].budget = true
                }
            }
            MyApplication.db.updateLog(moneyLogList[position])//db 반영
        })

        binding.ibClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.ibDelete.setOnClickListener(View.OnClickListener {
            MyApplication.db.deleteLog(moneyLogList[position])
            moneyLogList.removeAt(position)

            adapter.notifyItemRemoved(position)

            DataUtil.updateValue()//자산, 예산 최신화

            dialog.dismiss()

        })
        dialog.setOnDismissListener {
            moneyLogList[position].memo = binding.evMemo.text.toString()
            MyApplication.db.updateLog(moneyLogList[position])
            ListFragment.init()
        }

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