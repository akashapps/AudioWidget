package com.akash.audiowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

import com.akash.audiowidget.models.AudioStreamType;
import com.akash.audiowidget.views.VolumeControllerRemoteViews;

public class ListViewAudioWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.audio_list_widget);

            Intent intent = new Intent(context, MyWidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.list_view, intent);
            views.setPendingIntentTemplate(R.id.list_view, getPendingIntent(context));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras() != null){
            String string = intent.getExtras().getString(VolumeControllerRemoteViews.EXTRA_DATA);

            if (string != null && !string.isEmpty()){
                AudioStreamType type = AudioStreamType.getTypeFromString(string);

                if (type == null){
                    return;
                }

                type.doAction(context);
                sendUpdate(context);
                return;
            }
        }

        super.onReceive(context, intent);
    }

    PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, ListViewAudioWidget.class);
        return PendingIntent.getBroadcast(context, 101, intent, 0);
    }

    private void sendUpdate(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, ListViewAudioWidget.class));
        manager.notifyAppWidgetViewDataChanged(ids, R.id.list_view);
    }
}
