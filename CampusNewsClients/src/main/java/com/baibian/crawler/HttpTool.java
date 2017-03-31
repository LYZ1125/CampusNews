package com.baibian.crawler;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by XZY on 2016/11/8.
 */
public class HttpTool {

    public static String doGet(String urlStr) {
        URL url;
        String html = "";
        try {
            url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
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
