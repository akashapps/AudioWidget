package com.akash.audiowidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.akash.audiowidget.models.AudioStreamType;
import com.akash.audiowidget.views.VolumeControllerRemoteViews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WidgetRemoveViewFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    private int appWidgetId;

    List<AudioStreamType> itemList;

    AudioManager audioManager;

    WidgetRemoveViewFactory(Context context, Intent intent){
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

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
}
