package com.wangcong.warehousefirewarning.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.wangcong.warehousefirewarning.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private Button settingBtn;
    private TextView tem_tv;
    private TextView hum_tv;
    private TextView smoke_tv;
    private TextView warnCount_tv;
    private ToggleButton connect_tb;
    private TextView info_tv;
    private ProgressBar progressBar;
    private LinearLayout bg_color;
    private Switch linkage_sw;
    private LineChart chart;
    private AlertDialog alertDialog;
    private Button recordBtn;

    EditText time_et;

    EditText temHumIp_et;
    EditText temHumPort_et;
    EditText smokeIp_et;
    EditText smokePort_et;
    EditText fanIp_et;
    EditText fanPort_et;

    EditText temMaxLim_et;
    EditText humMinLim_et;
    EditText smokeMaxLim_et;

    Button cancel;
    Button confirm;

    private ConnectTask connectTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // 绑定控件
        bindView();
        // 初始化数据
        initData();
        // 事件监听
        initEvent();
    }

    /**
     * 绑定控件
     */
    private void bindView() {
        settingBtn = (Button) findViewById(R.id.settingBtn);
        connect_tb = (ToggleButton) findViewById(R.id.connect_tb);
        info_tv = (TextView) findViewById(R.id.info_tv);
        smoke_tv = (TextView) findViewById(R.id.smoke_tv);
        tem_tv = (TextView) findViewById(R.id.tem_tv);
        hum_tv = (TextView) findViewById(R.id.hum_tv);
        smoke_tv = (TextView) findViewById(R.id.smoke_tv);
        warnCount_tv = (TextView) findViewById(R.id.warnCount);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bg_color = (LinearLayout) findViewById(R.id.bg_color);
        linkage_sw = (Switch) findViewById(R.id.linkage_sw);
        recordBtn = (Button) findViewById(R.id.show_records);
//        chart = (LineChart) findViewById(R.id.tem_chart);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        linkage_sw.setChecked(true);
    }

    /**
     * 按钮监听
     */
    private void initEvent() {
        recordBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        linkage_sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Const.linkage = true;
                } else {
                    Const.linkage = false;
                }

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
        });

        // 参数设置
        settingBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showSettingPopwindow();
            }
        });

        // 连接
        connect_tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bg_color.setBackgroundColor(Color.parseColor("#3cc391"));
                    // 进度条显示
                    progressBar.setVisibility(View.VISIBLE);
                    // 开启任务
                    connectTask = new ConnectTask(context, tem_tv, hum_tv, smoke_tv, warnCount_tv, info_tv, progressBar);
                    connectTask.setCIRCLE(true);
                    connectTask.execute();
                } else {
                    bg_color.setBackgroundColor(Color.parseColor("#3d89d5"));
                    // 取消任务
                    if (connectTask != null && connectTask.getStatus() == AsyncTask.Status.RUNNING) {
                        connectTask.setCIRCLE(false);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 如果Task还在运行，则先取消它
                        connectTask.cancel(true);
                        connectTask.closeSocket();
                    }
                    // 进度条消失
                    progressBar.setVisibility(View.GONE);
                    info_tv.setText("请点击连接！");
                    info_tv.setTextColor(context.getResources().getColor(R.color.gray));
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        // 取消任务
        if (connectTask != null && connectTask.getStatus() == AsyncTask.Status.RUNNING) {
            connectTask.setCIRCLE(false);
            // 如果Task还在运行，则先取消它
            connectTask.cancel(true);
            connectTask.closeSocket();
        }
    }

    /**
     * 显示popupWindow悬浮框
     */
    private void showSettingPopwindow() {
//        // 利用layoutInflater获得View
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view = inflater.inflate(R.layout.popwindow_setting, null);
//
//        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
//
//        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, 450);
//
//        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
//        window.setFocusable(true);
//
//        // 实例化一个ColorDrawable颜色为半透明0xb00000
//        ColorDrawable dw = new ColorDrawable(0xb00000);
//
//        window.setBackgroundDrawable(dw);
//
//        // 设置popWindow的显示和消失动画
//        window.setAnimationStyle(R.style.mypopwindow_anim_style);
//        // 在底部显示,第一个参数是parent，不是在其中显示，而是通过子控件找到主窗体，随便一个子控件都行
//        window.showAtLocation(MainActivity.this.findViewById(R.id.connect_tb), Gravity.BOTTOM, 0, 0);
        alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.NoBackGroundDialog).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.popwindow_setting, null);

        // 获取控件
        time_et = (EditText) view.findViewById(R.id.time_et);

        temHumIp_et = (EditText) view.findViewById(R.id.temHumIp_et);
        temHumPort_et = (EditText) view.findViewById(R.id.temHumPort_et);
        smokeIp_et = (EditText) view.findViewById(R.id.smokeIp_et);
        smokePort_et = (EditText) view.findViewById(R.id.smokePort_et);
        fanIp_et = (EditText) view.findViewById(R.id.fanIp_et);
        fanPort_et = (EditText) view.findViewById(R.id.fanPort_et);

        temMaxLim_et = (EditText) view.findViewById(R.id.temMaxLim_et);
        humMinLim_et = (EditText) view.findViewById(R.id.humMinLim_et);
        smokeMaxLim_et = (EditText) view.findViewById(R.id.smokeMaxLim_et);

        cancel = (Button) view.findViewById(R.id.cancelBtn);
        confirm = (Button) view.findViewById(R.id.confirmBtn);

        // 初始化界面
        smokeIp_et.setText(Const.SMOKE_IP);
        smokePort_et.setText(String.valueOf(Const.SMOKE_PORT));
        temHumIp_et.setText(Const.TEMHUM_IP);
        temHumPort_et.setText(String.valueOf(Const.TEMHUM_PORT));
        fanIp_et.setText(Const.FAN_IP);
        fanPort_et.setText(String.valueOf(Const.FAN_PORT));

        time_et.setText(String.valueOf(Const.time));
        temMaxLim_et.setText(String.valueOf(Const.temMaxLim));
        humMinLim_et.setText(String.valueOf(Const.humMinLim));
        smokeMaxLim_et.setText(String.valueOf(Const.smokeMaxLim));

        // 点击事件

        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // 确定

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 获取采集周期、IP、端口、上下限
                String SMOKE_IP = smokeIp_et.getText().toString().trim();
                String SMOKE_PORT = smokePort_et.getText().toString().trim();
                String TEMHUM_IP = temHumIp_et.getText().toString().trim();
                String TEMHUM_PORT = temHumPort_et.getText().toString().trim();
                String FAN_IP = fanIp_et.getText().toString().trim();
                String FAN_PORT = fanPort_et.getText().toString().trim();

                String time = time_et.getText().toString().trim();
                String temMaxLim = temMaxLim_et.getText().toString().trim();
                String humMinLim = humMinLim_et.getText().toString().trim();
                String smokeMaxLim = smokeMaxLim_et.getText().toString().trim();
                if (checkIpPort(SMOKE_IP, SMOKE_PORT) && checkIpPort(TEMHUM_IP, TEMHUM_PORT)
                        && checkIpPort(FAN_IP, FAN_PORT) && time != null && time != "" && temMaxLim != null
                        && temMaxLim != "" && humMinLim != null && humMinLim != "" && smokeMaxLim != null
                        && smokeMaxLim != "") {
                    Const.SMOKE_IP = SMOKE_IP;
                    Const.SMOKE_PORT = Integer.parseInt(SMOKE_PORT);
                    Const.TEMHUM_IP = TEMHUM_IP;
                    Const.TEMHUM_PORT = Integer.parseInt(TEMHUM_PORT);
                    Const.FAN_IP = FAN_IP;
                    Const.FAN_PORT = Integer.parseInt(FAN_PORT);

                    Const.time = Integer.parseInt(time);
                    Const.temMaxLim = Integer.parseInt(temMaxLim);
                    Const.humMinLim = Integer.parseInt(humMinLim);
                    Const.smokeMaxLim = Integer.parseInt(smokeMaxLim);
                } else {
                    Toast.makeText(context, "配置信息不正确,请重输！", Toast.LENGTH_SHORT).show();
                    return;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.setContentView(view);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    /**
     * IP地址可用端口号验证，可用端口号（1024-65536）
     *
     * @param IP
     * @param port
     * @return
     */
    private boolean checkIpPort(String IP, String port) {
        boolean isIpAddress = false;
        boolean isPort = false;

        if (IP == null || IP.length() < 7 || IP.length() > 15 || "".equals(IP) || port == null || port.length() < 4
                || port.length() > 5) {
            return false;
        }
        // 判断IP格式和范围
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(IP);

        isIpAddress = mat.find();

        // 判断端口
        int portInt = Integer.parseInt(port);
        if (portInt > 1024 && portInt < 65536) {
            isPort = true;
        }

        return (isIpAddress && isPort);
    }
}
