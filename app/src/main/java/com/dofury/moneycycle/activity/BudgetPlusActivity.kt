package com.dofury.moneycycle.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.adapter.BudgetPlusAdapter
import com.dofury.moneycycle.databinding.ActivityBudgetPlusBinding
import com.dofury.moneycycle.databinding.ActivityInitBinding
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.util.DataUtil
import com.google.android.material.snackbar.Snackbar


class BudgetPlusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetPlusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBudgetPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //임시 구현
        var list:MutableList<MoneyLog> = mutableListOf()
        for(log in MoneyLogList.list){
            if(log.is_budget && log.sign){
                list.add(log)
            }
        }
        binding.rcvList.adapter = BudgetPlusAdapter(this,list)
        buttonEvent()
    }

    private fun buttonEvent() {
        binding.btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}