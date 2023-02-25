package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.activity.LogSearchActivity
import com.dofury.moneycycle.databinding.DialogDatePickerBinding
import java.time.LocalDateTime

class DatePickerDialog(private val context: AppCompatActivity) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialogDatePickerBinding
    @RequiresApi(Build.VERSION_CODES.O)
    fun show(tag: String){
        binding = DialogDatePickerBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)
        binding.datePicker.focusable = View.FOCUSABLE_AUTO
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

        binding.btnCancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        binding.btnOk.setOnClickListener(View.OnClickListener {
            val date = LocalDateTime.of(binding.datePicker.year,
                binding.datePicker.month+1,
            binding.datePicker.dayOfMonth,
            0,
            0)

            when(tag){
                "log_search_0" ->{
                    (context as LogSearchActivity).setDate(0,date)
                }
                "log_search_1" ->{
                    (context as LogSearchActivity).setDate(1,date)
                }
            }
            dialog.dismiss()
        })
    }

}