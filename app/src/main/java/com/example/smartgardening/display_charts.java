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

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.smartgardening.R;
import com.github.anastr.speedviewlib.SpeedView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                            List<DataEntry> seriesData = new ArrayList<>();
                            JSONObject jsonObj = null;
                            String myDateStr = null ;
                            Number myTempNum = null, myHumidNum = null;
                            AnyChartView anyChartView = findViewById(R.id.any_chart_view);
                            anyChartView.setProgressBar(findViewById(R.id.progress_bar));

                            Cartesian cartesian = AnyChart.line();

                            cartesian.animation(true);

                            cartesian.padding(10d, 20d, 5d, 20d);

                            cartesian.crosshair().enabled(true);
                            cartesian.crosshair()
                                    .yLabel(true)
                                    // TODO ystroke
                                    .yStroke((Stroke) null, null, null, (String) null, (String) null);

                            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

                            cartesian.title("Temperature and Humidity REadings");

                            cartesian.yAxis(0).title("Temperature Versus Humidity");
                            cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


                            anyChartView.setChart(cartesian);
                            JSONArray jArr = new JSONArray(response); // response is a JSON array that contains JSON objects
                            for(int i = 0; i < jArr.length(); i++) {
                                jsonObj = jArr.getJSONObject(i); // get our JSON object at index i of our JSON array
                                Log.d(TAG, jsonObj.toString());
                                myTempNum = jsonObj.getDouble("temp"); // get the value from the "temperature" key
                                myHumidNum = jsonObj.getDouble("humid"); // get the value from the "temperature" key
                                myDateStr = jsonObj.getString("date"); // get the value from the "date" key
                                seriesData.add(new CustomDataEntry(myDateStr, myTempNum, myHumidNum, 2.8));
//                                String myTempStr = jsonObj.getString("temperature"); // get the value from the "temperature" key
//                                String myHumidStr = jsonObj.getString("humidity"); // get the value from the "humidity" key

                            }

//        seriesData.add(new CustomDataEntry("1986", 3.6, 2.3, 2.8));

                            Set set = Set.instantiate();
                            set.data(seriesData);
                            Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                            Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

                            Line series1 = cartesian.line(series1Mapping);
                            series1.name("Temperature");
                            series1.hovered().markers().enabled(true);
                            series1.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series1.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            Line series2 = cartesian.line(series2Mapping);
                            series2.name("Humidity");
                            series2.hovered().markers().enabled(true);
                            series2.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series2.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            cartesian.legend().enabled(true);
                            cartesian.legend().fontSize(13d);
                            cartesian.legend().padding(0d, 0d, 10d, 0d);
                            anyChartView.setChart(cartesian);
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
    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }
}
