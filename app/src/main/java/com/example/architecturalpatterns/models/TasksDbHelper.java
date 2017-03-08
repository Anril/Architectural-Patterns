package com.example.architecturalpatterns.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksDbHelper extends SQLiteOpenHelper {

    private static final String TAG = TasksDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 2;

    abstract class TasksTable {
        static final String TABLE_NAME = "tasks";
        static final String COLUMN_ID = "id";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_COMPLETED = "completed";
    }

    public TasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TasksTable.TABLE_NAME + " ( " +
                TasksTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TasksTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                TasksTable.COLUMN_DESCRIPTION + " TEXT DEFAULT 'No description', " +
                TasksTable.COLUMN_COMPLETED + " INTEGER DEFAULT 0);"
        );

        sqLiteDatabase.execSQL("INSERT INTO " + TasksTable.TABLE_NAME + " (" +
                TasksTable.COLUMN_TITLE + ") VALUES (" +
                "'Awesome task #1');"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TasksTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
