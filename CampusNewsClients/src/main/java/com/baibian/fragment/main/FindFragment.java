package com.baibian.fragment.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.activity.ChannelActivity;
import com.baibian.R;
import com.baibian.adapter.NewsFragmentPagerAdapter;
import com.baibian.app.AppApplication;
import com.baibian.bean.ChannelItem;
import com.baibian.bean.ChannelManage;
import com.baibian.load.refresh;
import com.baibian.view.ColumnHorizontalScrollView;

import java.util.ArrayList;


public class FindFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View FindFragment = inflater.inflate(R.layout.find_layout, container, false);
        return FindFragment;
    }

}
