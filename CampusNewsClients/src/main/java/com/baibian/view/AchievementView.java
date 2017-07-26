package com.baibian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibian.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ellly on 2017/7/25.
 */

public class AchievementView extends RelativeLayout {

    public CircleImageView point;
    public TextView achieves;

    public AchievementView(Context context) {
        super(context);
        initView();
    }

    public AchievementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AchievementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.achievement_relative_layout, this, true);
        point = (CircleImageView) findViewById(R.id.point_text);
        achieves = (TextView) findViewById(R.id.achievement_content);
    }
}
