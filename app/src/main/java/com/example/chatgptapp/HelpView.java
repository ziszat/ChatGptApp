package com.example.chatgptapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class HelpView {

    public static View Load(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View helpView = inflater.inflate(R.layout.activity_help, null);

        return helpView;
    }
}
