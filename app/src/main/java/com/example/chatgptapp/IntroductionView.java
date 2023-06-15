package com.example.chatgptapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroductionView {

    public static View Load(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View introView = inflater.inflate(R.layout.activity_introduction, null);

        TextView tv_kaiyuan = introView.findViewById(R.id.tv_kaiyuan);
        tv_kaiyuan.setOnClickListener(v -> {
            Uri webpage = Uri.parse("https://github.com/ziszat/ChatGptTApp"); // 定义需要跳转的网页地址
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage); // 创建跳转浏览器的意图
//            if (intent.resolveActivity(getPackageManager()) != null) { // 检查是否有可用的浏览器应用
//                startActivity(intent); // 执行跳转
//            }
        });

        ImageView iv_introduction_back = introView.findViewById(R.id.iv_introduction_back);
        iv_introduction_back.setOnClickListener(v -> MainActivity.Instance.loadProfileActivity());
        return introView;
    }
}
