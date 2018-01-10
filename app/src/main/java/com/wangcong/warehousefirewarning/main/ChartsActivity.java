package com.wangcong.warehousefirewarning.main;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.wangcong.warehousefirewarning.R;
import com.wangcong.warehousefirewarning.utils.MyDatabaseUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {
    private LineChart tem_chart;
    private LineChart hum_chart;
    private LineChart smoke_chart;
    private MyDatabaseUtil database;
    private final int data_count = 10;
    private final int time_to_sleep = 1000;

    //消息处理
    private final int UPDATE_MSG = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MSG:
                    drawCharts();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        // 绑定控件
        bindView();
        // 初始化数据
        initData();
        // 事件监听
        initEvent();
    }

    private void bindView() {
        tem_chart = (LineChart) findViewById(R.id.tem_chart);
        hum_chart = (LineChart) findViewById(R.id.hum_chart);
        smoke_chart = (LineChart) findViewById(R.id.smoke_chart);

    }

    private void initData() {
        Const.do_loop = true;
        database = new MyDatabaseUtil(this);
//                MyDatabaseUtil database = new MyDatabaseUtil(getApplicationContext());
//                final List<DataBean> datas = database.queryData(10);
//                List<Entry> entries = new ArrayList<Entry>();
//                int i = 0;
//                for (DataBean item : datas) {
//                    entries.add(new Entry(i++, item.getTem()));
//                }
//                LineDataSet dataSet = new LineDataSet(entries, "温度"); // add entries to dataset
//                LineData lineData = new LineData(dataSet);
//                final DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                IAxisValueFormatter formatter = new IAxisValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value, AxisBase axis) {
//                        Date date = new Date(Long.valueOf(datas.get((int) value).getTimestamp()));
//                        return sdf.format(date).toString();
//                    }
//                };
//                XAxis xAxis = chart.getXAxis();
//                xAxis.setValueFormatter(formatter);
//                xAxis.enableGridDashedLine(10f, 10f, 0f);
//                chart.getAxisRight().setEnabled(false);
//                chart.getDescription().setEnabled(false);
//                chart.setDragEnabled(false);
//                chart.setScaleEnabled(false);
//                chart.setData(lineData);
//                chart.postInvalidate(); // refresh
    }

    private void initEvent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (Const.do_loop) {
                    try {
                        Thread.sleep(time_to_sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = UPDATE_MSG;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void drawCharts() {
        final List<DataBean> datas = database.queryData(data_count);
        List<Entry> tem_entries = new ArrayList<Entry>();
        List<Entry> hum_entries = new ArrayList<Entry>();
        List<Entry> smoke_entries = new ArrayList<Entry>();
        int i = 0;
        for (DataBean item : datas) {
            tem_entries.add(new Entry(i, item.getTem()));
            hum_entries.add(new Entry(i, item.getHum()));
            smoke_entries.add(new Entry(i, item.getSmoke()));
            ++i;
        }
        LineDataSet tem_dataSet = new LineDataSet(tem_entries, "温度");
        LineDataSet hum_dataSet = new LineDataSet(hum_entries, "湿度");
        LineDataSet smoke_dataSet = new LineDataSet(smoke_entries, "烟雾");

        tem_dataSet.setColor(Color.parseColor("#f17c67"));
        tem_dataSet.setValueTextColor(Color.parseColor("#f17c67"));
        tem_dataSet.setCircleColor(Color.BLACK);
        tem_dataSet.setCircleRadius(3f);
        tem_dataSet.setDrawCircleHole(false);
        tem_dataSet.setDrawFilled(true);
        tem_dataSet.setFillColor(Color.parseColor("#f17c67"));
        tem_dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        tem_dataSet.setCubicIntensity(0.2f);

        hum_dataSet.setColor(Color.parseColor("#DB9019"));
        hum_dataSet.setValueTextColor(Color.parseColor("#DB9019"));
        hum_dataSet.setCircleColor(Color.BLACK);
        hum_dataSet.setCircleRadius(3f);
        hum_dataSet.setDrawCircleHole(false);
        hum_dataSet.setDrawFilled(true);
        hum_dataSet.setFillColor(Color.parseColor("#DB9019"));
        hum_dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        hum_dataSet.setCubicIntensity(0.2f);

        smoke_dataSet.setColor(Color.parseColor("#9966CC"));
        smoke_dataSet.setValueTextColor(Color.parseColor("#9966CC"));
        smoke_dataSet.setCircleColor(Color.BLACK);
        smoke_dataSet.setCircleRadius(3f);
        smoke_dataSet.setDrawCircleHole(false);
        smoke_dataSet.setDrawFilled(true);
        smoke_dataSet.setFillColor(Color.parseColor("#9966CC"));
        smoke_dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        smoke_dataSet.setCubicIntensity(0.2f);

        LineData tem_lineData = new LineData(tem_dataSet);
        LineData hum_lineData = new LineData(hum_dataSet);
        LineData smoke_lineData = new LineData(smoke_dataSet);

        final DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date date = new Date(Long.valueOf(datas.get((int) value).getTimestamp()));
                return sdf.format(date).toString();
            }
        };

        XAxis xAxis = tem_chart.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
//        YAxis yAxis = tem_chart.getAxisLeft();
//        yAxis.setAxisMinimum(0);
//        yAxis.setAxisMaximum(30);
        tem_chart.getAxisRight().setEnabled(false);
        tem_chart.getDescription().setEnabled(false);
        tem_chart.setDragEnabled(false);
        tem_chart.setScaleEnabled(false);
        tem_chart.setData(tem_lineData);
        tem_chart.invalidate(); // refresh

        xAxis = hum_chart.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
//        yAxis = hum_chart.getAxisLeft();
//        yAxis.setAxisMinimum(0);
//        yAxis.setAxisMaximum(20);
        hum_chart.getAxisRight().setEnabled(false);
        hum_chart.getDescription().setEnabled(false);
        hum_chart.setDragEnabled(false);
        hum_chart.setScaleEnabled(false);
        hum_chart.setData(hum_lineData);
        hum_chart.invalidate(); // refresh

        xAxis = smoke_chart.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
//        yAxis = smoke_chart.getAxisLeft();
//        yAxis.setAxisMinimum(0);
//        yAxis.setAxisMaximum(20);
        smoke_chart.getAxisRight().setEnabled(false);
        smoke_chart.getDescription().setEnabled(false);
        smoke_chart.setDragEnabled(false);
        smoke_chart.setScaleEnabled(false);
        smoke_chart.setData(smoke_lineData);
        smoke_chart.invalidate(); // refresh
    }

    @Override
    public void onBackPressed() {
        Const.do_loop = false;
        this.finish();
    }
}
