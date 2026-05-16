package com.example.databaseMeneger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DataBaseCreator extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "ErasmusBuddy.db";
    private static final int DB_Version = 1;

    public static final String taskTableName = "TasksTable";

    // databases columns
    public final static String keyID_Name = "ID";
    public final static String task_Name = "task";
    public final static String isDone = "isDone";

    public DataBaseCreator(Context context)
    {
        super(context, DATABASE_NAME, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        String createTableQuery = "CREATE TABLE " + taskTableName + " ("
                + keyID_Name + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + task_Name + " TEXT NOT NULL, "
                + isDone + " INTEGER DEFAULT 0)";

        database.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + taskTableName);
        onCreate(sqLiteDatabase);
    }
}
