package com.dofury.moneycycle.dialog

import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.databinding.DialoglMoneySetBinding
import com.dofury.moneycycle.util.DataUtil

class MoneySetDialog(private val context: AppCompatActivity,private val tag: String) {
    private val dialog = Dialog(context)

    private lateinit var binding: DialoglMoneySetBinding
    @RequiresApi(Build.VERSION_CODES.O)
    fun show(){
        binding = DialoglMoneySetBinding.inflate(context.layoutInflater)

        dialog.setContentView(binding.root)

        buttonEvent()

        //크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    private fun buttonEvent(){
        binding.etMoney.hint = MyApplication.prefs.getString(tag,"0")

        binding.btnYes.setOnClickListener(View.OnClickListener {
            if(DataUtil().isNumber(binding.etMoney.text.toString())){
                MyApplication.prefs.setString(tag,binding.etMoney.text.toString())
                if(tag =="budget"){
                    MyApplication.prefs.setString("remain_budget",binding.etMoney.text.toString())
                }
                Toast.makeText(context,"설정 완료",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else{
                Toast.makeText(context,"숫자를 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        })
        binding.btnNo.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
    }

}