package com.screenrecorder.fragment.screen_recorder

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.hbisoft.hbrecorder.HBRecorder
import com.hbisoft.hbrecorder.HBRecorderListener
import com.screenrecorder.R
import com.screenrecorder.databinding.FragmentScreenRecorderBinding
import com.screenrecorder.fragment.base.BaseFragment
import com.screenrecorder.helper.FileHelper
import com.screenrecorder.helper.PermissionHelper
import com.screenrecorder.helper.StringHelper

class ScreenRecorderFragment :
    BaseFragment<FragmentScreenRecorderBinding>(R.layout.fragment_screen_recorder),
    HBRecorderListener {

    private lateinit var permissionHelper: PermissionHelper
    private val permissionRequestCode = 111
    private val screenRecordRequestCode = 222

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

    private lateinit var hbRecorder: HBRecorder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        permissionHelper = PermissionHelper()
        hbRecorder = HBRecorder(context, this)
        setupView()
    }

    private fun setupView() {
        dataBinding.btnStart.setOnClickListener {
            checkPermission()
        }

        dataBinding.btnPause.setOnClickListener {

        }

        dataBinding.btnStop.setOnClickListener {

        }
    }

    private fun checkPermission() {
        permissionHelper.check(requireActivity(), permissions,
            onSuccess = {
                startRecord()
            },
            onError = {
                permissionHelper.request(this, permissions, permissionRequestCode)
            })
    }

    private fun startRecord() {
        if (hbRecorder.isBusyRecording) {
            hbRecorder.stopScreenRecording()
        } else {
            quickSettings()
            val a = Context.MEDIA_PROJECTION_SERVICE
            val mediaProjectionManager =
                requireContext().getSystemService(a) as MediaProjectionManager?
            val permissionIntent = mediaProjectionManager?.createScreenCaptureIntent()
            startActivityForResult(permissionIntent, screenRecordRequestCode)
        }
    }

    private fun pauseRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (hbRecorder.isRecordingPaused) {
                hbRecorder.resumeScreenRecording()
                dataBinding.btnPause.text = "Pause"
            } else {
                hbRecorder.pauseScreenRecording()
                dataBinding.btnPause.text = "Resume"
            }
        } else {
            showToast("You need Android 7 or more to do this")
        }
    }

    private fun quickSettings() {
        hbRecorder.setAudioBitrate(128000)
        hbRecorder.setAudioSamplingRate(44100)
        hbRecorder.recordHDVideo(true)
        hbRecorder.isAudioEnabled(true)
        //Customise Notification
//        hbRecorder.setNotificationSmallIcon(R.drawable.ic_launcher_foreground)
        //hbRecorder.setNotificationSmallIconVector(R.drawable.ic_baseline_videocam_24);
        hbRecorder.setNotificationTitle("a")
        hbRecorder.setNotificationDescription("b")
    }

    private fun setOutputPath() {
        val filename: String = StringHelper.getDateTimeNow()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = requireActivity().contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, FileHelper.shortPath)
            contentValues.put(MediaStore.Video.Media.TITLE, filename)
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            val mUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
            //FILE NAME SHOULD BE THE SAME
            hbRecorder.fileName = filename
            hbRecorder.setOutputUri(mUri)
        } else {
            FileHelper.createAppFolder()
            hbRecorder.setOutputPath(FileHelper.path)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == screenRecordRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                //Set file path or Uri depending on SDK version
                setOutputPath()
                //Start screen recording
                hbRecorder.startScreenRecording(data, resultCode, activity)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        showToast("Don't have permissions for recording")
    }

    override fun HBRecorderOnStart() {

    }

    override fun HBRecorderOnComplete() {

    }

    override fun HBRecorderOnError(errorCode: Int, reason: String?) {

    }
}