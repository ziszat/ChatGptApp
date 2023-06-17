package com.example.chatgptapp.appView;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.chatgptapp.MainActivity;
import com.example.chatgptapp.R;
import com.example.chatgptapp.utils.OkhttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BookNoteAppView {

    public static View Load(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View appView = inflater.inflate(R.layout.activity_app_booknote, null);

        appView.findViewById(R.id.back_button).setOnClickListener(v -> MainActivity.Instance.loadAppActivity());

        EditText title= appView.findViewById(R.id.book_title_edittext);
        EditText summary=appView.findViewById(R.id.book_summary_edittext);
        TextView res=appView.findViewById(R.id.text_bar);

        OkhttpUtil okhttpUtil = new OkhttpUtil();

        Callback gptCallBack = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                Log.i("Response Content", str);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        res.setText(OkhttpUtil.getGptAnswer(str));
                    }
                });
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
            okhttpUtil.setContentSys("Create a Reading Notes for the book title and book summary the user given, the summary may be empty");
            okhttpUtil.setContentUsr("book title: "+title.getText().toString()+"\nbook summmary: "+summary.getText().toString());
            okhttpUtil.ipConfig(requestIpCallback);

        });
        return appView;

    }


}
