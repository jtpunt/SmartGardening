package com.example.smartgardening.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleCursorAdapter;


public class urlDBUtils {
    private static SQLiteDatabase urlDB;
    private static urlDBHelper urlDbHelper;

    public static void setupSQLite(Context myContext){
        urlDbHelper = new urlDBHelper(myContext); // instantiate the subclass of SQLiteOpenHelper to get access to the db
        urlDB = urlDbHelper.getWritableDatabase(); // gets the data repository in write mode
        Log.d("SQL", "Setting up SQL Database");
    }
    public static void insertData(String url){
        ContentValues values = new ContentValues();
        values.put(urlContract.urlEntry.COLUMN_NAME_STRING, url);
//        Log.d()
        urlDB.insert(urlContract.urlEntry.TABLE_NAME, null, values);
    }
    public static  void deleteData(int id){
        urlDB.delete(urlContract.urlEntry.TABLE_NAME, urlContract.urlEntry._ID+"=?", new String[]{String.valueOf(id)});
    }
    public static Cursor getDatabaseData(){
        // Specify the array of columns to return
        String[] projection = {
                urlContract.urlEntry._ID,
                urlContract.urlEntry.COLUMN_NAME_STRING
        };
        // Read from the database and return the Cursor object
        return urlDB.query(
                urlContract.urlEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }
    public static boolean validDB(){
        return urlDB != null;
    }
    public static SimpleCursorAdapter getCursorAdapter(Context myContext,int layout, Cursor mSQLCursor, int resource){
        return new SimpleCursorAdapter(myContext,
                layout,
                mSQLCursor,
                new String[]{urlContract.urlEntry.COLUMN_NAME_STRING},
                new int[]{resource},
                0);
    }
}
