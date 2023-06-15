package com.example.chatgptapp.utils;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkhttpUtil {
    private String firstContent = "";
    private String received = "";
    private String newContent = "";
    //    private String url = "https://ai.fakeopen.com/api/conversation";
//
//    public static String apiKey = "1";
    private static String apiKey = "pk-fCTNqrQIWZTAwypRpqfBCBajxDJPRPMZZLkraUxkxzHcmSJY";
    private String url = "https://api.pawan.krd/v1/chat/completions";
    private static String model = "gpt-3.5-turbo";
    //    public String prompt = "Human: Hello\\nAI:";
    private static double temperature = 0.7;
    private static int maxTokens = 1000;
    private String contentSys = "Good Assistant";
    private String contentUsr = "Who are you?";

    public void ipConfig(Callback newCallback) {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor);
        OkHttpClient client = builder.build();

        MediaType ip_mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", ip_mediaType);
        Request ipRequest = new Request.Builder()
                .url("https://api.pawan.krd/resetip")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        client.newCall(ipRequest).enqueue(newCallback);
    }

    public void doPost(Callback newCallback) {


        contentUsr = newContent;

        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor);
        OkHttpClient client = builder.build();

        MediaType mediaType = MediaType.parse("application/json");

        String requestBodyJSON = "{ \n  \"model\": \"" + model
                + "\",\n \"max_tokens\": " + maxTokens
                + ",\n    \"messages\":\n [\n     {\n         \"role\": \"system\",\n         \"content\": \"" + contentSys + "\"\n       },\n        {\n         \"role\": \"user\",\n           \"content\": \"" + contentUsr + "\"\n       }\n ], \"temperature\": " + temperature + " \n}";

        Log.i("Request body json", requestBodyJSON);
        RequestBody requestBody = RequestBody.create(requestBodyJSON, mediaType);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

//        Log.d("Post a msg ",requestBody.toString());
        client.newCall(request).enqueue(newCallback);
    }

    public static void setApiKey(String apiKey) {
        apiKey = apiKey;
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

    public void setContentUsr(String contentUsr) {
        this.contentUsr = contentUsr;
    }

    public static void setMaxTokens(int maxTokens) {
        maxTokens = maxTokens;
    }

    public static void setModel(String model) {
        model = model;
    }

    public static void setTemperature(double temperature) {
        temperature = temperature;
    }

    //    public String getFirstContent() {
//        return firstContent;
//    }
//
//    public String getNewContent() {
//        return newContent;
//    }

//    public String getReceived() {
//        return received;
//    }

    public String getUrl() {
        return url;
    }
}
