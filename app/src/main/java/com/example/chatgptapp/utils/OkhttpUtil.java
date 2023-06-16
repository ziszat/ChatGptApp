package com.example.chatgptapp.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkhttpUtil {
    private static String apiKey = "pk-fCTNqrQIWZTAwypRpqfBCBajxDJPRPMZZLkraUxkxzHcmSJY";
    private String url = "https://api.pawan.krd/v1/chat/completions";
    private static String model = "gpt-3.5-turbo";

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
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        client.newCall(ipRequest).enqueue(newCallback);
    }

    public void doPost(Callback newCallback) {

        try {
            HttpLoggingInterceptor httpLoggingInterceptor =
                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor);
            OkHttpClient client = builder.build();

            MediaType mediaType = MediaType.parse("application/json");

            JSONObject json = new JSONObject();
            json.put("model", model);
            json.put("max_tokens", maxTokens);

            JSONArray messages = new JSONArray();

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", contentSys);
            messages.put(systemMessage);

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", contentUsr);
            messages.put(userMessage);

            json.put("messages", messages);
            String jsonString = json.toString();

            RequestBody requestBody = RequestBody.create(jsonString, mediaType);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(newCallback);

        } catch (Exception e) {
            // Handle exception here
        }
    }

    
    public static String getGptAnswer(String response){
        try {
            JSONObject json = new JSONObject(response);

            JSONArray choices = json.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject choice = choices.getJSONObject(0);
                JSONObject message = choice.getJSONObject("message");
                String content = message.getString("content");
                return content;
            }
        }
        catch (Exception e){
            return "暂时没有结果";
        }
        return "";
    } 

    public static void setApiKey(String apiKey) {
        apiKey = apiKey;
    }

    public void setContentUsr(String contentUsr) {
        this.contentUsr = contentUsr;
    }

    public void setContentSys(String contentSys) {
        this.contentSys = contentSys;
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


}
