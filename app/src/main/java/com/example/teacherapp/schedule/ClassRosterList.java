package com.example.teacherapp.schedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.teacherapp.R;
import com.example.teacherapp.AppBase;
import com.example.teacherapp.profile.StudentRegistration;


import com.example.teacherapp.R;

public class ClassRosterList extends AppCompatActivity {

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<Integer> test;
    ArrayList<String> subs;
    ArrayList<String> subx;
    ArrayList<String> times;
    Activity activity = this;
    String rosterID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_roster_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sch);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(getBaseContext(), StudentRegistration.class);
                launchIntent.putExtra("rosterID", rosterID);
                startActivity(launchIntent);
            }
        });
        subs = new ArrayList<>();
        times = new ArrayList<>();
        subx = new ArrayList<>();
        listView = (ListView) findViewById(R.id.rosterList);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        rosterID=getNum(message);
        try {
            loadRoster(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResultSet rs = (ResultSet) adapter.getItem(position);
                try {
                    if(rs.first()){
                        String hello = rs.getString("rosterID");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Toast.makeText(activity.getApplicationContext(),""+rosterID,Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), ClassRosterList.class);
                //startActivity(intent);
            }
        });
    }
    public String getNum(String num){
        rosterID = num;
        return num;
    }
    private void loadRoster(String message) throws SQLException {

        String qu = "SELECT S.studentID, S.studentFirst, S.studentLast FROM isInClass I JOIN students S ON S.studentID=I.studentID where I.rosterID="+message+" order by studentLast;";
        ResultSet rs = AppBase.handler.execQuery(qu);
        if (rs == null) {
            Toast.makeText(getBaseContext(), "No Schedules Available", Toast.LENGTH_LONG).show();
        } else {
            rs.first();
            while (!rs.isAfterLast()) {
                subx.add(rs.getString(2));
                subs.add(rs.getString(2) + " " + rs.getString(3));
                times.add(rs.getString(3));
                rs.next();
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, subs);
        listView.setAdapter(adapter);

    }}
