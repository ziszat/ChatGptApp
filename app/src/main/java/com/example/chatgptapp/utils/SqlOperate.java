package com.example.chatgptapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chatgptapp.sql.MyDatabaseHelper;

public class SqlOperate {
    private Context context;
    private MyDatabaseHelper dbHelper;

    public SqlOperate(Context context) {
        this.context = context;
        dbHelper = new MyDatabaseHelper(context, "database.db", 1);
    }

    /**
     * 查询数据
     */
    public Cursor query() {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            cursor = db.query("chatList", null, null, null, null, null, null, null);
        }
        return cursor;
    }

    /**
     * 插入数据
     */
    public void insert(int type, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //确保数据库打开，安全操作
        if (db.isOpen()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("type", type);
            contentValues.put("content", content);
            db.insert("chatList", null, contentValues);
        }
    }
}
