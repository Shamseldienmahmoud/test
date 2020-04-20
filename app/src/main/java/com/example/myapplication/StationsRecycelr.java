package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.StationsAdapter;
import com.example.myapplication.Models.StationsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StationsRecycelr extends AppCompatActivity {
    ArrayList<StationsItem> stationslists = new ArrayList<>();
    RecyclerView recyclerView;
    StationsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_recycelr);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        recyclerView = findViewById(R.id.recycel);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
       getStations();

    }

    private void getStations() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.STAIONS_NAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        stationslists.add(
                                new StationsItem(
                                        jsonObject.getString("station_name"),
                                        jsonObject.getInt("station_line"),
                                        jsonObject.getInt("station_id")
                                )
                        );
                    }
                    adapter = new StationsAdapter(StationsRecycelr.this, stationslists);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
