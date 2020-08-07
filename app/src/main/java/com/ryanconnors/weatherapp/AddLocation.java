package com.ryanconnors.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);


        Places.initialize(this, "AIzaSyBy8YwXIxLnp0SJqkP8lWZSPGJVzX3dYos");
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        //limits results to cities
        autocompleteSupportFragment.setTypeFilter(TypeFilter.CITIES);

        //sets returned data to be the name and address of the Place
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.NAME,
                Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String locationName = place.getName();
                writeToCsv(locationName);

                returnToMain();
            }

            @Override
            public void onError(@NonNull Status status) {
                System.out.println("onError  Error: " + status);
            }
        });
    }


    public void writeToCsv(String locationName) {
        String fileName = "UserLocations.txt";
        File file = new File(this.getFilesDir(), fileName);


        try {

            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            //IF THE FILE DOES NOT EXIST, WRITE THIS LOCATION TO IT
            if (!file.isFile()) {

                //enters the first location name
                fileWriter.append(locationName);
                fileWriter.append("\n");


            //IF THE FILE DOES EXIST, CHECK IF LOCATION IS ALREADY IN IT, IF ITS NOT, ADD IT
            } else {

                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String row;
                List<String> entries = new ArrayList<>();

                //read each entry in file
                while ((row = bufferedReader.readLine()) != null) {
                    entries.add(row);
                }
                bufferedReader.close();

                //if the file doesn't contain locationName, add it.
                if(!entries.contains(locationName)) {
                    fileWriter.append(locationName);
                    fileWriter.append("\n");

                }
            }

            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }



    }


    public void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}