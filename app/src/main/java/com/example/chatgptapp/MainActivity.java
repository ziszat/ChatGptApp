package com.example.chatgptapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgptapp.adapter.ChatIndexAdapter;
import com.example.chatgptapp.ui.dialog.SendDialog;
import com.example.chatgptapp.utils.DensityUtil;
import com.example.chatgptapp.utils.OkhttpUtil;
import com.example.chatgptapp.utils.SqlOperate;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO: chat markdown
// TODO: main page, select prompt
public class MainActivity extends AppCompatActivity {


    private SqlOperate sqlOperate;
    private List<Msg> list = new ArrayList<>();
    private Handler handler;
    private ChatIndexAdapter chatIndexAdapter;
    private Dialog bottomDialog;
    private EditText ed_reply;

    private RecyclerView recyclerView;

    private boolean ipConfig = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the required permissions are granted
        AppPermission.requestPermissions(this);

        init();

        SpeechToText speechToText=new SpeechToText(findViewById(R.id.search_input),findViewById(R.id.bt_voice_input),this);


        findViewById(R.id.search_input).setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Handle the Enter key press event here
                    // Your code logic goes here
                    Context context=getApplicationContext();
                    View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_item_reply, null);

                    bottomDialog = new SendDialog(context, R.style.BottomDialog, ed_reply);
                    bottomDialog.setContentView(contentView);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
                    params.width = context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(context, 16f);
                    params.bottomMargin = DensityUtil.dp2px(context, 8f);
                    contentView.setLayoutParams(params);

                    TextInputEditText textbar=findViewById(R.id.search_input);

                    sendMsg(String.valueOf(textbar.getText()));
                    textbar.setText("");
                    return true; // Return true to indicate that the event is consumed
                }
                return false; // Return false if you want to propagate the event further
            }
        });

        findViewById(R.id.bt_send).setOnClickListener(v -> show());
        findViewById(R.id.ll_input).setOnClickListener(v -> show());
        findViewById(R.id.iv_photo).setOnClickListener(v -> startActivity(new Intent(this, IntroductionActivity.class)));
        findViewById(R.id.iv_set).setOnClickListener(v -> startActivity(new Intent(this, SettingActivity.class)));
        findViewById(R.id.bt_voice_input).setOnClickListener( v -> speechToText.checkPermissionAndStartSpeechRecognition(this));

    }



    private void init() {
        recyclerView=findViewById(R.id.recyclerView);

        sqlOperate = new SqlOperate(this);
        dataQuery();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatIndexAdapter = new ChatIndexAdapter(list);
        recyclerView.setAdapter(chatIndexAdapter);
        if (chatIndexAdapter.getItemCount() > 0)
            recyclerView.smoothScrollToPosition(chatIndexAdapter.getItemCount() - 1);

        handler = new Handler(msg -> {
            chatIndexAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chatIndexAdapter.getItemCount() - 1);
            return false;
        });
    }


    private void show() {

        Context context=getApplicationContext();
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_item_reply, null);
        ed_reply = contentView.findViewById(R.id.ed_reply);
        Button bt_reply = contentView.findViewById(R.id.bt_reply);
        bottomDialog = new SendDialog(context, R.style.BottomDialog, ed_reply);
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(context, 16f);
        params.bottomMargin = DensityUtil.dp2px(context, 8f);
        contentView.setLayoutParams(params);

        bt_reply.setOnClickListener(v -> sendMsg(ed_reply.getText().toString()));

        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }


    public void sendMsg(String text){


        Log.d("sendMsg",text);

        if (!text.isEmpty()) {
            OkhttpUtil okhttpUtil = new OkhttpUtil();
            dataInsert(new Msg(text, Msg.TYPE_SENT));

            String reply = "";
            for (Msg msg : list) {
                if (msg.getType() == Msg.TYPE_RECEIVED) {
                    reply = msg.getContent();
                    break;
                }
            }

            if (list.size() >= 3) {
                okhttpUtil.setFirstContent(list.get(list.size() - 3).getContent());
                okhttpUtil.setNewContent(text);
            } else {
                okhttpUtil.setFirstContent(text);
                okhttpUtil.setNewContent("");
            }
            okhttpUtil.setReceived(reply);

            list.add(new Msg("正在加载，请等待......", Msg.TYPE_RECEIVED));
            chatIndexAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chatIndexAdapter.getItemCount() - 1);

            if (!ipConfig) {
                okhttpUtil.ipConfig(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String str = response.body().string();
                        Log.i("IP Response", str);
                        handler.sendEmptyMessage(0);
                    }
                });
                ipConfig = true;
            }
            okhttpUtil.doPost(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    list.remove(list.size() - 1);
                    dataInsert(new Msg("AI回复失败，请重试......", Msg.TYPE_RECEIVED));
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    Log.i("Response Content", str);

                    int startIndex = str.indexOf("\"content\":\"") + "\"content\":\"".length();
                    int endIndex = str.indexOf(",\"role", startIndex) - 1;

                    String content = str.substring(startIndex, endIndex);

                    list.remove(list.size() - 1);
                    dataInsert(new Msg(content, Msg.TYPE_RECEIVED));
//                        Log.i("ResponeContent", str);
                    handler.sendEmptyMessage(0);
                }
            });
        }

        Log.i("dialog", "关闭输入法");
        InputMethodManager inputMgr = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        bottomDialog.dismiss();
    }
    public void dataInsert(Msg msg) {
        list.add(msg);
        sqlOperate.insert(msg.getType(), msg.getContent());
    }

    public void dataQuery() {
        Cursor cursor = sqlOperate.query();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex("type"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                list.add(new Msg(content, type));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
