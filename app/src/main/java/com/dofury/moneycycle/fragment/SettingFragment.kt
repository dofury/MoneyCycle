package com.dofury.moneycycle.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.activity.BudgetPlusActivity
import com.dofury.moneycycle.activity.MainActivity
import com.dofury.moneycycle.databinding.FragmentSettingBinding
import com.dofury.moneycycle.dialog.InputDialog
import com.dofury.moneycycle.dialog.ResetDialog
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.User
import com.dofury.moneycycle.util.DataUtil
import com.dofury.moneycycle.util.FileHelper
import com.dofury.moneycycle.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@AndroidEntryPoint
class SettingFragment : Fragment() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var isInit = false
    private lateinit var binding: FragmentSettingBinding
    private lateinit var viewModel: MainViewModel


    private var user = User()
    @OptIn(DelicateCoroutinesApi::class)
    private val createFileActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.data?.also { uri ->
                GlobalScope.launch(Dispatchers.IO) {
                    val moneyLogs = MyApplication.db.moneyLogDao().getAll()
                    FileHelper(requireContext()).writeCSV(context as MainActivity, moneyLogs, uri){
                        val handler = Handler(Looper.getMainLooper())//main thread
                        handler.postDelayed(Runnable {
                            Toast.makeText(context as MainActivity,"저장 성공",Toast.LENGTH_SHORT).show()
                        },0)

                    }
                }

            }
        }
        else{
            Toast.makeText(context as MainActivity,"저장 실패",Toast.LENGTH_SHORT).show()
        }
    }


    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK){
            val uri: Uri? = result.data?.data

            try {
                uri?.let {
                    FileHelper(requireContext()).readCSV(context as MainActivity,viewModel,it){
                        Toast.makeText(context as MainActivity,"불러오기 성공",Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: SecurityException) {
                Toast.makeText(context as MainActivity,"불러오기 실패",Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }


    }

    fun init(){
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        isInit = true
        binding.tvBackupDate.text = MyApplication.prefs.getString("backup_date","없음")
        userInit()
    }

    private fun userInit(){
        if(isInit){
            binding.tvNickname.text = user.nickname
            binding.tvEmail.text = user.email
            if(user.nickname.isNotBlank()){
                binding.cardUser.visibility = View.VISIBLE
                binding.cardLoginCheck.visibility = View.GONE
            }else{
                binding.cardUser.visibility = View.GONE
                binding.cardLoginCheck.visibility = View.VISIBLE
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        init()
        buttonEvent()

        return binding.root
    }

    fun buttonEvent(){
        binding.clGoal.setOnClickListener {
            val dialog = InputDialog(context as MainActivity)
            dialog.show("goal")
        }
        binding.clBudgetCharge.setOnClickListener {
            val dialog = InputDialog(context as MainActivity)
            dialog.show("budget")
        }
        binding.clBudgetCycle.setOnClickListener {
            Toast.makeText(context as MainActivity,"미구현",Toast.LENGTH_SHORT).show()
            //val dialog = InputDialog(mainActivity)
            //dialog.show("budget_cycle")
        }
        binding.clBudgetPlus.setOnClickListener {
            val intent = Intent(context, BudgetPlusActivity::class.java)
            startActivity(intent)
        }
        binding.clReset.setOnClickListener{
            val dialog = ResetDialog(context as MainActivity)
            dialog.show()
        }

        binding.btnCsvSave.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/csv"
                putExtra(Intent.EXTRA_TITLE, "moneyLog.csv")
            }
            createFileActivityResultLauncher.launch(intent)
        }

        binding.btnCsvLoad.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/*"
            }
            getContent.launch(intent);
        }

        binding.cardLoginCheck.setOnClickListener {
            //val intent = Intent(context, LoginActivity::class.java)
            //startActivity(intent)
            Toast.makeText(context as MainActivity,"미구현",Toast.LENGTH_SHORT).show()
        }
        binding.btnBackupSave.setOnClickListener {
            firebaseSave()
        }
        binding.btnBackupLoad.setOnClickListener {
            firebaseLoad()
        }

    }

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
            viewModel.addCSVLog(logs,requireActivity())
            Snackbar.make(binding.root,"복구 완료",Snackbar.LENGTH_SHORT).show()
        }

        Log.d("test","good")


    }




}