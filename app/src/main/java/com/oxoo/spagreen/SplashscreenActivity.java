package com.oxoo.spagreen;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;


public class SplashscreenActivity extends AppCompatActivity {

    private int SPLASH_TIME = 2000;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();


        //----dark mode----------
        preferences=getSharedPreferences("push",MODE_PRIVATE);
        if (preferences.getBoolean("dark",false)){
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }






        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
                    finish();

                }
            }
        };
        timer.start();

    }


}
