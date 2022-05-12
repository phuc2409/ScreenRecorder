package com.screenrecorder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenRecorderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val x = intent.getStringExtra("action")
        val screenRecorderIntent = Intent("com.screenrecorder.SCREEN_RECORDER")
        screenRecorderIntent.putExtra("action", x)
        context.sendBroadcast(screenRecorderIntent)
    }
}