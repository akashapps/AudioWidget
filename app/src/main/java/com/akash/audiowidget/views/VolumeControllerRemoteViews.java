package com.akash.audiowidget.views;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

import com.akash.audiowidget.ListViewAudioWidget;
import com.akash.audiowidget.R;
import com.akash.audiowidget.models.AudioStreamType;

public class VolumeControllerRemoteViews extends RemoteViews {

    public VolumeControllerRemoteViews(Context context) {
        this(context.getPackageName(), R.layout.list_item_voulume_cell);
    }

    public VolumeControllerRemoteViews(String packageName, int layoutId) {
        super(packageName, layoutId);
    }

    public void setupView(Context context, AudioStreamType type, AudioManager audioManager) {

        setTextViewText(R.id.title, type.title);
        setProgressBar(R.id.progress_bar, type.getMaxVolume(audioManager), type.getVolume(audioManager), false);
        setOnClickPendingIntent(R.id.plus, getPendingSelfIntent(context, type.volumeUpString));
        setOnClickPendingIntent(R.id.minus, getPendingSelfIntent(context, type.volumeDownString));
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, ListViewAudioWidget.class);
//        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
