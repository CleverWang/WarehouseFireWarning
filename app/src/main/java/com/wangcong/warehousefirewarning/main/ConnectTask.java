package com.wangcong.warehousefirewarning.main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wangcong.warehousefirewarning.R;
import com.wangcong.warehousefirewarning.utils.FROSmoke;
import com.wangcong.warehousefirewarning.utils.FROTemHum;
import com.wangcong.warehousefirewarning.utils.MyDatabaseUtil;
import com.wangcong.warehousefirewarning.utils.StreamUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ConnectTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    TextView tem_tv;
    TextView hum_tv;
    TextView smoke_tv;
    TextView warnCount_tv;
    TextView info_tv;
    ProgressBar progressBar;
//    LinearLayout bg_color;

    private Float tem;
    private Float hum;
    private Float smoke;
    private byte[] read_buff;

    private Socket smokeSocket;
    private Socket temHumSocket;
    private Socket fanSocket;
    private Socket buzzerSocket;

    private boolean CIRCLE = false;

    private MyDatabaseUtil database;

//    private boolean isDialogShow = false;

    public ConnectTask(Context context, TextView tem_tv, TextView hum_tv, TextView smoke_tv, TextView warnCount_tv, TextView info_tv,
                       ProgressBar progressBar) {
        this.context = context;
        this.tem_tv = tem_tv;
        this.hum_tv = hum_tv;
        this.smoke_tv = smoke_tv;
        this.warnCount_tv = warnCount_tv;
        this.info_tv = info_tv;
        this.progressBar = progressBar;
//        this.bg_color = bg_color;
        this.database = new MyDatabaseUtil(context);
    }

    /**
     * 更新界面
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        if (smokeSocket != null && temHumSocket != null && fanSocket != null && buzzerSocket != null) {
            // if (smokeSocket != null ) {
            info_tv.setTextColor(context.getResources().getColor(R.color.green));
            info_tv.setText("连接正常！");
//            Toast.makeText(context, "连接正常！", Toast.LENGTH_SHORT).show();
        } else {
            info_tv.setTextColor(context.getResources().getColor(R.color.red));
            info_tv.setText("连接失败！");
//            Toast.makeText(context, "连接失败！", Toast.LENGTH_SHORT).show();
        }

        // 进度条消失
        progressBar.setVisibility(View.GONE);

        // 显示数据
        if (Const.tem != null) {
            tem_tv.setText(String.valueOf(Const.tem));
        }
        if (Const.hum != null) {
            hum_tv.setText(String.valueOf(Const.hum));
        }
        if (Const.smoke != null) {
            smoke_tv.setText(String.valueOf(Const.smoke));
        }

        // 预警次数
        warnCount_tv.setText(String.valueOf(Const.warnCount));
    }

    /**
     * 准备
     */
    @Override
    protected void onPreExecute() {
        info_tv.setText("正在连接...");
//        Toast.makeText(context, "连接失败！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 子线程任务
     *
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {
        // 连接
        smokeSocket = getSocket(Const.SMOKE_IP, Const.SMOKE_PORT);
        temHumSocket = getSocket(Const.TEMHUM_IP, Const.TEMHUM_PORT);
        fanSocket = getSocket(Const.FAN_IP, Const.FAN_PORT);
        buzzerSocket = getSocket(Const.BUZZER_IP, Const.BUZZER_PORT);
        // 循环读取数据
        while (CIRCLE) {
            try {
                // 如果全部连接成功
                // if (smokeSocket != null ) {
                if (smokeSocket != null && temHumSocket != null && fanSocket != null && buzzerSocket != null) {

                    // 查询温湿度值
                    StreamUtil.writeCommand(temHumSocket.getOutputStream(), Const.TEMHUM_CHK);
                    Thread.sleep(Const.time / 2);
                    read_buff = StreamUtil.readData(temHumSocket.getInputStream());
                    tem = FROTemHum.getTemData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    if (tem != null) {
                        Const.tem = (int) (float) tem;
                    }
                    hum = FROTemHum.getHumData(Const.TEMHUM_LEN, Const.TEMHUM_NUM, read_buff);
                    if (hum != null) {
                        Const.hum = (int) (float) hum;
                    }

                    // 查询烟雾值
                    StreamUtil.writeCommand(smokeSocket.getOutputStream(), Const.SMOKE_CHK);
                    Thread.sleep(Const.time / 2);
                    read_buff = StreamUtil.readData(smokeSocket.getInputStream());
                    smoke = FROSmoke.getData(Const.SMOKE_LEN, Const.SMOKE_NUM, read_buff);
                    if (smoke != null) {
                        Const.smoke = (int) (float) smoke;
                    }

                    // 如果联动打开状态并且温度>上限,湿度<下限,烟雾>上限,打开风机,蜂鸣器报警1s
                    Log.i(Const.TAG, "Const.linkage=" + Const.linkage);
                    Log.i(Const.TAG, "Const.tem=" + Const.tem);
                    Log.i(Const.TAG, "Const.temMaxLim=" + Const.temMaxLim);
                    Log.i(Const.TAG, "Const.hum=" + Const.hum);
                    Log.i(Const.TAG, "Const.humMinLim=" + Const.humMinLim);
                    Log.i(Const.TAG, "Const.smoke=" + Const.smoke);
                    Log.i(Const.TAG, "Const.smokeMaxLim=" + Const.smokeMaxLim);
                    if (Const.linkage && Const.tem > Const.temMaxLim && Const.hum < Const.humMinLim
                            && Const.smoke > Const.smokeMaxLim) {
//                        bg_color.setBackgroundColor(Color.parseColor("#6050b1"));
                        // 预警次数加1
                        Const.warnCount++;
                        // 风扇
                        if (!Const.isFanOn) {
                            Const.isFanOn = true;
                            StreamUtil.writeCommand(fanSocket.getOutputStream(), Const.FAN_ON);
                            Thread.sleep(200);
                        }
                        // 蜂鸣器
                        if (!Const.isBuzzerOn) {
                            StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_ON);
                            Thread.sleep(1000);
                            StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
                            Thread.sleep(200);
                        }
                    } else {

                        if (Const.isFanOn) {
                            Const.isFanOn = false;
                            StreamUtil.writeCommand(fanSocket.getOutputStream(), Const.FAN_OFF);
                            Thread.sleep(200);
                        }
                    }
                }

                // 保存数据
                if (tem != null && hum != null && smoke != null) {
                    database.insertData(tem, hum, smoke);
                }

                // 更新界面
                publishProgress();
                Thread.sleep(200);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 最后关闭蜂鸣器，关闭风扇
        try {
            if (fanSocket != null) {
                Const.isFanOn = false;
                StreamUtil.writeCommand(fanSocket.getOutputStream(), Const.FAN_OFF);
                Thread.sleep(200);
            }
            if (buzzerSocket != null) {
                Const.isBuzzerOn = false;
                StreamUtil.writeCommand(buzzerSocket.getOutputStream(), Const.BUZZER_OFF);
                Thread.sleep(200);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
        }
        return null;
    }

    /**
     * 建立连接并返回socket，若连接失败返回null
     *
     * @param ip
     * @param port
     * @return
     */
    private Socket getSocket(String ip, int port) {
        Socket mSocket = new Socket();
        InetSocketAddress mSocketAddress = new InetSocketAddress(ip, port);
        // socket连接
        try {
            // 设置连接超时时间为3秒
            mSocket.connect(mSocketAddress, 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 检查是否连接成功
        if (mSocket.isConnected()) {
            Log.i(Const.TAG, ip + "连接成功！");
            return mSocket;
        } else {
            Log.i(Const.TAG, ip + "连接失败！");
            return null;
        }
    }

    public void setCIRCLE(boolean cIRCLE) {
        CIRCLE = cIRCLE;
    }

    @Override
    protected void onCancelled() {
        info_tv.setTextColor(context.getResources().getColor(R.color.gray));
        info_tv.setText("请点击连接！");
//        Toast.makeText(context, "请点击连接！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 关闭socket
     */
    void closeSocket() {
        try {
            if (smokeSocket != null) {
                smokeSocket.close();
            }
            if (temHumSocket != null) {
                temHumSocket.close();
            }
            if (fanSocket != null) {
                fanSocket.close();
            }
            if (buzzerSocket != null) {
                buzzerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
