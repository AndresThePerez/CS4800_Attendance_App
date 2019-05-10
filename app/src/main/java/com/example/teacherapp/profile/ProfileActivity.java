package com.example.teacherapp.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.teacherapp.R;
import com.example.teacherapp.AppBase;
import com.example.teacherapp.database.DatabaseHandler;

public class ProfileActivity extends AppCompatActivity {

    DatabaseHandler handler = AppBase.handler;
    Activity profileActivity = this;
    ListView listView;
    ProfileAdapter adapter;
    ArrayList<String> dates;
    ArrayList<String> datesALONE;
    ArrayList<Integer> hourALONE;
    ArrayList<Boolean> atts;
    Activity activity = this;
    private View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_profile);
        dates = new ArrayList<>();
        datesALONE = new ArrayList<>();
        hourALONE = new ArrayList<>();
        atts = new ArrayList<>();

        listView = (ListView) findViewById(R.id.attendProfileView_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(profileActivity, StudentRegistration.class);
                startActivity(launchIntent);
            }
        });

        TextView textView = (TextView) findViewById(R.id.profileContentView);
        assert textView != null;
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Delete Student");
                alert.setMessage("Are you sure ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) findViewById(R.id.editText);
                        String regno = editText.getText().toString();
                        String qu = "DELETE FROM STUDENT WHERE REGNO = '" + regno.toUpperCase() + "'";
                        if (AppBase.handler.execAction(qu)) {
                            Log.d("delete", "done from student");
                            String qa = "DELETE FROM ATTENDANCE WHERE register = '" + regno.toUpperCase() + "'";
                            if (AppBase.handler.execAction(qa)) {
                                Toast.makeText(getBaseContext(), "Deleted", Toast.LENGTH_LONG).show();
                                Log.d("delete", "done from attendance");
                            }
                        }
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
                return true;
            }
        });


        Button findButton = (Button) findViewById(R.id.buttonFind);
        assert findButton != null;
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    find(v);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void find(View view) throws SQLException {
        dates.clear();
        atts.clear();
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textView = (TextView) findViewById(R.id.profileContentView);
        String reg = editText.getText().toString();
        String qu = "SELECT * FROM STUDENT WHERE regno = '" + reg.toUpperCase() + "'";
        String qc = "SELECT * FROM ATTENDANCE WHERE register = '" + reg.toUpperCase() + "';";
        String qd = "SELECT * FROM ATTENDANCE WHERE register = '" + reg.toUpperCase() + "' AND isPresent = 1";
        ResultSet rs = handler.execQuery(qu);
        //Start Count Here

        float att = 0f;
        ResultSet r1 = handler.execQuery(qc);
        ResultSet r2 = handler.execQuery(qd);
        if (r1 == null) {
            Log.d("profile", "cur null");
        }
        if (r2 == null) {
            Log.d("profile", "cur1 null");
        }
        if (r1 != null && r2 != null) {
            r1.first();
            rs.first();
            try {
                att = ((float) r2.getFetchSize() / r1.getFetchSize()) * 100;
                if (att <= 0)
                    att = 0f;
                Log.d("ProfileActivity", "Total = " + r1.getFetchSize() + " avail = " + r2.getFetchSize() + " per " + att);
            } catch (Exception e) {
                att = -1;
            }
        }


        if (rs == null || rs.getFetchSize() == 0) {
            assert textView != null;
            textView.setText("No Data Available");
        } else {
            String attendance = "";
            if (att < 0) {
                attendance = "Attendance Not Available";
            } else
                attendance = " Attendance " + att + " %";
            rs.first();
            String buffer = "";
            buffer += " " + rs.getString(0) + "\n";
            buffer += " " + rs.getString(1) + "\n";
            buffer += " " + rs.getString(2) + "\n";
            buffer += " " + rs.getString(3) + "\n";
            buffer += " " + rs.getInt(4) + "\n";
            buffer += " " + attendance + "\n";
            textView.setText(buffer);

            String q = "SELECT * FROM ATTENDANCE WHERE register = '" + editText.getText().toString().toUpperCase() + "'";
            ResultSet r3 = handler.execQuery(q);
            if (r3 == null || r3.getFetchSize() == 0) {
                Toast.makeText(getBaseContext(), "No Attendance Info Available", Toast.LENGTH_LONG).show();
            } else {
                r3.first();
                while (!r3.isAfterLast()) {
                    datesALONE.add(r3.getString(0));
                    hourALONE.add(r3.getInt(1));
                    dates.add(r3.getString(0) + ":" + r3.getInt(1) + "th Hour");
                    if (r3.getInt(3) == 1)
                        atts.add(true);
                    else {
                        Log.d("profile", r3.getString(0) + " -> " + r3.getInt(3));
                        atts.add(false);
                    }
                    r3.next();
                }
                adapter = new ProfileAdapter(dates, atts, profileActivity, datesALONE, hourALONE, editText.getText().toString().toUpperCase());
                listView.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }


    public void editStudent(MenuItem item) {
        Intent launchIntent = new Intent(this, EditStudentActivity.class);
        startActivity(launchIntent);
    }
}
