package com.szmy.szmynews.model.bean;

import java.io.Serializable;

public class SearchBean implements Serializable {
    private int status;
    private String msg;
    private SearchResult result;

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

    public SearchResult getResult() {
        return result;
    }

    public void setResult(SearchResult result) {
        this.result = result;
    }
}
