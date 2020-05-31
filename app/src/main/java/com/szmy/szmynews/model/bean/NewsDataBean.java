package com.szmy.szmynews.model.bean;

import java.io.Serializable;

public class NewsDataBean implements Serializable {

    private int status;
    private String msg;
    private SzmyResult result;

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

    public SzmyResult getResult() {
        return result;
    }

    public void setResult(SzmyResult result) {
        this.result = result;
    }
}
