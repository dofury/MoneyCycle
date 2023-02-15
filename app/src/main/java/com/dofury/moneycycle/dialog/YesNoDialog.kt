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
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.adapter.BudgetPlusAdapter
import com.dofury.moneycycle.databinding.DialogLogPageBinding
import com.dofury.moneycycle.databinding.DialogLogSetBinding
import com.dofury.moneycycle.databinding.DialogYesNoBinding
import com.dofury.moneycycle.util.DataUtil
import java.text.SimpleDateFormat

class YesNoDialog(private val context: AppCompatActivity) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialogYesNoBinding
    @RequiresApi(Build.VERSION_CODES.O)
    fun show(list: MutableList<MoneyLog>,adapter: BudgetPlusAdapter,position: Int){
        binding =DialogYesNoBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)


        buttonEvent(list,adapter,position)
        //크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun buttonEvent(list: MutableList<MoneyLog>,adapter: BudgetPlusAdapter,position: Int){

        binding.ibClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.btnNo.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.btnYes.setOnClickListener(View.OnClickListener {

            list[position].is_budget = false
            MyApplication.db.updateLog( list[position])//db 반영
            list.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyDataSetChanged()

            dialog.dismiss()
        })
    }

}