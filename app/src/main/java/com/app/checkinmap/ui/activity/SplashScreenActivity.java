package com.app.checkinmap.ui.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.checkinmap.R;

public class SplashScreenActivity extends AppCompatActivity {
    public static final int SPLASH_SCREEN_TIME= 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(DashBoardActivity.getIntent(getApplication()));
                finish();
            }
        },SPLASH_SCREEN_TIME);
    }
}
