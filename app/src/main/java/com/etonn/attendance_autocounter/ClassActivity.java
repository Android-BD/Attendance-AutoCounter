package com.etonn.attendance_autocounter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.etonn.attendance_autocounter.db.DBManager;

import java.util.ArrayList;


public class ClassActivity extends ActionBarActivity {

    ListView lv; // for Class name listview
    DBManager dbManager;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // init database
        dbManager = new DBManager(ClassActivity.this);
        db = dbManager.getReadableDatabase();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addClass(View view) {
        // get Class name
        EditText editText = (EditText) findViewById(R.id.editTextClassName);
        String className = editText.getText().toString();
        if (className.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in a Class name.", Toast.LENGTH_SHORT).show();
        } else {
            // save to DB
            ContentValues values = new ContentValues();
            values.put("class_name", className);
            values.put("headcount", 20);
            Log.i("Log", "insert data");
            db.insert("classes", null, values);

            // get new data from db
            Cursor cursor = db.rawQuery("select * from classes order by classes_id DESC", null);
            ArrayList al = new ArrayList();
            while (cursor.moveToNext()) {
                //id = cursor.getString(cursor.getColumnIndex("id"));
                al.add(cursor.getString(cursor.getColumnIndex("class_name")));
            }

            // update list
            lv = (ListView)findViewById(R.id.listViewClasses);
            // bind listview with ArrayAdapter
            lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, al));

            // empty EditText
            editText.setText("");
        }

    }

}