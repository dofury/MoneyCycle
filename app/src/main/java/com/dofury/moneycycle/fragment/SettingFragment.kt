package com.dofury.moneycycle.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.databinding.FragmentSettingBinding
import com.dofury.moneycycle.dialog.MoneySetDialog


class SettingFragment : Fragment() {
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
        binding.clBudget.setOnClickListener(View.OnClickListener {
            val dialog = MoneySetDialog(mainActivity,"budget")
            dialog.show()
        })
    }

}