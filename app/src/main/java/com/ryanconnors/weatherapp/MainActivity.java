package com.ryanconnors.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //weatherAPI key : fe965d9067db07a703d64d8902e536ad

    private ListView locationList;
    private String selectedLocation;
    private Button addLocationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        locationList = findViewById(R.id.location_list);
        addLocationButton = findViewById(R.id.add_location_button);

        SQLiteDatabase locationDatabase = new UserLocationsDBHelper(this).getReadableDatabase();
        final Cursor locationDBCursor = getAllRows(locationDatabase);

        //checks if
        if (locationDBCursor.moveToFirst()) {
            List<String> locationNames = getLocationNames(locationDBCursor);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, locationNames);
            locationList.setAdapter(adapter);
            locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    selectedLocation = getClickedLocation(position, locationDBCursor);
                }
            });

        }

    }


    private void testButtonClicked(View view) {
        Intent intent = new Intent(this, ShowWeather.class);
        startActivity(intent);
    }



    private void locationSelected(View view) {
        Intent intent = new Intent(this, ShowWeather.class);
        intent.putExtra("EXTRA_LOCATION_NAME", selectedLocation);
        startActivity(intent);
    }


    private String getClickedLocation(int position, Cursor locationDBCursor) {
        return locationDBCursor.getString(position);
    }


    private Cursor getAllRows(SQLiteDatabase db) {
        Cursor allRows = db.rawQuery("select * from " + UserLocationsDBSchema.TABLE_NAME,
                null);
        return allRows;
    }

    private List<String> getLocationNames(Cursor locationDBCursor) {
        List<String> names = new ArrayList<>();
        while(locationDBCursor.moveToNext()) {
            names.add(locationDBCursor.getString(1));
        }
        return names;
    }
}