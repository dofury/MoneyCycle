package com.dofury.moneycycle.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.activity.MainActivity
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.InitActivity
import com.dofury.moneycycle.databinding.FragmentHomeBinding
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.DataUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*



lateinit var mainActivity: MainActivity
private lateinit var binding: FragmentHomeBinding
class HomeFragment : Fragment() {


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

/*        val objectMapper = ObjectMapper()
        val jsonArray = objectMapper.readValue(DataUtil.logToJson(),Array<MoneyLog>::class.java)

        val csvMapper = CsvMapper()
        val csvSchema = CsvSchema.builder()
            .addColumn("name")
            .addColumn("age")
            .addColumn("city")
            .build()
            .withHeader()

        val csvString = csvMapper.writeValueAsString(jsonArray)
        File("output.csv").writeText(csvString)*/



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
    fun init(){
        DataUtil.updateValue()//자산, 예산 최신화
        val cal = Calendar.getInstance()
        cal.set(LocalDate.now().year,LocalDate.now().monthValue-1,LocalDate.now().dayOfMonth)
        val goalValue = MyApplication.prefs.getString("goal","0")

        val budget = MyApplication.prefs.getString("budget","0")
        val budgetPlus = MyApplication.prefs.getString("budget_plus","0")
        val budgetValue = (budget.toLong() + budgetPlus.toLong()).toString()
        val moneyValue = MyApplication.prefs.getString("money","0")
        val remainBudgetValue = MyApplication.prefs.getString("remain_budget","0")
        val remainDayValue = (cal.getActualMaximum(Calendar.DAY_OF_MONTH)-LocalDate.now().dayOfMonth+1).toString()

        binding.tvPlusBudgetValue.text = if(budgetPlus == "0") "" else "+(${DataUtil.parseMoney(budgetPlus.toLong())})"
        binding.tvGoalValue.text= DataUtil.parseMoney(goalValue.toLong())
        binding.tvBudgetValue.text= DataUtil.parseMoney(budgetValue.toLong())
        binding.tvMoneyValue.text= DataUtil.parseMoney(moneyValue.toLong())
        binding.tvBudgetRemainValue.text= DataUtil.parseMoney(remainBudgetValue.toLong())
        //이번 달에 마지막 날을 가져와서 남은 일수를 계산
        binding.tvRemainDayValue.text = remainDayValue

        binding.cpvGoalPercent.progress = getPercent(goalValue, moneyValue)
        if(binding.cpvGoalPercent.progress<30){
            binding.cpvGoalPercent.setProgressColor(ContextCompat.getColor(binding.root.context, R.color.red))
        }else{
            binding.npbBudgetPercent.reachedBarColor = ContextCompat.getColor(binding.root.context,R.color.white_sky)
        }

        binding.npbBudgetPercent.progress = getPercent(budgetValue, remainBudgetValue)
        if(binding.npbBudgetPercent.progress<30){
            binding.npbBudgetPercent.reachedBarColor = ContextCompat.getColor(binding.root.context,R.color.red)
        }else{
            binding.npbBudgetPercent.reachedBarColor = ContextCompat.getColor(binding.root.context,R.color.white_sky)
        }

        val brvdv = remainBudgetValue.toLong()/ remainDayValue.toLong()
        if(brvdv>=0){
            binding.tvBudgetRemainValueDivideValue.text = DataUtil.parseMoney(brvdv)
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