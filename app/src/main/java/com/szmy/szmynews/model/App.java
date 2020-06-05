package com.szmy.szmynews.model;

public class App {

    private App(){}

    private static final String appKey = "59329b08598d54e3";
    private static final String baseUrl = "https://api.jisuapi.com/news/";
    private static final String newsChannel = "channel";
    private static final String news = "get";
    private static final String search = "search";
    public static String getAppKey() {
        return appKey;
    }
    public static String getBaseUrl() {
        return baseUrl;
    }
    public static String getNewsChannel() {
        return newsChannel;
    }
    public static String getNews() {
        return news;
    }

    public static String getSearch() {
        return search;
    }
}
