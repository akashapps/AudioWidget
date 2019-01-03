package com.akash.audiowidget.models

import android.content.Context
import android.media.AudioManager

enum class AudioStreamType private constructor(var streamConstant: Int, var title: String, var volumeUpString: String, var volumeDownString: String) {
    RING(2, "RING", "RING_VOLUME_UP", "RING_VOLUME_DOWN"),
    MUSIC(3, "MUSIC", "MUSIC_VOLUME_UP", "MUSIC_VOLUME_DOWN"),
    ALARM(4, "ALARM", "ALARM_VOLUME_UP", "ALARM_VOLUME_DOWN"),
    NOTIFICATION(5, "NOTIFICATION", "NOTIFICATION_VOLUME_UP", "NOTIFICATION_VOLUME_DOWN"),
    BLUETOOTH(6, "BLUETOOTH", "BLUETOOTH_VOLUME_UP", "BLUETOOTH_VOLUME_DOWN");

    var actionNeedToBeDone: String = ""

    fun getVolume(audioManager: AudioManager): Int {
        return audioManager.getStreamVolume(streamConstant)
    }

    fun getMaxVolume(audioManager: AudioManager): Int {
        return audioManager.getStreamMaxVolume(streamConstant)
    }

    fun doAction(context: Context) {

        if (actionNeedToBeDone.isEmpty()) {
            return
        }

        if (actionNeedToBeDone.contains("UP")) {
            setStreamVolumeUp(context)
        } else {
            setStreamVolumeDown(context)
        }
    }

    private fun setStreamVolumeUp(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val streamVolume = audioManager.getStreamVolume(streamConstant)
        val maxStreamVolume = audioManager.getStreamMaxVolume(streamConstant)

        var volumeNeedToBeSet = streamVolume + 2

        if (volumeNeedToBeSet > maxStreamVolume) {
            volumeNeedToBeSet = maxStreamVolume
        }

        audioManager.setStreamVolume(streamConstant, volumeNeedToBeSet, 0)
    }

    private fun setStreamVolumeDown(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val streamVolume = audioManager.getStreamVolume(streamConstant)
        val minStreamVolume = audioManager.getStreamMinVolume(streamConstant)

        var volumeNeedToBeSet = streamVolume - 2

        if (volumeNeedToBeSet < 0) {
            volumeNeedToBeSet = minStreamVolume
        }

        audioManager.setStreamVolume(streamConstant, volumeNeedToBeSet, 0)
    }

    companion object {

        fun getTypeFromString(s: String): AudioStreamType? {
            for (type in AudioStreamType.values()) {
                if (type.volumeDownString == s || type.volumeUpString == s) {
                    type.actionNeedToBeDone = s
                    return type
                }
            }

            return null
        }
    }
}
