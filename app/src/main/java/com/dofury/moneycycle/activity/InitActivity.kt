package com.dofury.moneycycle.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.databinding.ActivityInitBinding
import com.dofury.moneycycle.util.DataUtil
import com.google.android.material.snackbar.Snackbar


class InitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonEvent()
    }

    private fun buttonEvent() {
        binding.btnInitSubmit.setOnClickListener(View.OnClickListener {
            if(DataUtil().isNumber(binding.evInitGoalValue.toString()) &&
                DataUtil().isNumber(binding.evInitBudgetValue.toString()) &&
                DataUtil().isNumber(binding.evInitMoneyValue.toString())){

                MyApplication.prefs.setString("goal",binding.evInitGoalValue.text.toString())
                MyApplication.prefs.setString("budget",binding.evInitBudgetValue.text.toString())
                MyApplication.prefs.setString("money",binding.evInitMoneyValue.text.toString())

                MyApplication.prefs.setString("remain_budget",binding.evInitBudgetValue.text.toString())
                MyApplication.prefs.setBoolean("is_init",false)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                }else{
                    Snackbar.make(binding.root,"제대로 입력해주세요",Snackbar.LENGTH_SHORT).show()
            }

        })

    }

}