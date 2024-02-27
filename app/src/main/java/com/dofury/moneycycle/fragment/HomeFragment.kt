package com.dofury.moneycycle.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.InitActivity
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.activity.MainActivity
import com.dofury.moneycycle.databinding.FragmentHomeBinding
import com.dofury.moneycycle.util.DataUtil
import com.dofury.moneycycle.viewmodel.MainViewModel
import java.util.*




class HomeFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)


        mainActivity = context as MainActivity

        startInit()
        binding.fab.setOnClickListener{
            val intent = Intent(context, LogActivity::class.java)
            startActivity(intent)

        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d("test","main-start")
    }

    override fun onPause() {
        super.onPause()
        Log.d("test","main-pause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("test","main-resume")
        viewModel.currentAmountUpdate()
    }
    private fun startInit(){
        val isInit: Boolean = MyApplication.prefs.getBoolean("is_init",true)
        if(isInit){
            val intent = Intent(requireContext(), InitActivity::class.java)
            startActivity(intent)
        }else{
            DataUtil.budgetCheck(requireContext())
            init()
        }
    }
    fun init(){
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        viewModel.budgetPlusAmount.observe(viewLifecycleOwner) { value ->
            binding.tvPlusBudgetValue.text = value
        }
        viewModel.budgetSumAmount.observe(viewLifecycleOwner) {value ->
            binding.tvBudgetValue.text = value
        }

        viewModel.currentAmount.observe(viewLifecycleOwner) {value->
            binding.tvMoneyValue.text = DataUtil.parseMoney(value.toLong())
        }

        //DataUtil.updateValue()//자산, 예산 최신화
        val goalValue = viewModel.targetAmount.value
        val budgetPlus = viewModel.budgetPlusAmount.value
        val budgetValue = viewModel.budgetSumAmount.value
        val moneyValue = viewModel.currentAmount.value
        val remainBudgetValue = viewModel.remainBudgetAmount.value
        val remainDayValue = (DataUtil.getNowLastDate().dayOfMonth-DataUtil.getNowDate().dayOfMonth+1).toString()

        if (budgetPlus != null) {
            binding.tvPlusBudgetValue.text = if(budgetPlus == "0") "" else "+(${DataUtil.parseMoney(budgetPlus.toLong())})"
        }
        if (goalValue != null) {
            binding.tvGoalValue.text= DataUtil.parseMoney(goalValue.toLong())
        }
        if (budgetValue != null) {
            binding.tvBudgetValue.text= DataUtil.parseMoney(budgetValue.toLong())
        }

        if (remainBudgetValue != null) {
            binding.tvBudgetRemainValue.text= DataUtil.parseMoney(remainBudgetValue.toLong())
        }
        //이번 달에 마지막 날을 가져와서 남은 일수를 계산
        binding.tvRemainDayValue.text = remainDayValue

        binding.cpvGoalPercent.progress = goalValue?.let {DataUtil.getPercent(it, moneyValue!!)}!!
        if(binding.cpvGoalPercent.progress<30){
            binding.cpvGoalPercent.setProgressColor(ContextCompat.getColor(binding.root.context, R.color.red))
        }else{
            binding.npbBudgetPercent.reachedBarColor = ContextCompat.getColor(binding.root.context,R.color.blizzard_blue)
        }

        binding.npbBudgetPercent.progress = budgetValue?.let { DataUtil.getPercent(it, remainBudgetValue!!) }!!
        if(binding.npbBudgetPercent.progress<30){
            binding.npbBudgetPercent.reachedBarColor = ContextCompat.getColor(binding.root.context,R.color.red)
        }else{
            binding.npbBudgetPercent.reachedBarColor = ContextCompat.getColor(binding.root.context,R.color.blizzard_blue)
        }

        val brvdv = remainBudgetValue!!.toLong()/ remainDayValue.toLong()
        if(brvdv>=0){
            binding.tvBudgetRemainValueDivideValue.text = DataUtil.parseMoney(brvdv)
        }else{
            binding.tvBudgetRemainValueDivideValue.text = "0"
        }

    }




}