package com.screenrecorder.fragment.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.screenrecorder.R

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private var keyVideoResolution: ListPreference? = null
    private var keyAudioSource: ListPreference? = null
    private var keyVideoEncoder: ListPreference? = null
    private var keyVideoFps: ListPreference? = null
    private var keyVideoBitrate: ListPreference? = null
    private var keyOutputFormat: ListPreference? = null
    private var keyRecordAudio: SwitchPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_main)

        keyRecordAudio = findPreference(getString(R.string.key_record_audio))

        keyAudioSource = findPreference(getString(R.string.key_audio_source))
        if (keyAudioSource != null) {
            keyAudioSource!!.onPreferenceChangeListener = this
        }

        keyVideoEncoder = findPreference(getString(R.string.key_video_encoder))
        if (keyVideoEncoder != null) {
            keyVideoEncoder!!.onPreferenceChangeListener = this
        }

        keyVideoResolution = findPreference(getString(R.string.key_video_resolution))
        if (keyVideoResolution != null) {
            keyVideoResolution!!.onPreferenceChangeListener = this
        }

        keyVideoFps = findPreference(getString(R.string.key_video_fps))
        if (keyVideoFps != null) {
            keyVideoFps!!.onPreferenceChangeListener = this
        }

        keyVideoBitrate = findPreference(getString(R.string.key_video_bitrate))
        if (keyVideoBitrate != null) {
            keyVideoBitrate!!.onPreferenceChangeListener = this
        }

        keyOutputFormat = findPreference(getString(R.string.key_output_format))
        if (keyOutputFormat != null) {
            keyOutputFormat!!.onPreferenceChangeListener = this
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        val preferenceKey = preference.key
        val listPreference: ListPreference?

        when (preferenceKey) {
            "key_audio_source" -> {
                listPreference = findPreference(getString(R.string.key_audio_source))
                listPreference?.let {
                    listPreference.summary =
                        listPreference.entries[listPreference.findIndexOfValue(newValue.toString())]
                }
            }
            "key_video_encoder" -> {
                listPreference = findPreference(getString(R.string.key_video_encoder))
                listPreference?.let {
                    listPreference.summary =
                        listPreference.entries[listPreference.findIndexOfValue(newValue.toString())]
                    listPreference.value = newValue.toString()
                }
            }
            "key_video_resolution" -> {
                listPreference = findPreference(getString(R.string.key_video_resolution))
                listPreference?.let {
                    listPreference.summary =
                        listPreference.entries[listPreference.findIndexOfValue(newValue.toString())]
                    listPreference.value = newValue.toString()
                }
            }
            "key_video_fps" -> {
                listPreference = findPreference(getString(R.string.key_video_fps))
                listPreference?.let {
                    listPreference.summary =
                        listPreference.entries[listPreference.findIndexOfValue(newValue.toString())]
                    listPreference.value = newValue.toString()
                }
            }
            "key_video_bitrate" -> {
                listPreference = findPreference(getString(R.string.key_video_bitrate))
                listPreference?.let {
                    listPreference.summary =
                        listPreference.entries[listPreference.findIndexOfValue(newValue.toString())]
                    listPreference.value = newValue.toString()
                }
            }
            "key_output_format" -> {
                listPreference = findPreference(getString(R.string.key_output_format))
                listPreference?.let {
                    listPreference.summary =
                        listPreference.entries[listPreference.findIndexOfValue(newValue.toString())]
                    listPreference.value = newValue.toString()
                }
            }
        }

        return true
    }
}