package com.szmy.szmynews.model;

public class App {

    private App(){}

    private static final String appKey = "59329b08598d54e3";
    private static final String baseUrl = "https://api.jisuapi.com/news/";
    public static String getAppKey() {
        return appKey;
    }
    public static String getBaseUrl() {
        return baseUrl;
    }

}
