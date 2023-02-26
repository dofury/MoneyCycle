package com.dofury.moneycycle.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.adapter.BudgetPlusAdapter
import com.dofury.moneycycle.adapter.SearchResultAdapter
import com.dofury.moneycycle.databinding.ActivityBudgetPlusBinding
import com.dofury.moneycycle.databinding.ActivityInitBinding
import com.dofury.moneycycle.databinding.ActivityLogSearchResultBinding
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.dto.MoneyLogList
import com.dofury.moneycycle.util.DataUtil
import com.google.android.material.snackbar.Snackbar


class LogSearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogSearchResultBinding
    private lateinit var logs: MutableList<MoneyLog>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        binding.rcvList.layoutManager = createLayoutManager()
        binding.rcvList.adapter = SearchResultAdapter(logs,this)
        binding.rcvList.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
        buttonEvent()
    }
    fun init(){
        val intent = intent
        val sql = intent.getStringExtra("sql")
        val args = intent.getStringArrayListExtra("args")!!.toTypedArray()
        logs = MyApplication.db.getQueryLog(
            sql!!,
            args!!
        )
    }

    private fun buttonEvent() {
        binding.btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
    private fun createLayoutManager(): LinearLayoutManager {
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        return manager
    }

}