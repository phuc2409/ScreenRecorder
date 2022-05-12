package com.screenrecorder.service

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.screenrecorder.R
import com.screenrecorder.receiver.ScreenRecorderReceiver

class ScreenRecorderService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "002"
            val channelName = "RecordNotification"
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.lightColor = Color.BLUE
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            val notification = Notification.Builder(applicationContext, channelId).setOngoing(true)
                .setSmallIcon(R.drawable.ic_video).setContentText("Drag down")
                .addAction(createAction("Start", "start", 0))
                .addAction(createAction("Pause", "pause", 1)).build()

            startForeground(102, notification)
        } else {
            startForeground(102, Notification())
        }

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createAction(title: String, action: String, requestCode: Int): Notification.Action {
        val intent = Intent(this, ScreenRecorderReceiver::class.java)
        intent.putExtra("action", action)
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= 31) {
            PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, requestCode, intent, 0)
        }
        return Notification.Action.Builder(
            Icon.createWithResource(this, R.drawable.ic_camera),
            title,
            pendingIntent
        ).build()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
