package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.adapter.BudgetPlusAdapter
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.databinding.DialogYesNoBinding
import com.dofury.moneycycle.dto.MoneyLog

class BudgetPlusDialog(private val context: AppCompatActivity) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialogYesNoBinding
    private val db = MoneyLogDatabase.getInstance(context)
    fun show(list: List<MoneyLog>, adapter: BudgetPlusAdapter, position: Int){
        binding =DialogYesNoBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)


        init()
        buttonEvent(list.toMutableList(),adapter,position)
        //크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    fun buttonEvent(list: MutableList<MoneyLog>, adapter: BudgetPlusAdapter, position: Int){
        binding.btnNo.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.btnYes.setOnClickListener(View.OnClickListener {

            list[position].isBudget = false
            db!!.moneyLogDao().update( list[position])//db 반영
            list.removeAt(position)

            //MoneyLogList.list = MyApplication.db.allLogs

            adapter.notifyItemRemoved(position)
            adapter.notifyDataSetChanged()

            dialog.dismiss()
        })
    }
    private fun init(){
        binding.tvContent.text = context.getString(R.string.budget_question)
    }

}