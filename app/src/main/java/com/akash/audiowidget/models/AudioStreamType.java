package com.akash.audiowidget.models;

import android.content.Context;
import android.media.AudioManager;

public enum AudioStreamType {
    RING(2, "RING", "RING_VOLUME_UP", "RING_VOLUME_DOWN"),
    MUSIC(3, "MUSIC", "MUSIC_VOLUME_UP", "MUSIC_VOLUME_DOWN"),
    ALARM(4, "ALARM", "ALARM_VOLUME_UP", "ALARM_VOLUME_DOWN"),
    NOTIFICATION(5, "NOTIFICATION", "NOTIFICATION_VOLUME_UP", "NOTIFICATION_VOLUME_DOWN"),
    BLUETOOTH(6, "BLUETOOTH", "BLUETOOTH_VOLUME_UP", "BLUETOOTH_VOLUME_DOWN");

    public int streamConstant;
    public String title;
    public String volumeUpString;
    public String volumeDownString;
    public String actionNeedToBeDone;

    AudioStreamType(int i, String title, String volume_up, String volume_down) {
        streamConstant = i;
        this.title = title;
        volumeUpString = volume_up;
        volumeDownString = volume_down;
        actionNeedToBeDone = "";
    }

    public int getVolume(AudioManager audioManager){
        return audioManager.getStreamVolume(streamConstant);
    }

    public int getMaxVolume(AudioManager audioManager){
        return audioManager.getStreamMaxVolume(streamConstant);
    }

    public static AudioStreamType getTypeFromString(String s){
        for(AudioStreamType type: AudioStreamType.values()){
            if (type.volumeDownString.equals(s) || type.volumeUpString.equals(s)){
                type.actionNeedToBeDone = s;
                return type;
            }
        }

        return null;
    }

    public void doAction(Context context){

        if (actionNeedToBeDone.isEmpty()){
            return;
        }

        if (actionNeedToBeDone.contains("UP")){
            setStreamVolumeUp(context);
        }else {
            setStreamVolumeDown(context);
        }
    }

    private void setStreamVolumeUp(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = audioManager.getStreamVolume(streamConstant);
        int maxStreamVolume = audioManager.getStreamMaxVolume(streamConstant);

        int volumeNeedToBeSet = streamVolume + 2;

        if (volumeNeedToBeSet > maxStreamVolume){
            volumeNeedToBeSet = maxStreamVolume;
        }

        audioManager.setStreamVolume(streamConstant, volumeNeedToBeSet, 0);
    }

    private void setStreamVolumeDown(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = audioManager.getStreamVolume(streamConstant);
        int minStreamVolume = audioManager.getStreamMinVolume(streamConstant);

        int volumeNeedToBeSet = streamVolume - 2;

        if (volumeNeedToBeSet < 0){
            volumeNeedToBeSet = minStreamVolume;
        }

        audioManager.setStreamVolume(streamConstant, volumeNeedToBeSet, 0);
    }
}
