package com.example.teacherapp.schedule;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.teacherapp.R;
import com.example.teacherapp.AppBase;

import javax.xml.transform.Result;

public class Scheduler extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> subs;
    ArrayList<String> subx;
    ArrayList<String> times;
    ArrayList<Integer> test;
    ResultSet rs;
    String message;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sch);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(getBaseContext(), CreateScheduleActivity.class);
                startActivity(launchIntent);
            }
        });

        subs = new ArrayList<>();
        times = new ArrayList<>();
        subx = new ArrayList<>();
        test = new ArrayList<>();
        listView = (ListView) findViewById(R.id.schedulerList);
        try {
            loadSchedules();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ResultSet resultSet = (ResultSet)listView.getAdapter().getItem(position);
                message=test.get(position).toString();

                Toast.makeText(activity.getApplicationContext(),""+message,Toast.LENGTH_LONG).show();


                Intent intent = new Intent(getApplicationContext(), ClassRosterList.class);
                intent.putExtra("message", message);
                startActivity(intent);

            }
        });
    }

    public  void loadSchedules() throws SQLException {
        subs.clear();
        times.clear();
        test.clear();
        String qu = "SELECT * FROM classRoster ORDER BY className";
        ResultSet rs = AppBase.handler.execQuery(qu);
        if (rs == null) {
            Toast.makeText(getBaseContext(), "No Schedules Available", Toast.LENGTH_LONG).show();
        } else {
            rs.first();
            while (!rs.isAfterLast()) {
                test.add(rs.getInt(1));
                subx.add(rs.getString(2));
                subs.add(rs.getString(2) + "\nat " + rs.getString(7) + "\n" + rs.getString(6));
                times.add(rs.getString(3));
                rs.next();
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, subs);
        listView.setAdapter(adapter);
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Delete Schedule?");
        alert.setMessage("Do you want to delete this schedule ?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String qu = "DELETE FROM SCHEDULE WHERE subject = '" + subx.get(position) + "' AND timex = '" + times.get(position) + "'";
                if (AppBase.handler.execAction(qu)) {
                    Toast.makeText(getBaseContext(), "Deleted", Toast.LENGTH_LONG).show();
                    try {
                        loadSchedules();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
                    try {
                        loadSchedules();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
        return true;
    }

    public void refresh(MenuItem item) throws SQLException {
        loadSchedules();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scheduler_menu, menu);
        return true;
    }
}