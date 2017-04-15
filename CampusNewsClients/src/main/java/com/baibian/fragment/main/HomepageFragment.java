package com.baibian.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baibian.R;
import com.baibian.adapter.Homepage_Refresh_FootAdapter;
import com.baibian.adapter.RefreshFootAdapter;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HomepageFragment extends Fragment implements View.OnClickListener {
    private View main_head;
    private RelativeLayout fragment1_layout;//导航栏首页按钮
    private LinearLayout main_bottom;
   private Button homepage_search_button;
    private ListView mListView;
    private List<Map<String, Object>> data;
    private Context mActivity;
    private LinearLayout top_bar_linear_back;
    private TextView top_bar_title;
    private SwipeRefreshLayout demo_swiperefreshlayout;
    private RecyclerView demo_recycler;
    private Homepage_Refresh_FootAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private LinearLayout homepage_all_layout;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View HomepageFragment = inflater.inflate(R.layout.homepage_layout, container, false);
        initview();
       homepage_search_button = (Button) HomepageFragment.findViewById(R.id.homepage_search_button);
        homepage_all_layout=(LinearLayout) HomepageFragment.findViewById(R.id.homepage_all_layout);

        // top_bar_linear_back=(LinearLayout)this.findViewById(R.id.top_bar_linear_back);
        // top_bar_linear_back.setOnClickListener(new CustomOnClickListener());
        //  top_bar_title=(TextView)this.findViewById(R.id.top_bar_title);
        // top_bar_title.setText("RecyclerView下拉刷新,下拉加载更多...");
        demo_swiperefreshlayout = (SwipeRefreshLayout) HomepageFragment.findViewById(R.id.homepage_swiperefreshlayout);
        demo_recycler = (RecyclerView) HomepageFragment.findViewById(R.id.homepage_recycler);
        //设置刷新时动画的颜色，可以设置4个
        demo_swiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        demo_swiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        //     demo_swiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
        //             .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
        //                     .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        demo_recycler.setLayoutManager(linearLayoutManager);
        //添加分隔线
        // demo_recycler.addItemDecoration(new AdvanceDecoration(this, OrientationHelper.VERTICAL));
        demo_recycler.setAdapter(adapter = new Homepage_Refresh_FootAdapter(mActivity));
        adapter.setItemClickListener(new Homepage_Refresh_FootAdapter.MyItemClickListener() {
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), R.string.drawer_right_logout_hint_text + position, Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 刷新监听
         */
        demo_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> newDatas = new ArrayList<String>();
                        for (int i = 0; i < 5; i++) {
                            /**
                             * 这列是刷新的测试数据的内容
                             */
//                            int index = i + 1;
//                            newDatas.add("new 用户" + index);
                        }
                        adapter.addItem(newDatas);
                        demo_swiperefreshlayout.setRefreshing(false);
                        Toast.makeText(mActivity, R.string.refresh_success, Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }
        });
        demo_recycler.setOnTouchListener(new View.OnTouchListener() {
            int  mFirstY;
            int mCurrentY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //触摸时
                        mFirstY=(int)motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动
                        mCurrentY=(int)motionEvent.getY();
                        if(mFirstY - mCurrentY <0){
                            //向下滑动

//                            TranslateAnimation translateAnimationright=new TranslateAnimation(0,0,0,-homepage_search_button.getHeight());
//                       translateAnimationright.setDuration(200);
//                            homepage_search_button.startAnimation(translateAnimationright);
//                           // homepage_search_button.setVisibility(View.VISIBLE);
                        } else if ((mFirstY - mCurrentY > 0)) {
                            //向上滑动
//                            TranslateAnimation translateAnimationright=new TranslateAnimation(0,0,homepage_search_button.getHeight(),0);
//                            translateAnimationright.setDuration(200);
//                            homepage_search_button.startAnimation(translateAnimationright);
//                           homepage_search_button.setVisibility(View.GONE);
                        }


                        break;
                    case MotionEvent.ACTION_UP:
                        //離開
                        break;
                }
                return false;
            }
        });
        /**
         * 如果recyler的高度小于Homepagefragment的高度，那么就隐藏。
         */

        demo_recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> newDatas = new ArrayList<String>();
//                            for (int i = 0; i < 5; i++) {
//                                int index = i + 1;
//                                /**
//                                 * 这里是上滑加载更多的测试内容
//                                 */
//                                newDatas.add("more item" + index);
//                            }
                           adapter.addMoreItem(newDatas);
                            adapter.changeMoreStatus(RefreshFootAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 2500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                dy <0 表示 上滑， dy>0 表示下滑

                super.onScrolled(recyclerView, dx, dy);
                WindowManager wm = (WindowManager) getActivity()
                        .getSystemService(Context.WINDOW_SERVICE);
                int heighth = wm.getDefaultDisplay().getHeight();
                if (dy > heighth / 50) {//下滑

//                        TranslateAnimation translateAnimationright=new TranslateAnimation(0,0,0,-homepage_search_button.getHeight());
//                        translateAnimationright.setDuration(200);
//                        homepage_search_button.startAnimation(translateAnimationright);
//                        main_head.setVisibility(View.GONE);
                  //      main_bottom.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(), "xia畫", Toast.LENGTH_SHORT).show();

                }

                //
                else if (dy < -heighth / 50) {//
                 //   if (homepage_search_button.getVisibility() == View.GONE) {

                  //  homepage_search_button.setVisibility(View.GONE);

//                        main_head.setVisibility(View.VISIBLE);
              //          main_bottom.setVisibility(View.VISIBLE);
//                        TranslateAnimation translateAnimationright = new TranslateAnimation(0, 0, -homepage_search_button.getHeight(), 0);
//                        translateAnimationright.setDuration(200);
//                        homepage_search_button.startAnimation(translateAnimationright);
                  //  }
//
               }
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                }
            }

            );

            return HomepageFragment;
        }

    private void initview() {
        fragment1_layout = (RelativeLayout) getActivity().findViewById(R.id.fragment1_layout);
        main_head=(View) getActivity().findViewById(R.id.main_head);
        main_bottom=(LinearLayout) getActivity().findViewById(R.id.main_bottom);
        fragment1_layout.setOnTouchListener(new View.OnTouchListener() {
            int count;
            long firClick;
            long secClick;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    count++;
                    if (count == 1) {
                        firClick = System.currentTimeMillis();

                    } else if (count == 2) {
                        secClick = System.currentTimeMillis();
                        if (secClick - firClick < 1000) {
                            //双击事件
                            Toast.makeText(getActivity(), "双击刷新", Toast.LENGTH_SHORT).show();
                        }
                        count = 0;
                        firClick = 0;
                        secClick = 0;

                    }
                }
                return false;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }
    //class CustomOnClickListener implements View.OnClickListener{
    //    @Override
    //    public void onClick(View v) {
    //         MainActivity.this.finish();
    //    }
    //  }
}






