package com.screenrecorder.service

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
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
            val notification: Notification

            val startRecordIntent = Intent(this, ScreenRecorderReceiver::class.java)
            startRecordIntent.putExtra("action", "start")
            val startRecordPendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= 31) {
                PendingIntent.getBroadcast(this, 0, startRecordIntent, PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getBroadcast(this, 0, startRecordIntent, 0)
            }
            val startAction = Notification.Action.Builder(
                Icon.createWithResource(this, R.drawable.ic_camera),
                "Start",
                startRecordPendingIntent
            ).build()

            val pauseRecordIntent = Intent(this, ScreenRecorderReceiver::class.java)
            pauseRecordIntent.putExtra("action", "pause")
            val pauseRecordPendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= 31) {
                PendingIntent.getBroadcast(this, 1, pauseRecordIntent, PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getBroadcast(this, 1, pauseRecordIntent, 0)
            }
            val pauseAction = Notification.Action.Builder(
                Icon.createWithResource(this, R.drawable.ic_settings),
                "Pause",
                pauseRecordPendingIntent
            ).build()

            notification = Notification.Builder(applicationContext, channelId).setOngoing(true)
                .setSmallIcon(R.drawable.ic_video).setContentText("Drag down")
                .addAction(startAction).addAction(pauseAction).build()

            startForeground(102, notification)
        } else {
            startForeground(102, Notification())
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
