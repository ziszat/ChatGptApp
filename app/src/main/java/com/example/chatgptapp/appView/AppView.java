package com.example.chatgptapp.appView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgptapp.R;

public class AppView {

    public static View Load(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        View appView = inflater.inflate(R.layout.activity_app, null);

        RecyclerView recyclerView = appView.findViewById(R.id.app_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        // Create an adapter for the RecyclerView
        AppGridAdapter adapter = new AppGridAdapter();
        recyclerView.setAdapter(adapter);

        return appView;

    }


}
