package com.baibian.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baibian.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 主页的Listview的adapter
 * 这部分的代码已经作废，更改成为使用RecyclerView.Adapter这种的方法进行实现，留下这部分代码，只为后来如果有用参考
 */


public class Homepage_ListAdapter extends BaseAdapter implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mList;

    private View mViewPage;
    private ViewPager adsViewPager;
    private int currentItem = 0;
    private List<ImageView> imageViews;
    private int[] imageResId;
    private List<View> dots;
    private static int oldPosition;
    private ScheduledExecutorService scheduledExecutorService;
    private Handler handler;

    public Homepage_ListAdapter(Context context, List<Map<String, Object>> list) {

        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mList = list;
        initViewPager();
    }

    private void initViewPager() {
        mViewPage = mInflater.inflate(R.layout.homepage_list_item_viewpager, null);
        adsViewPager = (ViewPager) mViewPage.findViewById(R.id.homepage_ViewPager);
        oldPosition = 0;
        initImagesId();
        addImageViews();
        initDots(mViewPage);
        initChangePicHandler();
        startScheduledExecutorService();
        adsViewPager.setAdapter(new Homepage_ViewpagerAdapter(imageViews, imageResId));
        adsViewPager.setOnPageChangeListener(Homepage_ListAdapter.this);
    }

    public void initImagesId() {
        imageResId = new int[] { R.drawable.ads0, R.drawable.ads1,
                R.drawable.ads2, R.drawable.ads3, R.drawable.ads4 };
    }

    public void addImageViews() {
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < imageResId.length; i++) {
            final ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(imageResId[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }
    }

    public void initDots(View view) {
        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.v_dot0));
        dots.add(view.findViewById(R.id.v_dot1));
        dots.add(view.findViewById(R.id.v_dot2));
        dots.add(view.findViewById(R.id.v_dot3));
        dots.add(view.findViewById(R.id.v_dot4));
    }

    @SuppressLint("HandlerLeak")
    public void initChangePicHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                if (msg.what == 1002) {
                    adsViewPager.setCurrentItem(currentItem);
                }
            };
        };
    }

    public void startScheduledExecutorService() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1002;
                currentItem = (currentItem + 1) % imageViews.size();
                handler.sendMessage(msg);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        return position > 0 ? 0 : 1;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            convertView = mViewPage;
            return convertView;
        } else {
            convertView = mInflater.inflate(R.layout.homepage_list_item, null);
            TextView tv_item = (TextView) convertView
                    .findViewById(R.id.tv_item);
            tv_item.setText(mList.get(position - 1).get("data").toString());
        }
        return convertView;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
        dots.get(position).setBackgroundResource(R.drawable.dot_focused);
        oldPosition = position;

    }

}
