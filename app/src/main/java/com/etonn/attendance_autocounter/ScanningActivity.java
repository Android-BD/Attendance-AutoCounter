package com.etonn.attendance_autocounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.etonn.attendance_autocounter.bluetooth.BluetoothHelper;
import com.etonn.attendance_autocounter.db.DBManager;

import java.util.ArrayList;


public class ScanningActivity extends ActionBarActivity {

    DBManager db;
    BluetoothHelper bluetooth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        // init database
        db = new DBManager(ScanningActivity.this);

        // detect Bluetooth
        bluetooth = new BluetoothHelper();
        if (!bluetooth.detectBluetooth()) {
            // if user click button, close current activity
            new AlertDialog.Builder(this)
                    .setTitle("Message")
                    .setMessage("Your device does not support Bluetooth.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            ScanningActivity.this.finish();
                        }
                    })
                    .show();
        }

        // open Bluetooth
        if (!bluetooth.isBluetoothOpening()) {
            new AlertDialog.Builder(this)
                    .setTitle("Warning!")
                    .setMessage("Please turn on your Bluetooth.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            // open Bluetooth
                            bluetooth.openBluetooth();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            // return MainActivity
                            ScanningActivity.this.finish();
                        }
                    })
                    .show();
        }

        // scan Bluetooth device
        Toast.makeText(getApplicationContext(), "Scanning Students...", Toast.LENGTH_SHORT).show();
        ArrayList btlist = bluetooth.getBluetoothList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scanning, menu);
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

}
