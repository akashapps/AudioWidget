package com.akash.audiowidget;

import android.content.Context;
import android.widget.RemoteViews;

public class VolumeControllerRemoteViews extends RemoteViews {

    public VolumeControllerRemoteViews(Context context) {
        this(context.getPackageName(), R.layout.list_item_volume_controller);
    }

    public VolumeControllerRemoteViews(String packageName, int layoutId) {
        super(packageName, layoutId);
    }
}
