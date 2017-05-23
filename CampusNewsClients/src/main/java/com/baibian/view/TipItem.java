package com.baibian.view;

/**
 * Created by 邹特强 on 2017/5/21.
 */
import android.graphics.Color;

public class TipItem {

    private String title;

    private int textColor = Color.WHITE;
    public TipItem(String title) {
        this.title = title;
    }

    public TipItem(String title,  int textColor) {
        this.title = title;
        this.textColor = textColor;
    }

    public String getTitle() {
        return title;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
