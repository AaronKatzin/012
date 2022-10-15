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
        Toast.makeText(this,"Service created",Toast.LENGTH_SHORT).show();
        backGroundMusic = MediaPlayer.create(getApplicationContext(),R.raw.backgroundsound);
        backGroundMusic.setLooping(true);
    }
    public void onStart(Intent intent, int startid){
        Toast.makeText(this,"Service started",Toast.LENGTH_SHORT).show();
        backGroundMusic.start();
    }
    public void onDestroy(){
        Toast.makeText(this,"Service  stopped",Toast.LENGTH_SHORT).show();
        backGroundMusic.stop();
    }
}
