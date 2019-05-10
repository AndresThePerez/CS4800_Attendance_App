package com.example.teacherapp.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.teacherapp.AppBase;
import com.example.teacherapp.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler extends AsyncTask<String, String, String> {
    Activity activity;
    ProgressDialog pd;
    Connection con;
    private SQLiteDatabase database;
    AlertDialog alertDialog;
    String z="";
    ConnectionClass connectionClass;
    boolean isSuccess=false;


    public DatabaseHandler (Activity activity) {
        this.activity=activity;
    }


/*
    public DatabaseHandler(Activity activity) {
        this.activity = activity;
        database = activity.openOrCreateDatabase("ASSIST", activity.MODE_PRIVATE, null);
        createTable();
    }

    public void createTable() {
        try {
            String qu = "CREATE TABLE IF NOT EXISTS STUDENT(name varchar(1000)," +
                    "cl varchar(100), " +
                    "regno varchar(100) primary key, contact varchar(100),roll integer);";
            database.execSQL(qu);
        } catch (Exception e) {
            Toast.makeText(activity, "Error Occured for create table", Toast.LENGTH_LONG).show();
        }
        try {
            String qu = "CREATE TABLE IF NOT EXISTS ATTENDANCE(datex date," +
                    "hour int, " +
                    "register varchar(100) ,isPresent boolean);";
            database.execSQL(qu);
        } catch (Exception e) {
            Toast.makeText(activity, "Error Occured for create table", Toast.LENGTH_LONG).show();
        }

        try {
            String qu = "CREATE TABLE IF NOT EXISTS NOTES(title varchar(100) not null," +
                    "body varchar(10000), cls varchar(1000), sub varchar(1000) ,datex TIMESTAMP default CURRENT_TIMESTAMP);";
            database.execSQL(qu);
        } catch (Exception e) {
            Toast.makeText(activity, "Error Occured for create table", Toast.LENGTH_LONG).show();
        }

        try {
            String qu = "CREATE TABLE IF NOT EXISTS SCHEDULE(cl varchar(100),subject varchar(1000)," +
                    "timex time, day_week varchar(100));";
            database.execSQL(qu);
        } catch (Exception e) {
            Toast.makeText(activity, "Error Occured for create table", Toast.LENGTH_LONG).show();
        }
    }
*/
    public boolean execAction(String qu) {
        Log.i("DatabaseHandler", qu);
        try {
            ConnectionClass conn = new ConnectionClass();
            con = conn.CONN();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(qu);
        } catch (Exception e) {
            Log.e("DatabaseHandler", qu);
            Toast.makeText(activity, "Error Occured for execAction", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    public ResultSet execQuery(String qu) {
        try {
            ConnectionClass conStr = new ConnectionClass();
            con = conStr.CONN();
            Statement stmt = con.createStatement();
            ResultSet rs=stmt.executeQuery(qu);
            return rs;
        } catch (Exception e) {
            Log.e("DatabaseHandler", qu);
            Toast.makeText(activity,"Error Occurred for execQuery",Toast.LENGTH_LONG).show();
        }
        return null;
    }



    @Override
    protected String doInBackground(String... params) {
        //ProgressDialog progressDialog = new ProgressDialog();
        String type = params[0];
        String email = params[1];
        String fname = params[2];
        String lname = params[3];
        String password = params[4];
        String npassword = params[5];

        String tn, tp;
        if (type.equals("createAccount")){
            if(email.trim().equals("") || fname.trim().equals("") || lname.trim().equals("") || password.trim().equals("") || npassword.trim().equals(""))
            {
                z = "Please enter all fields.";
            }
            else    {
                if(!password.equals(npassword))
                {
                    z="The passwords do not match, please try again.";
                }
                else {


                    try {
                        ConnectionClass conn = new ConnectionClass();
                        con = conn.CONN();
                        if (con == null) {
                            z = "please check your internet connection.";
                        } else {

                            String query = " insert into teachers values ('1', '" + fname + "', '" + lname + "', '" + email + "', '" + password +"')";



                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);

                            isSuccess = true;
                            z = "Account Successfully Created.";


                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Exception: " + ex;
                    }

                }
            }


        }
        if(type.equals("login")) {
            if(email.trim().equals("")|| password.trim().equals(""))
                z = "Please enter all fields....";
            else
            {

                try {
                    ConnectionClass conStr = new ConnectionClass();
                    con = conStr.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                        isSuccess = false;
                    } else {

                        String query=" select * from teachers where teacherEmail='"+email+"' and password='"+password+"'";

                        Statement stmt = con.createStatement();

                        ResultSet rs=stmt.executeQuery(query);

                        while (rs.next())
                        {
                            tn = rs.getString(4);
                            tp = rs.getString(5);

                            if(tn.equals(email) && tp.equals(password))
                            {
                                isSuccess=true;
                                z="login successful";
                            }
                            else
                            {
                                isSuccess=false;
                                z="something's wrong";
                            }
                        }

                    }
                }

                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions: "+ex;
                }
            }

            return z;

        }
            /*
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/

        return null;
    }

    @Override
    protected void onPreExecute() {
        //alertDialog = new AlertDialog.Builder(activity).create();
        //alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(activity.getApplicationContext(),""+z,Toast.LENGTH_LONG).show();


        if(isSuccess) {
            Intent intent=new Intent(activity, AppBase.class);
            activity.startActivity(intent);
        }


    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}