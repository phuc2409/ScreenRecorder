package com.screenrecorder.helper

import android.os.Environment
import android.util.Log
import java.io.File

class FileHelper {
    companion object {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            .toString() + "/Screen Recorder"
        val shortPath = "Movies/Screen Recorder"

        fun createAppFolder() {
            val f = File(path)
            if (!f.exists()) {
                if (f.mkdirs()) {
                    Log.i("Folder ", "created")
                }
            }
        }
    }
}