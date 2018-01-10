package com.wangcong.warehousefirewarning.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wangcong.warehousefirewarning.main.DataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13307 on 2018/1/9.
 */

public class MyDatabaseUtil {
    private Context context;
    private MyDatabaseHelper dbHelper;

    public MyDatabaseUtil(Context context) {
        this.context = context;
        dbHelper = new MyDatabaseHelper(this.context, "firedata.db", null, 1);
    }

    public void insertData(float tem, float hum, float smoke) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tem", tem);
        values.put("hum", hum);
        values.put("smoke", smoke);
        values.put("timestamp", String.valueOf(System.currentTimeMillis()));
        db.insert("data", null, values);
        db.close();
    }

    public void insertRecord(float tem, float hum, float smoke) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tem", tem);
        values.put("hum", hum);
        values.put("smoke", smoke);
        values.put("timestamp", String.valueOf(System.currentTimeMillis()));
        db.insert("record", null, values);
        db.close();
    }

    public List<DataBean> queryData(int count) {
        List<DataBean> result = new ArrayList<>();
        // 查询表中所有的数据
//        Cursor cursor = db.query("data", null, null, null, null, null, null);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from data order by id desc limit ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(count)});
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                float tem = cursor.getFloat(cursor.getColumnIndex("tem"));
                float hum = cursor.getFloat(cursor.getColumnIndex("hum"));
                float smoke = cursor.getFloat(cursor.getColumnIndex("smoke"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                Log.d("data", "tem" + tem);
                Log.d("data", "hum " + hum);
                Log.d("data", "smoke " + smoke);
                Log.d("data", "timestamp " + timestamp);
                result.add(new DataBean(tem, hum, smoke, timestamp));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<DataBean> queryRecord() {
        List<DataBean> result = new ArrayList<>();
        // 查询表中所有的数据
//        Cursor cursor = db.query("data", null, null, null, null, null, null);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("record", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                float tem = cursor.getFloat(cursor.getColumnIndex("tem"));
                float hum = cursor.getFloat(cursor.getColumnIndex("hum"));
                float smoke = cursor.getFloat(cursor.getColumnIndex("smoke"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                Log.d("record", "tem" + tem);
                Log.d("record", "hum " + hum);
                Log.d("record", "smoke " + smoke);
                Log.d("record", "timestamp " + timestamp);
                result.add(new DataBean(tem, hum, smoke, timestamp));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
}
