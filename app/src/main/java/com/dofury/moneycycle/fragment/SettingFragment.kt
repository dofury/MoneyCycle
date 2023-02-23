package com.dofury.moneycycle.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.activity.BudgetPlusActivity
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.activity.MainActivity
import com.dofury.moneycycle.databinding.FragmentSettingBinding
import com.dofury.moneycycle.dialog.MoneySetDialog
import com.dofury.moneycycle.dialog.ResetDialog
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess


object SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)

        buttonEvent()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun buttonEvent(){
        binding.clGoal.setOnClickListener(View.OnClickListener {
            val dialog = MoneySetDialog(mainActivity,"goal")
            dialog.show()
        })
        binding.clBudgetCharge.setOnClickListener(View.OnClickListener {
            val dialog = MoneySetDialog(mainActivity,"budget")
            dialog.show()
        })
        binding.clBudgetPlus.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, BudgetPlusActivity::class.java)
            startActivity(intent)
        })
        binding.clReset.setOnClickListener(View.OnClickListener {
            val dialog = ResetDialog(mainActivity)
            dialog.show()
        })
    }

}