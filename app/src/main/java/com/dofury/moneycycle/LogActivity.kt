package com.dofury.moneycycle

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dofury.moneycycle.databinding.ActivityLogBinding
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG_NUM = "numPad_fragment"
private const val TAG_CATEGORY_IN = "category_in_fragment"
private const val TAG_CATEGORY_OUT = "category_out_fragment"
class LogActivity : AppCompatActivity() {

    lateinit var moneyLog: MoneyLog
    private lateinit var binding: ActivityLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        moneyLog = MoneyLog(0, 0,false,"null","null",false)

        buttonEvent()
        setFragment(TAG_NUM,NumPadFragment())
    }
    public fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if(manager.findFragmentByTag(tag)==null){
            fragTransaction.add(R.id.view,fragment,tag)
        }

        val numPad = manager.findFragmentByTag(TAG_NUM)
        val categoryIn = manager.findFragmentByTag(TAG_CATEGORY_IN)
        val categoryOut = manager.findFragmentByTag(TAG_CATEGORY_OUT)

        if(numPad != null){
            fragTransaction.hide(numPad)
        }
        if(categoryIn != null){
            fragTransaction.hide(categoryIn)
        }
        if(categoryOut != null){
            fragTransaction.hide(categoryOut)
        }

        if(tag == TAG_NUM){
            if(numPad!=null){
                fragTransaction.show(numPad)
            }
        }
        else if(tag == TAG_CATEGORY_IN){
            if(categoryIn!=null){
                fragTransaction.show(categoryIn)
            }
        }
        else if(tag == TAG_CATEGORY_OUT){
            if(categoryOut!=null){
                fragTransaction.show(categoryOut)
            }
        }
        fragTransaction.commitAllowingStateLoss()
    }

    private fun buttonEvent() {
        binding.ibClose.setOnClickListener(View.OnClickListener {//메인 화면 전환
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })

        binding.rbOne.setOnClickListener(View.OnClickListener { //지출을 클릭시
            moneyLog.sign = false
            binding.tvSign.text = "-"
        })
        binding.rbTwo.setOnClickListener(View.OnClickListener { //수입을 클릭시
            moneyLog.sign = true
            binding.tvSign.text = "+"

        })

    }

    fun inputNumber(number: String){

        if(binding.tvNumber.text == "0"){
            binding.tvNumber.text = ""
        }
        if(binding.tvNumber.text.length <=10){
            binding.tvNumber.text = binding.tvNumber.text.toString()+number
        }
        else{
            Snackbar.make(binding.root,"10자를 넘을 수 없습니다",Snackbar.LENGTH_LONG)
                .setAction("action",null)
                .show()
        }
    }
    fun isBlank(): Boolean {

        if(binding.tvNumber.text == "0"){
            return true
        }
        return false
    }
    fun removeNumber() {
        if(binding.tvNumber.text != "0"){
            binding.tvNumber.text = binding.tvNumber.text.toString().subSequence(0,binding.tvNumber.length()-1)
        }
        Log.d("text",binding.tvNumber.text.toString())
        if(binding.tvNumber.text == ""){
            binding.tvNumber.text = "0"
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setCategory(category : String) {
        moneyLog.category = category
        submitLog()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitLog(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        var money = MyApplication.prefs.getString("money","0").toInt()
        var remain = MyApplication.prefs.getString("remain_budget","0").toInt()

        moneyLog.charge = binding.tvNumber.text.toString().toInt()
        moneyLog.date = formatted


        if(moneyLog.sign){
            money += moneyLog.charge
        }
        else{
            money -= moneyLog.charge
            remain -= moneyLog.charge
        }

        MyApplication.prefs.setString("remain_budget",remain.toString())//예산 돈 반영
        MyApplication.prefs.setString("money",money.toString())//자산 돈 반영

        MoneyLogList.list.add(moneyLog)
        MyApplication.db.addLog(moneyLog)

        MyApplication.prefs.setList("moneyLogList", MoneyLogList.list)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    public fun radioOff(){
        binding.rbOne.isEnabled=false
        binding.rbTwo.isEnabled=false
    }
    public fun radioOn(){
        binding.rbOne.isEnabled=true
        binding.rbTwo.isEnabled=true
    }
}