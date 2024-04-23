package com.example.myapplication.Toolbox;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;


import com.example.myapplication.Model.TodoItem;
import com.example.myapplication.Toolbox.SQLiteManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DataBaseManager {

    SQLiteDatabase database;
    SQLiteManager sqLiteManager;
    String dbTableName = "task";
    String dbColumnId = "id";
    String dbColumnStatus = "status";
    String dbColumnName = "taskName";
    String dbColumnDate = "taskDate";


    public DataBaseManager(Context context) {

        SQLiteManager.dbTableName = dbTableName;
        sqLiteManager = new SQLiteManager(context);
        database = sqLiteManager.getWritableDatabase();
    }

    public void addTask(boolean status, String taskName, String date) {
        ContentValues value = new ContentValues();
        value.put(dbColumnStatus, status);
        value.put(dbColumnName, taskName);
        value.put(dbColumnDate, date);
        database.insert(dbTableName, null, value);

    }

    public void removeTask(int id) {
        database.execSQL("DELETE FROM " + dbTableName + " WHERE " + dbColumnId + "=" + id + ";");

    }

    public void updateTask(Boolean taskStatus, int id, String taskName, String taskDate) {
        ContentValues values = new ContentValues();
        values.put(dbColumnStatus, taskStatus);
        values.put(dbColumnName, taskName);
        values.put(dbColumnDate, taskDate);
        database.update(dbTableName, values, dbColumnId + " = " + id, null);

    }
    public int getTotalTasksCount() {
        String query = "SELECT COUNT(*) FROM " + dbTableName;
        Cursor cursor = database.rawQuery(query, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }
    public int getCompletedTasksCount() {
        String query = "SELECT COUNT(*) FROM " + dbTableName + " WHERE " + dbColumnStatus + " = 1";
        Cursor cursor = database.rawQuery(query, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }


    public ArrayList<TodoItem> readFromDB(boolean updateFromServer) throws ExecutionException, InterruptedException {
            return readinLocalDB();
    }

    @SuppressLint("Range")
    ArrayList<TodoItem> readinLocalDB() {
        ArrayList<TodoItem> tasksTemp = new ArrayList<>();
        Cursor c = database.query(dbTableName, new String[]{dbColumnId, dbColumnName, dbColumnStatus, dbColumnDate}, null, null, null, null, null);
        c.moveToFirst();

        int recordsCount = c.getCount();
        Log.d("Database", "Number of records retrieved: " + recordsCount);

        while (!c.isAfterLast()) {
            String itemName = c.getString(c.getColumnIndex(dbColumnName));
            if (itemName != null) {
                int itemId = c.getInt(c.getColumnIndex(dbColumnId));
                boolean itemStatus = c.getInt(c.getColumnIndex(dbColumnStatus)) == 1;
                String itemDate = c.getString(c.getColumnIndex(dbColumnDate));

                TodoItem item = new TodoItem(itemId, itemName, itemStatus, itemDate);
                tasksTemp.add(item);

                // Log individual item details
                Log.d("Database", "Item added: " + item.toString());
            }
            c.moveToNext();
        }
        c.close();
        return tasksTemp;
    }

    public int getCurrentBiggestId() {
        String query = "SELECT MAX(" + dbColumnId + ") FROM " + dbTableName;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null)
            if (cursor.moveToFirst()) return cursor.getInt(0) + 1;
        return 0;
    }
}
