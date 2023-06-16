package com.example.chatgptapp.appView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.chatgptapp.MainActivity;
import com.example.chatgptapp.R;
import com.example.chatgptapp.utils.OkhttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StoryAppView {


    private static String storyLength="";

    public static View Load(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        View appView = inflater.inflate(R.layout.activity_app_storycreate, null);

        Button briefBtn=appView.findViewById(R.id.btn_brief);
        Button middleBtn=appView.findViewById(R.id.btn_middle_length);
        Button longBtn=appView.findViewById(R.id.btn_long);
        TextView textView=appView.findViewById(R.id.story_idea);
        TextView res=appView.findViewById(R.id.text_bar);

        OkhttpUtil okhttpUtil = new OkhttpUtil();

        appView.findViewById(R.id.back_button).setOnClickListener(v-> MainActivity.Instance.loadAppActivity());

        briefBtn.setSelected(true);

        briefBtn.setOnClickListener(v -> {
            storyLength = "brief";
            briefBtn.setSelected(true);
            middleBtn.setSelected(false);
            longBtn.setSelected(false);
        });

        middleBtn.setOnClickListener(v -> {
            storyLength = "middle-length";
            briefBtn.setSelected(false);
            middleBtn.setSelected(true);
            longBtn.setSelected(false);
        });

        longBtn.setOnClickListener(v -> {
            storyLength = "long";
            briefBtn.setSelected(false);
            middleBtn.setSelected(false);
            longBtn.setSelected(true);
        });

        Callback gptCallBack = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                Log.i("Response Content", str);

                res.setText(OkhttpUtil.getGptAnswer(str));
            }
        };

        Callback requestIpCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ipRes = response.body().string();
                Log.i("IP Response", ipRes);
                okhttpUtil.doPost(gptCallBack);
            }
        };


        appView.findViewById(R.id.create_button).setOnClickListener(v->{
            res.setText("正在生成结果。。。");
            okhttpUtil.setContentSys("Finish a "+storyLength+" story with the basic idea of the given content");
            okhttpUtil.setContentUsr(textView.getText().toString());
            okhttpUtil.ipConfig(requestIpCallback);

        });

        return appView;

    }


}
