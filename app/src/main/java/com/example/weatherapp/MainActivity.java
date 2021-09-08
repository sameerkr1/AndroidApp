package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Button btnWeather;
    TextView tvDisplay;
    EditText etCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWeather = findViewById(R.id.btnWeather);
        tvDisplay = findViewById(R.id.tvDisplay);
        etCity = findViewById(R.id.etCity);

    }

    public void get(View v) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + etCity.getText().toString() + "&appid=" + "9fb611d0a3d8e37b47883276b0ccb617";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        @SuppressLint("SetTextI18n") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                JSONObject jsonObject = response.getJSONObject("main");
                String temp = jsonObject.getString("temp");
                Double tempDouble = Double.parseDouble(temp) - 273.15;
                tvDisplay.setText("Temperature of " + etCity.getText() + " is " + df.format(tempDouble));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "something went wrong1", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println(error.toString());
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        });
        queue.add(jsonObjectRequest);
    }
}