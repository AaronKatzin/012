package com.hit.game012;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.hit.game012.startupsequence.MyNotificationPublisher;

import java.util.Locale;

public class Config {

    public static int gridThemeID;
    public static final String GOOGLE_SERVER_CLIENT_ID = "778865611995-44u44113jejfrehf0im3o7at69c8k6l0.apps.googleusercontent.com";
    public static int COLOR_TILE_ONE;
    public static int COLOR_TILE_ZERO;
    public static String language;
    public static boolean inGameTimerEnabled;
    public static final String SELECTED_LANGUAGE = "Selected.Language";
    public static final String NOTIFICATION_CHANNEL_ID = "0" ;
    private final static String default_notification_channel_id = "default" ;

    public static void setInGameTimerEnabled(boolean inGameTimerEnabled) {
        Config.inGameTimerEnabled = inGameTimerEnabled;
    }


    public static void setLanguage(String language) {
        Config.language = language;

    }

    public static void setGridThemeID(int gridThemeID) {
        System.out.println(Config.gridThemeID);
        Config.gridThemeID = gridThemeID;
        System.out.println(Config.gridThemeID);

        if (gridThemeID == 1) {
            COLOR_TILE_ONE = R.drawable.tile_one_theme_1;
            COLOR_TILE_ZERO = R.drawable.tile_zero_theme_1;
        } else {
            COLOR_TILE_ONE = R.drawable.tile_one_theme_2;
            COLOR_TILE_ZERO = R.drawable.tile_zero_theme_2;
        }
    }

//    private void scheduleNotification (Notification notification , int delay) {
//        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
//        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
//        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
//        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
//        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
//        AlarmManager alarmManager = (AlarmManager)getSystemService(Context. ALARM_SERVICE ) ;
//        assert alarmManager != null;
//        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
//    }
//    private Notification getNotification (String content) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
//        builder.setContentTitle( "Scheduled Notification" ) ;
//        builder.setContentText(content) ;
//        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
//        builder.setAutoCancel( true ) ;
//        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
//        return builder.build() ;
//    }

}
