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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.baibian.R;
import com.baibian.adapter.Homepage_Refresh_FootAdapter;
import com.baibian.adapter.RefreshFootAdapter;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HomepageFragment extends Fragment {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View HomepageFragment = inflater.inflate(R.layout.homepage_layout, container, false);
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
        demo_recycler.setAdapter(  adapter = new Homepage_Refresh_FootAdapter(mActivity));
        adapter.setItemClickListener(new Homepage_Refresh_FootAdapter.MyItemClickListener(){
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
                            int index = i + 1;
                            newDatas.add("new 用户" + index);
                        }
                        adapter.addItem(newDatas);
                        demo_swiperefreshlayout.setRefreshing(false);
                        Toast.makeText(mActivity, R.string.reflesh, Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }
        });
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

        return HomepageFragment;
    }

    //class CustomOnClickListener implements View.OnClickListener{
    //    @Override
    //    public void onClick(View v) {
    //         MainActivity.this.finish();
    //    }
    //  }
}






