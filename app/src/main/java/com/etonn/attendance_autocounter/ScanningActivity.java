package com.etonn.attendance_autocounter;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.etonn.attendance_autocounter.bluetooth.BluetoothHelper;
import com.etonn.attendance_autocounter.db.DBManager;

import java.util.ArrayList;


public class ScanningActivity extends ActionBarActivity {

    DBManager db;
    BluetoothHelper bluetooth = null;
    public static ArrayList studentsList = new ArrayList();

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
        bluetooth.startDiscovery();

        // register receiver for scanning
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter);
        // register receiver for finish scanning
        mFilter = new IntentFilter(bluetooth.mBluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver
        unregisterReceiver(mReceiver);
    }

    /**
     * BroadcastReceiver to receive bluetooth devices
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // scan device which has not bonded
                studentsList.add(device.getName());
                Log.i("Log",device.getName());
                Log.i("Log",device.getAddress());
                // update listview
                ListView lv = (ListView)findViewById(R.id.listViewStudents);
                // bind listview with ArrayAdapter
                lv.setAdapter(new ArrayAdapter<String>(ScanningActivity.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        studentsList));
                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }

        }
    };

}
