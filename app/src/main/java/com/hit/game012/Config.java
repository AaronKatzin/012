package com.hit.game012;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;

import java.util.Locale;

public class Config {

    public static int gridThemeID;
    public static final String GOOGLE_SERVER_CLIENT_ID = "778865611995-44u44113jejfrehf0im3o7at69c8k6l0.apps.googleusercontent.com";
    public static int COLOR_TILE_ONE;
    public static int COLOR_TILE_ZERO;
    public static boolean[] dailyBoardPlayed;

    public static void setGridThemeID(int gridThemeID) {
        System.out.println(Config.gridThemeID);
        Config.gridThemeID = gridThemeID;
        System.out.println(Config.gridThemeID);
        if(gridThemeID==1){
            COLOR_TILE_ONE=R.drawable.tile_one_theme_1;
            COLOR_TILE_ZERO=R.drawable.tile_zero_theme_1;
        }
        else{
            COLOR_TILE_ONE=R.drawable.tile_one_theme_2;
            COLOR_TILE_ZERO=R.drawable.tile_zero_theme_2;
        }
    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }



}
