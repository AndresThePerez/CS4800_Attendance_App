package com.example.teacherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SecondActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("Your Classes");
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.listView);

  /*      ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(SecondActivity.this,
                android.R.layout.simple_list_item_1,
                //getResources().getStringArray(R.array.Classes)
                );
*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                Intent intent2 = new Intent(SecondActivity.this, StudentList.class);
                intent2.putExtra("Student", listView.getItemAtPosition(i).toString());
                startActivity(intent2);

            }
        });
       // listView.setAdapter(mAdapter);
    }
}
