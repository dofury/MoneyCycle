package com.dofury.moneycycle.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arrayMapOf
import androidx.core.content.ContextCompat
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.databinding.ActivityLogSearchBinding
import com.dofury.moneycycle.dialog.DatePickerDialog
import com.dofury.moneycycle.dto.DBHelper
import com.dofury.moneycycle.fragment.mainActivity
import com.dofury.moneycycle.util.DataUtil
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class LogSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogSearchBinding

    private lateinit var isInlay: MutableMap<String,Boolean>
    private lateinit var isOutlay: MutableMap<String,Boolean>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        buttonEvent()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
        isInlay = arrayMapOf<String,Boolean>(getString(R.string.income) to false, getString(R.string.pocket_money) to false,getString(R.string.extra_income) to false,getString(R.string.finance) to false,getString(R.string.refund) to false,getString(R.string.change) to false,getString(R.string.etc) to false)
        isOutlay = arrayMapOf<String,Boolean>(getString(R.string.food) to false,getString(R.string.traffic) to false,getString(R.string.culture) to false,getString(R.string.fashion) to false, getString(R.string.house) to false, getString(R.string.event) to false,getString(R.string.hobby) to false,getString(R.string.change) to false,getString(R.string.etc) to false)
        binding.tvFirstDate.text = DataUtil.getNowFirstDate().format(formatter)
        binding.tvSecondDate.text = DataUtil.getNowDate().format(formatter)
    }

    private fun firstCheck(isFirst: Boolean): MutableList<String>{
        val firstSQL = "SELECT * FROM ${DBHelper.TABLE_NAME} WHERE "
        val andSQL = " AND "
        var sqlList = mutableListOf<String>("false","")

        return if(isFirst){
            sqlList[1] = firstSQL
            sqlList
        }else{
            sqlList[1] = andSQL
            sqlList
        }
    }
    private fun searchLogs(){
        var isFirst = true //첫 sql인지 체크
        var selectQueryHandler = ""
        var isContents = false//리스트에 요소가 있는지 체크
        var args = mutableListOf<String>()
        try {
            if(binding.switchDate.isChecked){
                //date 조건+
                val sqlCheck = firstCheck(isFirst)
                isFirst = sqlCheck[0].toBoolean()
                selectQueryHandler += sqlCheck[1]

                //CONTENT
                val firstDate = binding.tvFirstDate.text
                val lastDate = binding.tvSecondDate.text

                selectQueryHandler += "strftime('%Y-%m-%d', ${DBHelper.COL_DATE}) BETWEEN ? AND ?"

                //args에 추가
                args.add(firstDate.toString())
                args.add(lastDate.toString())

            }
            if(binding.switchCategory.isChecked){

                val sqlCheck = firstCheck(isFirst)
                isFirst = sqlCheck[0].toBoolean()
                selectQueryHandler += sqlCheck[1]

                var sql = "((SIGN = ? AND CATEGORY IN("
                args.add("1")//sign 1
                for(check in isInlay){
                    if(check.value){
                        sql += "?,"
                        args.add(check.key)
                        isContents = true
                    }
                }

                if(isContents){//리스트에 요소가 있다면
                    sql = sql.subSequence(0,sql.length-1) as String //마지막 콤마를 지운다
                    isContents = false //재 사용을 위해 원래 대로 돌림
                }
                sql += "))"
                selectQueryHandler += sql
                sql = " OR (SIGN = ? AND CATEGORY IN("
                args.add("0")//sign 0

                for(check in isOutlay){
                    if(check.value){
                        sql += "?,"
                        args.add(check.key)
                        isContents = true
                    }
                }

                if(isContents){//리스트에 요소가 있다면
                    sql = sql.subSequence(0,sql.length-1) as String //마지막 콤마를 지운다
                    isContents = false //재 사용을 위해 원래 대로 돌림
                }
                sql += ")))"
                selectQueryHandler += sql

            }
            if(binding.switchBudget.isChecked){

                val sqlCheck = firstCheck(isFirst)
                isFirst = sqlCheck[0].toBoolean()
                selectQueryHandler += sqlCheck[1]


                var sql = "((SIGN = ? AND IS_BUDGET IN("
                args.add("1")//sign 1

                if(binding.cbYesBudget.isChecked){
                    sql += "?))"
                    args.add("1")//sign 1
                    selectQueryHandler += sql
                }else{
                    sql += "))"
                    selectQueryHandler += sql
                }

                sql = " OR (SIGN = ? AND IS_BUDGET IN("
                args.add("0")//sign 0

                if(binding.cbNoBudget.isChecked){
                    sql += "?))"
                    args.add("0")//sign 0
                    selectQueryHandler += sql
                }else{
                    sql += "))"
                    selectQueryHandler += sql
                }

                selectQueryHandler += ")"

            }
            if(binding.switchMemo.isChecked){

                val sqlCheck = firstCheck(isFirst)
                isFirst = sqlCheck[0].toBoolean()//지워도 됨
                selectQueryHandler += sqlCheck[1]

                var sql = "MEMO = ?"

                args.add(binding.etMemo.text.toString())

                selectQueryHandler += sql
            }

            if(selectQueryHandler == "" || args.size == 0){
                throw NullPointerException("아무것도 입력하지 않음")
            }

            val intent = Intent(this,LogSearchResultActivity::class.java)
            val list = ArrayList<String>(args.toTypedArray().toList())
            intent.putExtra("sql",selectQueryHandler)
            intent.putStringArrayListExtra("args",list)
            startActivity(intent)
            finish()

        }catch (e: NullPointerException){
            Snackbar.make(binding.root,"검색 조건을 설정 해주세요.",Snackbar.LENGTH_SHORT).show()
            Log.d("bug","bug")
        }

        Log.d("test",selectQueryHandler)
        Log.d("test",args.toString())

    }
    private fun searchLog(){
        //date(and) ,category(and 카테고리 내부는 or), is_budget(and 내부는 or), memo(and)
        var firstSearch = true
        var selectQueryHandler = ""
        if(binding.switchDate.isChecked){
            //date 조건+
            selectQueryHandler += "SELECT * FROM ${DBHelper.TABLE_NAME} WHERE "
            firstSearch = false

            //CONTENT

        }
        if(binding.switchCategory.isChecked){

            if(firstSearch){
                selectQueryHandler += "SELECT * FROM ${DBHelper.TABLE_NAME} WHERE "
                firstSearch = false
            }else{
                selectQueryHandler += " AND "
            }

            var sql = "((SIGN = 1 AND CATEGORY IN("
            for(check in isInlay){
                if(check.value){
                    sql += "'${check.key}',"
                }
            }

            sql = if(sql != "((SIGN = 1 AND CATEGORY IN("){
                sql = sql.subSequence(0,sql.length-1) as String
                sql += "))"
                selectQueryHandler += sql
                " OR (SIGN = 0 AND CATEGORY IN("
            }else{
                sql += "))"
                selectQueryHandler += sql
                " OR (SIGN = 0 AND CATEGORY IN("
            }
            for(check in isOutlay){
                if(check.value){
                    sql += "'${check.key}',"
                }
            }
            sql = sql.subSequence(0,sql.length-1) as String
            sql += ")))"
            selectQueryHandler += sql

        }
        if(binding.switchBudget.isChecked){
            if(firstSearch){
                selectQueryHandler += "SELECT * FROM ${DBHelper.TABLE_NAME} WHERE "
                firstSearch = false
            }else{
                selectQueryHandler += " AND "
            }
            selectQueryHandler += "("

            if(binding.cbYesBudget.isChecked){
                var sql = "(SIGN = 1 AND IS_BUDGET IN(1) )"
                selectQueryHandler += sql
            }

            if(!firstSearch){
                selectQueryHandler += " AND "
            }

            if(binding.cbNoBudget.isChecked){
                var sql = "(SIGN = 0 AND IS_BUDGET IN(0) )"
                selectQueryHandler += sql
            }

            selectQueryHandler += ")"


        }
        if(binding.switchMemo.isChecked){

            if(firstSearch){
                selectQueryHandler += "SELECT * FROM ${DBHelper.TABLE_NAME} WHERE"
            }else{
                selectQueryHandler += " AND "
            }

            var sql = "MEMO = '${binding.etMemo.text}'"
            selectQueryHandler += sql
        }
        Log.d("test",selectQueryHandler)



    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            binding.clContentOut.visibility = View.VISIBLE
            binding.clContentIn.visibility = View.GONE
        }
        binding.rbInlay.setOnClickListener {
            binding.clContentOut.visibility = View.GONE
            binding.clContentIn.visibility = View.VISIBLE
        }
        categoryButtonEvent()
    }

    private fun categoryButtonEvent(){
        val inlayButtons = arrayMapOf(getString(R.string.income) to binding.btnIncome,getString(R.string.pocket_money) to binding.btnPocketMoney,getString(R.string.extra_income) to binding.btnExtraIncome,
            getString(R.string.finance) to binding.btnFinance,getString(R.string.refund) to binding.btnRefund,getString(R.string.change) to binding.btnChangeIn,
            getString(R.string.etc) to binding.btnEtcIn)
        val outlayButtons = arrayMapOf(getString(R.string.food) to binding.btnFood,getString(R.string.traffic) to binding.btnTraffic,getString(R.string.culture) to binding.btnCulture,
            getString(R.string.fashion) to binding.btnFashion,getString(R.string.house) to binding.btnHouse,getString(R.string.event) to binding.btnEvent,
            getString(R.string.hobby) to binding.btnHobby,getString(R.string.change) to binding.btnChangeOut,getString(R.string.etc) to binding.btnEtcOut)

        inlayButtons.forEach{
            val btn = it.value
            val key = it.key
            btn.setOnClickListener{
                if(isInlay[key] == true){
                    btn.setBackgroundResource(R.drawable.btn_white_gray)
                    btn.setTextColor(
                        ContextCompat.getColor(this,R.color.siam))
                    isInlay[key] = false
                }else{
                    btn.setBackgroundResource(R.drawable.btn_siam)
                    btn.setTextColor(
                        ContextCompat.getColor(this,R.color.white))
                    isInlay[key] = true
                }
            }
        }

        outlayButtons.forEach {
            val btn = it.value
            val key = it.key
            btn.setOnClickListener{
                if(isOutlay[key] == true){
                    btn.setBackgroundResource(R.drawable.btn_white_gray)
                    btn.setTextColor(
                        ContextCompat.getColor(this,R.color.siam))
                    isOutlay[key] = false
                }else{
                    btn.setBackgroundResource(R.drawable.btn_siam)
                    btn.setTextColor(
                        ContextCompat.getColor(this,R.color.white))
                    isOutlay[key] = true
                }
            }
        }

    }
    /*
    * type 0 = first date
    * type 1 = second date*/
    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(type:Int, date: LocalDateTime){
        val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
        when(type){
            0 -> binding.tvFirstDate.text = date.format(formatter)
            1 -> binding.tvSecondDate.text = date.format(formatter)
        }
    }

}