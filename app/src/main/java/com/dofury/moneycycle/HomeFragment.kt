package com.dofury.moneycycle

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.dofury.moneycycle.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

lateinit var mainActivity: MainActivity
private lateinit var binding: FragmentHomeBinding
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = context as MainActivity

        startInit()
        binding.fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, LogActivity::class.java)
            startActivity(intent)


        })
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startInit(){
        var is_init = false
        is_init = MyApplication.prefs.getBoolean("is_init",true)
        if(is_init){
            val intent = Intent(mainActivity, InitActivity::class.java)
            startActivity(intent)
        }else{
            init()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        val cal = Calendar.getInstance()
        binding.tvGoalValue.text= MyApplication.prefs.getString("goal","0")
        binding.tvBudgetValue.text= MyApplication.prefs.getString("budget","0")
        binding.tvMoneyValue.text= MyApplication.prefs.getString("money","0")

        binding.tvBudgetRemainValue.text= MyApplication.prefs.getString("remain_budget","0")

        binding.cpvGoalPercent.progress = getPercent(
            binding.tvGoalValue.text.toString(), binding.tvMoneyValue.text.toString()
        )
        if(binding.cpvGoalPercent.progress<30){
            binding.cpvGoalPercent.setProgressColor(ContextCompat.getColor(binding.root.context,R.color.red))
        }

        binding.npbBudgetPercent.progress = getPercent(
            binding.tvBudgetValue.text.toString(), binding.tvBudgetRemainValue.text.toString())
        if(binding.npbBudgetPercent.progress<30){
            binding.npbBudgetPercent.reachedBarColor = ContextCompat.getColor(binding.root.context,R.color.red)
        }

        //이번 달에 마지막 날을 가져와서 남은 일수를 계산
        cal.set(LocalDate.now().year,LocalDate.now().monthValue-1,LocalDate.now().dayOfMonth)
        binding.tvRemainDayValue.text = (cal.getActualMaximum(Calendar.DAY_OF_MONTH)-LocalDate.now().dayOfMonth+1).toString()

        val brvdv = (binding.tvBudgetRemainValue.text.toString().toInt()/
                binding.tvRemainDayValue.text.toString().toInt())
        if(brvdv>=0){
            binding.tvBudgetRemainValueDivideValue.text = brvdv.toString()
        }else{
            binding.tvBudgetRemainValueDivideValue.text = "0"
        }

    }

    private fun getPercent(all: String, now: String): Int {
        val df = DecimalFormat("#.##")
        var percent : Int = 0
        var value = now.toDouble()/all.toDouble() *100
        percent = value.toInt()

        return percent
    }


}