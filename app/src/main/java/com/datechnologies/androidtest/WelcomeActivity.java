package com.datechnologies.androidtest;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.datechnologies.androidtest.login.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {


    private final int SPLASH_DISPLAY_LENGTH = 1500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        getSupportActionBar().hide();


        runSplashScreen();


    }

    private void runSplashScreen() {

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {


                gotoMain();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void gotoMain() {
        Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }
}