package com.baibian.tool;

import android.content.Context;
import android.media.session.MediaSession;
import android.util.Log;

import com.baibian.crawler.StreamTool;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by XZY on 2016/11/8.
 */
public class HttpTool {

    private static final String BASE_URL = "http://112.74.107.202";

    public static String doGetOkHttp(String urlStr, String method, String path) {

        final String[] htmlStr = {"error"};
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(BASE_URL + path)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                htmlStr[0] = response.body().string();
            }
        });
        return htmlStr[0];
    }

    public static Response doGetOkHttpResponse( String path) {
        Response getedresponse=null;
        final Response[] htmlRes = new Response[1];
        final String[] htmlStr = {"error"};
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(BASE_URL + path).get()
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        try {
            getedresponse=call.execute();
        }catch (IOException e ){
            e.printStackTrace();
        }
        if (getedresponse==null)Log.d("错误问题","11111");
        return getedresponse;
    }
    public static Response doPutOkHttpResponse(String path, String json, String auth_token) {
        Response getedresponse=null;
        final String[] htmlStr = {"error"};
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String auth_token2= "Token token="+auth_token+"";
        //创建一个Request.
        Log.d("权限",auth_token);
        final Request request = new Request.Builder().addHeader("Authorization",auth_token2)
                .url(BASE_URL + path).put(RequestBody.create(JSON, json))
                .build();


//        request.header("Authorization")="\"Token token="+auth_token+"\"";


        //new call
        Call call = mOkHttpClient.newCall(request);
        try {
            getedresponse=call.execute();
            System.out.println("response" + getedresponse);
        }catch (IOException e ){
            e.printStackTrace();
        }
        if (getedresponse==null)Log.d("错误问题","11111");
        Log.d(" ",getedresponse.toString());
        return getedresponse;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String getSignUpJson(String username, String password) {
        return "{ \"user\": {\"mobile\": \"" + username + "\",\"password\": \"" + password + "\"}}";
    }

    public static String getSignInJson(String username, String password) {
        return "{ \"session\": {\"mobile\": \"" + username + "\",\"password\": \"" + password + "\"}}";
    }


    public static Response post(String json, String path) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .post(RequestBody.create(JSON, json))
                .build();
        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
            System.out.println("response" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String doGet(String urlStr, String method, String path) {
        URL url;
        urlStr = BASE_URL;
        String html = "";
        try {
            url = new URL(urlStr + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(2000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                html = StreamTool.inToStringByByte(in);
            } else {
                System.out.println("返回值不为200");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GET is false");
        }
        return html;
    }
}