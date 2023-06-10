package com.example.chatgptapp.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createTable = "create table chatList (" +
            " id integer primary key autoincrement," +
            "type integer," +
            "content text)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTable)
        Log.i("MyDatabaseHelper","database create success")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}