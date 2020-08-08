package com.ryanconnors.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuListActivity extends AppCompatActivity {

    //weatherAPI key :  fe965d9067db07a703d64d8902e536ad

    private String selectedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        getSupportActionBar().hide();

        //assigning activity buttons/lists
        final ListView locationList = findViewById(R.id.location_list);
        ImageView addLocationImage = findViewById(R.id.add_location_image);

        //access locations file
        String fileName = "UserLocations.txt";
        File file = new File(this.getFilesDir(), fileName);
        if (file.isFile()) {
            final List<String> savedLocationNames = readCsv(file);

            if(savedLocationNames.size() != 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, savedLocationNames);
                locationList.setAdapter(adapter);
                locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        selectedLocation = savedLocationNames.get(position);
                        locationSelected();

                    }
                });
            }
        }

    }


    public List<String> readCsv(File file) {
        List<String> entries = new ArrayList<>();
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String row;
            while ((row = bufferedReader.readLine()) != null) {
                entries.add(row);
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return entries;
    }


    public void addLocationClicked(View view) {
        Intent intent = new Intent(this, AddLocation.class);
        startActivity(intent);
    }


    private void locationSelected() {
        Intent intent = new Intent(this, ShowWeather.class);
        intent.putExtra("EXTRA_LOCATION_NAME", selectedLocation);
        startActivity(intent);
    }

}