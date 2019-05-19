package com.example.smartgardening;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.anastr.speedviewlib.SpeedView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.smartgardening.database.urlDBUtils;
import java.util.Random;

public class display_gauges extends AppCompatActivity {
    private static final String TAG = "STATE1: ";
    SimpleCursorAdapter mSQLCursorAdapter;
    Cursor mSQLCursor;
    final int min = 0, max = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlDBUtils.setupSQLite(this);
        Log.d(TAG, "onStart() called");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        1);
            }

        }else{
            Log.d("PERM", "App does have all permissions!");
            setContentView(R.layout.activity_gauges);
            RequestQueue queue = Volley.newRequestQueue(getApplication());
            mSQLCursor = urlDBUtils.getDatabaseData();
            Log.d(TAG, DatabaseUtils.dumpCursorToString(mSQLCursor));
            try {
                if(mSQLCursor.moveToFirst()){
                    do{
                        Log.d(TAG, DatabaseUtils.dumpCurrentRowToString(mSQLCursor));
                        String url = mSQLCursor.getString(mSQLCursor.getColumnIndex("url")); // get our URL stored in sqlite
                        Log.d(TAG, url);
                        queue.add(buildStrRequest(url)); // make a get request on "http://192.168.1.128:8080/readings"
                    }while (mSQLCursor.moveToNext());
                }
            } finally {
                Log.d(TAG, "Closing cursor");
                mSQLCursor.close();
            }
        }

    }
    StringRequest buildStrRequest(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            LinearLayout m_ll = (LinearLayout) findViewById(R.id.llMain); // get our main linear layout
                            JSONArray jArr = new JSONArray(response); // response is a JSON array that contains JSON objects
                            for(int i = 0; i < jArr.length(); i++) {
                                Context myContext = getApplicationContext(); // get our context
                                LinearLayout newLayout = new LinearLayout(myContext); // create a new linear layout, which will be appended to the main linear layout
                                // set layout param constraints so that each gauge will take up the same amount of space
                                LinearLayout.LayoutParams gaugeParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

                                JSONObject jsonObj = jArr.getJSONObject(i); // get our JSON object at index i of our JSON array
                                String myTempStr = jsonObj.getString("temperature"); // get the value from the "temperature" key
                                String myHumidStr = jsonObj.getString("humidity"); // get the value from the "humidity" key
                                Log.d(TAG, jsonObj.toString());
                                Float temp = new Float(myTempStr) * 9 / 5 + 32; // convert the string to a float, then convert from celcius to fahrenheit
                                Float humid = new Float(myHumidStr); // convert the string to a float

                                SpeedView tempGauge = new SpeedView(myContext); // Create a new gauge for our temp value
                                tempGauge.speedTo(temp); // set the speed to our temp reading value
                                tempGauge.setUnit(" \u2109"); // set the unit to show the degree symbol, plus F for Fahrenheit
                                tempGauge.setWithTremble(false); // have the indicator on the gauge slowly increase until it is at our temp value and stop there
                                tempGauge.setLayoutParams(gaugeParam); // set the gauge layout parameters

                                SpeedView humidGauge = new SpeedView(myContext); // Create a new gauge for our humid value
                                humidGauge.speedTo(humid); // set the speed to our humidity reading value
                                humidGauge.setUnit(" %"); // set the unit to show %, the percentage of humidity in the air
                                humidGauge.setWithTremble(false);
                                humidGauge.setLayoutParams(gaugeParam);

                                newLayout.addView(tempGauge); // add the gauges to our newly created linear layout
                                newLayout.addView(humidGauge);
                                m_ll.addView(newLayout); // add the newly created linear layout (row) to our main linear layout
                            }
                        }catch (JSONException e) {
                            Log.e("STATE1", "onPostExecute > Try > JSONException => " + e);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("STATE1", String.valueOf(error));
            }
        });
        return stringRequest;
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}