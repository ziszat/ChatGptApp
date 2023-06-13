package com.example.chatgptapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatgptapp.utils.OkhttpUtil;

public class SettingActivity extends AppCompatActivity {
    private String[] starArray = {"gtp-3.5-turbo", "text-davinci-003"};
    private String model = "";
    private double temperature = 0.7;
    private int maxToken = 1000;
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

        Button confirm_button = findViewById(R.id.confirm_api);
        confirm_button.setOnClickListener(v -> {
            // 向 SharedPreferences 文件中写入数据
            SharedPreferences.Editor editor = sp.edit();
            if (ed_key.getText().toString().substring(0, 2) == "pk-"){
                OkhttpUtil.setApiKey(ed_key.getText().toString());
                editor.putString("key", ed_key.getText().toString());
                editor.apply();
                Toast.makeText(getApplicationContext(), "Api Key设置成功", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "不符合条件的Api Key", Toast.LENGTH_SHORT).show();
            }
        });

        //model
        Spinner model_sp = findViewById(R.id.spinner);
        model_sp.setOnItemSelectedListener(new MySelectedListener());

        //temperature
        SeekBar temp_bar = findViewById(R.id.seekBar);
        temp_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                temperature = i/10;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //max token
        SeekBar tokenBar = findViewById(R.id.maxToken);
        tokenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                maxToken = i*500;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //confirm
        Button saveSetting = findViewById(R.id.confirm_others);
        saveSetting.setOnClickListener(v -> {
            OkhttpUtil.setModel(model);
            OkhttpUtil.setTemperature(temperature);
            OkhttpUtil.setMaxTokens(maxToken);
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
        });

    }

    class MySelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            model = starArray[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
