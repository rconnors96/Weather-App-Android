package com.ryanconnors.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowWeather extends AppCompatActivity {

    //weatherAPI key : fe965d9067db07a703d64d8902e536ad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        URL url = null;
        try {
            url = new URL("api.openweathermap.org/data/2.5/weather?q=Boston&appid=fe965d9067db07a703d64d8902e536ad");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}