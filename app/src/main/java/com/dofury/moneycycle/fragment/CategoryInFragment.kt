package com.dofury.moneycycle.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.R
import com.dofury.moneycycle.databinding.FragmentCategoryInBinding

class CategoryInFragment : Fragment() {
    private lateinit var binding: FragmentCategoryInBinding
    private lateinit var activity: LogActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = getActivity() as LogActivity
        binding = FragmentCategoryInBinding.inflate(layoutInflater)
        buttonEvent()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun buttonEvent() {
        binding.btnPrev.setOnClickListener(View.OnClickListener {//
            activity.setFragment("numPad_fragment", NumPadFragment())
        })

        binding.civIncome.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.income))
        })

        binding.civPocketMoney.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.pocket_money))
        })

        binding.civExtraIncome.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.extra_income))
        })

        binding.civFinance.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.finance))
        })

        binding.civRefund.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.refund))
        })

        binding.civChange.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.change))
        })


        binding.civEtc.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.etc))
        })
    }
}