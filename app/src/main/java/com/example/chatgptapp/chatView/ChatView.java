package com.example.chatgptapp.chatView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgptapp.MainActivity;
import com.example.chatgptapp.R;
import com.example.chatgptapp.SpeechToText;
import com.example.chatgptapp.model.Msg;
import com.example.chatgptapp.utils.OkhttpUtil;
import com.example.chatgptapp.utils.SqlOperate;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// TODO: chat markdown
// TODO: main page, select prompt

public class ChatView {

    private SqlOperate sqlOperate;
    private static List<Msg> chatList = new ArrayList<>();

    public static List<Msg> getChatList() {
        return chatList;
    }

    private ChatViewAdapter chatViewAdapter;
    private RecyclerView recyclerView;
    private static boolean ipConfig = false;

    public View Load(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View chatView = inflater.inflate(R.layout.activity_chat, null);

        sqlOperate = new SqlOperate();

        initChatList();

        Log.d("chatList inited", String.valueOf(chatList.get(0).getType()));

        recyclerView = chatView.findViewById(R.id.chat_recycler_View);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));

        chatViewAdapter = new ChatViewAdapter();

        recyclerView.setAdapter(chatViewAdapter);

        //initial speech to text
        SpeechToText speechToText = new SpeechToText(chatView.findViewById(R.id.search_input), chatView.findViewById(R.id.bt_voice_input), this);

        chatView.findViewById(R.id.bt_voice_input).setOnClickListener(v -> speechToText.checkPermissionAndStartSpeechRecognition(MainActivity.Instance));
        chatView.findViewById(R.id.search_input).setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    TextInputEditText textInput = chatView.findViewById(R.id.search_input);
                    String text=textInput.getText().toString();
                    insertChatData(new Msg(text, Msg.TYPE_SENT));
                    updateAndScroll();
                    sendMsg(text);
                    textInput.setText("");
                    return true; // Return true to indicate that the event is consumed
                }
                return false; // Return false if you want to propagate the event further
            }
        });
        updateAndScroll();
        return chatView;
    }

    private void updateAndScroll(){

        // Update the adapter
        chatViewAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(chatViewAdapter.getItemCount() - 1);

    }



    public void sendMsg(String text) {


        Log.d("sendMsg", text);

        if (!text.isEmpty()) {
            OkhttpUtil okhttpUtil = new OkhttpUtil();

            okhttpUtil.setContentUsr(text);
            chatList.add(new Msg("正在加载，请等待......", Msg.TYPE_RECEIVED));

            updateAndScroll();

            if (!ipConfig) {
                okhttpUtil.ipConfig(new Callback() {

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String str = response.body().string();
                        Log.i("IP Response", str);
                    }
                });
                ipConfig = true;
                return;
            }
            okhttpUtil.doPost(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    chatList.remove(chatList.size() - 1);
                    insertChatData(new Msg("AI回复失败，请重试......", Msg.TYPE_RECEIVED));
                    MainActivity.Instance.runOnUiThread(() -> {
                        updateAndScroll();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    Log.i("Response Content", str);

                    int startIndex = str.indexOf("\"content\":\"") + "\"content\":\"".length();
                    int endIndex = str.indexOf(",\"role", startIndex) - 1;

                    String content = str.substring(startIndex, endIndex);


                    chatList.remove(chatList.size() - 1);
                    insertChatData(new Msg(content, Msg.TYPE_RECEIVED));
                    MainActivity.Instance.runOnUiThread(() -> {
                        updateAndScroll();
                    });
                }
            });
        }


    }

    public void insertChatData(Msg msg) {
        chatList.add(msg);
        sqlOperate.insert(msg.getType(), msg.getContent());
    }

    public void initChatList() {
        Cursor cursor = sqlOperate.query();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex("type"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                chatList.add(new Msg(content, type));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
