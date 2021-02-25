package com.dojo.tasklist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context){
        super(context, TaskContract.DB_NAME,null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuerry = String.format("CREATE TABLE %s (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT)",com.dojo.tasklist.db.TaskContract.TABLE,TaskContract.Columns.TAREFA);
        Log.d("TaskDBHelper","Query to form table :"+ sqlQuerry);
        db.execSQL(sqlQuerry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TaskContract.TABLE);
    }
}
