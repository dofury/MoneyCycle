package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.dofury.moneycycle.adapter.ListAdapter
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.activity.MainActivity
import com.dofury.moneycycle.adapter.BudgetPlusAdapter
import com.dofury.moneycycle.databinding.DialogLogPageBinding
import com.dofury.moneycycle.databinding.DialogLogSetBinding
import com.dofury.moneycycle.databinding.DialogYesNoBinding
import com.dofury.moneycycle.fragment.mainActivity
import com.dofury.moneycycle.util.DataUtil
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import kotlin.system.exitProcess

class ResetDialog(private val context: AppCompatActivity) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialogYesNoBinding
    @RequiresApi(Build.VERSION_CODES.O)
    fun show(){
        binding =DialogYesNoBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)


        init()
        buttonEvent()
        //크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun buttonEvent(){
        binding.btnNo.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.btnYes.setOnClickListener(View.OnClickListener {

            MyApplication.dataReset()

            Snackbar.make(binding.root,"초기화 되었습니다", Snackbar.LENGTH_SHORT).show()

            dialog.dismiss()

            mainActivity.restart()//재시작
        })
    }
    private fun init(){
        binding.tvContent.text = context.getString(R.string.reset_question)
    }

}