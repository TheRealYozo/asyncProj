package com.example.asyncproj;

import android.media.MediaPlayer;

import java.lang.ref.WeakReference;

public class runnableMusic implements Runnable
{
    private MediaPlayer mediaPlayer;
    private final WeakReference<MainActivity> activityRef;

    public runnableMusic() {
        activityRef = null;
    }

    @Override
    public void run() {
        mediaPlayer = MediaPlayer.create(activityRef.get(), R.raw.dead);
        mediaPlayer.start();
    }
}
