package com.example.databaseMeneger;

import android.content.ContentValues;
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

    private final Context context;

    public DataBaseCreator(Context context)
    {
        super(context, DATABASE_NAME, null, DB_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        String createTableQuery = "CREATE TABLE " + taskTableName + " ("
                + keyID_Name + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + task_Name + " TEXT NOT NULL, "
                + isDone + " INTEGER DEFAULT 0)";

        database.execSQL(createTableQuery);
        insertDefaulTasks(database);
    }

    private void insertDefaulTasks(SQLiteDatabase db){
        // We use context.getString() to fetch localized strings from strings.xml
        String[] defaultTasks = {
                context.getString(com.example.erasmusbuddy.R.string.db_hospital),
                context.getString(com.example.erasmusbuddy.R.string.db_friend),
                context.getString(com.example.erasmusbuddy.R.string.db_cultural),
                context.getString(com.example.erasmusbuddy.R.string.db_transport),
                context.getString(com.example.erasmusbuddy.R.string.db_ESN),
                context.getString(com.example.erasmusbuddy.R.string.db_supermarket),
                context.getString(com.example.erasmusbuddy.R.string.db_coffe)
        };

        for (String task : defaultTasks){
            ContentValues values = new ContentValues();
            values.put(task_Name, task);
            values.put(isDone, 0);
            db.insert(taskTableName, null, values);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + taskTableName);
        onCreate(sqLiteDatabase);
    }
}
