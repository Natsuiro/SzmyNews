package com.szmy.szmynews.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.szmy.szmynews.app.SzmyApplication;
import com.szmy.szmynews.model.bean.ChannelBean;
import com.szmy.szmynews.model.bean.NewsBean;
import com.szmy.szmynews.model.bean.SearchBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;


public class SzmyModel {
    private static final String TAG = "SzmyModel";
    private static String FILEPATH = SzmyApplication.instance().getFilesDir().getAbsolutePath();
    private static SzmyModel instance;

    private SzmyModel() {
    }

    public static SzmyModel instance() {
        if (instance == null) {
            instance = new SzmyModel();
        }
        return instance;
    }

    /**
     * 从本地缓存或者网络获取新闻频道数组
     *
     * @param callBack 请求的回调接口
     */
    public void getNewsChannel(final SzmyCallBack<ChannelBean> callBack) {
        new Thread() {
            @Override
            public void run() {
                String key = "news_channel";
                ChannelBean channelBean;
                if (isNetworkConnected()) {
                    channelBean = loadNewsChannelFromServer();
                } else {
                    channelBean = loadNewsChannelFromLocal(key);
                }
                if (channelBean!=null){
                    saveData(key,channelBean);
                    callBack.onResponse(channelBean);
                }else{
                    callBack.onFailed("请求失败~");
                }
            }
        }.start();
    }

    /**
     * 根据关键词搜索新闻列表
     * @param keyWord 关键字
     * @param callBack 请求回调
     */
    public void searchWithKeyWord(final String keyWord, final SzmyCallBack<SearchBean> callBack){
        new Thread(){
            @Override
            public void run() {
                SearchBean searchBean = null;
                if (isNetworkConnected()){
                    //开始真正的逻辑
                    searchBean = searchWithKeyWordFromServer(keyWord);
                }else{
                    callBack.onFailed("网络断开~");
                }
                if (searchBean!=null){
                    callBack.onResponse(searchBean);
                }else{
                    callBack.onFailed("请求失败~");
                }
            }
        }.start();

    }

    private <T> T accessData(String urlPath, Class<T> klazz) throws IOException {
        URL url = new URL(urlPath);
        //得到connection对象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式：get
        conn.setRequestMethod("GET");
        //连接
        conn.connect();
        //响应码
        int code = conn.getResponseCode();
        //响应内容
        final String message = conn.getResponseMessage();
        if (code == HttpURLConnection.HTTP_OK) {
            //请求成功
            //解析字节流
            String json = streamToString(conn.getInputStream());
            Log.d(TAG, "run: " + json);
            Gson gson = new Gson();
            return gson.fromJson(json, klazz);
        } else {//请求失败，从本地加载
            return null;
        }
    }

    private SearchBean searchWithKeyWordFromServer(String keyWord) {
        try {
            String urlPath = App.getBaseUrl() + App.getSearch() + "?appkey=" + App.getAppKey()+
                    "&&keyword="+keyWord;
            return accessData(urlPath, SearchBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ChannelBean loadNewsChannelFromLocal(String key) {
        return readChannel(key);
    }

    private ChannelBean loadNewsChannelFromServer() {
        try {
            //构造url请求路径
            String urlPath = App.getBaseUrl() + App.getNewsChannel() + "?appkey=" + App.getAppKey();
            return accessData(urlPath, ChannelBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //从本地文件序列化对象
    private ChannelBean readChannel(String key) {

        Log.d(TAG, "readChannel: 网络断开，读取缓存");

        String fileName = key + ".data";
        FileInputStream fileInputStream;//打开文件输出流
        ObjectInputStream objectInputStream;//打开对象输出流
        File file = new File(FILEPATH, fileName);//新建文件
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                return (ChannelBean) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    //将输入流转换为json字符串
    private String streamToString(InputStream inputStream) throws IOException {
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        String str;
        StringBuilder sb = new StringBuilder();
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString() + "";
    }
    //获取指定新闻列表，并缓存
    public void loadNews(final String channel, final int start, final int num, final SzmyCallBack<NewsBean> callBack) {
        Log.d(TAG, "loadNews: " + start);
        new Thread() {
            @Override
            public void run() {
                String key = channel + "_news_" + start;
                NewsBean newsBean;
                if (isNetworkConnected()) {
                    newsBean = loadNewsFromServer(channel, start, num);
                } else {
                    newsBean = loadNewsFromLocal(key);
                }
                if (newsBean!=null){
                    callBack.onResponse(newsBean);
                }else {
                    callBack.onFailed("请求失败~");
                }
            }
        }.start();
    }

    private NewsBean loadNewsFromLocal(String key) {
        return readNewsData(key);
    }

    private NewsBean loadNewsFromServer(String channel, int start, int num) {
        try {
            //构造url请求路径
            String urlPath = App.getBaseUrl() + App.getNews() +
                    "?channel=" + channel +
                    "&start=" + start +
                    "&num=" + num +
                    "&appkey=" + App.getAppKey();
            return accessData(urlPath, NewsBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //从本地文件序列化对象
    private NewsBean readNewsData(String key) {
        String fileName = key + ".data";
        FileInputStream fileInputStream;//打开文件输出流
        ObjectInputStream objectInputStream;//打开对象输出流
        File file = new File(FILEPATH, fileName);//新建文件
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                return (NewsBean) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    //保存序列化对象到本地文件
    private <T extends Serializable> void saveData(String key, T newsDataBean) {
        String fileName = key + ".data";
        FileOutputStream fileOutputStream;//打开文件输出流
        ObjectOutputStream objectOutputStream;//打开对象输出流
        File file = new File(FILEPATH, fileName);//新建文件
        boolean isDeleteOrExist;
        if (file.exists()) {
            isDeleteOrExist = file.delete();
        } else {
            isDeleteOrExist = true;
        }
        if (isDeleteOrExist) {
            try {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    fileOutputStream = new FileOutputStream(file);//将新建的文件写入文件输出流
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);//向对象输出流写入文件输出流
                    objectOutputStream.writeObject(newsDataBean);//将序列化后的对象写入对象输出流
                    objectOutputStream.close();//关闭对象输出流
                    fileOutputStream.close();//关闭文件输出流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return 是否有网络连接
     */
    public boolean isNetworkConnected() {
        Context context = SzmyApplication.instance().getApplicationContext();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = null;
            if (mConnectivityManager != null) {
                mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            }
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    //请求回调
    public interface SzmyCallBack<T> {
        void onResponse(T body);
        void onFailed(String msg);
    }
}
