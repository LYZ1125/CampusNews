package com.baibian.fragment;

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

import com.baibian.ChannelActivity;
import com.baibian.R;
import com.baibian.adapter.NewsFragmentPagerAdapter;
import com.baibian.app.AppApplication;
import com.baibian.bean.ChannelItem;
import com.baibian.bean.ChannelManage;
import com.baibian.load.refresh;
import com.baibian.view.ColumnHorizontalScrollView;

import java.util.ArrayList;


public class FindFragment extends Fragment {

    private Context mContext;
    /**
     * 自定义HorizontalScrollView
     */
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_column;
    private ViewPager mViewPager;
    private ImageView button_more_columns;

    /**
     *用户选择的新闻分类列表
     */
    private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /**
     * *当前选中的栏目
     */
    private int columnSelectIndex = 0;
    /**
     * �左阴影部分
     */
    public ImageView shade_left;
    /**
     *右阴影部分
     */
    public ImageView shade_right;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;
    /**
     * Item宽度
     */
    private int mItemWidth = 0;
    private ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();
// protected SlidingMenu side_drawer;
    /**
     * head 头部 的中间的loading
     */
    private ProgressBar top_progress;
    /**
     * head 头部 中间的刷新按钮
     */
    private ImageView top_refresh;
/**
 *  head 头部 的左侧菜单 按钮
 */
//  private ImageView top_head;
/**
 *head 头部 的右侧菜单 按钮
 */
// private ImageView top_more;
    /**
     * 请求CODE
     *
     */
    public final static int CHANNELREQUEST = 1;
    /**
     * 调整返回的RESULTCODE
     */
    public final static int CHANNELRESULT = 10;
//    private static final int STATE_REFRESHING = 3;

    private NewsFragmentPagerAdapter mAdapter;
    private int position1 = 0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View FindFragment = inflater.inflate(R.layout.find_layout, container, false);
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) FindFragment.findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) FindFragment.findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) FindFragment.findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) FindFragment.findViewById(R.id.rl_column);
        button_more_columns = (ImageView) FindFragment.findViewById(R.id.button_more_columns);
        mViewPager = (ViewPager) FindFragment.findViewById(R.id.mViewPager);
        shade_left = (ImageView) FindFragment.findViewById(R.id.shade_left);
        shade_right = (ImageView) FindFragment.findViewById(R.id.shade_right);
//        top_head = (ImageView) HomepageFragment.findViewById(R.id.top_head);
//        top_more = (ImageView) HomepageFragment.findViewById(R.id.top_more);
        top_refresh = (ImageView) getActivity().findViewById(R.id.top_refresh);
//        top_progress = (ProgressBar) HomepageFragment.findViewById(R.id.top_progress);
        initView();
        return FindFragment;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    private FindFragment.OnRefreshListener mListener;

    public void setOnRefreshListener(FindFragment.OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 初始化layout控件
     */
    private void initView() {



//        setChangelView();
        //加号
        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_channel = new Intent(getActivity().getApplicationContext(), ChannelActivity.class);
                startActivityForResult(intent_channel, CHANNELREQUEST);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        top_refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String name = makeFragmentName(container.getId(), itemId);
//                View viewById = mAdapter.getItem(1).getView().findViewById(R.layout.pull_to_refresh_header);
//                getFragmentManager().findFragmentByTag()
//                View viewById = getSupportFragmentManager().findFragmentById(R.layout.news_fragment).getView().findViewById(R.layout.pull_to_refresh_header);
//                Log.d("position", String.valueOf("android:switcher:" + mViewPager.getId() + ":" + mViewPager.getCurrentItem()));
//                ???getFragmentManager??getSupportFragmentManager??????????????????????????
//                Log.d("position1", String.valueOf(getSupportFragmentManager().findFragmentByTag(fragments.get(1).getTag()).getView()));
//                Animation loadAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.refresh_rotate);
//                开启一个动画
                rotateTopRefresh();
//               某些未知的情况下，此处会强退。猜测是因为fragment还没有完全加载完成
                NewsFragment fragmentByTag = (NewsFragment)getChildFragmentManager().findFragmentByTag(fragments.get(mViewPager.getCurrentItem()).getTag());
//                HeadListView headListView = fragmentByTag.getmHeadListView();
////                headListView.mCurrentState = STATE_REFRESHING;
//                ((TextView) headListView.findViewById(R.id.tv_title)).setText("???????...");
//                (headListView.findViewById(R.id.iv_arrow)).clearAnimation();
//                (headListView.findViewById(R.id.iv_arrow)).setVisibility(View.INVISIBLE);
//                headListView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                refresh.refreshHeadView(fragmentByTag);
                mListener.onRefresh();
//                fragmentByTag.refreshData();
//                top_refresh.clearAnimation();
//                ivArrow.clearAnimation();
//                pbProgress.setVisibility(VISIBLE);
//                ivArrow.setVisibility(INVISIBLE);
//                if (mAdapter.fragmentTAG == null) {
//                }
//                viewById.setPadding(0, 0, 0, 0);
//                HeadListView listView = currentFragment.mListView;
////                listView.initHeaderView();
//                View headerView = listView.getHeaderView();
////                View headerView = currentFragment.getListView().getHeaderView();
//                headerView.setPadding(0, 0, 0, 0);
            }
        });
        setChangelView();
    }

    public void rotateTopRefresh() {
        RotateAnimation rotateAnimation = refresh.refreshAnimation();
        ///  top_refresh.startAnimation(rotateAnimation);
        rotateAnimation.startNow();
    }

    /**
     * 当栏目项发生变化时候调用
     */
    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }

    /**
     * 获取Column栏目 数据
     */
    private void initColumnData() {
        userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
    }
    @Override
    public void onAttach(Activity activity){

        super.onAttach(activity);
        this.mContext = activity;
    }
    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();

        mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
            TextView columnTextView = new TextView(mContext);
            columnTextView.setTextAppearance(mContext, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getActivity().getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        //判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    /**
     * 初始化Fragment，viewpager
     */
    private void initFragment() {
        fragments.clear();//???
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            data.putString("text", userChannelList.get(i).getName());
            data.putInt("id", userChannelList.get(i).getId());
//            data.putString("source", this.getSharedPreferences("channelSource", Context.MODE_PRIVATE).getString(String.valueOf(i+1), "null"));
            NewsFragment newFragment = new NewsFragment();
//            currentFragment = newFragment;
            newFragment.setArguments(data);
            fragments.add(newFragment);
        }
//        getSupportFragmentManager().beginTransaction().add()
        mAdapter = new NewsFragmentPagerAdapter(getChildFragmentManager(), fragments);
//		mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(pageListener);

    }

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
            if (position1 != position) {
                //top_refresh.clearAnimation();
            }
            position1 = position;
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case CHANNELREQUEST:
                if (resultCode == CHANNELRESULT) {
                    setChangelView();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
