package com.example.chatgptapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_introduction.*

/**
 * @author :lwh
 * @date : 2023/3/17
 */
class IntroductionActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        tv_kaiyuan.setOnClickListener {
            val webpage: Uri = Uri.parse("https://github.com/CatOfSilence/ChatGPTAPP") // 定义需要跳转的网页地址
            val intent = Intent(Intent.ACTION_VIEW, webpage) // 创建跳转浏览器的意图
            if (intent.resolveActivity(packageManager) != null) { // 检查是否有可用的浏览器应用
                startActivity(intent) // 执行跳转
            }
        }
        iv_introduction_back.setOnClickListener { finish() }
    }
}