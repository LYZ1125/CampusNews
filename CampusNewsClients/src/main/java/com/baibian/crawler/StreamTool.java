package com.baibian.crawler;

import java.io.InputStream;

/**
 * Created by XZY on 2016/11/8.
 */
public class StreamTool {
    public static String inToStringByByte(InputStream in)throws Exception{
//        ByteArrayOutputStream outStr = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        StringBuilder content = new StringBuilder();
        while ((len = in.read(buffer)) != -1) {
            content.append(new String(buffer,0, len, "UTF-8"));
        }
//        outStr.close();
        in.close();
        return content.toString();
    }
}
