package com.example.teacherapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.teacherapp.R;
import com.example.teacherapp.database.DatabaseHandler;

public class CreateAcct extends AppCompatActivity {

    EditText nEmail;
    EditText firstname;
    EditText lastname;
    EditText nPassword;
    EditText cPassword;
    EditText id;
    Button Save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acct);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nEmail = (EditText)findViewById(R.id.new_email);
        firstname = (EditText)findViewById(R.id.fname);
        lastname = (EditText)findViewById(R.id.lname);
        nPassword = (EditText)findViewById(R.id.teachpass);
        cPassword = (EditText)findViewById(R.id.teachpassconf);

        Save = (Button)findViewById(R.id.buttonSAVE);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void OnSave(View view)
    {
        String email = nEmail.getText().toString();
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String password = nPassword.getText().toString();
        String cpassword = cPassword.getText().toString();
        String type = "createAccount";
        DatabaseHandler backgroundWorker = new DatabaseHandler(this);
        backgroundWorker.execute(type, email, fname, lname, password, cpassword);


    }
}
