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

    public static void setLanguage(String language) {
        System.out.println(Config.language);
        Config.language = language;
        System.out.println(Config.language);

//        if(language=="en"){
//
//        }
//        else{ //iw
//
//        }
    }

    public static boolean[] dailyBoardPlayed;

    public static final String SELECTED_LANGUAGE = "Selected.Language";

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

//
//    public static void setLocale(Activity activity, String languageCode) {
//        Locale locale = new Locale(languageCode);
//        Locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }
//
//    public static Context setLocale(Context context, String language) {
//        persist(context, language);
//
//        // updating the language for devices above android nougat
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            return updateResources(context, language);
//        }
//        // for devices having lower version of android os
//        return updateResourcesLegacy(context, language);
//    }
//    private static void persist(Context context, String language) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECTED_LANGUAGE, language);
//        editor.apply();
//    }
//
//    // the method is used update the language of application by creating
//    // object of inbuilt Locale class and passing language argument to it
//    @TargetApi(Build.VERSION_CODES.N)
//    private static Context updateResources(Context context, String language) {
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//
//        Configuration configuration = context.getResources().getConfiguration();
//        configuration.setLocale(locale);
//        configuration.setLayoutDirection(locale);
//
//        return context.createConfigurationContext(configuration);
//    }
//
//
//    @SuppressWarnings("deprecation")
//    private static Context updateResourcesLegacy(Context context, String language) {
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//
//        Resources resources = context.getResources();
//
//        Configuration configuration = resources.getConfiguration();
//        configuration.locale = locale;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLayoutDirection(locale);
//        }
//
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//
//        return context;
//    }


}
