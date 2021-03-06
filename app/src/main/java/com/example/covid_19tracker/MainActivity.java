package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvCases,
            tvActive,
            tvCritical,
            tvRecovered,
            tvDeaths,
            tvTests,
            tvTodayCases,
            tvTodayDeaths,
            tvCasesPerMillion,
            tvAffectedCountries,
            tvPopulation;
    SimpleArcLoader simpleArcLoader;
    ScrollView scrollView;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCases=findViewById(R.id.tvCases);
        tvActive=findViewById(R.id.tvActive);
        tvCritical=findViewById(R.id.tvCritical);
        tvRecovered=findViewById(R.id.tvRecovered);
        tvDeaths=findViewById(R.id.tvDeaths);
        tvTests=findViewById(R.id.tvTests);
        tvTodayCases=findViewById(R.id.tvTodayCases);
        tvTodayDeaths=findViewById(R.id.tvTodayDeaths);
        tvCasesPerMillion=findViewById(R.id.tvCasesPerMillion);
        tvAffectedCountries=findViewById(R.id.tvAffectedCountries);
        tvPopulation=findViewById(R.id.tvPopulation);

        simpleArcLoader=findViewById(R.id.loader);
        scrollView=findViewById(R.id.scrollStats);
        pieChart=findViewById(R.id.piechart);

        fetchData();
    }

    private void fetchData() {
        String url="https://corona.lmao.ninja/v3/covid-19/all";
        simpleArcLoader.start();
        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            tvCases.setText(jsonObject.getString("cases"));
                            tvActive.setText(jsonObject.getString("active"));
                            tvCritical.setText(jsonObject.getString("critical"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvDeaths.setText(jsonObject.getString("deaths"));
                            tvTests.setText(jsonObject.getString("tests"));
                            tvTodayCases.setText(jsonObject.getString("todayCases"));
                            tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                            tvCasesPerMillion.setText(jsonObject.getString("casesPerOneMillion"));
                            tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));
                            tvPopulation.setText(jsonObject.getString("population"));

                            pieChart.addPieSlice(new PieModel("Active",Integer.parseInt
                                    (tvActive.getText().toString()), Color.parseColor("#FFA726")));
                            pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt
                                    (tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                            pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt
                                    (tvDeaths.getText().toString()), Color.parseColor("#EF5350")));
                            pieChart.setUsePieRotation(false);
                            pieChart.startAnimation();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void goTrackCountries(View view){
        startActivity(new Intent(getApplicationContext(),AffectedCountries.class));
    }
}