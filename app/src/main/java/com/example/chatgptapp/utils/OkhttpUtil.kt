package com.lwh.comm.utils

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor

class OkhttpUtil {
    var firstContent = ""
    var received = ""
    var newContent = ""
    var url = "http://chatapi.qload.cn/api/v2/answer"

    companion object {
        var apiKey = ""
    }

    fun doPost(newCallback: Callback) {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        val builder = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
        val client = builder.build()

        val conversations = mutableListOf<Map<String, String>>()

        if (firstContent.isNotEmpty()) {
            conversations.add(mapOf("role" to "user", "content" to firstContent))
        }
        if (received.isNotEmpty()) {
            conversations.add(mapOf("role" to "assistant", "content" to received))
        }
        if (newContent.isNotEmpty()) {
            conversations.add(mapOf("role" to "user", "content" to newContent))
        }
        apiKey = apiKey?.replace(":\"","")
        val requestBody =
            Gson().toJson(mapOf("conversations" to conversations, "apikey" to apiKey))
                .toRequestBody("application/json".toMediaType())

        var request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(newCallback);
    }
}

