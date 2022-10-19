package com.hit.game012;

/**
 * Config class is a static helper class to save the custom settings for the user.
 */
public class Config {

    public static final String GOOGLE_SERVER_CLIENT_ID = "778865611995-44u44113jejfrehf0im3o7at69c8k6l0.apps.googleusercontent.com";
    public static final String SELECTED_LANGUAGE = "Selected.Language";

    public static String language;
    public static boolean soundEnabled;
    public static boolean inGameTimerEnabled;

    public static int gridThemeID;
    public static int COLOR_TILE_ONE;
    public static int COLOR_TILE_ZERO;


    public static void setInGameTimerEnabled(boolean inGameTimerEnabled) {
        Config.inGameTimerEnabled = inGameTimerEnabled;
    }

    public static void setSoundEnabled(boolean soundEnabled) {
        Config.soundEnabled = soundEnabled;
    }

    public static void setLanguage(String language) {
        Config.language = language;

    }

    /**
     * Change the tile colors according to parameter
     *
     * @param gridThemeID id of tile color theme
     */
    public static void setGridThemeID(int gridThemeID) {
        System.out.println(Config.gridThemeID);
        Config.gridThemeID = gridThemeID;
        System.out.println(Config.gridThemeID);

        if (gridThemeID == 1) {
            COLOR_TILE_ONE = R.drawable.tile_zero_theme_1;
            COLOR_TILE_ZERO = R.drawable.tile_one_theme_1;
        } else {
            COLOR_TILE_ONE = R.drawable.tile_one_theme_2;
            COLOR_TILE_ZERO = R.drawable.tile_zero_theme_2;
        }
    }


}
