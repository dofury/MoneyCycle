package com.dofury.moneycycle.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dofury.moneycycle.MyApplication
import com.dofury.moneycycle.R
import com.dofury.moneycycle.databinding.ActivityMainBinding
import com.dofury.moneycycle.fragment.HomeFragment
import com.dofury.moneycycle.fragment.ListFragment
import com.dofury.moneycycle.fragment.SettingFragment
import com.dofury.moneycycle.viewmodel.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

private const val TAG_HOME = "home_fragment"
private const val TAG_SETTING = "setting_fragment"
private const val TAG_LIST = "list_fragment"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        setFragment(TAG_HOME, HomeFragment())
        viewModel.currentAmountUpdate()

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.listFragment -> setFragment(TAG_LIST, ListFragment())
                R.id.settingFragment -> setFragment(TAG_SETTING, SettingFragment())
            }
            true
        }
        if(MyApplication.prefs.getBoolean("auto_login",false) && MyApplication.prefs.getString("id_token","").isNotBlank()){
            LoginActivity().firebaseAuthWithGoogle(MyApplication.prefs.getString("id_token",""),this)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.currentAmountUpdate()
        viewModel.remainBudgetAmountUpdate()
        viewModel.budgetPlusAmountUpdate()
        viewModel.moneyLogListLoad()
    }
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
                viewModel.currentAmountUpdate()
                viewModel.remainBudgetAmountUpdate()
                viewModel.budgetPlusAmountUpdate()
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
                //ListFragment
                fragTransaction.show(list)
            }
        }
        fragTransaction.commitAllowingStateLoss()
    }


    fun restart(){
        ActivityCompat.finishAffinity(this)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        exitProcess(0)
    }
}