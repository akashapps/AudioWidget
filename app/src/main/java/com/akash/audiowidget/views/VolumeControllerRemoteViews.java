package com.akash.audiowidget.views;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.akash.audiowidget.R;
import com.akash.audiowidget.models.AudioStreamType;

public class VolumeControllerRemoteViews extends RemoteViews {

    public static String EXTRA_DATA = "EXTRA_DATA";

    public VolumeControllerRemoteViews(Context context) {
        this(context.getPackageName(), R.layout.list_item_voulume_cell);
    }

    public VolumeControllerRemoteViews(String packageName, int layoutId) {
        super(packageName, layoutId);
    }

    public void setupView(AudioStreamType type, AudioManager audioManager) {
        setTextViewText(R.id.title, type.title);
        setProgressBar(R.id.progress_bar, type.getMaxVolume(audioManager), type.getVolume(audioManager), false);

        setOnClickFillInIntent(R.id.plus, getIntent(type.volumeUpString));
        setOnClickFillInIntent(R.id.minus, getIntent(type.volumeDownString));
    }

    private Intent getIntent(String action) {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putString(EXTRA_DATA, action);
        intent.putExtras(extras);
        return intent;
    }
}
