package com.wangcong.warehousefirewarning.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

public class StaticChartsActivity extends AppCompatActivity {

    private LineChart tem_static_chart; // 温度图表
    private LineChart hum_static_chart; // 湿度图表
    private LineChart smoke_static_chart; // 烟雾值图表
    private MyDatabaseUtil database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_charts);

        // 绑定控件
        bindView();
        // 初始化数据
        initData();
        // 事件监听
        initEvent();
    }

    private void bindView() {
        tem_static_chart = (LineChart) findViewById(R.id.tem_static_chart);
        hum_static_chart = (LineChart) findViewById(R.id.hum_static_chart);
        smoke_static_chart = (LineChart) findViewById(R.id.smoke_static_chart);

    }

    private void initData() {
        database = new MyDatabaseUtil(this);
    }

    private void initEvent() {
        drawCharts();
    }

    /**
     * 绘制图表
     */
    private void drawCharts() {
        final List<DataBean> datas = database.queryData();
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
            tem_dataSet.setDrawCircles(false);
//            tem_dataSet.setCircleColor(Color.BLACK);
//            tem_dataSet.setCircleRadius(3f);
//            tem_dataSet.setDrawCircleHole(false);
            tem_dataSet.setDrawFilled(true);
            tem_dataSet.setFillColor(Color.parseColor("#f17c67"));
            tem_dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            tem_dataSet.setCubicIntensity(0.2f);

            hum_dataSet.setColor(Color.parseColor("#DB9019"));
            hum_dataSet.setValueTextColor(Color.parseColor("#DB9019"));
            hum_dataSet.setDrawCircles(false);
//            hum_dataSet.setCircleColor(Color.BLACK);
//            hum_dataSet.setCircleRadius(3f);
//            hum_dataSet.setDrawCircleHole(false);
            hum_dataSet.setDrawFilled(true);
            hum_dataSet.setFillColor(Color.parseColor("#DB9019"));
            hum_dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            hum_dataSet.setCubicIntensity(0.2f);

            smoke_dataSet.setColor(Color.parseColor("#9966CC"));
            smoke_dataSet.setValueTextColor(Color.parseColor("#9966CC"));
            smoke_dataSet.setDrawCircles(false);
//            smoke_dataSet.setCircleColor(Color.BLACK);
//            smoke_dataSet.setCircleRadius(3f);
//            smoke_dataSet.setDrawCircleHole(false);
            smoke_dataSet.setDrawFilled(true);
            smoke_dataSet.setFillColor(Color.parseColor("#9966CC"));
            smoke_dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            smoke_dataSet.setCubicIntensity(0.2f);

            LineData tem_lineData = new LineData(tem_dataSet);
            LineData hum_lineData = new LineData(hum_dataSet);
            LineData smoke_lineData = new LineData(smoke_dataSet);

            final DateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    Date date = new Date(Long.valueOf(datas.get((int) value).getTimestamp()));
                    return sdf.format(date).toString();
                }
            };

            XAxis xAxis = tem_static_chart.getXAxis();
            xAxis.setValueFormatter(formatter);
            xAxis.enableGridDashedLine(10f, 10f, 0f);
//        YAxis yAxis = tem_static_chart.getAxisLeft();
//        yAxis.setAxisMinimum(0);
//        yAxis.setAxisMaximum(30);
            tem_static_chart.getAxisRight().setEnabled(false);
            tem_static_chart.getDescription().setEnabled(false);
//            tem_static_chart.setDragEnabled(false);
//            tem_static_chart.setScaleEnabled(false);
            tem_static_chart.setData(tem_lineData);
            tem_static_chart.invalidate(); // refresh

            xAxis = hum_static_chart.getXAxis();
            xAxis.setValueFormatter(formatter);
            xAxis.enableGridDashedLine(10f, 10f, 0f);
//        yAxis = hum_static_chart.getAxisLeft();
//        yAxis.setAxisMinimum(0);
//        yAxis.setAxisMaximum(20);
            hum_static_chart.getAxisRight().setEnabled(false);
            hum_static_chart.getDescription().setEnabled(false);
//            hum_static_chart.setDragEnabled(false);
//            hum_static_chart.setScaleEnabled(false);
            hum_static_chart.setData(hum_lineData);
            hum_static_chart.invalidate(); // refresh

            xAxis = smoke_static_chart.getXAxis();
            xAxis.setValueFormatter(formatter);
            xAxis.enableGridDashedLine(10f, 10f, 0f);
//        yAxis = smoke_static_chart.getAxisLeft();
//        yAxis.setAxisMinimum(0);
//        yAxis.setAxisMaximum(20);
            smoke_static_chart.getAxisRight().setEnabled(false);
            smoke_static_chart.getDescription().setEnabled(false);
//            smoke_static_chart.setDragEnabled(false);
//            smoke_static_chart.setScaleEnabled(false);
            smoke_static_chart.setData(smoke_lineData);
            smoke_static_chart.invalidate(); // refresh
        }
    }
}
