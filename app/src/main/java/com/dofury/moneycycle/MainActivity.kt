package com.dofury.moneycycle

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.dofury.moneycycle.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


private const val TAG_HOME = "home_fragment"
private const val TAG_SETTING = "setting_fragment"

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar : Toolbar = findViewById(R.id.toolbar)




        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.settingFragment -> setFragment(TAG_SETTING, SettingFragment())
            }
            true
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)//기본 타이틀 삭제
/*        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)*/

    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if(manager.findFragmentByTag(tag)==null){
            fragTransaction.add(R.id.nav_host_fragment_content_main,fragment,tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val setting = manager.findFragmentByTag(TAG_SETTING)

        if(home != null){
            fragTransaction.hide(home)
        }
        if(setting != null){
            fragTransaction.hide(setting)
        }

        if(tag == TAG_HOME){
            if(home!=null){
                fragTransaction.show(home)
            }
        }
        else if(tag == TAG_SETTING){
            if(setting!=null){
                fragTransaction.show(setting)
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
                Log.d("ToolBar_item: ", "뒤로가기 버튼 클릭")
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}