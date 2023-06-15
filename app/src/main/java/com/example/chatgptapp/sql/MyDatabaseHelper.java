package com.example.chatgptapp.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chatgptapp.MainActivity;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "CREATE TABLE chatList ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "type INTEGER, "
            + "content TEXT)";

    public MyDatabaseHelper( String name, int version) {
        super(MainActivity.Instance.getApplicationContext(), name, null, version);
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
