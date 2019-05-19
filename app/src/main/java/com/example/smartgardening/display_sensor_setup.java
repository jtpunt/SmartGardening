package com.example.smartgardening;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.smartgardening.database.urlDBUtils;

public class display_sensor_setup extends AppCompatActivity {
    Button mSQLSubmitButton;
    SimpleCursorAdapter mSQLCursorAdapter;
    Cursor mSQLCursor;
    private static final String TAG = "SQLActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_setup);
        urlDBUtils.setupSQLite(this);

        mSQLSubmitButton = (Button) findViewById(R.id.sql_add_row_button);
        mSQLSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlDBUtils.validDB()){
                    urlDBUtils.insertData(((EditText)findViewById(R.id.sql_text_input)).getText().toString());
                    populateTable();
                } else {
                    Log.d(TAG, "Unable to access database for writing.");
                }
            }
        });

        populateTable();
    }
    // ShowData
    private void populateTable(){
        if(urlDBUtils.validDB()) {
            try {
                if(mSQLCursorAdapter != null && mSQLCursorAdapter.getCursor() != null){
                    if(!mSQLCursorAdapter.getCursor().isClosed()){
                        mSQLCursorAdapter.getCursor().close();
                    }
                }
                mSQLCursor = urlDBUtils.getDatabaseData();
//                try {
//                    while (mSQLCursor.moveToNext()) {
//
//                    }
//                } finally {
//                    mSQLCursor.close();
//                }
                ListView SQLListView =  findViewById(R.id.sql_list_view);
                mSQLCursorAdapter = urlDBUtils.getCursorAdapter(this, R.layout.sql_item, mSQLCursor, R.id.sql_listview_string);
                SQLListView.setAdapter(mSQLCursorAdapter);
//                mSQLCursor.close();
            } catch (Exception e) {
                Log.d(TAG, "Error loading data from database");
            }
        }
    }
}