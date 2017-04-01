package com.baibian.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
public class Profile_Drawer_Right_ViewPager_Adapter extends PagerAdapter {
        public List<View > Viewlist;
    public Activity activity;
    private Button guide_register_btn;
    private Button guide_login_btn;
    private  Button guide_visitor_btn;
    public  Profile_Drawer_Right_ViewPager_Adapter(List<View>Viewlist, Activity activity){
        this.activity=activity;
        this.Viewlist=Viewlist;
    }
    /**
     * ??????????
     */
    public  int getCount(){
        return Viewlist.size();
    }

    /**
     *View??????????
     */
    public boolean isViewFromObject(View arg0, Object arg1){
        return  arg0==arg1;
    }

    /**
     * ???????????
     */
    // public Object instantiateItem(ViewGroup container,int position){
    //    container.addView(Viewlist.get(position));
    //    return Viewlist.get(position);
    //  }
    /**
     * ??????????
     */
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView(Viewlist.get(position));

    }
    @Override
    public Object instantiateItem(View arg0,int arg1){
        ((ViewPager) arg0).addView(Viewlist.get(arg1), 0);
        if (arg1== Viewlist.size()-1) {
        }
        return Viewlist.get(arg1);
    }


}