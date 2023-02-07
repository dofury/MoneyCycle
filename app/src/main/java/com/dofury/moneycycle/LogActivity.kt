package com.dofury.moneycycle

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.dofury.moneycycle.databinding.ActivityLogBinding
import com.dofury.moneycycle.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG_NUM = "numPad_fragment"
private const val TAG_CATEGORY = "category_fragment"
class LogActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonEvent()
        setFragment(TAG_NUM,NumPadFragment())
    }
    public fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if(manager.findFragmentByTag(tag)==null){
            fragTransaction.add(R.id.view,fragment,tag)
        }

        val numPad = manager.findFragmentByTag(TAG_NUM)
        val category = manager.findFragmentByTag(TAG_CATEGORY)

        if(numPad != null){
            fragTransaction.hide(numPad)
        }
        if(category != null){
            fragTransaction.hide(category)
        }

        if(tag == TAG_NUM){
            if(numPad!=null){
                fragTransaction.show(numPad)
            }
        }
        else if(tag == TAG_CATEGORY){
            if(category!=null){
                fragTransaction.show(category)
            }
        }
        fragTransaction.commitAllowingStateLoss()
    }

    private fun buttonEvent() {
        binding.ibClose.setOnClickListener(View.OnClickListener {//메인 화면 전환
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })

        binding.rbOne.setOnClickListener(View.OnClickListener { //지출을 클릭시
            binding.tvSign.text = "-"
        })
        binding.rbTwo.setOnClickListener(View.OnClickListener { //수입을 클릭시
            binding.tvSign.text = "+"
        })

    }

    fun inputNumber(number: String){

        if(binding.tvNumber.text == "0"){
            binding.tvNumber.text = ""
        }
        if(binding.tvNumber.text.length <=10){
            binding.tvNumber.text = binding.tvNumber.text.toString()+number
        }
        else{
            Snackbar.make(binding.root,"10자를 넘을 수 없습니다",Snackbar.LENGTH_LONG)
                .setAction("action",null)
                .show()
        }

    }
    fun removeNumber() {
        if(binding.tvNumber.text != "0"){
            binding.tvNumber.text = binding.tvNumber.text.toString().subSequence(0,binding.tvNumber.length()-1)
        }
        Log.d("text",binding.tvNumber.text.toString())
        if(binding.tvNumber.text == ""){
            binding.tvNumber.text = "0"
        }
    }
}