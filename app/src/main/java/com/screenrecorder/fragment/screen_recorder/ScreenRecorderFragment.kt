package com.screenrecorder.fragment.screen_recorder

import android.os.Bundle
import android.view.View
import com.screenrecorder.R
import com.screenrecorder.databinding.FragmentScreenRecorderBinding
import com.screenrecorder.fragment.base.BaseFragment

class ScreenRecorderFragment :
    BaseFragment<FragmentScreenRecorderBinding>(R.layout.fragment_screen_recorder) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        dataBinding.btnPlay.setOnClickListener {

        }

        dataBinding.btnStop.setOnClickListener {

        }
    }
}