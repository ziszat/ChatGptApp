package com.example.chatgptapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class HelpView {

    public static View Load(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View helpView = inflater.inflate(R.layout.activity_help, null);
        ImageView iv_help_back = helpView.findViewById(R.id.iv_help_back);
        iv_help_back.setOnClickListener(v -> MainActivity.Instance.loadProfileActivity());

        return helpView;
    }
}
