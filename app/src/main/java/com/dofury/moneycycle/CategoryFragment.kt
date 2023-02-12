package com.dofury.moneycycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dofury.moneycycle.databinding.ActivityLogBinding
import com.dofury.moneycycle.databinding.FragmentCategoryBinding
import com.dofury.moneycycle.databinding.FragmentPadBinding
import com.google.android.material.snackbar.Snackbar

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
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
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        buttonEvent()
        return binding.root
    }
    private fun buttonEvent() {
        binding.btnPrev.setOnClickListener(View.OnClickListener {
            activity.setFragment("numPad_fragment",NumPadFragment())
        })

    }
}