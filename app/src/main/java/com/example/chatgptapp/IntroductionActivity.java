package com.example.chatgptapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroductionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        TextView tv_kaiyuan = findViewById(R.id.tv_kaiyuan);
        tv_kaiyuan.setOnClickListener(v -> {
            Uri webpage = Uri.parse("https://github.com/CatOfSilence/ChatGPTAPP"); // 定义需要跳转的网页地址
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage); // 创建跳转浏览器的意图
            if (intent.resolveActivity(getPackageManager()) != null) { // 检查是否有可用的浏览器应用
                startActivity(intent); // 执行跳转
            }
        });

        ImageView iv_introduction_back = findViewById(R.id.iv_introduction_back);
        iv_introduction_back.setOnClickListener(v -> finish());
    }
}
