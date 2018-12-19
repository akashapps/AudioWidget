package com.akash.audiowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WidgetRemoveViewFactory implements RemoteViewsService.RemoteViewsFactory {

    enum AudioStreamType {
        RING(2, "RING", "RING_VOLUME_UP", "RING_VOLUME_DOWN"),
        MUSIC(3, "MUSIC", "MUSIC_VOLUME_UP", "MUSIC_VOLUME_DOWN"),
        ALARM(4, "ALARM", "ALARM_VOLUME_UP", "ALARM_VOLUME_DOWN"),
        NOTIFICATION(5, "NOTIFICATION", "NOTIFICATION_VOLUME_UP", "NOTIFICATION_VOLUME_DOWN"),
        BLUETOOTH(6, "BLUETOOTH", "BLUETOOTH_VOLUME_UP", "BLUETOOTH_VOLUME_DOWN");

        int streamConstant;
        String title;
        String volumeUpString;
        String volumeDownString;

        AudioStreamType(int i, String title, String volume_up, String volume_down) {
            streamConstant = i;
            this.title = title;
            volumeUpString = volume_up;
            volumeDownString = volume_down;
        }

        int getVolume(AudioManager audioManager){
            return audioManager.getStreamVolume(streamConstant);
        }

        int getMaxVolume(AudioManager audioManager){
            return audioManager.getStreamMaxVolume(streamConstant);
        }

        static AudioStreamType getStringTypeFromString(String s){
            for(AudioStreamType type: AudioStreamType.values()){
                if (type.volumeDownString.equals(s) || type.volumeUpString.equals(s)){
                    return type;
                }
            }

            return AudioStreamType.RING;
        }
    }

    Context context;
    private int appWidgetId;

    List<AudioStreamType> itemList;

    AudioManager audioManager;

    WidgetRemoveViewFactory(Context context, Intent intent){
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        itemList = new ArrayList<>();

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private void updateListView(){
        if (itemList.isEmpty() == false){
            return;
        }

        itemList.addAll(Arrays.asList(AudioStreamType.values()));
    }

    @Override
    public void onCreate() {
        updateListView();
    }

    @Override
    public void onDataSetChanged() {
        updateListView();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        AudioStreamType type = itemList.get(position);

        VolumeControllerRemoteViews remoteViews = new VolumeControllerRemoteViews(context);
        remoteViews.setupView(context, type, audioManager);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, ListViewAudioWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
