package com.example.chatgptapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chatgptapp.utils.OkhttpUtil;

public class SettingView {
    private static String[] modelArray = {"gtp-3.5-turbo", "text-davinci-003"};
    private static String model = "";
    private static double temperature = 0.7;
    private static int maxToken = 1000;

    public static View Load(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View settingView = inflater.inflate(R.layout.activity_setting, null);

        // 获取 SharedPreferences 对象，参数 "mySP" 表示 SharedPreferences 文件的名称
        SharedPreferences sp = MainActivity.Instance.getSharedPreferences("openAi Setting", Context.MODE_PRIVATE);

        // 从 SharedPreferences 文件中读取数据
        String key = sp.getString("key", "");
        EditText ed_key = settingView.findViewById(R.id.ed_key);
        ed_key.setText(key);
        ed_key.setSelection(key.length());

        ImageView iv_setting_back = settingView.findViewById(R.id.iv_setting_back);
        iv_setting_back.setOnClickListener(v -> MainActivity.Instance.loadProfileActivity());

        Button confirm_button = settingView.findViewById(R.id.confirm_api);
        confirm_button.setOnClickListener(v -> {
            // 向 SharedPreferences 文件中写入数据
            SharedPreferences.Editor editor = sp.edit();
            if (!ed_key.getText().toString().equals("") && ed_key.getText().toString().substring(0, 2) == "pk-"){
                OkhttpUtil.setApiKey(ed_key.getText().toString());
                editor.putString("key", ed_key.getText().toString());
                editor.apply();
                Toast.makeText(context, "Api Key设置成功", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "不符合条件的Api Key", Toast.LENGTH_SHORT).show();
            }
        });

        //model
        Spinner model_sp = settingView.findViewById(R.id.spinner);
        model_sp.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener (){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                model = modelArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //temperature
        SeekBar temp_bar = settingView.findViewById(R.id.seekBar);
        int temp = sp.getInt("temperature", 7);
        temp_bar.setProgress(temp);
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
        SeekBar tokenBar = settingView.findViewById(R.id.maxToken);
        int token = sp.getInt("maxToken", 5);
        tokenBar.setProgress(token);
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
        Button saveSetting = settingView.findViewById(R.id.confirm_others);
        saveSetting.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sp.edit();
            OkhttpUtil.setModel(model);
            OkhttpUtil.setTemperature(temperature);
            OkhttpUtil.setMaxTokens(maxToken);
            int v1 = (int)temperature * 10;
            editor.putInt("temperature", v1);
            editor.putInt("maxToken", (int) maxToken/500);
            editor.apply();
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        });

        return settingView;

    }


}
