package com.wangcong.warehousefirewarning.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wangcong.warehousefirewarning.R;
import com.wangcong.warehousefirewarning.utils.MyDatabaseUtil;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private List<DataBean> recordList = new ArrayList<>();
    private MyDatabaseUtil database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initRecords();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);
    }

    private void initRecords() {
        this.database = new MyDatabaseUtil(getApplicationContext());
        List<DataBean> list = database.queryRecord();
        for (DataBean item : list) {
            recordList.add(item);
        }
        Log.i(Const.TAG, "data size:" + recordList.size());
    }
}
