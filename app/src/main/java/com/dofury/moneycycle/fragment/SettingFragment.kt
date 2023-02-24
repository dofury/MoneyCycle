package com.dofury.moneycycle.fragment

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.activity.BudgetPlusActivity
import com.dofury.moneycycle.databinding.FragmentSettingBinding
import com.dofury.moneycycle.dialog.MoneySetDialog
import com.dofury.moneycycle.dialog.ResetDialog
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.CSVWriter
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val contentResolver = mainActivity.contentResolver
            try {
                //contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                val displayName = getFileName(contentResolver, uri)
                val path = uri.path
                //Toast.makeText(mainActivity, "Selected file: $displayName", Toast.LENGTH_SHORT).show()

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

    @SuppressLint("Range")
    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
        var displayName: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return displayName
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buttonEvent(){
        binding.clGoal.setOnClickListener {
            val dialog = MoneySetDialog(mainActivity,"goal")
            dialog.show()
        }
        binding.clBudgetCharge.setOnClickListener {
            val dialog = MoneySetDialog(mainActivity,"budget")
            dialog.show()
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
            CSVWriter.writeToFile(MyApplication.db.allLogs,"moneyLogs.csv", mainActivity)
            Snackbar.make(binding.root,"Download/moneyLog.csv",Snackbar.LENGTH_SHORT).show()
        }

        binding.btnCsvLoad.setOnClickListener {

            getContent.launch("*/*");

        }

    }




}