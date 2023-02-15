package com.dofury.moneycycle.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dofury.moneycycle.fragment.ListFragment
import com.dofury.moneycycle.fragment.SettingFragment

import com.dofury.moneycycle.fragment.HomeFragment

class MainPagerAdapter(activity : FragmentActivity) : FragmentStateAdapter(activity) {

    val fragments: List<Fragment>
    init {
        fragments = listOf(HomeFragment(),ListFragment(),SettingFragment())
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
    override fun getItemCount(): Int {
        return fragments.size
    }



}