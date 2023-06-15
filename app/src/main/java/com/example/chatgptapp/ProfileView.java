package com.example.chatgptapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ProfileView {

    public static View Load(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View profileView = inflater.inflate(R.layout.activity_profile, null);

        Button settingButton = profileView.findViewById(R.id.setting_btn);
        Button aboutUsButton = profileView.findViewById(R.id.about_us_btn);
        Button rateButton = profileView.findViewById(R.id.rate_btn);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Instance.loadSettingActivity();
            }
        });
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Instance.loadIntroActivity();
            }
        });
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Instance.loadHelpActivity();
            }
        });
        return profileView;
    }
}
