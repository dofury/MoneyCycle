package com.dofury.moneycycle.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.activity.LogSearchActivity
import com.dofury.moneycycle.adapter.ListAdapter
import com.dofury.moneycycle.database.MoneyLogDatabase
import com.dofury.moneycycle.databinding.FragmentListBinding
import com.dofury.moneycycle.dto.MoneyLog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var date: LocalDateTime
    private lateinit var moneyLogList: MutableList<MoneyLog>
    private lateinit var db: MoneyLogDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)
        db = MoneyLogDatabase.getInstance(requireContext())!!
        startInit()

        init()
        binding.rcvList.layoutManager = createLayoutManager()
        binding.rcvList.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        return binding.root
    }
    private fun createLayoutManager(): LinearLayoutManager {
        val manager = LinearLayoutManager(context)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        return manager
    }

    private fun startInit(){
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        date = LocalDateTime.now()
        binding.tvDate.text=date.format(formatter)
        dateEvent(formatter)
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun init(){
        val month = YearMonth.from(date)
        val firstDate = month.atDay(1)
        val lastDate = month.atEndOfMonth()
        GlobalScope.launch(Dispatchers.IO){
            moneyLogList = db.moneyLogDao().getDateLog(firstDate.toString(),lastDate.toString()).toMutableList()
            // UI 업데이트는 메인 스레드에서 수행
            withContext(Dispatchers.Main) {
                // UI 업데이트 작업 수행
                binding.rcvList.adapter = ListAdapter(moneyLogList,requireContext())
            }
        }

    }

    private fun dateEvent(formatter: DateTimeFormatter){
        binding.ibLeft.setOnClickListener {
            date = date.plusMonths(-1)
            binding.tvDate.text = date.format(formatter)
            init()
        }
        binding.ibRight.setOnClickListener {
            date = date.plusMonths(1)
            binding.tvDate.text = date.format(formatter)
            init()
        }
        binding.ibSearch.setOnClickListener{
            val intent = Intent(context,LogSearchActivity::class.java)
            startActivity(intent)
        }
    }
}