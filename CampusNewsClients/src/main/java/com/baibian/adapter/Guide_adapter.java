package com.baibian.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.baibian.Login4Activity;
import com.baibian.MainActivity;
import com.baibian.R;
import com.baibian.registerActivity;

import java.util.List;

/**
 * 引导界面的Viewpager的adapter
 */
public class Guide_adapter extends PagerAdapter  {
    public List<View > Viewlist;
    public Activity activity;
    private Button guide_register_btn;
    private Button guide_login_btn;
    private  Button guide_visitor_btn;
    public  Guide_adapter(List<View>Viewlist, Activity activity){
        this.activity=activity;
        this.Viewlist=Viewlist;
    }
    /**
     * 返回页卡数目
     */
    public  int getCount(){
        return Viewlist.size();
    }



    /**
     *View是否来自对象
     */
    public boolean isViewFromObject(View arg0, Object arg1){
        return  arg0==arg1;
    }

    /**
     * 实例化一个页卡
     */
    // public Object instantiateItem(ViewGroup container,int position){
    //    container.addView(Viewlist.get(position));
    //    return Viewlist.get(position);
    //  }
    /**
     * 销毁一个页卡
     */
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView(Viewlist.get(position));

    }
    @Override
    public Object instantiateItem(View arg0,int arg1){
        ((ViewPager) arg0).addView(Viewlist.get(arg1), 0);
        /**
         * 当进入最后一个界面时，实例化三个按钮，
         */
        if (arg1== Viewlist.size()-1){
            guide_login_btn=(Button) arg0.findViewById(R.id.guide_login_btn);
            guide_register_btn=(Button) arg0.findViewById(R.id.guide_register_btn);
            guide_visitor_btn=(Button) arg0.findViewById(R.id.guide_visitor_btn);
            /**
             * 登录按钮，进入登录界面
             */
        guide_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, Login4Activity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
            /**
             * 注册按钮，进入注册界面
             */
        guide_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity,registerActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
            /**
             * 游客模式按钮，进入主界面
             */
            guide_visitor_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity,MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            });
        }

        return Viewlist.get(arg1);
    }


}