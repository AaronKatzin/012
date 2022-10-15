package com.hit.game012;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class Config {

    public static int gridThemeID;
    public static final String GOOGLE_SERVER_CLIENT_ID = "778865611995-44u44113jejfrehf0im3o7at69c8k6l0.apps.googleusercontent.com";
    public static int COLOR_TILE_ONE;
    public static int COLOR_TILE_ZERO;
    public static String language;
    public static final String SELECTED_LANGUAGE = "Selected.Language";


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


}
