package com.example.teacherapp.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teacherapp.R;
import com.example.teacherapp.AppBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentRegistration extends AppCompatActivity {


    Activity activity = this;
    Spinner spinner;
    ArrayList<String> studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__registartion);
        Bundle bundle = getIntent().getExtras();
        final String message = bundle.getString("rosterID");
        studentID = new ArrayList<>();

/*        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AppBase.divisions);
        spinner.setAdapter(adapter);*/

        Button btn = (Button) findViewById(R.id.buttonSAVE);
        assert btn != null;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveToDatabase(v,message);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void saveToDatabase(View view, String message) throws SQLException {
        EditText first = (EditText) findViewById(R.id.edit_first);
        EditText last = (EditText) findViewById(R.id.edit_last);
        EditText contact = (EditText) findViewById(R.id.contact);
        //String classSelected = spinner.getSelectedItem().toString();

        if (first.getText().length() < 2 || last.getText().length() == 0 ||
                contact.getText().length() < 2) {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Invalid");
            alert.setMessage("Insufficient Data");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }

/*        String qu = "INSERT INTO STUDENT VALUES('" + name.getText().toString() + "'," +
                "'" + classSelected + "'," +
                "'" + register.getText().toString().toUpperCase() + "'," +
                "'" + contact.getText().toString() + "'," +
                "" + Integer.parseInt(roll.getText().toString()) + ");";*/
        String qu = "insert into students set studentFirst='"+first.getText().toString()+"', studentLast='"+last.getText().toString()+"';";
        Log.d("Student Reg", qu);
        AppBase.handler.execAction(qu);
        //qu = "SELECT * FROM students ORDER BY studentID";
/*        ResultSet rs = AppBase.handler.execQuery(qu);
        if (rs == null) {
            Toast.makeText(getBaseContext(), "No Schedules Available", Toast.LENGTH_LONG).show();
        } else {
            rs.first();
            while (!rs.isAfterLast()) {
                studentID.add(rs.getInt(1));
                rs.next();
            }
        }*/

            String qw = "Select * from students;";
            ResultSet rs = AppBase.handler.execQuery(qw);
            if (rs == null) {
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
            } else {
                rs.first();
                while (!rs.isAfterLast()) {
                    studentID.add(rs.getString(1));
                    rs.next();
                }
            }
            String lastnum = studentID.get(studentID.size()-1);


        qu = "SELECT * FROM students where studentID = "+lastnum+";";
        Log.d("Student Reg", qu);
        if (AppBase.handler.execQuery(qu) != null) {
            Toast.makeText(getBaseContext(), "Student Added", Toast.LENGTH_LONG).show();
            this.finish();
        }
        qu="insert into isinclass set studentID="+lastnum+" ,rosterID='"+message+"';";
        Log.d("Student Reg", qu);
        AppBase.handler.execAction(qu);

    }
}

