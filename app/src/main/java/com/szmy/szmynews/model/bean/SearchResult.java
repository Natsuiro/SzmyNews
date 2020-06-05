package com.szmy.szmynews.model.bean;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {

    private String keyword;
    private int num;
    private List<SearchData> list;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<SearchData> getList() {
        return list;
    }

    public void setList(List<SearchData> list) {
        this.list = list;
    }
}
