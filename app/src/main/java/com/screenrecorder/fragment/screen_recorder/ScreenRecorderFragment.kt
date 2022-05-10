package com.screenrecorder.fragment.screen_recorder

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.PathUtils
import com.screenrecorder.R
import com.screenrecorder.databinding.FragmentScreenRecorderBinding
import com.screenrecorder.fragment.base.BaseFragment
import com.screenrecorder.helper.ScreenRecorderHelper
import java.io.File

class ScreenRecorderFragment :
    BaseFragment<FragmentScreenRecorderBinding>(R.layout.fragment_screen_recorder) {
    private var screenRecorderHelper: ScreenRecorderHelper? = null
    private val afdd: AssetFileDescriptor by lazy { requireActivity().assets.openFd("test.aac") }
    private var mediaPlayer: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {

        dataBinding.btnPlay.setOnClickListener {
            if (screenRecorderHelper == null) {
                screenRecorderHelper =
                    ScreenRecorderHelper(
                        requireActivity(),
                        this,
                        object : ScreenRecorderHelper.OnVideoRecordListener {
                            override fun onBeforeRecord() {
                            }

                            override fun onStartRecord() {
                                play()
                            }

                            override fun onCancelRecord() {
                                releasePlayer()
                            }

                            override fun onEndRecord() {
                                releasePlayer()
                            }

                        },
                        Environment.getExternalStorageDirectory().absolutePath + File.separator + "DCIM" + File.separator + "Camera"
                    )
            }
            screenRecorderHelper?.apply {
                if (!isRecording) {
                    //todo: nghiên cứu mở audio
//                    recordAudio = true
                    startRecord()
                }
            }
        }

        dataBinding.btnStop.setOnClickListener {
            screenRecorderHelper?.apply {
                if (isRecording) {
                    if (mediaPlayer != null) {
                        // 如果选择带参数的 stop 方法，则录制音频无效
                        stopRecord(mediaPlayer!!.duration.toLong(), 15 * 1000, afdd)
                    } else {
                        stopRecord()
                    }
                }
            }
        }
    }

    private fun play() {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer?.apply {
                this.reset()
                this.setDataSource(afdd.fileDescriptor, afdd.startOffset, afdd.length)
                this.isLooping = true
                this.prepare()
                this.start()
            }
        } catch (e: Exception) {
            Log.d("nanchen2251", "播放音乐失败")
        } finally {

        }
    }

    private fun releasePlayer() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            screenRecorderHelper?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        screenRecorderHelper?.clearAll()
        afdd.close()
        super.onDestroy()
    }
}