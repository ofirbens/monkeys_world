package com.example.my_application;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.security.Provider;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener
{
    MediaPlayer mediaPlayer;

    @Override
    public void onCompletion(MediaPlayer mediaPlayer)
    {
        mediaPlayer = MediaPlayer.create(this, R.raw.music1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }


    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate()
    {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.music1);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

}
