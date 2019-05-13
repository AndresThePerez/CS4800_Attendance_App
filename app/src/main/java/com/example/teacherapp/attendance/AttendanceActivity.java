package com.example.teacherapp.attendance;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.teacherapp.schedule.ClassRosterList;

public class AttendanceActivity extends AppCompatActivity {

    public static String time, period, date;
    ListView listView;
    ListAdapter adapter;
    ArrayAdapter<String> adapterSpinner;
    ArrayList<String> names;
    ArrayList<String> registers;
    ArrayList<String> subs;
    ArrayList<String> subx;
    ArrayList<String> times;
    ArrayList<Integer> test;
    String message;
    Activity activity = this;
    Spinner spinner;
    String rosterID;

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
        try {
            loadSchedules();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ResultSet resultSet = (ResultSet)listView.getAdapter().getItem(position);
                message =test.get(position).toString();


                Intent intent = new Intent(getApplicationContext(), RosterAttendance.class);
                intent.putExtra("DOW", date);
                intent.putExtra("DATE", time);
                intent.putExtra("PERIOD", period);
                intent.putExtra("message", message);
                startActivity(intent);

            }
    });
    }
    public  void loadSchedules() throws SQLException {
        subs.clear();
        times.clear();
        test.clear();
        String qu = "SELECT * FROM classroster ORDER BY className";
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

/*    public void loadListView(View view) throws SQLException {
        names.clear();
        registers.clear();
        String qu = "SELECT * FROM STUDENT WHERE cl = '" + spinner.getSelectedItem().toString() + "' " +
                "ORDER BY ROLL";
        ResultSet rs = AppBase.handler.execQuery(qu);
        if (rs == null || rs.equals(0)) {
            Log.e("Attendance", "Null cursor");
        } else {
            int ctr = 0;
            rs.first();
            while (!rs.isAfterLast()) {
                names.add(rs.getString(0) + " (" + rs.getInt(4) + ')');
                registers.add(rs.getString(2));
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
    }*/
}
