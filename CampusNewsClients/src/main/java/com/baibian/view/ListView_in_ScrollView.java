package com.baibian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class ListView_in_ScrollView extends ListView {
    public ListView_in_ScrollView(Context context) {
        super(context);
    }

    public ListView_in_ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListView_in_ScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
     //   int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec        );
    }
}

