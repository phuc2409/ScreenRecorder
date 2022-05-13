package com.hbisoft.hbrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        Intent screenRecorderIntent = new Intent("com.screenrecorder.SCREEN_RECORDER");
        screenRecorderIntent.putExtra("action", action);
        context.sendBroadcast(screenRecorderIntent);
    }
}
