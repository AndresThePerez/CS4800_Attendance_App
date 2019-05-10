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

public class AttendanceActivity extends AppCompatActivity {

    public static String time, period;
    ListView listView;
    ListAdapter adapter;
    ArrayAdapter<String> adapterSpinner;
    ArrayList<String> names;
    ArrayList<String> registers;
    Activity thisActivity = this;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        time = getIntent().getStringExtra("DATE");
        period = getIntent().getStringExtra("PERIOD");

        Log.d("Attendance", time + " -- " + period);
        listView = (ListView) findViewById(R.id.attendanceListViwe);
        names = new ArrayList<>();
        registers = new ArrayList<>();

        Button btn = (Button) findViewById(R.id.loadButton);
        assert btn != null;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadListView(v);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnx = (Button) findViewById(R.id.buttonSaveAttendance);
        assert btnx != null;
        btnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Saving", Toast.LENGTH_LONG).show();
                adapter.saveAll();
            }
        });

        spinner = (Spinner) findViewById(R.id.attendanceSpinner);
        adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AppBase.divisions);
        assert spinner != null;
        spinner.setAdapter(adapterSpinner);

    }

    public void loadListView(View view) throws SQLException {
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
    }
}
