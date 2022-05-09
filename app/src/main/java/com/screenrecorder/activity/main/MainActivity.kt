package com.screenrecorder.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.screenrecorder.R
import com.screenrecorder.databinding.ActivityMainBinding
import com.screenrecorder.fragment.add.AddFragment
import com.screenrecorder.fragment.edit.EditFragment
import com.screenrecorder.fragment.screen_capture.ScreenCaptureFragment
import com.screenrecorder.fragment.screen_recorder.ScreenRecorderFragment
import com.screenrecorder.fragment.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupView()
    }

    private fun setupView() {
        loadFragment(ScreenRecorderFragment())
        dataBinding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.screen_recorder -> {
                    loadFragment(ScreenRecorderFragment())
                    true
                }
                R.id.screen_capture -> {
                    loadFragment(ScreenCaptureFragment())
                    true
                }
                R.id.add -> {
                    loadFragment(AddFragment())
                    true
                }
                R.id.edit -> {
                    loadFragment(EditFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}