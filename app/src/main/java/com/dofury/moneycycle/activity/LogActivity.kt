package com.dofury.moneycycle.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dofury.moneycycle.*
import com.dofury.moneycycle.databinding.ActivityLogBinding
import com.dofury.moneycycle.dialog.LogSetDialog
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.fragment.CategoryInFragment
import com.dofury.moneycycle.fragment.CategoryOutFragment
import com.dofury.moneycycle.fragment.NumPadFragment
import com.dofury.moneycycle.util.DataUtil
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG_NUM = "numPad_fragment"
private const val TAG_CATEGORY_IN = "category_in_fragment"
private const val TAG_CATEGORY_OUT = "category_out_fragment"

class LogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogBinding
    lateinit var moneyLog: MoneyLog
    private var tag = TAG_NUM
    private var moneyBuffer: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moneyLog = MoneyLog(0, 0, false, "", "", "",true,false)
        setFragment(TAG_NUM, NumPadFragment())
        buttonEvent()
    }
    public fun setFragment(tag :String,fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        val numPad = manager.findFragmentByTag(TAG_NUM)
        val categoryIn = manager.findFragmentByTag(TAG_CATEGORY_IN)
        val categoryOut = manager.findFragmentByTag(TAG_CATEGORY_OUT)

        this.tag = tag

        if(manager.findFragmentByTag(tag)==null){
            fragTransaction.add(R.id.view,fragment,tag)
        }


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
        binding.ibClose.setOnClickListener(View.OnClickListener {//?????? ?????? ??????
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })

        binding.rbOne.setOnClickListener(View.OnClickListener { //????????? ?????????
            moneyLog.sign = false
            binding.tvSign.text = "-"
            if(tag != TAG_NUM){
                setFragment(TAG_CATEGORY_OUT, CategoryOutFragment())
            }
        })
        binding.rbTwo.setOnClickListener(View.OnClickListener { //????????? ?????????
            moneyLog.sign = true
            binding.tvSign.text = "+"
            if(tag != TAG_NUM){
                setFragment(TAG_CATEGORY_IN, CategoryInFragment())
            }

        })

    }

    fun inputNumber(number: String){

        if(binding.tvNumber.text == "0"){
            moneyBuffer = ""
            binding.tvNumber.text = ""
        }
        if(binding.tvNumber.text.length <=10){
            moneyBuffer += number
            binding.tvNumber.text = DataUtil().parseMoney(moneyBuffer.toLong())
        }
        else{
            Snackbar.make(binding.root, "10?????? ?????? ??? ????????????", Snackbar.LENGTH_LONG)
                .setAction("action",null)
                .show()
        }
    }
    fun isBlank(): Boolean {

        if(moneyBuffer == ""){
            return true
        }
        return false
    }
    fun removeNumber() {
        if(moneyBuffer != ""){
            moneyBuffer = moneyBuffer.subSequence(0,moneyBuffer.length-1) as String
            binding.tvNumber.text = if(moneyBuffer!="") DataUtil().parseMoney(moneyBuffer.toLong())else "0"
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setCategory(category : String) {
        moneyLog.category = category
        val dialog = LogSetDialog(this)
        this.layoutInflater
        dialog.show(moneyLog)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitLog(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        var money = MyApplication.prefs.getString("money","0").toLong()
        var remain = MyApplication.prefs.getString("remain_budget","0").toLong()

        moneyLog.charge = moneyBuffer.toLong()
        moneyLog.date = formatted


        if(moneyLog.sign){
            money += moneyLog.charge
        }
        else{
            money -= moneyLog.charge
            remain -= moneyLog.charge
        }

        MyApplication.prefs.setString("money",money.toString())//?????? ??? ??????

        MyApplication.db.addLog(moneyLog)//db ??????
        MoneyLogList.list = MyApplication.db.allLogs//db?????? ?????? ????????????

        DataUtil().updateValue()//??????, ?????? ?????????

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun logToMain(){//??????
        submitLog()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}