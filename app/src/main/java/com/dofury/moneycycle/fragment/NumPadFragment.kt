package com.dofury.moneycycle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.LogActivity
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
        val buttons = arrayListOf(
            binding.btnOne,
            binding.btnTwo,
            binding.btnThree,
            binding.btnFour,
            binding.btnFive,
            binding.btnSix,
            binding.btnSeven,
            binding.btnEight,
            binding.btnNine,
            binding.btnDoubleZero,
            binding.btnZero
        )

        buttons.forEach {
            it.setOnClickListener {view->
                val number = when(view.id) {
                    R.id.btn_one -> "1"
                    R.id.btn_two -> "2"
                    R.id.btn_three -> "3"
                    R.id.btn_four -> "4"
                    R.id.btn_five -> "5"
                    R.id.btn_six -> "6"
                    R.id.btn_seven -> "7"
                    R.id.btn_eight -> "8"
                    R.id.btn_nine -> "9"
                    R.id.btn_double_zero -> "00"
                    else -> "0"
                }
                activity.inputNumber(number)
            }
        }

        binding.btnRemove.setOnClickListener(View.OnClickListener {//숫자 지우기
            activity.removeNumber()
        })
        binding.btnNext.setOnClickListener(View.OnClickListener {
            if(activity.isBlank()){
                Snackbar.make(binding.root,"숫자를 입력해주세요", Snackbar.LENGTH_SHORT)
                    .setAction("action",null)
                    .show()
            }
            else{
                if(activity.moneyLog.sign){
                    activity.setFragment("category_in_fragment", CategoryInFragment())
                }
                else{
                    activity.setFragment("category_out_fragment", CategoryOutFragment())
                }
            }
        })


    }
}