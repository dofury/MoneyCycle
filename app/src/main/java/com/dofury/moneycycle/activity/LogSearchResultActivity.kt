package com.dofury.moneycycle.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.adapter.SearchResultAdapter
import com.dofury.moneycycle.databinding.ActivityLogSearchResultBinding
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.viewmodel.SearchResultViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LogSearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogSearchResultBinding
    private lateinit var viewModel: SearchResultViewModel
    private lateinit var adapter: SearchResultAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogSearchResultBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SearchResultViewModel::class.java]
        setContentView(binding.root)

        init()

        buttonEvent()
    }

    fun init(){
        val sql = intent.getStringExtra("sql")
        binding.rcvList.layoutManager = createLayoutManager()
        adapter = SearchResultAdapter(this, viewModel)
        binding.rcvList.adapter = adapter
        binding.rcvList.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))

        viewModel.moneyLogList.observe(this) {
            adapter.updateLog()
        }

        if (sql != null) {
            viewModel.moneyLogListLoad(sql)
        }

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