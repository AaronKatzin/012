package com.hit.game012;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BackGroundMusicService extends Service {
    private MediaPlayer backGroundMusic;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    public void onCreate(){
        backGroundMusic = MediaPlayer.create(getApplicationContext(),R.raw.backgroundsound);
        backGroundMusic.setLooping(true);
    }
    public void onStart(Intent intent, int startid){
        backGroundMusic.start();
    }
    public void onDestroy(){
        backGroundMusic.stop();
    }
}
