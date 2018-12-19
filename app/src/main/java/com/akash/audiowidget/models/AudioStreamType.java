package com.akash.audiowidget.models;

import android.media.AudioManager;

import com.akash.audiowidget.WidgetRemoveViewFactory;

public enum AudioStreamType {
    RING(2, "RING", "RING_VOLUME_UP", "RING_VOLUME_DOWN"),
    MUSIC(3, "MUSIC", "MUSIC_VOLUME_UP", "MUSIC_VOLUME_DOWN"),
    ALARM(4, "ALARM", "ALARM_VOLUME_UP", "ALARM_VOLUME_DOWN"),
    NOTIFICATION(5, "NOTIFICATION", "NOTIFICATION_VOLUME_UP", "NOTIFICATION_VOLUME_DOWN"),
    BLUETOOTH(6, "BLUETOOTH", "BLUETOOTH_VOLUME_UP", "BLUETOOTH_VOLUME_DOWN");

    public final int streamConstant;
    public final String title;
    public final String volumeUpString;
    public final String volumeDownString;

    AudioStreamType(int i, String title, String volume_up, String volume_down) {
        streamConstant = i;
        this.title = title;
        volumeUpString = volume_up;
        volumeDownString = volume_down;
    }

    public int getVolume(AudioManager audioManager){
        return audioManager.getStreamVolume(streamConstant);
    }

    public int getMaxVolume(AudioManager audioManager){
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
