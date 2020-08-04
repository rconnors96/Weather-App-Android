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
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //weatherAPI key : fe965d9067db07a703d64d8902e536ad

    private ListView locationList;
    private String selectedLocation;
    private ImageView addLocationImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //assigning activity buttons/lists
        locationList = findViewById(R.id.location_list);
        addLocationImage = findViewById(R.id.add_location_image);

        SQLiteDatabase locationDatabase = new UserLocationsDBHelper(this).getReadableDatabase();
        final Cursor locationDBCursor = getAllRows(locationDatabase);

        //If there are no items in the user's location list, nothing happens with the ListView
        //.moveToFirst() returns false if the cursor is empty
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

        locationDatabase.close();

    }


    public void addLocationClicked(View view) {
        Intent intent = new Intent(this, AddLocation.class);
        startActivity(intent);
    }


    public void testButtonClicked(View view) {
        Intent intent = new Intent(this, ShowWeather.class);
        intent.putExtra("EXTRA_LOCATION_NAME","Boston");
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

    //returns a List<String> of the location name strings used in the database
    private List<String> getLocationNames(Cursor locationDBCursor) {
        List<String> names = new ArrayList<>();
        while(locationDBCursor.moveToNext()) {
            names.add(locationDBCursor.getString(1));
        }
        return names;
    }
}