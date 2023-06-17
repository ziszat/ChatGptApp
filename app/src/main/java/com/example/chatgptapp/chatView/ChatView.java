package com.example.chatgptapp.chatView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    public View Load(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View chatView = inflater.inflate(R.layout.activity_chat, null);

        sqlOperate = new SqlOperate();

        initChatList();

        recyclerView = chatView.findViewById(R.id.chat_recycler_View);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        chatViewAdapter = new ChatViewAdapter();

        recyclerView.setAdapter(chatViewAdapter);

        //initial speech to text
        SpeechToText speechToText = new SpeechToText(chatView.findViewById(R.id.search_input), chatView.findViewById(R.id.bt_voice_input), this);

        chatView.findViewById(R.id.bt_voice_input).setOnClickListener(v -> speechToText.checkPermissionAndStartSpeechRecognition(MainActivity.Instance));

        chatView.findViewById(R.id.bt_send).setOnClickListener(v -> {
            TextInputEditText textInput = chatView.findViewById(R.id.search_input);
            String text = textInput.getText().toString();
            chatList.add(new Msg(text, Msg.TYPE_SENT));
            updateAndScroll();
            sendMsg(text);
            textInput.setText("");
        });
        chatView.findViewById(R.id.search_input).setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    TextInputEditText textInput = chatView.findViewById(R.id.search_input);
                    String text = textInput.getText().toString();
                    chatList.add(new Msg(text, Msg.TYPE_SENT));
                    updateAndScroll();
                    sendMsg(text);
                    textInput.setText("");
                    return true; // Return true to indicate that the event is consumed
                }
                return false; // Return false if you want to propagate the event further
            }
        });
        chatView.findViewById(R.id.btn_delete_chat).setOnClickListener(v->showDeleteConfirmationDialog());
        updateAndScroll();
        return chatView;
    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.Instance);
        builder.setTitle("确认删除");
        builder.setMessage("你确定要删除聊天记录吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform delete operation
                sqlOperate.deleteAll();
                chatList.clear();
                updateAndScroll();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateAndScroll() {

        // Update the adapter
        chatViewAdapter.notifyDataSetChanged();
        if(chatViewAdapter.getItemCount()>0)
            recyclerView.smoothScrollToPosition(chatViewAdapter.getItemCount() - 1);
        else
            recyclerView.smoothScrollToPosition(0);
    }


    public void sendMsg(String text) {


        Log.d("sendMsg", text);

        if (!text.isEmpty()) {
            OkhttpUtil okhttpUtil = new OkhttpUtil();

            okhttpUtil.setContentUsr(text);

            chatList.add(new Msg("正在加载，请等待......", Msg.TYPE_RECEIVED));

            updateAndScroll();

            Callback gptCallBack = new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    chatList.remove(chatList.size() - 1);

                    chatList.add(new Msg("AI回复失败，请重试......", Msg.TYPE_RECEIVED));

                    MainActivity.Instance.runOnUiThread(() -> {
                        updateAndScroll();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String str = response.body().string();
                    Log.i("Response Content", str);

                    String content = OkhttpUtil.getGptAnswer(str);


                    chatList.remove(chatList.size() - 1);
                    storeChatData(chatList.get(chatList.size() - 1));
                    storeChatData(new Msg(content, Msg.TYPE_RECEIVED));
                    chatList.add(new Msg(content, Msg.TYPE_RECEIVED));
                    MainActivity.Instance.runOnUiThread(() -> {
                        updateAndScroll();
                    });
                }
            };

            Callback requestIPCallback = new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String ipRes = response.body().string();
                    Log.i("IP Response", ipRes);
                    okhttpUtil.doPost(gptCallBack);
                }
            };

            okhttpUtil.ipConfig(requestIPCallback);
        }


    }

    public void storeChatData(Msg msg) {
        sqlOperate.insert(msg.getType(), msg.getContent());
    }

    public void initChatList() {
        chatList.clear();
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
