package com.baibian.fragment.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.activity.CityListActivity;
import com.baibian.activity.newsDetailsActivity;
import com.baibian.R;
import com.baibian.adapter.NewsAdapter;
import com.baibian.bean.NewsEntity;
import com.baibian.crawler.CrawlerChannel;
import com.baibian.tool.Constants;
import com.baibian.view.HeadListView;

import java.util.ArrayList;


public class NewsFragment extends Fragment {
    private final static String TAG = "NewsFragment";
    Activity activity;
    ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
    HeadListView mHeadListView;
    NewsAdapter mAdapter;
    String text;
    int channel_id;
    ImageView detail_loading;
    public final static int SET_NEWSLIST = 0;
    private static int NEWSITEM_ID = 0;
    //Toast��ʾ��
    private RelativeLayout notify_view;
    private TextView notify_view_text;
    private TextView footTitle;
    private int currentPosition;
    private TextView item_title;
    CrawlerChannel crawlerChannel;
    //    初始化item时根据id初始化
//    private ArrayList<Integer> isReaded =new ArrayList<Integer>();
//    private ImageView popicon;
//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle args = getArguments();
        text = args != null ? args.getString("text") : "";
        channel_id = args != null ? args.getInt("id", 0) : 0;
        Log.d("channel", String.valueOf(channel_id));
        initData();
//        getFragmentManager().beginTransaction()
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;
        super.onAttach(activity);
    }

    /**
     * 此方法意思为fragment是否可见，可见时加载数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //fragment可见时加载数据
//            if (newsList != null && newsList.size() != 0) {
//                handler.obtainMessage(SET_NEWSLIST).sendToTarget();
//            } else {

//    注释掉以上三行，是的每次载入必须延时2秒，以此等待listview的刷新
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    handler.obtainMessage(SET_NEWSLIST).sendToTarget();
                }
            }).start();
//            }
        } else {
            //fragment不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
//
//    public HeadListView getListView(){
////        if (mHeadListView == null) {
////            Log.d("main!!!", String.valueOf(mHeadListView));
////        }
//        return mHeadListView;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
//        Log.d("position2", String.valueOf(view));
        mHeadListView = (HeadListView) view.findViewById(R.id.mListView);
//        Log.d("position3", String.valueOf(mHeadListView));
        TextView item_textView = (TextView) view.findViewById(R.id.item_textview);
        detail_loading = (ImageView) view.findViewById(R.id.detail_loading);
        //Toast提示框
        notify_view = (RelativeLayout) view.findViewById(R.id.notify_view);
        notify_view_text = (TextView) view.findViewById(R.id.notify_view_text);
        item_title = (TextView) view.findViewById(R.id.item_title);
        //pop
//        popicon = (ImageView) view.findViewById(R.id.popicon);
        item_textView.setText(text);
        return view;
    }

    public HeadListView getmHeadListView() {
        return mHeadListView;
    }

    private void initData() {
//        下面俩句不能调换顺序，需要先初始化！！！
        crawlerChannel = new CrawlerChannel(activity);
//        先初始化回调函数！！否则空指针
        handler.obtainMessage(SET_NEWSLIST).sendToTarget();
        newsList = Constants.getNewsList(NEWSITEM_ID, channel_id, crawlerChannel);
    }


    private int loadTime = 0;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case SET_NEWSLIST:
                    detail_loading.setVisibility(View.GONE);
                    if (mAdapter == null) {
                        mAdapter = new NewsAdapter(activity, newsList);
                        //判断是不是城市频道
                        if (channel_id == Constants.CHANNEL_CITY) {
                            //是城市频道
                            mAdapter.setCityChannel(true);
                            initCityChannel();
                        }
                        mHeadListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }

                    ((Rubbish_java) getParentFragment()).setOnRefreshListener(new Rubbish_java.OnRefreshListener() {

                        public void onRefresh() {
//						getDataFromServer();
                            ((Rubbish_java) getParentFragment()).rotateTopRefresh();
                            crawlerChannel.pullToRefresh(channel_id);
                        }
                    });

//                  设置滑动channel（频道）条
                    mHeadListView.setPinnedHeaderView(LayoutInflater.from(activity).inflate(R.layout.list_item_section, mHeadListView, false));

                    crawlerChannel.setOnRefreshListener(new CrawlerChannel.OnRefreshListener() {

                        @Override
                        public void refreshItem(int i) {
//                            0代表下来刷新没有新的动态了
                            if (i == 0) {
                                Toast.makeText(activity, "û�и���������~", Toast.LENGTH_SHORT).show();
                            }

                            handler.obtainMessage(SET_NEWSLIST).sendToTarget();
                        }

                        @Override
                        public void pullRefreshItem(int i) {
                            newsList.clear();
                            NEWSITEM_ID = 0;
                            newsList.addAll(0, Constants.getNewsList(NEWSITEM_ID, channel_id, crawlerChannel));
                            initNotify(i);
                            mHeadListView.onRefreshComplete();
//                            handler.obtainMessage(SET_NEWSLIST).sendToTarget();
                        }
                    });

                    mHeadListView.setOnRefreshListener(new HeadListView.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
//						getDataFromServer();
                            ((Rubbish_java  ) getParentFragment()).rotateTopRefresh();
                            crawlerChannel.pullToRefresh(channel_id);
                        }

                        @Override
                        public void onLoadMore() {
//  -------------------------  loadTime表示下来2次，待会改成有数据就继续刷新
//                            if (loadTime++ <= 1) {
                            NEWSITEM_ID += 10;
                            newsList.addAll(Constants.getNewsList(NEWSITEM_ID, channel_id, crawlerChannel));
//                            } else {
//                                Toast.makeText(activity, "没有新闻啦~", Toast.LENGTH_SHORT).show();
//                            }
                            mHeadListView.onRefreshComplete();
                            handler.obtainMessage(SET_NEWSLIST).sendToTarget();
                        }

                    });

                    mHeadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
//                            Log.d("position1111", String.valueOf(position));
                            currentPosition = position - 1;
//                            Log.d("position", String.valueOf(position));
//                            Log.d("position", String.valueOf(newsList.size()));
//                            禁点第一个和最后一个
                            if (currentPosition >= 0 && currentPosition != newsList.size()) {

//                                mHeadListView.findViewWithTag()
//                                mHeadListView.getChildAt(currentPosition).findViewById(R.id.popicon).setOnClickListener(new popAction(position));
//                                popicon.setOnClickListener(new popAction(position));
//                                isReaded.add(mAdapter.getItem(currentPosition).getId());

//                                mAdapter.getItem(currentPosition).setReadStatus(true);
//                              本应该用getSharedPreferences记录点击过与否，但是因为是内置数据没有位移的item标识符，无法保证标记准确，暂时不考虑。
//                                Log.d("itemId", String.valueOf(mAdapter.getItem(currentPosition).getId()));
                                activity.getSharedPreferences("publishTime", Context.MODE_PRIVATE).edit().putBoolean(String.valueOf(mAdapter.getItem(currentPosition).getId()), true).commit();
//                                item_title.setTextColor(Color.GRAY);
                                mAdapter.notifyDataSetChanged();
                                Intent intent = new Intent(activity, newsDetailsActivity.class);
                                if (channel_id == Constants.CHANNEL_CITY) {
                                    if (currentPosition != 0) {
                                        intent.putExtra("itemId", mAdapter.getItem(currentPosition - 1).getId());
                                        intent.putExtra("news", mAdapter.getItem(currentPosition - 1));
//                                        intent.putExtra("collectStatus", mAdapter.getItem(currentPosition - 1).getCollectStatus());
//                                        startActivity(intent);
                                        startActivityForResult(intent, 0);
                                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }
                                } else {
                                    intent.putExtra("itemId", mAdapter.getItem(currentPosition).getId());
                                    intent.putExtra("news", mAdapter.getItem(currentPosition));
//                                    startActivity(intent);
                                    startActivityForResult(intent, 0);
                                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }
                        }
                    });
//                   这句后面要改，应该是有刷新的时候才调用InitNotify();
//                    if (channel_id == 1) {
//                    initNotify();
//                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

//    public void refreshData() {
////        ArrayList<NewsEntity> moreNewsList = Constants.getNewsList(-1, channel_id, crawlerChannel);
////        ArrayList<NewsEntity> moreNewsList = new ArrayList<NewsEntity>();
////                moreNewsList.add(crawlerChannel.ConstantsAdapter(-1, channel_id, new NewsEntity()));
////        this.newsList.addAll(0, moreNewsList);
//
//        if (moreNewsList.size() != 0) {
//            initNotify(moreNewsList.size());
//        } else {
//            initNotify(0);
//        }
//        mHeadListView.onRefreshComplete();
//    }

    /* 初始化选择城市的header*/
    public void initCityChannel() {
        View headView = LayoutInflater.from(activity).inflate(R.layout.city_category_list_tip, null);
        TextView chose_city_tip = (TextView) headView.findViewById(R.id.chose_city_tip);
        chose_city_tip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(activity, CityListActivity.class);
                startActivity(intent);
            }
        });
        mHeadListView.addHeaderView(headView);
    }

    /* 初始化通知栏目，发送刷新通知֪ͨ*/
    private void initNotify(final int count) {
        new Handler(activity.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (count == 0) {
                    notify_view_text.setText("���޸���");
                } else {
                    notify_view_text.setText(String.format(getString(R.string.ss_pattern_update), count));
                }
                notify_view.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        notify_view.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        }, 1000);
    }


    /* 摧毁视图ͼ */
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        Log.d("onDestroyView", "channel_id = " + channel_id);
        mAdapter = null;
    }

    /* 摧毁该Fragment，一般是FragmentActivity被摧毁的时候伴随着摧毁 */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "channel_id = " + channel_id);
    }
}
