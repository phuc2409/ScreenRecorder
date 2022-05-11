package com.screenrecorder.fragment.screen_recorder

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.hbisoft.hbrecorder.Constants
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
        dataBinding.btnPause.isEnabled = false
        dataBinding.btnStart.setOnClickListener {
            checkPermission()
        }

        dataBinding.btnPause.setOnClickListener {
            pauseRecord()
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
//            quickSettings()
            customSettings()
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
        //Customise Notification with png icon
//        hbRecorder.setNotificationSmallIcon(R.drawable.ic_launcher_foreground)
        //hbRecorder.setNotificationSmallIconVector(R.drawable.ic_baseline_videocam_24);
        hbRecorder.setNotificationTitle("a")
        hbRecorder.setNotificationDescription("b")
    }

    private fun customSettings() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        //Is audio enabled
        val audioEnabled = prefs.getBoolean("key_record_audio", true)
        hbRecorder.isAudioEnabled(audioEnabled)

        //Audio Source
        val audioSource = prefs.getString("key_audio_source", null)
        if (audioSource != null) {
            when (audioSource) {
                "0" -> hbRecorder.setAudioSource("DEFAULT")
                "1" -> hbRecorder.setAudioSource("MIC")
                "2" -> hbRecorder.setAudioSource("VOICE_UPLINK")
                "3" -> hbRecorder.setAudioSource("VOICE_DOWNLINK")
                "4" -> hbRecorder.setAudioSource("VOICE_CALL")
                "5" -> hbRecorder.setAudioSource("CAMCODER")
                "6" -> hbRecorder.setAudioSource("VOICE_RECOGNITION")
                "7" -> hbRecorder.setAudioSource("VOICE_COMMUNICATION")
                "8" -> hbRecorder.setAudioSource("REMOTE_SUBMIX")
                "9" -> hbRecorder.setAudioSource("UNPROCESSED")
                "10" -> hbRecorder.setAudioSource("VOICE_PERFORMANCE")
            }
        }

        //Video Encoder
        val videoEncoder = prefs.getString("key_video_encoder", null)
        if (videoEncoder != null) {
            when (videoEncoder) {
                "0" -> hbRecorder.setVideoEncoder("DEFAULT")
                "1" -> hbRecorder.setVideoEncoder("H263")
                "2" -> hbRecorder.setVideoEncoder("H264")
                "3" -> hbRecorder.setVideoEncoder("MPEG_4_SP")
                "4" -> hbRecorder.setVideoEncoder("VP8")
                "5" -> hbRecorder.setVideoEncoder("HEVC")
            }
        }

        //NOTE - THIS MIGHT NOT BE SUPPORTED SIZES FOR YOUR DEVICE
        //Video Dimensions
        val videoResolution = prefs.getString("key_video_resolution", null)
        if (videoResolution != null) {
            when (videoResolution) {
                "0" -> hbRecorder.setScreenDimensions(426, 240)
                "1" -> hbRecorder.setScreenDimensions(640, 360)
                "2" -> hbRecorder.setScreenDimensions(854, 480)
                "3" -> hbRecorder.setScreenDimensions(1280, 720)
                "4" -> hbRecorder.setScreenDimensions(1920, 1080)
            }
        }

        //Video Frame Rate
        val videoFrameRate = prefs.getString("key_video_fps", null)
        if (videoFrameRate != null) {
            when (videoFrameRate) {
                "0" -> hbRecorder.setVideoFrameRate(60)
                "1" -> hbRecorder.setVideoFrameRate(50)
                "2" -> hbRecorder.setVideoFrameRate(48)
                "3" -> hbRecorder.setVideoFrameRate(30)
                "4" -> hbRecorder.setVideoFrameRate(25)
                "5" -> hbRecorder.setVideoFrameRate(24)
            }
        }

        //Video Bitrate
        val videoBitRate = prefs.getString("key_video_bitrate", null)
        if (videoBitRate != null) {
            when (videoBitRate) {
                "1" -> hbRecorder.setVideoBitrate(12000000)
                "2" -> hbRecorder.setVideoBitrate(8000000)
                "3" -> hbRecorder.setVideoBitrate(7500000)
                "4" -> hbRecorder.setVideoBitrate(5000000)
                "5" -> hbRecorder.setVideoBitrate(4000000)
                "6" -> hbRecorder.setVideoBitrate(2500000)
                "7" -> hbRecorder.setVideoBitrate(1500000)
                "8" -> hbRecorder.setVideoBitrate(1000000)
            }
        }

        //Output Format
        val outputFormat = prefs.getString("key_output_format", null)
        if (outputFormat != null) {
            when (outputFormat) {
                "0" -> hbRecorder.setOutputFormat("DEFAULT")
                "1" -> hbRecorder.setOutputFormat("THREE_GPP")
                "2" -> hbRecorder.setOutputFormat("AMR_NB")
                "3" -> hbRecorder.setOutputFormat("AMR_WB")
                "4" -> hbRecorder.setOutputFormat("AAC_ADTS")
                "5" -> hbRecorder.setOutputFormat("MPEG_2_TS")
                "6" -> hbRecorder.setOutputFormat("WEBM")
                "7" -> hbRecorder.setOutputFormat("OGG")
                "8" -> hbRecorder.setOutputFormat("MPEG_4")
            }
        }
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
                dataBinding.btnStart.text = "Stop"
                dataBinding.btnPause.isEnabled = true
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
        Log.d("Record", "Start record")
    }

    override fun HBRecorderOnComplete() {
        dataBinding.btnStart.text = "Start"
        dataBinding.btnPause.isEnabled = false
    }

    override fun HBRecorderOnError(errorCode: Int, reason: String?) {
        // Error 38 happens when
        // - the selected video encoder is not supported
        // - the output format is not supported
        // - if another app is using the microphone

        //It is best to use device default

        when (errorCode) {
            Constants.SETTINGS_ERROR -> {
                showToast("Some settings are not supported by your device")
            }
            Constants.MAX_FILE_SIZE_REACHED_ERROR -> {
                showToast("The file reached the designated max size")
            }
            else -> {
                showToast("Record error")
            }
        }
        Log.e("Record", reason!!)

        dataBinding.btnStart.text = "Start"
    }
}