package com.example.chatgptapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.lwh.comm.utils.OkhttpUtil
import kotlinx.android.synthetic.main.activity_introduction.*
import kotlinx.android.synthetic.main.activity_introduction.iv_introduction_back
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        // 获取 SharedPreferences 对象，参数 "mySP" 表示 SharedPreferences 文件的名称
        val sp = getSharedPreferences("openAiKey", Context.MODE_PRIVATE)

        // 从 SharedPreferences 文件中读取数据
        val key = sp.getString("key", "")
        ed_key.setText(key)
        ed_key.setSelection(key.toString().length)

        iv_setting_back.setOnClickListener { finish() }

        iv_confirm.setOnClickListener {
            // 向 SharedPreferences 文件中写入数据
            val editor = sp.edit()
            OkhttpUtil.apiKey = ed_key.text.toString()
            editor.putString("key", ed_key.text.toString())
            editor.apply()
            Toast.makeText(applicationContext,"设置成功",Toast.LENGTH_SHORT).show()
        }
    }
}