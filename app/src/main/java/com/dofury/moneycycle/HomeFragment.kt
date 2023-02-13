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
    @RequiresApi(Build.VERSION_CODES.O)
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
        cal.set(LocalDate.now().year,LocalDate.now().monthValue-1,LocalDate.now().dayOfMonth)
        val goalValue = MyApplication.prefs.getString("goal","0")
        val budgetValue = MyApplication.prefs.getString("budget","0")
        val moneyValue = MyApplication.prefs.getString("money","0")
        val remainBudgetValue = MyApplication.prefs.getString("remain_budget","0")
        val remainDayValue = (cal.getActualMaximum(Calendar.DAY_OF_MONTH)-LocalDate.now().dayOfMonth+1).toString()
        binding.tvGoalValue.text= DataUtil().parseMoney(goalValue.toInt())
        binding.tvBudgetValue.text= DataUtil().parseMoney(budgetValue.toInt())
        binding.tvMoneyValue.text= DataUtil().parseMoney(moneyValue.toInt())
        binding.tvBudgetRemainValue.text= DataUtil().parseMoney(remainBudgetValue.toInt())
        //이번 달에 마지막 날을 가져와서 남은 일수를 계산
        binding.tvRemainDayValue.text = remainDayValue

        binding.cpvGoalPercent.progress = getPercent(goalValue, moneyValue)
        if(binding.cpvGoalPercent.progress<30){
            binding.cpvGoalPercent.setProgressColor(ContextCompat.getColor(binding.root.context,R.color.red))
        }

        binding.npbBudgetPercent.progress = getPercent(budgetValue, remainBudgetValue)
        if(binding.npbBudgetPercent.progress<30){
            binding.npbBudgetPercent.reachedBarColor = ContextCompat.getColor(binding.root.context,R.color.red)
        }

        val brvdv = remainBudgetValue.toInt()/ remainDayValue.toInt()
        if(brvdv>=0){
            binding.tvBudgetRemainValueDivideValue.text = DataUtil().parseMoney(brvdv)
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