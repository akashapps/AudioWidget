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

    //music
    private static final String MUSIC_VOLUME_UP = "MUSIC_VOLUME_UP";
    private static final String MUSIC_VOLUME_DOWN = "MUSIC_VOLUME_DOWN";

    //alarm
    private static final String ALARM_VOLUME_UP = "ALARM_VOLUME_UP";
    private static final String ALARM_VOLUME_DOWN = "ALARM_VOLUME_DOWN";

    //ring
    private static final String RING_VOLUME_UP = "RING_VOLUME_UP";
    private static final String RING_VOLUME_DOWN = "RING_VOLUME_DOWN";

    //notification
    private static final String NOTIFICATION_VOLUME_UP = "NOTIFICATION_VOLUME_UP";
    private static final String NOTIFICATION_VOLUME_DOWN = "NOTIFICATION_VOLUME_DOWN";

    //Bluetooth
    private static final String BLUETOOTH_VOLUME_UP = "BLUETOOTH_VOLUME_UP";
    private static final String BLUETOOTH_VOLUME_DOWN = "BLUETOOTH_VOLUME_DOWN";

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

        // ring volume
        int ringVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int maxRingVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        views.setProgressBar(R.id.ring_progress_bar, maxRingVolume, ringVolume, false);
        views.setOnClickPendingIntent(R.id.plus_ring, getPendingSelfIntent(context, RING_VOLUME_UP));
        views.setOnClickPendingIntent(R.id.minus_ring, getPendingSelfIntent(context, RING_VOLUME_DOWN));

        //alarm volume
        int alarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        int maxAlarmVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);

        views.setProgressBar(R.id.alarm_progress_bar, maxAlarmVolume, alarmVolume, false);
        views.setOnClickPendingIntent(R.id.plus_alarm, getPendingSelfIntent(context, ALARM_VOLUME_UP));
        views.setOnClickPendingIntent(R.id.minus_alarm, getPendingSelfIntent(context, ALARM_VOLUME_DOWN));

        //Notification Volume
        int notificationVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int maxNotificationVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);

        views.setProgressBar(R.id.notification_progress_bar, maxNotificationVolume, notificationVolume, false);
        views.setOnClickPendingIntent(R.id.plus_notification, getPendingSelfIntent(context, NOTIFICATION_VOLUME_UP));
        views.setOnClickPendingIntent(R.id.minus_notification, getPendingSelfIntent(context, NOTIFICATION_VOLUME_DOWN));

        //bluetooth
        int bluetoothVolume = audioManager.getStreamVolume(6);
        int maxBluetoothVolume = audioManager.getStreamMaxVolume(6);

        views.setProgressBar(R.id.bluetooth_progress_bar, maxBluetoothVolume, bluetoothVolume, false);
        views.setOnClickPendingIntent(R.id.plus_bluetooth, getPendingSelfIntent(context, NOTIFICATION_VOLUME_UP));
        views.setOnClickPendingIntent(R.id.minus_bluetooth, getPendingSelfIntent(context, NOTIFICATION_VOLUME_DOWN));
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
                break;
            case MUSIC_VOLUME_UP:
                setStreamVolumeUp(context, AudioManager.STREAM_MUSIC);
                break;
            case MUSIC_VOLUME_DOWN:
                setStreamVolumeDown(context, AudioManager.STREAM_MUSIC);
                break;
            case RING_VOLUME_UP:
                setStreamVolumeUp(context, AudioManager.STREAM_RING);
                break;
            case RING_VOLUME_DOWN:
                setStreamVolumeDown(context, AudioManager.STREAM_RING);
                break;
            case ALARM_VOLUME_UP:
                setStreamVolumeUp(context, AudioManager.STREAM_ALARM);
                break;
            case ALARM_VOLUME_DOWN:
                setStreamVolumeDown(context, AudioManager.STREAM_ALARM);
                break;
            case NOTIFICATION_VOLUME_UP:
                setStreamVolumeUp(context, AudioManager.STREAM_NOTIFICATION);
                break;
            case NOTIFICATION_VOLUME_DOWN:
                setStreamVolumeDown(context, AudioManager.STREAM_NOTIFICATION);
                break;
            case BLUETOOTH_VOLUME_UP:
                setStreamVolumeUp(context, 6);
                break;
            case BLUETOOTH_VOLUME_DOWN:
                setStreamVolumeDown(context, 6);
                break;
            default:
                super.onReceive(context, intent);
                break;
        }

        sendUpdate(context);
    }

    private void setStreamVolumeUp(Context context, int stream){
     AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
     int streamVolume = audioManager.getStreamVolume(stream);
     int maxStreamVolume = audioManager.getStreamMaxVolume(stream);

     int volumeNeedToBeSet = streamVolume + 2;

     if (volumeNeedToBeSet > maxStreamVolume){
         volumeNeedToBeSet = maxStreamVolume;
     }

     audioManager.setStreamVolume(stream, volumeNeedToBeSet, 0);
    }

    private void setStreamVolumeDown(Context context, int stream){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = audioManager.getStreamVolume(stream);
        int minStreamVolume = audioManager.getStreamMinVolume(stream);

        int volumeNeedToBeSet = streamVolume - 2;

        if (volumeNeedToBeSet < 0){
            volumeNeedToBeSet = minStreamVolume;
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

