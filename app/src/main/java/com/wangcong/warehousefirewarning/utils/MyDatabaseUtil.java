package com.wangcong.warehousefirewarning.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wangcong.warehousefirewarning.beans.DataBean;

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

    /**
     * 向data表中插入一条数据，用于实时动态图表展示
     *
     * @param tem   温度
     * @param hum   湿度
     * @param smoke 烟雾值
     */
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

    /**
     * 向record表中插入一条预警记录，用于预警历史记录查询
     *
     * @param tem   温度
     * @param hum   湿度
     * @param smoke 烟雾值
     */
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

    /**
     * 从data表中取指定条数的最近插入的数据
     *
     * @param count 需要取出的数据的条数
     * @return 取出的数据组成的列表
     */
    public List<DataBean> queryData(int count) {
        List<DataBean> result = new ArrayList<>();
        // 查询表中所有的数据
//        Cursor cursor = db.query("data", null, null, null, null, null, null);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from data order by id desc limit ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(count)});
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                float tem = cursor.getFloat(cursor.getColumnIndex("tem"));
                float hum = cursor.getFloat(cursor.getColumnIndex("hum"));
                float smoke = cursor.getFloat(cursor.getColumnIndex("smoke"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                result.add(new DataBean(tem, hum, smoke, timestamp));
//                Log.d("data", "tem" + tem);
//                Log.d("data", "hum " + hum);
//                Log.d("data", "smoke " + smoke);
//                Log.d("data", "timestamp " + timestamp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 从data表中取所有插入的数据
     *
     * @return 取出的数据组成的列表
     */
    public List<DataBean> queryData() {
        List<DataBean> result = new ArrayList<>();
        // 查询表中所有的数据
//        Cursor cursor = db.query("data", null, null, null, null, null, null);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("data", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                float tem = cursor.getFloat(cursor.getColumnIndex("tem"));
                float hum = cursor.getFloat(cursor.getColumnIndex("hum"));
                float smoke = cursor.getFloat(cursor.getColumnIndex("smoke"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                result.add(new DataBean(tem, hum, smoke, timestamp));
//                Log.d("data", "tem" + tem);
//                Log.d("data", "hum " + hum);
//                Log.d("data", "smoke " + smoke);
//                Log.d("data", "timestamp " + timestamp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 从record表中取出所有预警历史记录
     *
     * @return 所有预警历史记录组成的列表
     */
    public List<DataBean> queryRecord() {
        List<DataBean> result = new ArrayList<>();
        // 查询表中所有的数据
//        Cursor cursor = db.query("data", null, null, null, null, null, null);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("record", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                float tem = cursor.getFloat(cursor.getColumnIndex("tem"));
                float hum = cursor.getFloat(cursor.getColumnIndex("hum"));
                float smoke = cursor.getFloat(cursor.getColumnIndex("smoke"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
//                Log.d("record", "tem" + tem);
//                Log.d("record", "hum " + hum);
//                Log.d("record", "smoke " + smoke);
//                Log.d("record", "timestamp " + timestamp);
                result.add(new DataBean(tem, hum, smoke, timestamp));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
}
