package com.example.chatgptapp.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chatgptapp.sql.MyDatabaseHelper;

public class SqlOperate {
    private MyDatabaseHelper dbHelper;

    public SqlOperate() {
        dbHelper = new MyDatabaseHelper("chat.db", 1);
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
