package com.example.teacherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

import com.example.teacherapp.R;
import com.example.teacherapp.components.About;
import com.example.teacherapp.components.GridAdapter;
import com.example.teacherapp.components.SettingsActivity;
import com.example.teacherapp.database.DatabaseHandler;

public class AppBase extends AppCompatActivity {

    public static ArrayList<String> divisions;
    public static DatabaseHandler handler;
    public static ArrayList<Integer> ids;
    public static Activity activity;
    ArrayList<String> basicFields;
    GridAdapter adapter;
    GridView gridView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        basicFields = new ArrayList<>();
        handler = new DatabaseHandler(this);
        activity = this;

        getSupportActionBar().show();
        divisions = new ArrayList<>();
        divisions.add("Winter");
        divisions.add("Fall");
        divisions.add("Summer");
        divisions.add("Sping");
        gridView = (GridView) findViewById(R.id.grid);
        basicFields.add("ATTENDANCE");
        basicFields.add("SCHEDULER");
        basicFields.add("NOTES");
        basicFields.add("PROFILE");
        basicFields.add("GPA CALCULATOR");
        basicFields.add("LOG OUT");
        adapter = new GridAdapter(this, basicFields);
        gridView.setAdapter(adapter);
    }

    public void loadSettings(MenuItem item) {
        Intent launchIntent = new Intent(this, SettingsActivity.class);
        startActivity(launchIntent);
    }

    public void loadAbout(MenuItem item) {
        Intent launchIntent = new Intent(this, About.class);
        startActivity(launchIntent);
    }
}
