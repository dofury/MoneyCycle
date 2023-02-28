package com.dofury.moneycycle.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.activity.BudgetPlusActivity
import com.dofury.moneycycle.activity.LoginActivity
import com.dofury.moneycycle.databinding.FragmentSettingBinding
import com.dofury.moneycycle.dialog.InputDialog
import com.dofury.moneycycle.dialog.ResetDialog
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.CSVWriter
import com.dofury.moneycycle.util.DataUtil
import com.dofury.moneycycle.util.Permission
import com.dofury.moneycycle.util.Permission.isWriteExternalStoragePermissionGranted
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val contentResolver = mainActivity.contentResolver
            try {
                //contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                val displayName = DataUtil.getFileName(contentResolver, uri)
                //Toast.makeText(mainActivity, "Selected file: $displayName", Toast.LENGTH_SHORT).show()
                if(!displayName.toString().contains(".csv")){//csv 파일이 아니라면
                    throw SecurityException("csv 파일이 아님")
                }
                contentResolver.openInputStream(uri)?.use {
                        inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val logs: MutableList<MoneyLog> = mutableListOf()
                    var line: String? = reader.readLine()
                    var count = 0
                    while(line!=null){
                        val tokens = line.split(",")
                        if(count>0){
                            val log = MoneyLog()
                            log.uid = tokens[0].toInt() //uid
                            log.charge = tokens[1].toLong() //uid
                            log.sign = tokens[2].toBoolean() //uid
                            log.category = tokens[3] //uid
                            log.date = tokens[4] //uid
                            log.memo = tokens[5] //uid
                            log.budget = tokens[6].toBoolean() //uid
                            log.server = tokens[7].toBoolean() //uid
                            logs.add(log)
                        }
                        line = reader.readLine()
                        count++
                    }
                    MyApplication.db.allAddLog(logs)
                }


                // Do something with the selected file
            } catch (e: SecurityException) {
                Toast.makeText(mainActivity,"오류가 발생했습니다",Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)

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
            val dialog = InputDialog(mainActivity)
            dialog.show("budget_cycle")
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
            if (isWriteExternalStoragePermissionGranted(mainActivity)) {
                // 권한이 부여된 경우 실행할 코드
                CSVWriter.writeToFile(MyApplication.db.allLogs,"moneyLogs.csv", mainActivity)
                Snackbar.make(binding.root,"Download/moneyLog.csv",Snackbar.LENGTH_SHORT).show()
            } else {
                // 권한이 부여되지 않은 경우 권한 요청 코드 실행
                Permission.verifyStoragePermissions(mainActivity)
                Snackbar.make(binding.root,"권한이 없습니다",Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnCsvLoad.setOnClickListener {

            getContent.launch("*/*");
        }

        binding.llLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

    }




}