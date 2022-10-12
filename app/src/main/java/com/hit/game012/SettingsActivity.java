package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    SwitchMaterial notifications;
    SwitchMaterial sound;
    RadioGroup language;
    RadioGroup colorTheme;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notifications = findViewById(R.id.settings_notifications_enabled);
        sound = findViewById(R.id.settings_sound_enabled);
        language = findViewById(R.id.radio_group_language);
        colorTheme = findViewById(R.id.radio_group_color_theme);
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
        editor.putInt("language", language.getCheckedRadioButtonId());
        editor.putInt("colorTheme", colorTheme.getCheckedRadioButtonId());
        editor.apply();


    }

    public void loadSettings() {
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        notifications.setChecked(sharedPref.getBoolean("notificationsEnabled", true));
        sound.setChecked(sharedPref.getBoolean("soundEnabled", true));
        language.check(sharedPref.getInt("language", R.id.settings_lang_english));
        colorTheme.check(sharedPref.getInt("colorTheme", R.id.settings_color_theme_1));
    }

    public void exitSettings(View view) {
        saveSettings();
        finish();
    }

    public void changeGridColor(View view) {
        if (colorTheme.getCheckedRadioButtonId() == R.id.settings_color_theme_1)
            Config.setGridThemeID(R.style.Theme_012_GridColorTheme1);
        else
            Config.setGridThemeID(R.style.Theme_012_GridColorTheme2);
        setTheme(Config.gridThemeID);
    }

}