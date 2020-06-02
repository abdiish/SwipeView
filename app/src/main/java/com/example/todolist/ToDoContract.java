package com.example.todolist;

import android.provider.BaseColumns;

public class ToDoContract {

    public static final class ToDoListEntry implements BaseColumns {

        public static final String TABLE_NAME = "todolist";
        public static final String COLUMN_TASK = "task";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
