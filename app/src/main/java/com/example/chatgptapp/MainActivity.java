package com.example.chatgptapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    public static MainActivity Instance;

    public ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Instance =this;

        setContentView(R.layout.activity_main);


        chatView=new ChatView();

        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item selection here
                switch (item.getItemId()) {
                    case R.id.nav_chat:
                        loadChatActivity();
                        return true;
                    case R.id.nav_app:
                        loadAppActivity();
                        return true;
                    case R.id.nav_myself:
                        loadProfileActivity();
                        return true;
                }
                return false;
            }
        });

        frameLayout.addView(chatView.Load(this));
    }

    private void loadAppActivity() {

        frameLayout.removeAllViews();
        frameLayout.addView(AppView.Load(this));

    }

    public void loadChatActivity() {

        frameLayout.removeAllViews();
        frameLayout.addView(chatView.Load(this));
    }

    public void loadProfileActivity() {

        frameLayout.removeAllViews();
        frameLayout.addView(ProfileView.Load(this));
    }

    public void loadSettingActivity(){
        frameLayout.removeAllViews();
        frameLayout.addView(SettingView.Load(this));
    }

    public void loadIntroActivity(){
        frameLayout.removeAllViews();
        frameLayout.addView(IntroductionView.Load(this));
    }


    public void loadHelpActivity() {
        frameLayout.removeAllViews();
        frameLayout.addView(HelpView.Load(this));
    }


}
