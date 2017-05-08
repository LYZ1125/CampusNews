package com.baibian.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibian.R;
import com.baibian.adapter.Discussion_FootAdapter;
import com.baibian.adapter.RefreshFootAdapter;
import com.baibian.tool.UI_Tools;

import java.util.ArrayList;
import java.util.List;


public class DiscussionActivity extends Activity {

    private SwipeRefreshLayout integration_swiperefreshlayout;
    private RecyclerView integration_recycler;
    private Discussion_FootAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;

    private RelativeLayout progress_relativelayout3;
    private RelativeLayout progress_relativelayout2;
    private RelativeLayout progress_relativelayout1;
    private TextView progress_text3;
    private TextView progress_text2;
    private TextView progress_text1;
    private EditText send_edittext;
    private LinearLayout discussion_layout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.discussionlayout);
        initview();
        initprogress();
    }
    private void initview(){
        progress_relativelayout1=(RelativeLayout) findViewById(R.id.discussion_progress_relativelayout1);
        progress_relativelayout2=(RelativeLayout) findViewById(R.id.discussion_progress_relativelayout2);
        progress_relativelayout3=(RelativeLayout) findViewById(R.id.discussion_progress_relativelayout3);
        progress_text1=(TextView) findViewById(R.id.discussion_progress_text1);
        progress_text2=(TextView) findViewById(R.id.discussion_progress_text2);
        progress_text3=(TextView) findViewById(R.id.discussion_progress_text3);
        Button back_btn=(Button) findViewById(R.id.choise_direction_back);
        send_edittext=(EditText) findViewById(R.id.send_edittext);
        discussion_layout=(LinearLayout) findViewById(R.id.discussion_layout);
        UI_Tools ui_tools=new UI_Tools();
        ui_tools.CancelFocusOne(this,discussion_layout,send_edittext);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initlist();
    }
    private void initprogress(){
        int positiveNumber=80;
         int neutralNumber=30;
        int negetiveNumber=50;

        WindowManager wm = (WindowManager)   getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        String positiveString=""+(int)positiveNumber;
        String negetiveString=""+(int)negetiveNumber;
        String neutralString=""+(int)neutralNumber;
        float progress_text1width=  (width*positiveNumber/(positiveNumber+negetiveNumber+neutralNumber));
        float progress_text2width=  (width*neutralNumber/(positiveNumber+negetiveNumber+neutralNumber));
        float progress_text3width=  width*negetiveNumber/(positiveNumber+negetiveNumber+neutralNumber);
        progress_relativelayout3.setMinimumWidth((int) progress_text3width);
        progress_relativelayout2.setMinimumWidth((int)progress_text2width);
        progress_relativelayout1.setMinimumWidth((int) progress_text1width);
        progress_text3.setText(negetiveString);
        progress_text2.setText(neutralString);
        progress_text1.setText(positiveString);
    }
    private void initlist(){
        // top_bar_linear_back=(LinearLayout)this.findViewById(R.id.top_bar_linear_back);
        // top_bar_linear_back.setOnClickListener(new CustomOnClickListener());
        //  top_bar_title=(TextView)this.findViewById(R.id.top_bar_title);
        // top_bar_title.setText("RecyclerView下拉刷新,下拉加载更多...");
        integration_swiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.integration_swiperefreshlayout);
        integration_recycler = (RecyclerView) findViewById(R.id.integration_recycler);
        //设置刷新时动画的颜色，可以设置4个
        integration_swiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        integration_swiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        //     demo_swiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
        //             .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
        //                     .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        integration_recycler.setLayoutManager(linearLayoutManager);
        //添加分隔线
        // demo_recycler.addItemDecoration(new AdvanceDecoration(this, OrientationHelper.VERTICAL));
        integration_recycler.setAdapter(  adapter = new Discussion_FootAdapter(this));

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
                        for (int i = 0; i < 0; i++) {
                            /**
                             * 这列是刷新的测试数据的内容
                             */
                            int index = i + 1;
                            newDatas.add("new 用户" + index);
                        }
                        adapter.addItem(newDatas);
                        integration_swiperefreshlayout.setRefreshing(false);
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
                            for (int i = 0; i < 0; i++) {
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

    }

}
