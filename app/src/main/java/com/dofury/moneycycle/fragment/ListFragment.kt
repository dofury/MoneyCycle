package com.dofury.moneycycle.fragment

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
import com.dofury.moneycycle.adapter.ListAdapter
import com.dofury.moneycycle.databinding.FragmentListBinding
import com.dofury.moneycycle.dto.MoneyLog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var date: LocalDateTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startInit(){
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        date = LocalDateTime.now()
        binding.tvDate.text=date.format(formatter)
        dateEvent(formatter)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        binding.rcvList.adapter = ListAdapter(MyApplication.db.getDateLog(date))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateEvent(formatter: DateTimeFormatter){
        binding.ibLeft.setOnClickListener(View.OnClickListener {
            date = date.plusMonths(-1)
            binding.tvDate.text = date.format(formatter)
            init()
        })
        binding.ibRight.setOnClickListener(View.OnClickListener {
            date = date.plusMonths(1)
            binding.tvDate.text = date.format(formatter)
            init()
        })
    }
}