package com.wangcong.warehousefirewarning.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.wangcong.warehousefirewarning.R;
import com.wangcong.warehousefirewarning.beans.DataBean;
import com.wangcong.warehousefirewarning.adapters.RecordAdapter;
import com.wangcong.warehousefirewarning.utils.MyDatabaseUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private List<DataBean> recordList = new ArrayList<>();
    private MyDatabaseUtil database;
    private RecordAdapter adapter;
    private SwipeRefreshLayout swipe_refresh;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // 初始化数据库工具
        this.database = new MyDatabaseUtil(getApplicationContext());

        // 读取初始的所有数据
        initRecords();

        // 列表控件
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);

        // 下拉刷新控件
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecords();
            }
        });

        // 按日期搜索控件
        Button search_by_date_Btn = (Button) findViewById(R.id.search_by_date_Btn);
        search_by_date_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
    }


    private void initRecords() {
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

    private void pickDate() {
        alertDialog = new AlertDialog.Builder(RecordActivity.this, R.style.NoBackGroundDialog).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        View view = LayoutInflater.from(RecordActivity.this).inflate(R.layout.date_pick_view, null);

        // 获取日期提取器
        final DatePicker date_picker = (DatePicker) view.findViewById(R.id.date_picker);

        // 获取取消按钮
        Button date_cancel = (Button) view.findViewById(R.id.date_cancel);
        date_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        // 获取确定按钮
        Button date_confirm = (Button) view.findViewById(R.id.date_confirm);
        date_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int y = date_picker.getYear();
                int m = date_picker.getMonth() + 1;
                int d = date_picker.getDayOfMonth();
                refreshRecordsByDate(y, m, d);
                alertDialog.dismiss();
            }
        });

        alertDialog.setContentView(view);
    }

    private void refreshRecordsByDate(int y, int m, int d) {
        long start_time = (new Date(y, m, d, 0, 0, 0)).getTime();
        long end_time = (new Date(y, m, d, 23, 59, 59)).getTime();
        recordList.clear();
        List<DataBean> list = database.queryRecord();
        for (DataBean item : list) {
            long time = Long.parseLong(item.getTimestamp());
            if (start_time <= time && end_time >= time) {
                recordList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
        if (recordList.size() == 0) {
            Toast.makeText(this, "未找到数据！", Toast.LENGTH_SHORT).show();
        }
    }
}
