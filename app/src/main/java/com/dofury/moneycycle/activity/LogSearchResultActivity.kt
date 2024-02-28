package com.dofury.moneycycle.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.adapter.SearchResultAdapter
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.databinding.ActivityLogSearchResultBinding
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.viewmodel.MainViewModel


class LogSearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogSearchResultBinding
    private lateinit var logs: MutableList<MoneyLog>
    private val db = MoneyLogDatabase.getInstance(applicationContext)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        binding.rcvList.layoutManager = createLayoutManager()
        binding.rcvList.adapter = SearchResultAdapter(this, MainViewModel())
        binding.rcvList.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
        buttonEvent()
    }
    fun init(){
        val intent = intent
        val sql = intent.getStringExtra("sql")
        val args = intent.getStringArrayListExtra("args")!!.toTypedArray()
        logs = db!!.moneyLogDao().getQueryLog(sql!!).toMutableList()
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