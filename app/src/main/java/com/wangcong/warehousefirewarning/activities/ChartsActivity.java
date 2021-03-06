package com.wangcong.warehousefirewarning.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.wangcong.warehousefirewarning.R;
import com.wangcong.warehousefirewarning.beans.DataBean;
import com.wangcong.warehousefirewarning.utils.MyDatabaseUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {
    private LineChart tem_chart; // 温度图表
    private LineChart hum_chart; // 湿度图表
    private LineChart smoke_chart; // 烟雾值图表
    private MyDatabaseUtil database;


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
    }

    private void initEvent() {
        // 转跳到静态图表按钮
        Button static_chart_Btn = (Button) findViewById(R.id.static_chart_Btn);
        static_chart_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChartsActivity.this, StaticChartsActivity.class);
                startActivity(intent);
            }
        });
        setTimer();
    }

    private void setTimer() {
        // 启动定时器，每隔一定时间通知图表进行更新
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (Const.do_loop) {
                    try {
                        Thread.sleep(Const.time);
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

    /**
     * 绘制图表
     */
    private void drawCharts() {
        final List<DataBean> datas = database.queryData(Const.data_count);
        if (datas.size() > 0) {

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
    }

    @Override
    public void onBackPressed() {
        // 返回后取消定时器循环
        Const.do_loop = false;
        this.finish();
    }
}
