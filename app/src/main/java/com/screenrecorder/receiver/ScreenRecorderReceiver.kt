package com.screenrecorder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.screenrecorder.service.MyScreenRecorderService

class ScreenRecorderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mAction = intent.getStringExtra("action")
        val screenRecorderIntent = Intent("com.screenrecorder.SCREEN_RECORDER")
        screenRecorderIntent.putExtra("action", mAction)
       var intent2 = Intent(context , MyScreenRecorderService::class.java)
        intent2.putExtra("action", mAction)
        context.sendBroadcast(screenRecorderIntent)
    }
}