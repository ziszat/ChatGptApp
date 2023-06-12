package com.example.chatgptapp.utils;

import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OkhttpUtil {
    private String firstContent = "";
    private String received = "";
    private String newContent = "";
    private String url = "https://ai.fakeopen.com/api/conversation";

    public static String apiKey = "1";

    public void doPost(Callback newCallback) {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor);
        OkHttpClient client = builder.build();

        List<Map<String, String>> conversations = new ArrayList<>();

        if (!firstContent.isEmpty()) {
            conversations.add(Map.of("role", "user", "content", firstContent));
        }
        if (!received.isEmpty()) {
            conversations.add(Map.of("role", "assistant", "content", received));
        }
        if (!newContent.isEmpty()) {
            conversations.add(Map.of("role", "user", "content", newContent));
        }
        apiKey = apiKey.replace(":\"", "");
        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json"),
                new Gson().toJson(Map.of("conversations", conversations, "accessToken", apiKey, "apiReverseProxyUrl", url))
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(newCallback);
    }

    public static void setApiKey(String apiKey) {
        OkhttpUtil.apiKey = apiKey;
    }

    public void setFirstContent(String firstContent) {
        this.firstContent = firstContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFirstContent() {
        return firstContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public String getReceived() {
        return received;
    }

    public String getUrl() {
        return url;
    }
}
