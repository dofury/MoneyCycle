package com.dofury.moneycycle.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arrayMapOf
import androidx.core.content.ContextCompat
import com.dofury.moneycycle.R
import com.dofury.moneycycle.dao.QueryCondition
import com.dofury.moneycycle.databinding.ActivityLogSearchBinding
import com.dofury.moneycycle.dialog.DatePickerDialog
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.DataUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class LogSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogSearchBinding


    private lateinit var isInlay: MutableMap<String,Boolean>
    private lateinit var isOutlay: MutableMap<String,Boolean>
    private lateinit var inlayButtons: MutableMap<String, Button>
    private lateinit var outlayButtons: MutableMap<String,Button>
    private var isInSwitch = false
    private var isOutSwitch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogSearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
        buttonEvent()
    }
    private fun init(){
        val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
        isInlay = arrayMapOf<String,Boolean>(getString(R.string.income) to false, getString(R.string.pocket_money) to false,getString(R.string.extra_income) to false,getString(R.string.finance) to false,getString(R.string.refund) to false,getString(R.string.change) to false,getString(R.string.etc) to false)
        isOutlay = arrayMapOf<String,Boolean>(getString(R.string.food) to false,getString(R.string.traffic) to false,getString(R.string.culture) to false,getString(R.string.fashion) to false, getString(R.string.house) to false, getString(R.string.event) to false,getString(R.string.hobby) to false,getString(R.string.change) to false,getString(R.string.etc) to false)
        inlayButtons = arrayMapOf(getString(R.string.income) to binding.btnIncome,getString(R.string.pocket_money) to binding.btnPocketMoney,getString(R.string.extra_income) to binding.btnExtraIncome,
            getString(R.string.finance) to binding.btnFinance,getString(R.string.refund) to binding.btnRefund,getString(R.string.change) to binding.btnChangeIn,
            getString(R.string.etc) to binding.btnEtcIn)
        outlayButtons = arrayMapOf(getString(R.string.food) to binding.btnFood,getString(R.string.traffic) to binding.btnTraffic,getString(R.string.culture) to binding.btnCulture,
            getString(R.string.fashion) to binding.btnFashion,getString(R.string.house) to binding.btnHouse,getString(R.string.event) to binding.btnEvent,
            getString(R.string.hobby) to binding.btnHobby,getString(R.string.change) to binding.btnChangeOut,getString(R.string.etc) to binding.btnEtcOut)
        binding.tvFirstDate.text = DataUtil.getNowFirstDate().format(formatter)
        binding.tvSecondDate.text = DataUtil.getNowDate().format(formatter)
    }

    private fun categoryInAllOn(){
        isInlay.forEach { (t, _) ->
            isInlay[t] = true
        }
        inlayButtons.forEach { (t, u) ->
            inButtonOn(u,t)
        }
    }
    private fun categoryInAllOff(){
        isInlay.forEach { (t, _) ->
            isInlay[t] = false
        }
        inlayButtons.forEach { (t, u) ->
            inButtonOff(u,t)
        }
    }
    private fun categoryOutAllOn(){
        isOutlay.forEach { (t, _) ->
            isOutlay[t] = true
        }
        outlayButtons.forEach { (t, u) ->
            outButtonOn(u,t)
        }
    }
    private fun categoryOutAllOff(){
        isOutlay.forEach { (t, _) ->
            isOutlay[t] = false
        }
        outlayButtons.forEach { (t, u) ->
            outButtonOff(u,t)
        }
    }
    private fun searchLogs(){
        //room db를 사용한 가계부 기록 조건 검색 기능을 구현해
        //검색 조건을 설정하지 않았을 경우에는 검색 조건을 설정해주세요 라는 메시지를 띄워주고
        //검색 조건을 설정했을 경우에는 검색 조건에 맞는 가계부 기록을 검색하여 LogSearchResultActivity로 이동하도록 구현해
        var firstDate: String? = binding.tvFirstDate.text.toString()
        var secondDate: String? = binding.tvSecondDate.text.toString()
        var categories: MutableList<String>? = null
        var isBudget: Boolean? = null
        var memo: String? = null
        if(binding.switchDate.isChecked){
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val tempFirstDate = LocalDate.parse(binding.tvFirstDate.text,formatter)
            val tempSecondDate = LocalDate.parse(binding.tvSecondDate.text,formatter)
            if(tempFirstDate.isAfter(tempSecondDate)){
                Snackbar.make(binding.root, "날짜를 제대로 설정해주세요", Snackbar.LENGTH_LONG)
                    .setAction("action",null)
                    .show()
                return
            }
            if(firstDate == "" || secondDate == ""){
                Snackbar.make(binding.root, "날짜를 설정해주세요", Snackbar.LENGTH_LONG)
                    .setAction("action",null)
                    .show()
                return
            }
        }else{
            firstDate = null
            secondDate = null
        }

        if (binding.switchBudget.isChecked){
            isBudget = if(binding.rbYesBudget.isChecked)
                true
            else if(binding.rbNoBudget.isChecked)
                false
            else{
                Snackbar.make(binding.root, "예산 여부를 설정해주세요", Snackbar.LENGTH_LONG)
                    .setAction("action",null)
                    .show()
                return
            }
        }

        if(binding.switchCategory.isChecked){
            categories = mutableListOf()

            for((key,value) in isInlay){
                if(value){
                    categories.add(key)
                }
            }

            for((key,value) in isOutlay){
                if(value){
                    categories.add(key)
                }
            }

            if (categories.isEmpty()){
                Snackbar.make(binding.root, "카테고리를 설정해주세요", Snackbar.LENGTH_LONG)
                    .setAction("action",null)
                    .show()
                return
            }
        }

        if(binding.switchMemo.isChecked){
            memo = binding.etMemo.text.toString()
            if (memo.isEmpty()){
                Snackbar.make(binding.root, "메모를 설정해주세요", Snackbar.LENGTH_LONG)
                    .setAction("action",null)
                    .show()
                return
            }
        }

        val queryCondition = QueryCondition(firstDate,secondDate, categories,isBudget,memo)


        val intent = Intent(this,LogSearchResultActivity::class.java)
        intent.putExtra("query",queryCondition)
        startActivity(intent)
        finish()

    }

    private fun buttonEvent() {
        val dialog = DatePickerDialog(this)
        binding.btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        binding.switchDate.setOnClickListener{
            if(binding.switchDate.isChecked){
                binding.clDate.visibility = View.VISIBLE
            }
            else{
                binding.clDate.visibility = View.GONE
            }
        }
        binding.switchCategory.setOnClickListener{
            if(binding.switchCategory.isChecked){
                binding.clCategory.visibility = View.VISIBLE
            }
            else{
                binding.clCategory.visibility = View.GONE
            }
        }
        binding.switchBudget.setOnClickListener {
            if(binding.switchBudget.isChecked){
                binding.clBudget.visibility = View.VISIBLE
            }
            else{
                binding.clBudget.visibility = View.GONE
            }
        }
        binding.switchMemo.setOnClickListener {
            if(binding.switchMemo.isChecked){
                binding.clMemo.visibility = View.VISIBLE
            }
            else{
                binding.clMemo.visibility = View.GONE
            }
        }
        binding.tvFirstDate.setOnClickListener{
            dialog.show("log_search_0")
        }
        binding.tvSecondDate.setOnClickListener{
            dialog.show("log_search_1")
        }
        binding.btnOk.setOnClickListener {
            searchLogs()
        }
        binding.rbOutlay.setOnClickListener {
            binding.switchCategoryAll.isChecked = isOutSwitch
            binding.clContentOut.visibility = View.VISIBLE
            binding.clContentIn.visibility = View.GONE
        }
        binding.rbInlay.setOnClickListener {
            binding.switchCategoryAll.isChecked = isInSwitch
            binding.clContentOut.visibility = View.GONE
            binding.clContentIn.visibility = View.VISIBLE
        }
        binding.switchCategoryAll.setOnClickListener{
            if(binding.rbInlay.isChecked){
                if(binding.switchCategoryAll.isChecked){
                    isInSwitch = true
                    categoryInAllOn()
                }else{
                    isInSwitch = false
                    categoryInAllOff()
                }
            }
            if(binding.rbOutlay.isChecked){
                if(binding.switchCategoryAll.isChecked){
                    isOutSwitch = true
                    categoryOutAllOn()
                }else{
                    isOutSwitch = false
                    categoryOutAllOff()
                }
            }

        }
        categoryButtonEvent()
    }

    private fun inButtonOn(btn: Button,key: String){
        btn.setBackgroundResource(R.drawable.btn_siam)
        btn.setTextColor(
            ContextCompat.getColor(this,R.color.white))
        isInlay[key] = true
        if(isAllInButton()){
            isInSwitch = true
            binding.switchCategoryAll.isChecked = isInSwitch
        }
    }
    private fun outButtonOn(btn: Button,key: String){
        btn.setBackgroundResource(R.drawable.btn_siam)
        btn.setTextColor(
            ContextCompat.getColor(this,R.color.white))
        isOutlay[key] = true
        if(isAllOutButton()){
            isOutSwitch = true
            binding.switchCategoryAll.isChecked = isOutSwitch
        }
    }
    private fun inButtonOff(btn: Button,key: String){
        btn.setBackgroundResource(R.drawable.btn_white_gray)
        btn.setTextColor(
            ContextCompat.getColor(this,R.color.siam))
        isInlay[key] = false
        if(isNotAllInButton()){
            isInSwitch = false
            binding.switchCategoryAll.isChecked = isInSwitch
        }
    }
    private fun outButtonOff(btn: Button,key: String){
        btn.setBackgroundResource(R.drawable.btn_white_gray)
        btn.setTextColor(
            ContextCompat.getColor(this,R.color.siam))
        isOutlay[key] = false
        if(isNotAllOutButton()){
            isOutSwitch = false
            binding.switchCategoryAll.isChecked = isOutSwitch
        }
    }
    private fun isNotAllInButton(): Boolean{
        isInlay.forEach { (t, u)->
            if(u){
                return false
            }
        }
        return true
    }
    private fun isAllInButton(): Boolean{
        isInlay.forEach { (t, u)->
            if(!u){
                return false
            }
        }
        return true
    }
    private fun isNotAllOutButton(): Boolean{
        isOutlay.forEach { (t, u)->
            if(u){
                return false
            }
        }
        return true
    }
    private fun isAllOutButton(): Boolean{
        isOutlay.forEach { (t, u)->
            if(!u){
                return false
            }
        }
        return true
    }
    private fun categoryButtonEvent(){


        inlayButtons.forEach{
            val btn = it.value
            val key = it.key
            btn.setOnClickListener{
                if(isInlay[key] == true){
                    inButtonOff(btn,key)
                }else{
                    inButtonOn(btn,key)
                }
            }
        }

        outlayButtons.forEach {
            val btn = it.value
            val key = it.key
            btn.setOnClickListener{
                if(isOutlay[key] == true){
                    outButtonOff(btn,key)
                }else{
                    outButtonOn(btn,key)
                }
            }
        }

    }
    /*
    * type 0 = first date
    * type 1 = second date*/
    fun setDate(type:Int, date: LocalDateTime){
        val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
        when(type){
            0 -> binding.tvFirstDate.text = date.format(formatter)
            1 -> binding.tvSecondDate.text = date.format(formatter)
        }
    }

}