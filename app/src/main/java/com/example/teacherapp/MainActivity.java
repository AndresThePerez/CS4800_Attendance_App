
package com.example.teacherapp;

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

import com.example.teacherapp.database.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    Context context;
    private EditText Email;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int Counter = 5;
    Button CreateAcct;
    Toolbar toolbarz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



/*    toolbarz = (Toolbar)findViewById(R.id.toolbar1);
        toolbarz.setTitle("Login");*/


        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvinfo);
        Login = (Button)findViewById(R.id.btnLogin);
        CreateAcct = (Button) findViewById(R.id.createAcc);

        Info.setText("No of attempts remaining: 5");

        CreateAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CreateAcct.class);
                startActivity(intent);
            }
        });
    }
    public void OnLogin(View view)
    {
        String email = Email.getText().toString();
        String fname = "";
        String lname = "";
        String password = Password.getText().toString();
        String npassword = "";
        String type = "login";
        ProgressDialog pd = new ProgressDialog(this);
        DatabaseHandler backgroundWorker = new DatabaseHandler(this);
        backgroundWorker.execute(type, email, fname, lname, password, npassword);
    }

/*    public void validate(String auth)
    {
       if((userName.equals("Admin")) && (userPassword.equals("1234"))) {
           Intent intent = new Intent(MainActivity.this, AppBase.class);
           startActivity(intent);
       }
           else
           {
               Counter--;
               Info.setText("No of attempts remaining: " + Counter);
               if(Counter == 0)
               {
                   Login.setEnabled(false);
               }
           }
    }*/
}
