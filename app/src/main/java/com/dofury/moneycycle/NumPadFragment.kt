package com.dofury.moneycycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.databinding.ActivityLogBinding
import com.dofury.moneycycle.databinding.FragmentPadBinding
import com.google.android.material.snackbar.Snackbar

class NumPadFragment : Fragment() {
    private lateinit var binding: FragmentPadBinding
    private lateinit var activity: LogActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = getActivity() as LogActivity
        binding = FragmentPadBinding.inflate(layoutInflater)
        buttonEvent()
        return binding.root
    }
    private fun buttonEvent() {
        binding.btnOne.setOnClickListener(View.OnClickListener {
            activity.inputNumber("1")
        })
        binding.btnTwo.setOnClickListener(View.OnClickListener {
            activity.inputNumber("2")
        })
        binding.btnThree.setOnClickListener(View.OnClickListener {
            activity.inputNumber("3")
        })
        binding.btnFour.setOnClickListener(View.OnClickListener {
            activity.inputNumber("4")
        })
        binding.btnFive.setOnClickListener(View.OnClickListener {
            activity.inputNumber("5")
        })
        binding.btnSix.setOnClickListener(View.OnClickListener {
            activity.inputNumber("6")
        })
        binding.btnSeven.setOnClickListener(View.OnClickListener {
            activity.inputNumber("7")
        })
        binding.btnEight.setOnClickListener(View.OnClickListener {
            activity.inputNumber("8")
        })
        binding.btnNine.setOnClickListener(View.OnClickListener {
            activity.inputNumber("9")
        })
        binding.btnZero.setOnClickListener(View.OnClickListener {
            activity.inputNumber("0")
        })

        binding.btnRemove.setOnClickListener(View.OnClickListener {//숫자 지우기
            activity.removeNumber()
        })
        binding.btnNext.setOnClickListener(View.OnClickListener {
            activity.setFragment("category_fragment",CategoryFragment())
        })


    }
}