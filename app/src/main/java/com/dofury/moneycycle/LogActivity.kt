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


class LogActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibClose.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })

    }


}