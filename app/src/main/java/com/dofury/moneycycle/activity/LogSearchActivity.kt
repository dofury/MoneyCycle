package com.dofury.moneycycle.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arrayMapOf
import androidx.core.content.ContextCompat
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.databinding.ActivityLogSearchBinding
import com.dofury.moneycycle.dialog.DatePickerDialog
import com.dofury.moneycycle.util.DataUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LogSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogSearchBinding

    private var isInlay = arrayListOf<Boolean>(false,false,false,false,false,false,false)
    private var isOutlay = arrayListOf<Boolean>(false,false,false,false,false,false,false,false,false)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        buttonEvent()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
        binding.tvFirstDate.text = DataUtil.getNowFirstDate().format(formatter)
        binding.tvSecondDate.text = DataUtil.getNowDate().format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buttonEvent() {
        val dialog = DatePickerDialog(this)
        binding.btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        binding.switchDate.setOnClickListener{
            if(binding.switchDate.isChecked){
                binding.clDate.visibility = View.VISIBLE
            }
            else{
                binding.clDate.visibility = View.GONE
            }
        }
        binding.switchCategory.setOnClickListener{
            if(binding.switchCategory.isChecked){
                binding.clCategory.visibility = View.VISIBLE
            }
            else{
                binding.clCategory.visibility = View.GONE
            }
        }
        binding.switchBudget.setOnClickListener {
            if(binding.switchBudget.isChecked){
                binding.clBudget.visibility = View.VISIBLE
            }
            else{
                binding.clBudget.visibility = View.GONE
            }
        }
        binding.switchMemo.setOnClickListener {
            if(binding.switchMemo.isChecked){
                binding.clMemo.visibility = View.VISIBLE
            }
            else{
                binding.clMemo.visibility = View.GONE
            }
        }
        binding.tvFirstDate.setOnClickListener{
            dialog.show("log_search_0")
        }
        binding.tvSecondDate.setOnClickListener{
            dialog.show("log_search_1")
        }
        binding.btnOk.setOnClickListener {
            finish()
        }
        binding.rbOutlay.setOnClickListener {
            binding.clContentOut.visibility = View.VISIBLE
            binding.clContentIn.visibility = View.GONE
        }
        binding.rbInlay.setOnClickListener {
            binding.clContentOut.visibility = View.GONE
            binding.clContentIn.visibility = View.VISIBLE
        }
        categoryButtonEvent()
    }

    private fun categoryButtonEvent(){
        val inlayButtons = arrayMapOf(0 to binding.btnIncome,1 to binding.btnPocketMoney,2 to binding.btnExtraIncome,
            3 to binding.btnFinance,4 to binding.btnRefund,5 to binding.btnChangeIn,
            6 to binding.btnEtcIn)
        val outlayButtons = arrayMapOf(0 to binding.btnFood,1 to binding.btnTraffic,2 to binding.btnCulture,
            3 to binding.btnFashion,4 to binding.btnHouse,5 to binding.btnEvent,
            6 to binding.btnHobby,7 to binding.btnChangeOut,8 to binding.btnEtcOut)

        inlayButtons.forEach{
            val btn = it.value
            val index = it.key
            btn.setOnClickListener{
                if(isInlay[index]){
                    btn.setBackgroundResource(R.drawable.btn_white_gray)
                    btn.setTextColor(
                        ContextCompat.getColor(this,R.color.siam))
                    isInlay[index] = false
                }else{
                    btn.setBackgroundResource(R.drawable.btn_siam)
                    btn.setTextColor(
                        ContextCompat.getColor(this,R.color.white))
                    isInlay[index] = true
                }
            }
        }

        outlayButtons.forEach {
            val btn = it.value
            val index = it.key
            btn.setOnClickListener{
                if(isOutlay[index]){
                    btn.setBackgroundResource(R.drawable.btn_white_gray)
                    btn.setTextColor(
                        ContextCompat.getColor(this,R.color.siam))
                    isOutlay[index] = false
                }else{
                    btn.setBackgroundResource(R.drawable.btn_siam)
                    btn.setTextColor(
                        ContextCompat.getColor(this,R.color.white))
                    isOutlay[index] = true
                }
            }
        }

    }
    /*
    * type 0 = first date
    * type 1 = second date*/
    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(type:Int, date: LocalDateTime){
        val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
        when(type){
            0 -> binding.tvFirstDate.text = date.format(formatter)
            1 -> binding.tvSecondDate.text = date.format(formatter)
        }
    }

}