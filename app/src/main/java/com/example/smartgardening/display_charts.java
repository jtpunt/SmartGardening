package com.example.smartgardening;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.anastr.speedviewlib.SpeedView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class display_charts extends AppCompatActivity {
    private static final String TAG = "STATE1: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        String url = "http://192.168.1.128:8080/charts/data";
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        StringRequest stringRequest = buildStrRequest(url);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(stringRequest); // make a get request on "http://192.168.1.128:8080/readings"
    }
    StringRequest buildStrRequest(String url){
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray jArr = new JSONArray(response); // response is a JSON array that contains JSON objects
                            for(int i = 0; i < jArr.length(); i++) {
                                JSONObject jsonObj = jArr.getJSONObject(i); // get our JSON object at index i of our JSON array
                                Log.d(TAG, jsonObj.toString());

//                                String myTempStr = jsonObj.getString("temperature"); // get the value from the "temperature" key
//                                String myHumidStr = jsonObj.getString("humidity"); // get the value from the "humidity" key

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
}
