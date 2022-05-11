package com.screenrecorder.activity.main

import android.Manifest
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
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
    private val permissionRequestCode = 111

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionHelper = PermissionHelper()
        setupView()
        permissionHelper.request(this, permissions, permissionRequestCode)
    }

    private fun setupView() {
        val mainAdapter = MainAdapter(this)
        mainAdapter.addFragment(ScreenRecorderFragment(), "")
        mainAdapter.addFragment(ScreenCaptureFragment(), "")
        mainAdapter.addFragment(AddFragment(), "")
        mainAdapter.addFragment(EditFragment(), "")
        mainAdapter.addFragment(SettingsFragment(), "")
        dataBinding.viewPager2.offscreenPageLimit = 5
        dataBinding.viewPager2.adapter = mainAdapter

        TabLayoutMediator(dataBinding.tabLayout, dataBinding.viewPager2) { tab, position ->
            tab.text = mainAdapter.fragmentTitles[position]
        }.attach()

        dataBinding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_video)
        dataBinding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_camera)
        dataBinding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_add_circle)
        dataBinding.tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_edit)
        dataBinding.tabLayout.getTabAt(4)?.setIcon(R.drawable.ic_settings)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.check(this, this.permissions,
            onSuccess = {
                showToast("Full permissions")
            },
            onError = {
                showToast("Not full permissions")
            })
    }
}