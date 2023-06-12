package com.example.chatgptapp.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "CREATE TABLE chatList ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "type INTEGER, "
            + "content TEXT)";

    public MyDatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.i("MyDatabaseHelper", "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement upgrade logic here if needed
    }
}
