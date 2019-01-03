package com.akash.audiowidget.views

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.widget.RemoteViews

import com.akash.audiowidget.R
import com.akash.audiowidget.models.AudioStreamType

class VolumeControllerRemoteViews(packageName: String, layoutId: Int) : RemoteViews(packageName, layoutId) {

    constructor(context: Context) : this(context.packageName, R.layout.list_item_voulume_cell) {}

    fun setupView(type: AudioStreamType, audioManager: AudioManager) {
        setTextViewText(R.id.title, type.title)
        setProgressBar(R.id.progress_bar, type.getMaxVolume(audioManager), type.getVolume(audioManager), false)

        setOnClickFillInIntent(R.id.plus, getIntent(type.volumeUpString))
        setOnClickFillInIntent(R.id.minus, getIntent(type.volumeDownString))
    }

    private fun getIntent(action: String): Intent {
        val intent = Intent()
        val extras = Bundle()
        extras.putString(EXTRA_DATA, action)
        intent.putExtras(extras)
        return intent
    }

    companion object {

        var EXTRA_DATA = "EXTRA_DATA"
    }
}
