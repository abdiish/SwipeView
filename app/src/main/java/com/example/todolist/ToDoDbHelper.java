package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ToDoDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "todolist.db";
    private static final int DB_VERSION = 3;

    public ToDoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TODOLIST_TABLE = "CREATE TABLE " +
                ToDoContract.ToDoListEntry.TABLE_NAME + " (" +
                ToDoContract.ToDoListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ToDoContract.ToDoListEntry.COLUMN_TASK + " TEXT NOT NULL, " +
                ToDoContract.ToDoListEntry.COLUMN_PRIORITY + " INTEGER NOT NULL, " +
                ToDoContract.ToDoListEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(CREATE_TODOLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ ToDoContract.ToDoListEntry.TABLE_NAME);
        onCreate(db);
    }
}
