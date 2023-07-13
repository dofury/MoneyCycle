package com.dofury.moneycycle.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.activity.BudgetPlusActivity
import com.dofury.moneycycle.activity.LoginActivity
import com.dofury.moneycycle.databinding.FragmentSettingBinding
import com.dofury.moneycycle.dialog.InputDialog
import com.dofury.moneycycle.dialog.ResetDialog
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.User
import com.dofury.moneycycle.util.DataUtil
import com.dofury.moneycycle.util.FileHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.BufferedReader
import java.io.InputStreamReader

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var isInit = false

    var user = User()
    @RequiresApi(Build.VERSION_CODES.O)
    private val createFileActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.data?.also { uri ->
                FileHelper.writeCSV(mainActivity,MyApplication.db.allLogs,uri)
            }
        }
    }


    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK){
            val uri: Uri? = result.data?.data

            try {
                uri?.let {
                    FileHelper.readCSV(mainActivity,it)
                }

            } catch (e: SecurityException) {
                Toast.makeText(mainActivity,"오류가 발생했습니다",Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }


    }

    fun init(){
        isInit = true
        binding.tvBackupDate.text = MyApplication.prefs.getString("backup_date","없음")
        userInit()
    }

    fun userInit(){
        if(isInit){
            binding.tvNickname.text = user.nickname
            binding.tvEmail.text = user.email
            if(!user.nickname.isNullOrBlank()){
                binding.cardUser.visibility = View.VISIBLE
                binding.cardLoginCheck.visibility = View.GONE
            }else{
                binding.cardUser.visibility = View.GONE
                binding.cardLoginCheck.visibility = View.VISIBLE
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        init()
        buttonEvent()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buttonEvent(){
        binding.clGoal.setOnClickListener {
            val dialog = InputDialog(mainActivity)
            dialog.show("goal")
        }
        binding.clBudgetCharge.setOnClickListener {
            val dialog = InputDialog(mainActivity)
            dialog.show("budget")
        }
        binding.clBudgetCycle.setOnClickListener {
            Toast.makeText(mainActivity,"미구현",Toast.LENGTH_SHORT).show()
            //val dialog = InputDialog(mainActivity)
            //dialog.show("budget_cycle")
        }
        binding.clBudgetPlus.setOnClickListener {
            val intent = Intent(context, BudgetPlusActivity::class.java)
            startActivity(intent)
        }
        binding.clReset.setOnClickListener{
            val dialog = ResetDialog(mainActivity)
            dialog.show()
        }

        binding.btnCsvSave.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/csv"
                putExtra(Intent.EXTRA_TITLE, "moneyLogs.csv")
            }
            createFileActivityResultLauncher.launch(intent)
            Toast.makeText(mainActivity,"저장 완료",Toast.LENGTH_SHORT).show()
        }

        binding.btnCsvLoad.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/*"
            }
            getContent.launch(intent);
        }

        binding.cardLoginCheck.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnBackupSave.setOnClickListener {
            firebaseSave()
        }
        binding.btnBackupLoad.setOnClickListener {
            firebaseLoad()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun firebaseSave(){
        val firebaseAuth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().getReference("MoneyCycle")
        val now = DataUtil.getNowDate()
        val formatted = DataUtil.parseDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

        MyApplication.prefs.setString("backup_date",formatted)
        binding.tvBackupDate.text = formatted
        // 데이터 베이스 삽입
        databaseReference.child("UserLogs").child(firebaseAuth.uid!!).setValue(DataUtil.logToJson())
        Snackbar.make(binding.root,"백업 완료",Snackbar.LENGTH_SHORT).show()
        Log.d("test","good")


    }
    private fun firebaseLoad(){
        val firebaseAuth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().getReference("MoneyCycle")

        // 데이터 베이스 삽입
        databaseReference.child("UserLogs").child(firebaseAuth.uid!!).get().addOnCompleteListener {
            val logs:MutableList<MoneyLog> = DataUtil.jsonToLog(it.result.value.toString())!!
            MyApplication.db.allAddLog(logs)
            Snackbar.make(binding.root,"복구 완료",Snackbar.LENGTH_SHORT).show()
        }

        Log.d("test","good")


    }




}