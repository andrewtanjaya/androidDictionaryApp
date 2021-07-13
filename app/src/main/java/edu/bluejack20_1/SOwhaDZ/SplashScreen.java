package edu.bluejack20_1.SOwhaDZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 2000;

    public static SharedRef sharedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedRef  = new SharedRef(this);

        if (sharedRef.loadNighModeState()) {
            setTheme(R.style.darkTheme);
        } else setTheme(R.style.lightTheme);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent introIntent = new Intent(SplashScreen.this, IntroActivity.class);
                startActivity(introIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}