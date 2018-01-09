package com.wangcong.warehousefirewarning.main;

/**
 * Created by 13307 on 2018/1/9.
 */

public class DataBean {

    private Float tem;
    private Float hum;
    private Float smoke;
    private String timestamp;

    public DataBean() {
    }

    public DataBean(Float tem, Float hum, Float smoke, String timestamp) {
        this.tem = tem;
        this.hum = hum;
        this.smoke = smoke;
        this.timestamp = timestamp;
    }

    public DataBean(Float tem, Float hum, Float smoke) {
        this.tem = tem;
        this.hum = hum;
        this.smoke = smoke;
    }

    public Float getTem() {
        return tem;
    }

    public void setTem(Float tem) {
        this.tem = tem;
    }

    public Float getHum() {
        return hum;
    }

    public void setHum(Float hum) {
        this.hum = hum;
    }

    public Float getSmoke() {
        return smoke;
    }

    public void setSmoke(Float smoke) {
        this.smoke = smoke;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
