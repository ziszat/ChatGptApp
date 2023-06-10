package com.example.chatgptapp.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.chatgptapp.sql.MyDatabaseHelper

/**
 * @author :lwh
 * @date : 2023/3/13
 */
class SqlOprate {
    private var context: Context? = null
    var dbHelper: MyDatabaseHelper? = null

    constructor(context: Context?) {
        this.context = context
        dbHelper = MyDatabaseHelper(context!!, "database.db", 1)
    }

    /**
     * 查询数据
     */
    fun query(): Cursor? {
        var db = dbHelper!!.readableDatabase
        var cursor:Cursor ?= null
        if (db.isOpen) {
            cursor = db.query("chatList", null, null, null, null, null, null, null)
        }
        return cursor
    }

    /**
     * 插入数据
     */
    fun insert(type:Int,content:String) {
        var db = dbHelper!!.writableDatabase
        //确保数据库打开，安全操作
        if (db.isOpen) {
            var contentValues = ContentValues().apply {
                put("type", type)
                put("content", content)
            }
            db.insert("chatList", null, contentValues)
        }
    }
}