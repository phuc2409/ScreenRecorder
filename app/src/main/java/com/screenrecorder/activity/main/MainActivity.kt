package com.screenrecorder.activity.main

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.screenrecorder.R
import com.screenrecorder.activity.base.BaseActivity
import com.screenrecorder.databinding.ActivityMainBinding
import com.screenrecorder.fragment.add.AddFragment
import com.screenrecorder.fragment.edit.EditFragment
import com.screenrecorder.fragment.screen_capture.ScreenCaptureFragment
import com.screenrecorder.fragment.screen_recorder.ScreenRecorderFragment
import com.screenrecorder.fragment.settings.SettingsFragment
import com.screenrecorder.helper.PermissionHelper

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var permissionHelper: PermissionHelper

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionHelper = PermissionHelper(this)
        setupView()
        permissionHelper.request(permissions)
    }

    private fun setupView() {
        loadFragment(R.id.container, ScreenRecorderFragment())
        dataBinding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.screen_recorder -> {
                    loadFragment(R.id.container, ScreenRecorderFragment())
                    true
                }
                R.id.screen_capture -> {
                    loadFragment(R.id.container, ScreenCaptureFragment())
                    true
                }
                R.id.add -> {
                    loadFragment(R.id.container, AddFragment())
                    true
                }
                R.id.edit -> {
                    loadFragment(R.id.container, EditFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(R.id.container, SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.check(this.permissions,
            onSuccess = {
                Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show()
            },
            onError = {
                Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
            })
    }
}