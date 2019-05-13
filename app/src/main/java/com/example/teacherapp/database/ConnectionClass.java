package com.example.teacherapp.database;
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    String url = "jdbc:mysql://10.0.0.201:3306/attendance?useSSL=false";
    String user = "test";
    String pass = "test";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, pass);
        }

        catch (SQLException se)
            {
                Log.e("error here 1 : ", se.getMessage());
            }
        catch (ClassNotFoundException e)
            {
                Log.e("error here 2 : ", e.getMessage());
            }
        catch (Exception e)
            {
                Log.e("error here 3 : ", e.getMessage());
            }
        return connection;
    }
}
