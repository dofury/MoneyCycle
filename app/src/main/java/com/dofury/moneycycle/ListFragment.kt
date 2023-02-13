package com.dofury.moneycycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dofury.moneycycle.databinding.FragmentListBinding
import com.dofury.moneycycle.dto.MoneyLog


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var moneyLogList: List<MoneyLog>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)

        val manger = LinearLayoutManager(context)
        manger.reverseLayout = true
        manger.stackFromEnd = true

        binding.rcvList.layoutManager = manger
        binding.rcvList.adapter = ListAdapter()
        binding.rcvList.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        return binding.root
    }

}