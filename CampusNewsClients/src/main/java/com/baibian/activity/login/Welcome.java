package com.baibian.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.baibian.R;
import com.baibian.activity.MainActivity;
import com.baibian.activity.login.GuideActivity;
import com.baibian.app.AppApplication;
import com.baibian.base.BaseActivity;
import com.baibian.tool.PrefTools;

public class Welcome extends BaseActivity {
    private AlphaAnimation start_anima;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.welcome, null);
        setContentView(view);
        initView();
            /*开启线程取得数据*/
//		CrawlerChannel crawlerChannel = new CrawlerChannel(this);
//		crawlerChannel.getItem();
        initData();
    }

    private void initData() {
//		new ChannelManage(this);
        start_anima = new AlphaAnimation(0.3f, 1.0f);
        start_anima.setDuration(2000);
        view.startAnimation(start_anima);
        start_anima.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                redirectTo();
            }
        });
    }

    private void initView() {

    }


    /**
     * 引导界面初始化部分
     */
    private void redirectTo() {
        // 动画结束,跳转页面
        // 如果是第一次进入, 跳新手引导
        // 否则跳主页面
        boolean isFirstEnter = PrefTools.getBoolean(
                Welcome.this, "is_first_enter", true);
        //更新sp, 已经不是第一次进入了
        //AppApplication.getContext():通用的取context的方法
        PrefTools.setBoolean(AppApplication.getContext(), "is_first_enter", false);
        Intent intent;
        if (isFirstEnter) {
            // 新手引导
            intent = new Intent(getApplicationContext(),
                    GuideActivity.class);
        } else {
            // 主页面
            intent = new Intent(getApplicationContext(),
                    MainActivity.class);
        }

        startActivity(intent);

        finish();// 结束当前页面
    }
}