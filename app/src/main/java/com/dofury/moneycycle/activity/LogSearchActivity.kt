package com.dofury.moneycycle.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.databinding.ActivityLogSearchBinding
import com.dofury.moneycycle.dialog.DatePickerDialog
import com.dofury.moneycycle.util.DataUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LogSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogSearchBinding

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
        binding.tvFirstDate.setOnClickListener{
            dialog.show("log_search_0")
        }
        binding.tvSecondDate.setOnClickListener{
            dialog.show("log_search_1")
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