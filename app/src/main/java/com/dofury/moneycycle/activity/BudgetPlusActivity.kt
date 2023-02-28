package com.dofury.moneycycle.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.adapter.BudgetPlusAdapter
import com.dofury.moneycycle.databinding.ActivityBudgetPlusBinding


class BudgetPlusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetPlusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBudgetPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcvList.layoutManager = createLayoutManager()
        binding.rcvList.adapter = BudgetPlusAdapter(this,MyApplication.db.budgetLogs)
        binding.rcvList.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
        buttonEvent()
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