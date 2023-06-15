package com.example.chatgptapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class AppView {

    public static View Load(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        View appView = inflater.inflate(R.layout.activity_app, null);

        return appView;

    }
}
