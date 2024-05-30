package com.dofury.moneycycle.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dofury.moneycycle.activity.LogSearchActivity
import com.dofury.moneycycle.adapter.ListAdapter
import com.dofury.moneycycle.databinding.FragmentListBinding
import com.dofury.moneycycle.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@AndroidEntryPoint
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var date: LocalDateTime
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        startInit()

        init()
        binding.rcvList.layoutManager = createLayoutManager()
        adapter = ListAdapter(requireContext(),viewModel)
        binding.rcvList.adapter =
            viewModel.moneyLogList.value?.let { adapter }
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

    fun init(){
        viewModel.moneyLogList.observe(viewLifecycleOwner) {
            adapter.updateLog()
        }
    }

    private fun dateEvent(formatter: DateTimeFormatter){
        binding.ibLeft.setOnClickListener {
            date = date.plusMonths(-1)
            binding.tvDate.text = date.format(formatter)
            viewModel.moneyLogListDateLoad(date)
        }
        binding.ibRight.setOnClickListener {
            date = date.plusMonths(1)
            binding.tvDate.text = date.format(formatter)
            viewModel.moneyLogListDateLoad(date)
        }
        binding.ibSearch.setOnClickListener{
            val intent = Intent(context,LogSearchActivity::class.java)
            startActivity(intent)
        }
    }
}