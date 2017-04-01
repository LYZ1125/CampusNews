package com.baibian.activity.login;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.baibian.R;
import com.baibian.adapter.Guide_adapter;
import com.baibian.tool.springindicator.SpringIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导界面的活动
 */

public class GuideActivity extends Activity{
    private List<View> viewList;
    private Guide_adapter myPagerAdapter;
    public ViewPager pager;
    SpringIndicator springIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);
        viewList=new ArrayList<View>();
        /**
         * 3个View分别对应三个界面
         */
        View view1=View.inflate(this,R.layout.guide_childview1,null);
        View view2=View.inflate(this,R.layout.guide_childview2,null);
        View view3=View.inflate(this,R.layout.guide_childview3,null);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        pager=(ViewPager)findViewById(R.id.pager);
        Guide_adapter myPagerAdapter=new Guide_adapter(viewList,this);
        pager.setAdapter(myPagerAdapter);
        springIndicator=(SpringIndicator) findViewById(R.id.indicator);
        springIndicator.setViewPager(pager);


    }
}
