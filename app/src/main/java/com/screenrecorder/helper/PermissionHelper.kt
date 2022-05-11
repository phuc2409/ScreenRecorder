package com.screenrecorder.helper

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHelper() {

    fun request(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    fun request(fragment: Fragment, permissions: Array<String>, requestCode: Int) {
        fragment.requestPermissions(permissions, requestCode)
    }

    fun check(
        activity: Activity,
        permissions: Array<String>,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        for (i in permissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    i
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                onError()
                return
            }
        }
        onSuccess()
    }
}