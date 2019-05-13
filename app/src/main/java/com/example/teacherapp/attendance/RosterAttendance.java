package com.example.teacherapp.attendance;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.teacherapp.R;
import com.example.teacherapp.AppBase;
import com.example.teacherapp.components.ListAdapter;

public class RosterAttendance extends AppCompatActivity {

    public static String time, period, date, message;
    ListView listView;
    ListAdapter adapter;
    ArrayAdapter<String> adapterSpinner;
    ArrayList<String> names;
    ArrayList<String> registers;
    ArrayList<String> subs;
    ArrayList<String> subx;
    ArrayList<String> times;
    ArrayList<Integer> test;
    String rosterID;
    Activity thisActivity = this;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        time = getIntent().getStringExtra("DATE");
        period = getIntent().getStringExtra("PERIOD");
        date = getIntent().getStringExtra("DOW");

        Log.d("Attendance", time + " -- " + period);
        listView = (ListView) findViewById(R.id.classList);
        names = new ArrayList<>();
        registers = new ArrayList<>();
        subs = new ArrayList<>();
        times = new ArrayList<>();
        subx = new ArrayList<>();
        test = new ArrayList<>();
        rosterID=getNum(message);

        message = getIntent().getStringExtra("message");
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        String qu = "Insert into classinstance set rosterID='"+message+"', instanceDate='"+time+"';";
        AppBase.handler.execAction(qu);
        Log.d("Updated Instance", rosterID + " -- " + time);

        try {
            loadListView(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Button btnx = (Button) findViewById(R.id.buttonSaveAttendance);
        assert btnx != null;
        btnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Saving", Toast.LENGTH_LONG).show();
                adapter.saveAll();
            }
        });

/*        adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AppBase.divisions);
        assert spinner != null;
        spinner.setAdapter(adapterSpinner);*/

    }
    public String getNum(String num){
        rosterID = num;
        return num;
    }

    public void loadListView(String message) throws SQLException {
        names.clear();
        registers.clear();
        String qu = "SELECT S.studentID, S.studentFirst, S.studentLast FROM isInClass I JOIN students S ON S.studentID=I.studentID where I.rosterID="+message+" order by studentLast;";
        ResultSet rs = AppBase.handler.execQuery(qu);
        if (rs == null) {
            Log.e("Attendance", "Null cursor");
        } else {
            int ctr = 0;
            rs.first();
            while (!rs.isAfterLast()) {
                names.add(rs.getString(2) + " (" + rs.getString(3) + ')');
                registers.add(rs.getString(1));
                rs.next();
                ctr++;
            }
            if (ctr == 0) {
                Toast.makeText(getBaseContext(), "No Students Found", Toast.LENGTH_LONG).show();
            }
            Log.d("Attendance", "Got " + ctr + " students");
        }
        adapter = new ListAdapter(thisActivity, names, registers);
        listView.setAdapter(adapter);
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

