package com.szmy.szmynews.model.bean;

import java.io.Serializable;

public class NewsBean implements Serializable {

    private int status;
    private String msg;
    private NewsResult result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NewsResult getResult() {
        return result;
    }

    public void setResult(NewsResult result) {
        this.result = result;
    }
}
