package com.etonn.attendance_autocounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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

    /**
     * save data into database
     * @param students
     */
    public boolean putStudents(Map students) {
        Iterator iter = students.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object value = students.get(key);
            Log.i("Log:DBManager", "insert data");
            ContentValues mContentValues = new ContentValues();
            mContentValues.put("mac", value.toString());
            try {
                db.insert("students", null, mContentValues);
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
