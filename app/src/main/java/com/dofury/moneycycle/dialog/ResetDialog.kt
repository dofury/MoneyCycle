package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.MainActivity
import com.dofury.moneycycle.databinding.DialogYesNoBinding
import com.google.android.material.snackbar.Snackbar

class ResetDialog(private val context: Context) {
    private val dialog = Dialog(context)
    private lateinit var mainActivity: MainActivity
    private lateinit var binding: DialogYesNoBinding
    fun show(){
        mainActivity = context as MainActivity
        binding =DialogYesNoBinding.inflate(mainActivity.layoutInflater)

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

            MyApplication.dataReset(context)

            Snackbar.make(binding.root,"초기화 되었습니다", Snackbar.LENGTH_SHORT).show()

            dialog.dismiss()

            mainActivity.restart()//재시작
        })
    }
    private fun init(){
        binding.tvContent.text = context.getString(R.string.reset_question)
    }

}