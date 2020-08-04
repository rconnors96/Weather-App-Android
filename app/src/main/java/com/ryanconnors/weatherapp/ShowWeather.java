package com.ryanconnors.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowWeather extends AppCompatActivity {
    //weatherAPI key : fe965d9067db07a703d64d8902e536ad

    private static String apiResults;
    String editableTempString;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        //getSupportActionBar().hide();
        res = getResources();
        editableTempString = res.getString(R.string.editable_temp_string);

        String providedLocation = getIntent().getStringExtra("EXTRA_LOCATION_NAME");

        //uses provided location to obtain a JSON in the form of a String (apiResults)
        new AccessWeatherTask().execute("http://api.openweathermap.org/data/2.5/weather?q="
                + providedLocation
                + "&units=imperial&appid=fe965d9067db07a703d64d8902e536ad");

    }


    /**
     * Utilizes the OpenWeatherMap API to obtain weather details about a given location
     * It then does the work of processing the received JSON and displaying the data
     */
    protected class AccessWeatherTask extends AsyncTask<String, Integer, String> {

        TextView locationName = findViewById(R.id.location_name_textview);
        TextView temperature = findViewById(R.id.temperature_textview);
        TextView editTemperature = findViewById(R.id.edit_temp_textview);
        TextView feelsLike = findViewById(R.id.feelslike_textview);
        TextView editFeelsLike = findViewById(R.id.edit_feelslike_textview);

        @Override
        protected String doInBackground(String... strings) {

            //sets the url to the provided String parameter
            String urlString = strings[0];

            //creates empty String object to use for assembling the received JSON
            String line;

            //obtains and assembles the JSON for the specified weather location
            URL url;
            try {
                url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
                apiResults = stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String results) {
            //creates a WeatherData object using GSON
            Gson gson = new Gson();
            WeatherData weatherData = gson.fromJson(apiResults, WeatherData.class);

            String name = weatherData.getName();
            double temp = weatherData.getMain().temp;
            double feels_like = weatherData.getMain().feels_like;

            locationName.setText(name);
            editTemperature.setText(String.format(editableTempString, temp));
            editFeelsLike.setText(String.format(editableTempString, feels_like));

            revealTextViews();
        }

        //hides progress bar and reveals textviews
        public void revealTextViews() {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);

            locationName.setVisibility(View.VISIBLE);
            temperature.setVisibility(View.VISIBLE);
            editTemperature.setVisibility(View.VISIBLE);
            feelsLike.setVisibility(View.VISIBLE);
            editFeelsLike.setVisibility(View.VISIBLE);
        }

    }
}