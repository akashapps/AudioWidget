package com.akash.audiowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AudioWidget extends AppWidgetProvider {

    private static final String REFRESH_CLICK_ACTION = "REFRESH_CLICK_ACTION";
    private static final String MUSIC_VOLUME_UP = "MUSIC_VOLUME_UP";
    private static final String MUSIC_VOLUME_DOWN = "MUSIC_VOLUME_DOWN";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.audio_widget);
        views.setOnClickPendingIntent(R.id.refresh, getPendingSelfIntent(context, REFRESH_CLICK_ACTION));
        updateRemoveViews(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private void updateRemoveViews(Context context, RemoteViews views) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // music volume
        int musicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxMusicVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        views.setProgressBar(R.id.music_progress_bar, maxMusicVolume, musicVolume, false);

        views.setOnClickPendingIntent(R.id.plus_music, getPendingSelfIntent(context, MUSIC_VOLUME_UP));
        views.setOnClickPendingIntent(R.id.minus_music, getPendingSelfIntent(context, MUSIC_VOLUME_DOWN));



        int alarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        int notificationVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int ringVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);

        //bluetooth
        int bluetoothVolume = audioManager.getStreamVolume(6);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) {
            return;
        }

        switch (intent.getAction()) {
            case REFRESH_CLICK_ACTION:
                sendUpdate(context);
                break;
            case MUSIC_VOLUME_UP:
                setStreamVolumeUp(context, AudioManager.STREAM_MUSIC);
                sendUpdate(context);
                break;
            case MUSIC_VOLUME_DOWN:
                setStreamVolumeDown(context, AudioManager.STREAM_MUSIC);
                sendUpdate(context);
                break;
            default:
                super.onReceive(context, intent);
                break;
        }
    }

    private void setStreamVolumeUp(Context context, int stream){
     AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
     int musicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
     int maxMusicVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

     int volumeNeedToBeSet = musicVolume + 2;

     if (volumeNeedToBeSet > maxMusicVolume){
         volumeNeedToBeSet = maxMusicVolume;
     }

     audioManager.setStreamVolume(stream, volumeNeedToBeSet, 0);
    }

    private void setStreamVolumeDown(Context context, int stream){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int musicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int minMusicVolume = audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);

        int volumeNeedToBeSet = musicVolume - 2;

        if (volumeNeedToBeSet < 0){
            volumeNeedToBeSet = minMusicVolume;
        }

        audioManager.setStreamVolume(stream, volumeNeedToBeSet, 0);
    }

    private void sendUpdate(Context context) {
        Intent intent = new Intent(context, AudioWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, AudioWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

