package com.screenrecorder.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val context: Context) {

    fun request(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(context as Activity, permissions, requestCode)
    }

    fun check(permissions: Array<String>, onSuccess: () -> Unit, onError: () -> Unit) {
        for (i in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context as Activity,
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