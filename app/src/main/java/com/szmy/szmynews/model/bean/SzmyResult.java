package com.szmy.szmynews.model.bean;

import java.util.List;

public class SzmyResult {

    private String channel;
    private int num;
    private List<SzmyNewsBean> list;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<SzmyNewsBean> getList() {
        return list;
    }

    public void setList(List<SzmyNewsBean> list) {
        this.list = list;
    }
}
