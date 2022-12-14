package com.hit.game012;

import static com.hit.game012.Config.GOOGLE_SERVER_CLIENT_ID;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hit.game012.startupsequence.AnimatedImageView;
import com.hit.game012.startupsequence.AnimatedTextView;

import java.util.Locale;

/**
 * Entry point to the app.
 * Loads all user's settings from SharedPreferences and updates the Config class.
 * Loads start animation.
 * If soundEnabled starts background music.
 * Takes care of Firebase login.
 */
public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSigninClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMemSettings();
        if (Config.soundEnabled)
		    startService(new Intent(getApplicationContext(),BackGroundMusicService.class)); /*defualt activate sound*/

        AnimatedImageView left = findViewById(R.id.left_tile);
        AnimatedImageView right = findViewById(R.id.right_tile);
        left.setBackground(getResources().getDrawable(Config.COLOR_TILE_ONE, this.getTheme()));
        right.setBackground(getResources().getDrawable(Config.COLOR_TILE_ZERO, this.getTheme()));
        AnimatedTextView gameName = findViewById(R.id.app_name_startup);
        left.initFadeInAnimation(1500, 1000);
        right.initFadeInAnimation(1500, 2000);
        gameName.initFadeInAnimation(2000, 3000);
        authOnCreate();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            LinearLayout enterLayout = findViewById(R.id.enterLayout);
            Button enterButton = findViewById(R.id.enterButton);

            enterButton.setOnClickListener(view -> continueToMenu(view));

            enterButton.setText(String.format(getResources().getString(R.string.welcome_button),  user.getDisplayName()));
            enterLayout.setVisibility(View.VISIBLE);

        } else {
            LinearLayout signinButtons = findViewById(R.id.SigninButtonLayout);
            signinButtons.setVisibility(View.VISIBLE);
            animateSigninButtons();

        }

    }

    private void animateSigninButtons() {
        Animation fadein = new AlphaAnimation(0, 1);
        fadein.setInterpolator(new DecelerateInterpolator());
        fadein.setDuration(2000);
        fadein.setStartOffset(4000);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadein);
        findViewById(R.id.SigninButtonLayout).setAnimation(animation);
    }

    private void authOnCreate() {
        mAuth = FirebaseAuth.getInstance();
        createLoginRequest();
        findViewById(R.id.sign_in).setOnClickListener(view -> signIn());
    }


    private void createLoginRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_SERVER_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSigninClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSigninClient.getSignInIntent();
        getResult.launch(signInIntent);
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    final ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d("MainACtivity", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    //Log.d("Main", "signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login Succeeded!", Toast.LENGTH_SHORT).show();
                        continueToMenu(getCurrentFocus());

                    } else {
                        Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public void continueToMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
//        intent.putExtra("userID", mAuth.getCurrentUser().getUid());
        startActivity(intent);
        finish();
    }

    public void loadMemSettings() {
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        String lang = sharedPref.getString(Config.SELECTED_LANGUAGE, "en");
        Config.setLanguage(lang);


        int theme = sharedPref.getInt("colorTheme", 1);
        Config.setGridThemeID(theme);

        String languageToLoad = Config.language;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        boolean inGameTimerEnabled = sharedPref.getBoolean("enableInGameTimer", true);
        Config.setInGameTimerEnabled(inGameTimerEnabled);

        boolean soundEnabled = sharedPref.getBoolean("soundEnabled", true);
        Config.setSoundEnabled(soundEnabled);

    }

//    public void saveMem() {
//        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(Config.SELECTED_LANGUAGE, Config.language);
//        editor.putInt("colorTheme", Config.gridThemeID);
//        editor.apply();
//    }
}