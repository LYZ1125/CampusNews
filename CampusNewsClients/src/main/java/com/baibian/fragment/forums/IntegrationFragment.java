package com.baibian.fragment.forums;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.activity.ChoiceDirectionActivity;
import com.baibian.R;
import com.baibian.adapter.Forums_Integration_Refresh_FootAdapter;
import com.baibian.adapter.RefreshFootAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 论坛碎片中的综合碎片
 */

public class IntegrationFragment extends Fragment {
    private ListView mListView;
    private List<Map<String, Object>> data;
    private Context mActivity;
    private LinearLayout top_bar_linear_back;
    private TextView top_bar_title;
    private SwipeRefreshLayout integration_swiperefreshlayout;
    private RecyclerView integration_recycler;
    private Forums_Integration_Refresh_FootAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View IntegrationFragment = inflater.inflate(R.layout.integration_fragment_layout, container, false);
        // top_bar_linear_back=(LinearLayout)this.findViewById(R.id.top_bar_linear_back);
        // top_bar_linear_back.setOnClickListener(new CustomOnClickListener());
        //  top_bar_title=(TextView)this.findViewById(R.id.top_bar_title);
        // top_bar_title.setText("RecyclerView下拉刷新,下拉加载更多...");
        integration_swiperefreshlayout = (SwipeRefreshLayout) IntegrationFragment.findViewById(R.id.integration_swiperefreshlayout);
        integration_recycler = (RecyclerView) IntegrationFragment.findViewById(R.id.integration_recycler);
        //设置刷新时动画的颜色，可以设置4个
        integration_swiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        integration_swiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        //     demo_swiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
        //             .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
        //                     .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        integration_recycler.setLayoutManager(linearLayoutManager);
        //添加分隔线
        // demo_recycler.addItemDecoration(new AdvanceDecoration(this, OrientationHelper.VERTICAL));
        integration_recycler.setAdapter(  adapter = new Forums_Integration_Refresh_FootAdapter(mActivity));
        adapter.setItemClickListener(new Forums_Integration_Refresh_FootAdapter.MyItemClickListener(){
            public void onItemClick(View view, int position) {
                if(position==0){
                    //这里进入选择辩题方向的界面，暂时只是用第一个来进行测试
                    Intent intent= new Intent(mActivity, ChoiceDirectionActivity.class);
                    startActivity(intent);
                }else
                Toast.makeText(getActivity(), R.string.drawer_right_logout_hint_text + position, Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 刷新监听
         */
        integration_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                            int index = i + 1;
                            newDatas.add("new 用户" + index);
                        }
                        adapter.addItem(newDatas);
                        integration_swiperefreshlayout.setRefreshing(false);
                        Toast.makeText(mActivity, R.string.reflesh, Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }
        });
        integration_recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> newDatas = new ArrayList<String>();
                            for (int i = 0; i < 5; i++) {
                                int index = i + 1;
                                /**
                                 * 这里是上滑加载更多的测试内容
                                 */
                                newDatas.add("more item" + index);
                            }
                            adapter.addMoreItem(newDatas);
                            adapter.changeMoreStatus(RefreshFootAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 2500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


        return IntegrationFragment;
    }
}