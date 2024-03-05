package com.dofury.moneycycle.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.LogActivity
import com.dofury.moneycycle.databinding.FragmentCategoryOutBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class CategoryOutFragment : Fragment() {
    private lateinit var binding: FragmentCategoryOutBinding
    private lateinit var activity: LogActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = getActivity() as LogActivity
        binding = FragmentCategoryOutBinding.inflate(layoutInflater)
        buttonEvent()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun buttonEvent() {
        binding.btnPrev.setOnClickListener(View.OnClickListener {//
            activity.setFragment("numPad_fragment", NumPadFragment())
        })

        binding.civFood.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.food))
        })

        binding.civTraffic.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.traffic))
        })

        binding.civCulture.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.culture))
        })

        binding.civFashion.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.fashion))
        })

        binding.civChange.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.change))
        })

        binding.civEvent.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.event))
        })

        binding.civHobby.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.hobby))
        })

        binding.civHouse.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.house))
        })

        binding.civEtc.setOnClickListener(View.OnClickListener {
            activity.setCategory(getString(R.string.etc))
        })
    }
}