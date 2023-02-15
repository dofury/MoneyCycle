package com.dofury.moneycycle.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.dofury.moneycycle.R
import com.dofury.moneycycle.adapter.MainPagerAdapter
import com.dofury.moneycycle.databinding.ActivityMainBinding
import com.dofury.moneycycle.fragment.HomeFragment
import com.dofury.moneycycle.fragment.ListFragment
import com.dofury.moneycycle.fragment.SettingFragment

private const val TAG_HOME = "home_fragment"
private const val TAG_SETTING = "setting_fragment"
private const val TAG_LIST = "list_fragment"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("test","hi")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar : Toolbar = findViewById(R.id.toolbar)

        binding.navHostFragmentContentMain.adapter = MainPagerAdapter(this)
        binding.navHostFragmentContentMain.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.navigationView.menu.getItem(position).isChecked = true
                }
            }
        )
        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> binding.navHostFragmentContentMain.currentItem = 0
                R.id.settingFragment -> binding.navHostFragmentContentMain.currentItem = 1
                R.id.listFragment -> binding.navHostFragmentContentMain.currentItem = 2
            }
            true
        }
        //setFragment(TAG_HOME, HomeFragment())

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)//기본 타이틀 삭제

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if(manager.findFragmentByTag(tag)==null){
            fragTransaction.add(R.id.nav_host_fragment_content_main,fragment,tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val setting = manager.findFragmentByTag(TAG_SETTING)
        val list = manager.findFragmentByTag(TAG_LIST)

        if(home != null){
            fragTransaction.hide(home)
        }
        if(setting != null){
            fragTransaction.hide(setting)
        }

        if(list != null){
            fragTransaction.hide(list)
        }

        if(tag == TAG_HOME){
            if(home!=null){
                HomeFragment().init()
                fragTransaction.show(home)
            }
        }
        else if(tag == TAG_SETTING){
            if(setting!=null){
                fragTransaction.show(setting)
            }
        }
        else if(tag == TAG_LIST){
            if(list!=null){
                fragTransaction.show(list)
            }
        }
        fragTransaction.commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {

                System.exit(0) // 현재 액티비티를 종료시킨다.
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}