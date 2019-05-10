package com.example.teacherapp.profile;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teacherapp.R;
import com.example.teacherapp.AppBase;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.Result;

public class EditStudentActivity extends AppCompatActivity {
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__student);

        Button loadButton = (Button) findViewById(R.id.loadForEdit);
        assert loadButton != null;
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText adm = (EditText) findViewById(R.id.register_);
                String qu = "SELECT * FROM STUDENT WHERE regno = '" + adm.getText().toString().toUpperCase() + "'";
                ResultSet rs = AppBase.handler.execQuery(qu);
                try {
                    if (rs == null || ((ResultSet) rs).getFetchSize() == 0) {
                        Toast.makeText(getBaseContext(), "No Such Student", Toast.LENGTH_LONG).show();
                    } else {
                        rs.first();
                        try {
                            EditText name = (EditText) findViewById(R.id.edit_name_);
                            EditText roll = (EditText) findViewById(R.id.roll_);
                            EditText contact = (EditText) findViewById(R.id.contact_);
                            assert name != null;
                            name.setText(rs.getString(0));
                            assert roll != null;
                            roll.setText(rs.getString(4));
                            assert contact != null;
                            contact.setText(rs.getString(3));
                        } catch (Exception e) {
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


        Button saveEdit = (Button) findViewById(R.id.buttonSAVEEDITS);
        assert saveEdit != null;
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.edit_name_);
                EditText roll = (EditText) findViewById(R.id.roll_);
                EditText contact = (EditText) findViewById(R.id.contact_);
                EditText adm = (EditText) findViewById(R.id.register_);

                String qu = "UPDATE STUDENT SET name = '" + name.getText().toString() + "' , " +
                        " roll = " + roll.getText().toString() + " , contact = '" + contact.getText().toString() + "' " +
                        "WHERE regno = '" + adm.getText().toString().toUpperCase() + "'";
                Log.d("EditStudentActivity", qu);
                if (AppBase.handler.execAction(qu)) {
                    Toast.makeText(getBaseContext(), "Edit Saved", Toast.LENGTH_LONG).show();
                    activity.finish();

                } else
                    Toast.makeText(getBaseContext(), "Error Occured", Toast.LENGTH_LONG).show();

            }
        });
    }

}
