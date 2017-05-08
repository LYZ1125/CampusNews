package com.baibian.adapter;


import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Ö÷Ò³viewpagerµÄadapter
 */

public class Homepage_ViewpagerAdapter extends PagerAdapter {

    public List<ImageView> imageViews;
    public int[] imageResId;

    public Homepage_ViewpagerAdapter(List<ImageView> imageViews, int[] imageResId) {
        this.imageViews = imageViews;
        this.imageResId = imageResId;
    }

    @Override
    public int getCount() {
        return imageResId.length;
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(imageViews.get(arg1));
        return imageViews.get(arg1);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }

}
