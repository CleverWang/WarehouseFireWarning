package com.wangcong.warehousefirewarning.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_HISTORY = "create table data ("
            + "id integer primary key autoincrement, "
            + "tem real, "
            + "hum real, "
            + "smoke real, "
            + "timestamp char(10))"; // 创建保存每条传过来的数据的表的SQL语句
    public static final String CREATE_RECORD = "create table record ("
            + "id integer primary key autoincrement, "
            + "tem real, "
            + "hum real, "
            + "smoke real, "
            + "timestamp char(10))"; // 创建保存预警信息的表的SQL语句
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY);
        db.execSQL(CREATE_RECORD);
        //Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists data");
        db.execSQL("drop table if exists record");
        onCreate(db);
    }
}
