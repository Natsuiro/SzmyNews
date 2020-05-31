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
import com.szmy.szmynews.model.bean.NewsDataBean;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Handler handler = new Handler(Looper.getMainLooper());

    public void runOnUiThread(Runnable r) {
        handler.post(r);
    }

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
                if (isNetworkConnected()) {
                    loadNewsChannelFromServer(key, callBack);
                } else {
                    loadNewsChannelFromLocal(key, callBack);
                }
            }
        }.start();
    }

    private void loadNewsChannelFromLocal(String key, final SzmyCallBack<ChannelBean> callBack) {
        final ChannelBean channelBean = readChannel(key);
        if (channelBean != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onResponse(channelBean);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onFailed("加载失败");
                }
            });
        }
    }

    private void loadNewsChannelFromServer(String key, final SzmyCallBack<ChannelBean> callBack) {
        try {
            //构造url请求路径
            String urlPath = App.getBaseUrl() + App.getNewsChannel() + "?appkey=" + App.getAppKey();
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
                final ChannelBean channelBean;
                channelBean = gson.fromJson(json, ChannelBean.class);
                int status = channelBean.getStatus();
                if (status == 0) {
                    //缓存数据
                    saveData(key, channelBean);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onResponse(channelBean);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(channelBean.getMsg());
                        }
                    });
                }
            } else {//请求失败，从本地加载
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(message);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void loadNews(final String channel, final int start, final int num, final SzmyCallBack<NewsDataBean> callBack) {
        Log.d(TAG, "loadNews: " + start);
        new Thread() {
            @Override
            public void run() {
                String key = channel + "_news_" + start;
                if (isNetworkConnected()) {
                    loadNewsFromServer(key, channel, start, num, callBack);
                } else {
                    loadNewsFromLocal(key, callBack);
                }

            }
        }.start();
    }

    private void loadNewsFromLocal(String key, final SzmyCallBack<NewsDataBean> callBack) {
        final NewsDataBean newsDataBean = readNewsData(key);
        if (newsDataBean != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onResponse(newsDataBean);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onFailed("加载失败");
                }
            });
        }
    }

    private void loadNewsFromServer(String key, String channel, int start, int num, final SzmyCallBack<NewsDataBean> callBack) {
        try {
            //构造url请求路径
            String urlPath = App.getBaseUrl() + App.getNews() +
                    "?channel=" + channel +
                    "&start=" + start +
                    "&num=" + num +
                    "&appkey=" + App.getAppKey();

            Log.d(TAG, "url: " + urlPath);

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
                Log.d(TAG, "loadNews: " + json);
                Gson gson = new Gson();
                final NewsDataBean newsDataBean;
                newsDataBean = gson.fromJson(json, NewsDataBean.class);
                int status = newsDataBean.getStatus();
                if (status == 0) {
                    //缓存数据
                    saveData(key, newsDataBean);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onResponse(newsDataBean);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(newsDataBean.getMsg());
                        }
                    });
                }
            } else {//请求失败，从本地加载

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(message);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从本地文件序列化对象
    private NewsDataBean readNewsData(String key) {
        String fileName = key + ".data";
        FileInputStream fileInputStream;//打开文件输出流
        ObjectInputStream objectInputStream;//打开对象输出流
        File file = new File(FILEPATH, fileName);//新建文件
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                return (NewsDataBean) objectInputStream.readObject();
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

    //判断是否有网络连接
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
