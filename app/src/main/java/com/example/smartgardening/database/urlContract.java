package com.example.smartgardening.database;

import android.provider.BaseColumns;

public final class urlContract {
    private urlContract(){}; // To prevent someone from accidentally instantiating the contract class, make the constructor private

    // Inner class that defines the table contents
    public final class urlEntry implements BaseColumns {
        public static final String DB_NAME = "demo_db";
        public static final String TABLE_NAME = "demo";
        public static final String COLUMN_NAME_STRING = "url";
        public static final int DB_VERSION = 1;


        public static final String SQL_CREATE_URL_TABLE = "CREATE TABLE " +
                urlEntry.TABLE_NAME + "(" + urlEntry._ID + " INTEGER PRIMARY KEY NOT NULL," +
                urlEntry.COLUMN_NAME_STRING + " VARCHAR(255));";

        public  static final String SQL_DROP_URL_TABLE = "DROP TABLE IF EXISTS " + urlEntry.TABLE_NAME;
    }
}
