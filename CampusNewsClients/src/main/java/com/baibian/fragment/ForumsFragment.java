package com.baibian.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baibian.R;
import com.baibian.adapter.Tablayout_Adapter_Right;

import java.util.ArrayList;
import java.util.List;


public class ForumsFragment extends Fragment{
    /**
     * 右边tablayout部分
     */
    private TabLayout forums_title;//定义TabLayout
    private ViewPager forums_pager;                             //定义viewPager
    private FragmentPagerAdapter fAdapter;                               //定义adapter

    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表

    private IntegrationFragment integrationFragment;                    //综合碎片
    private HotFragment hotFragment;                                    //热门碎片
    private RealTimeFragment realTimeFragment;                         //实时碎片
    private ClassificationFragment classificationFragment;                             //分类碎片
    private FocusFragment focusFragment;                             //关注碎片
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ForumsFragment = inflater.inflate(R.layout.forums_layout, container, false);
        //右侧TabLayout部分，联系人。消息
        forums_title=(TabLayout) ForumsFragment.findViewById(R.id.forums_title);
        forums_pager =(ViewPager) ForumsFragment.findViewById(R.id.forums_pager);
        init_right_Tablayout();
        return ForumsFragment;
    }
    /**
     *  TabLayout的使用
     */
    private void init_right_Tablayout(){
        //初始化各fragment
        integrationFragment = new IntegrationFragment();
        hotFragment = new HotFragment();
        realTimeFragment  = new RealTimeFragment();
        classificationFragment = new ClassificationFragment();
        focusFragment  = new FocusFragment();

        //将fragment装进列表中
        list_fragment = new ArrayList<Fragment>();
        list_fragment.add(integrationFragment);
        list_fragment.add(hotFragment);
        list_fragment.add(realTimeFragment);
        list_fragment.add(classificationFragment);
        list_fragment.add(focusFragment);

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        list_title.add(getString(R.string.integration));
        list_title.add(getString(R.string.hot));
        list_title.add(getString(R.string.real_time));
        list_title.add(getString(R.string.classification));
        list_title.add(getString(R.string.focus));
        //设置TabLayout的模式
        forums_title.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        forums_title.addTab(forums_title.newTab().setText(list_title.get(0)));
        forums_title.addTab(forums_title.newTab().setText(list_title.get(1)));
        forums_title.addTab(forums_title.newTab().setText(list_title.get(2)));
        forums_title.addTab(forums_title.newTab().setText(list_title.get(3)));
        forums_title.addTab(forums_title.newTab().setText(list_title.get(4)));
        fAdapter = new Tablayout_Adapter_Right(getChildFragmentManager(),list_fragment,list_title);

        //viewpager加载adapter
        forums_pager.setAdapter(fAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        forums_title.setupWithViewPager(forums_pager);
        //tab_FindFragment_title.set
    }


}
