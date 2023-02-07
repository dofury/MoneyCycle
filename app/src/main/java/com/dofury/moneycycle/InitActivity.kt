package com.dofury.moneycycle

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.databinding.ActivityInitBinding


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
            MyApplication.prefs.setString("goal",binding.tvInitGoalValue.text.toString())
            MyApplication.prefs.setString("budget",binding.tvInitBudgetValue.text.toString())
            MyApplication.prefs.setString("money",binding.tvInitMoneyValue.text.toString())

            MyApplication.prefs.setString("remain_budget",binding.tvInitBudgetValue.text.toString())
            MyApplication.prefs.setBoolean("is_init",false)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })

    }

}