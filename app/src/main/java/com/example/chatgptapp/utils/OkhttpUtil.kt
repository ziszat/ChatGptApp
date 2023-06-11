package com.czl.comm.utils

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor

class OkhttpUtil {
    var firstContent = ""
    var received = ""
    var newContent = ""
    var url = "https://ai.fakeopen.com/api/conversation"

    companion object {
        var apiKey = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJjaGVuemlsb25nMjBAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWV9LCJodHRwczovL2FwaS5vcGVuYWkuY29tL2F1dGgiOnsidXNlcl9pZCI6InVzZXItd1puT05TbmZXTGdTazVzbFN1T2JZNFR2In0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJnb29nbGUtb2F1dGgyfDEwMDM1NjUzODM0MDEwNzY4Mjc0NiIsImF1ZCI6WyJodHRwczovL2FwaS5vcGVuYWkuY29tL3YxIiwiaHR0cHM6Ly9vcGVuYWkub3BlbmFpLmF1dGgwYXBwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2ODU2MjUzNTEsImV4cCI6MTY4NjgzNDk1MSwiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvcmdhbml6YXRpb24ud3JpdGUifQ.dbW1j-ijPhZS-D-d0hx93TK57TNYLP0--C_AcZ_hQ397m0dHwI7CL5x3uhC4i5RPYFz3xzGgOyi8oR_PsBazSXvvwoVo_-lYzrUeaUz2nlOjYwCQjsE_ylfvn3vJUzYW6c4Kb7PvADVYwo-fDN3h4loBhd8yn5KVquyFVDWdpTVcz2nl30ct8sbuGNd8J_cWzCinbOFB4PPTteQm6PdR_GUNhEtb2aHlA7ECxeAQJ-ge9hugq2TRVfe6xEi3H7Nrbnq9jjrWGxj9suwium51c8NS_jA-q9m937gXNrlVyVDeQdq7kd_OuxDuOX8TXZC3OUBfAEi1XxOdfVHFqRby6w"
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
            Gson().toJson(mapOf("conversations" to conversations, "accessToken" to apiKey, "apiReverseProxyUrl" to url))
                .toRequestBody("application/json".toMediaType())

        var request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(newCallback);
    }
}

