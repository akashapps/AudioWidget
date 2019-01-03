package com.akash.audiowidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import com.akash.audiowidget.models.AudioStreamType
import com.akash.audiowidget.views.VolumeControllerRemoteViews

class ListViewAudioWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.audio_list_widget)

            val intent = Intent(context, MyWidgetRemoteViewsService::class.java)
            views.setRemoteAdapter(R.id.list_view, intent)
            views.setPendingIntentTemplate(R.id.list_view, getPendingIntent(context))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.extras != null) {
            val string = intent.extras!!.getString(VolumeControllerRemoteViews.EXTRA_DATA)

            if (string != null && !string.isEmpty()) {
                val type = AudioStreamType.getTypeFromString(string) ?: return

                type.doAction(context)
                sendUpdate(context)
                return
            }
        }

        super.onReceive(context, intent)
    }

    internal fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ListViewAudioWidget::class.java)
        return PendingIntent.getBroadcast(context, 101, intent, 0)
    }

    private fun sendUpdate(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, ListViewAudioWidget::class.java))
        manager.notifyAppWidgetViewDataChanged(ids, R.id.list_view)
    }
}
