package com.wangcong.warehousefirewarning.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wangcong.warehousefirewarning.R;
import com.wangcong.warehousefirewarning.utils.MyDatabaseUtil;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private List<DataBean> recordList = new ArrayList<>();
    private MyDatabaseUtil database;
    private RecordAdapter adapter;
    private SwipeRefreshLayout swipe_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initRecords();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecords();
            }
        });
    }

    private void initRecords() {
        this.database = new MyDatabaseUtil(getApplicationContext());
        List<DataBean> list = database.queryRecord();
        for (DataBean item : list) {
            recordList.add(item);
        }
//        Log.i(Const.TAG, "data size:" + recordList.size());
    }

    private void refreshRecords() {
        recordList.clear();
        List<DataBean> list = database.queryRecord();
        for (DataBean item : list) {
            recordList.add(item);
        }
        adapter.notifyDataSetChanged();
        swipe_refresh.setRefreshing(false);
    }
}
