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

import java.util.Arrays;

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
                addPlaceToDatabase(locationName);
                returnToMain();
            }

            @Override
            public void onError(@NonNull Status status) {
                System.out.println("onError  Error: " + status);
            }
        });
    }

    public void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addPlaceToDatabase(String locationName) {
        SQLiteDatabase sqLiteDatabase = new UserLocationsDBHelper(this).getWritableDatabase();
        Cursor checkCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + UserLocationsDBSchema.TABLE_NAME +
                " WHERE " + UserLocationsDBSchema.LOCATION +
                " LIKE '%" + locationName + "%';", null);

        if (checkCursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserLocationsDBSchema.LOCATION, locationName);

            sqLiteDatabase.insert(UserLocationsDBSchema.TABLE_NAME,
                    null,
                    contentValues);
        }

        sqLiteDatabase.close();
    }
}