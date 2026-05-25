package com.example.databaseMeneger;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DataBaseController
{
    private DataBaseCreator DBcreator;
    private SQLiteDatabase DBObject;

    private static final String TAG = "DataBaseController";

    private final static String[] columns = {
            DataBaseCreator.keyID_Name,
            DataBaseCreator.task_Name,
            DataBaseCreator.isDone
    };


    public DataBaseController(Context context)
    {
        DBcreator = new DataBaseCreator(context);
    }

    // everytime when DB is used before for loop etc. you need to open it, or there will be errors. REMEMBER TO CLOSE IT AFTER USING, couse there will be memory leaks
    private void openDB()
    {
        DBObject = DBcreator.getWritableDatabase();
    }

    // REMEMBER to close DB after using it
    private void closeDB()
    {
        if (DBObject != null && DBObject.isOpen())
        {
            DBObject.close();
        }
    }

    // insert task adding on the back of table
    // if this will be lagging its probably becouse DB is often openigng and closing so we can do it manually with petla
    // have this in mind it could be the foult but idk, check it lol my brother and sister.
    public boolean insertTask(String taskName)
    {
        openDB();
        ContentValues values = new ContentValues();

        values.put(DataBaseCreator.task_Name, taskName);
        values.put(DataBaseCreator.isDone, 0);

        long result = -1;

        try
        {
            result = DBObject.insert(DataBaseCreator.taskTableName, null, values);
        }
        catch (RuntimeException e)
        {
            Log.e(TAG, "Error during database operation");
        }
        finally
        {
            closeDB();
        }

        if (result == -1)
            return false;
        else
            return true;
    }


    // return ALL tasks from DataBase

    // dont need to use open or close DB -> do it automatically
    public List<Task> getTaskList()
    {
        List<Task> allTasks = new ArrayList<>();

        openDB();

        Cursor reader = DBObject.query(
                DataBaseCreator.taskTableName,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        try
        {
            if (reader != null && reader.moveToFirst())
            {
                do
                {
                    int id = reader.getInt(reader.getColumnIndexOrThrow(DataBaseCreator.keyID_Name));
                    String task = reader.getString(reader.getColumnIndexOrThrow(DataBaseCreator.task_Name));
                    int isDone = reader.getInt(reader.getColumnIndexOrThrow(DataBaseCreator.isDone));

                    Task tempTask = new Task(id, task, isDone);

                    allTasks.add(tempTask);
                }
                while (reader.moveToNext()); // return true if there is still something there thats why do while eseses
            }

        }
        catch (Exception e)
        {
            Log.e(TAG, "Error during database operation");
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
            closeDB();
        }

        return allTasks;
    }

    // delete record using ID, same as in object Task
    // also openDB and closeDB is not necessery
    public boolean deleteAtID(int ID)
    {
        openDB();

        String whereQuerry = DataBaseCreator.keyID_Name + " = ?";

        String[] whereArgs = {
                String.valueOf(ID)
        };

        int deletedRows = 0;

        try
        {
            deletedRows = DBObject.delete(
                    DataBaseCreator.taskTableName,
                    whereQuerry,
                    whereArgs
            );
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error during database operation");
        }
        finally
        {
            closeDB();
        }

        if (deletedRows > 0)
            return true;
        else
            return false;
    }

    // updating record using ID, adding newTaskName and was it done at start
    // using open and close is not necessary
    public boolean updateAtID(int ID, String newTaskName, int isDone)
    {
        openDB();

        ContentValues values = new ContentValues();
        values.put(DataBaseCreator.task_Name, newTaskName);
        values.put(DataBaseCreator.isDone, isDone);

        String whereQuerry = DataBaseCreator.keyID_Name + " = ?";
        String[] whereArgs = {
                String.valueOf(ID)
        };


        int updatedRows = 0;

        try
        {
            updatedRows = DBObject.update(DataBaseCreator.taskTableName, values, whereQuerry, whereArgs);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error during database operation");
        }
        finally
        {
            closeDB();
        }

        if (updatedRows > 0)
            return true;
        else
            return false;
    }
}
