package com.akash.audiowidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.RemoteViews
import android.widget.RemoteViewsService

import com.akash.audiowidget.models.AudioStreamType
import com.akash.audiowidget.views.VolumeControllerRemoteViews

import java.util.ArrayList
import java.util.Arrays

class WidgetRemoveViewFactory internal constructor(internal var context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private val appWidgetId: Int

    internal var itemList: MutableList<AudioStreamType>

    internal var audioManager: AudioManager

    init {
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        itemList = ArrayList()

        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private fun updateListView() {
        if (itemList.isEmpty() == false) {
            return
        }

        itemList.addAll(Arrays.asList(*AudioStreamType.values()))
    }

    override fun onCreate() {
        updateListView()
    }

    override fun onDataSetChanged() {
        updateListView()
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getViewAt(position: Int): RemoteViews {

        val type = itemList[position]

        val remoteViews = VolumeControllerRemoteViews(context)
        remoteViews.setupView(type, audioManager)

        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}
