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

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void saveSettings() {
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("notificationsEnabled", notifications.isChecked());
        editor.putBoolean("soundEnabled", sound.isChecked());
        editor.putInt("language", languageRadioGroup.getCheckedRadioButtonId());
        editor.putInt("colorTheme", colorThemeRadioGroup.getCheckedRadioButtonId());
        editor.apply();
        changeGridColor();
        changeLanguage();


    }

    public void loadSettings() {
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        notifications.setChecked(sharedPref.getBoolean("notificationsEnabled", true));
        sound.setChecked(sharedPref.getBoolean("soundEnabled", true));
        languageRadioGroup.check(sharedPref.getInt("language", R.id.settings_lang_english));
        colorThemeRadioGroup.check(sharedPref.getInt("colorTheme", R.id.settings_color_theme_1));
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

    public void changeLanguage(){
        if (languageRadioGroup.getCheckedRadioButtonId() == R.id.settings_lang_english)
            Config.setLanguage("en");
        else
            Config.setLanguage("iw");
//        Context context = Config.setLocale(getApplicationContext(), Config.language);
//        Resources resources = context.getResources();

        String languageToLoad  =Config.language; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

}



