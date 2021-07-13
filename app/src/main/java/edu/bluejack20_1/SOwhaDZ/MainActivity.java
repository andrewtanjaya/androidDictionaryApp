package edu.bluejack20_1.SOwhaDZ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import Model.User;

public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


    String state = "home";

    protected void onCreate(Bundle savedInstanceState){



        if (SplashScreen.sharedRef.loadNighModeState()) {
            setTheme(R.style.darkTheme);
        } else setTheme(R.style.lightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);



        contextOfApplication = getApplicationContext();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (getIntent().getStringExtra("state") == null){
            state = "home";
        }else{
            state = getIntent().getStringExtra("state");
        }

        Fragment selectedFragment = new FragmentHome();
        switch (state){
            case "home":
                selectedFragment = new FragmentHome();
                break;
            case "translate":
                selectedFragment = new FragmentTranslate();
                break;
            case "user":
                selectedFragment = new FragmentUser();
                break;
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new FragmentHome();
                            break;
                        case R.id.nav_translation:
                            selectedFragment = new FragmentTranslate();
                            break;
                        case R.id.nav_user:
                            selectedFragment = new FragmentUser();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}