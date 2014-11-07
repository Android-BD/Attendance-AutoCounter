package com.etonn.attendance_autocounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Cheng on 2014/11/6.
 */
public class DBManager extends DBHelper {

    SQLiteDatabase db;

    public DBManager(Context context) {
        super(context);
        db = this.getReadableDatabase();
    }

    public ArrayList getClassList() {
        String sql = "select * from classes order by classes_id DESC";
        Log.i("Log:DBManager", sql);
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList al = new ArrayList();
        while (cursor.moveToNext()) {
            //id = cursor.getString(cursor.getColumnIndex("id"));
            al.add(
                    cursor.getString(cursor.getColumnIndex("class_name")) +
                            " (" + cursor.getString(cursor.getColumnIndex("headcount"))  + ")"
            );
        }
        return al;
    }

    public void putClass(ContentValues contentValues) {
        Log.i("Log:DBManager", "insert data");
        try {
            db.insert("classes", null, contentValues);
        } catch (android.database.SQLException e) {
            e.printStackTrace();
        }
    }

}
