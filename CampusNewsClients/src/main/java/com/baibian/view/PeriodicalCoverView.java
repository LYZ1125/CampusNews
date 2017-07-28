package com.baibian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibian.R;

/**
 * Created by Ellly on 2017/7/24.
 */

public class PeriodicalCoverView extends RelativeLayout {

    /**
     * Lazy to construct interface methods for widgets, thus publicize them.
     */
    public ImageView cover;
    public TextView title;
    public TextView subTitle;

    public PeriodicalCoverView(Context context) {
        super(context);
        initView();
    }

    public PeriodicalCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PeriodicalCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.periodical_cover_view_layout, this, true);
        cover = (ImageView) findViewById(R.id.periodical_cover);
        title = (TextView) findViewById(R.id.periodical_title);
        subTitle = (TextView) findViewById(R.id.periodical_subtitle);
    }
}
