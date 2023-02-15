package com.dofury.moneycycle.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.databinding.ActivityInitBinding
import com.dofury.moneycycle.util.DataUtil
import com.google.android.material.snackbar.Snackbar


class BudgetPlusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonEvent()
    }

    private fun buttonEvent() {

    }

}