package com.edot.foodapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Set;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Set<String> tables = AppConstants.TABLE_LIST.keySet();
        for(String table : tables)
        {
            String createQuery = "CREATE TABLE "+table+"(";
            HashMap<String,String> tableProperties = AppConstants.TABLE_LIST.get(table);
            Set<String> columns = tableProperties.keySet();
            boolean appendSeparator = false;
            for(String column : columns)
            {
                if(appendSeparator)
                {
                    createQuery = createQuery + ",";
                }
                createQuery = createQuery + column;
                createQuery = createQuery + " ";
                createQuery = createQuery + tableProperties.get(column);
                appendSeparator = true;
            }
            createQuery = createQuery+")";
            db.execSQL(createQuery);
            Log.d(AppConstants.LOG_TAG,"Created Table : "+table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Set<String> tables = AppConstants.TABLE_LIST.keySet();
        for(String table : tables)
        {
            db.execSQL("DROP TABLE IF EXISTS "+table);
        }
        onCreate(db);
    }
}
