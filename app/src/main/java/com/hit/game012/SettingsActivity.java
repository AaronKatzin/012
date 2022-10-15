package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    SwitchMaterial notifications;
    SwitchMaterial sound;
    RadioGroup languageRadioGroup;
    RadioGroup colorThemeRadioGroup;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notifications = findViewById(R.id.settings_notifications_enabled);
        sound = findViewById(R.id.settings_sound_enabled);
        languageRadioGroup = findViewById(R.id.radio_group_language);
        colorThemeRadioGroup = findViewById(R.id.radio_group_color_theme);
        logout = findViewById(R.id.logout);
        loadSettings();

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        int[] langRadioButtons = {R.id.settings_lang_english, R.id.settings_lang_hebrew};
        for (int id : langRadioButtons)
            findViewById(id).setOnClickListener(v -> {
                changeLanguage();
                refreshActivity();
            });
    }

    public void saveSettings() {
        changeGridColor();
        changeLanguage();

        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("notificationsEnabled", notifications.isChecked());
        editor.putBoolean("soundEnabled", sound.isChecked());
        editor.putInt("colorTheme", colorThemeRadioGroup.getCheckedRadioButtonId());
        editor.putString(Config.SELECTED_LANGUAGE, Config.language);
        editor.apply();
    }

    public void loadSettings() {
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        notifications.setChecked(sharedPref.getBoolean("notificationsEnabled", true));
        sound.setChecked(sharedPref.getBoolean("soundEnabled", true));

        String lang = sharedPref.getString(Config.SELECTED_LANGUAGE, "en");
        if (Objects.equals(lang, "en"))
            languageRadioGroup.check(R.id.settings_lang_english);
        else
            languageRadioGroup.check(R.id.settings_lang_hebrew);

        int colorTheme = sharedPref.getInt("colorTheme", 1);
        if (colorTheme == 1)
            colorThemeRadioGroup.check(R.id.settings_color_theme_1);
        else
            colorThemeRadioGroup.check(R.id.settings_color_theme_2);

    }

    private void refreshActivity() {
        recreate();
    }

    public void exitSettings(View view) {
        saveSettings();
        finish();
    }

    public void changeGridColor() {
        if (colorThemeRadioGroup.getCheckedRadioButtonId() == R.id.settings_color_theme_1)
            Config.setGridThemeID(1);
        else
            Config.setGridThemeID(2);
    }

    public void changeLanguage() {
        String lang;
        lang = (languageRadioGroup.getCheckedRadioButtonId() == R.id.settings_lang_english) ? "en" : "iw";

        if (Objects.equals(lang, Config.language))
            return;

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Config.setLanguage(lang);

    }

}



