package com.example.chatgptapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatgptapp.utils.OkhttpUtil;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 获取 SharedPreferences 对象，参数 "mySP" 表示 SharedPreferences 文件的名称
        SharedPreferences sp = getSharedPreferences("openAiKey", Context.MODE_PRIVATE);

        // 从 SharedPreferences 文件中读取数据
        String key = sp.getString("key", "");
        EditText ed_key = findViewById(R.id.ed_key);
        ed_key.setText(key);
        ed_key.setSelection(key.length());

        ImageView iv_setting_back = findViewById(R.id.iv_setting_back);
        iv_setting_back.setOnClickListener(v -> finish());

        ImageView iv_confirm = findViewById(R.id.iv_confirm);
        iv_confirm.setOnClickListener(v -> {
            // 向 SharedPreferences 文件中写入数据
            SharedPreferences.Editor editor = sp.edit();
//            OkhttpUtil.setApiKey(ed_key.getText().toString());
            editor.putString("key", ed_key.getText().toString());
            editor.apply();
            Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
        });
    }
}
