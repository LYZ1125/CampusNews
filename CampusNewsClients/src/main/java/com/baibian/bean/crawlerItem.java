package com.baibian.bean;

/**
 * Created by XZY on 2016/11/8.
 */

public class crawlerItem {

    private String title;
    private String publishData;
    private String source;
    private int readTimes;
    private String body;
    private String[] imageUrls;
    private int index;

    public crawlerItem(int index, String[] imageUrls, String title, String publishData,
                       String source, int readTimes, String body) {
        this.index = index;
        this.imageUrls = imageUrls;
        this.title = title;
        this.publishData = publishData;
        this.source = source;
        this.readTimes = readTimes;
        this.body = body;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
