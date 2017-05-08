package com.baibian.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baibian.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 能够实现上啦刷新，下滑加载更多的adapter
 */
public class Discussion_FootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MyItemClickListener mItemClickListener;

    private Context mContext;
    private ScheduledExecutorService scheduledExecutorService;
    private Handler handler;
    private int currentItem = 0;
    private List<ImageView> imageViews;
    private int[] imageResId;
    private List<View> dots;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0

    //   public static final int FIRST_ITEM = 2;//第一个子项更改的请求

    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private List<String> mTitles = null;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private LikeButton like_btn;
    private LikeButton collect_btn;
    private Button respond_btn;
    public Discussion_FootAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mTitles = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            int index = i + 1;
            mTitles.add("item" + index);
        }

    }

    /**
     * item显示类型
     *
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.disscussion_item_layout, parent, false);
            collect_btn=(LikeButton) view.findViewById(R.id.collect_btn);
            like_btn=(LikeButton) view.findViewById(R.id.like_btn);
            respond_btn=(Button) view.findViewById(R.id.respond_btn);
            respond_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            like_btn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                }

                @Override
                public void unLiked(LikeButton likeButton) {

                }
            });
            collect_btn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                }

                @Override
                public void unLiked(LikeButton likeButton) {

                }
            });
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view,mItemClickListener);
            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.recycler_load_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }




    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText(R.string.load_more);
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText(R.string.loading_more);
                    break;
            }
        }
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mTitles.size() ;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public float itemwidth;
        private LinearLayout forums_item_all_layout;
        public TextView item_tv;
        private TextView progress_text1;
        private TextView progress_text2;
        private TextView progress_text3;
        private RelativeLayout progress_relativelayout1;

        private RelativeLayout   progress_relativelayout2;
        private RelativeLayout   progress_relativelayout3;

        private  MyItemClickListener mListener;
        //   private ProgressBar forums_item_progressBar;
        public ItemViewHolder(View view) {
            super(view);
            item_tv = (TextView) view.findViewById(R.id.item_tv);
            //  forums_item_progressBar=(ProgressBar) view.findViewById(R.id.forums_item_progressBar);
        }
        public ItemViewHolder(View itemView,MyItemClickListener myItemClickListener){
            super(itemView);

            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
            forums_item_all_layout=(LinearLayout) itemView.findViewById(R.id.forums_item_all_layout);
           //   forums_item_progressBar=(ProgressBar) itemView.findViewById(R.id.forums_item_progressBar);
            this.mListener=myItemClickListener;
            itemView.setOnClickListener(this);
        }
        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }



    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;
        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
        }
    }

    //添加数据
    public void addItem(List<String> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mTitles);
        mTitles.removeAll(mTitles);
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<String> newDatas) {
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    /***
     *
     */

    public void initImagesId() {
        imageResId = new int[]{R.drawable.ads0, R.drawable.ads1,
                R.drawable.ads2, R.drawable.ads3, R.drawable.ads4};
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

    public void initDots(View mViewPage) {
        dots = new ArrayList<View>();
        dots.add(mViewPage.findViewById(R.id.v_dot0));
        dots.add(mViewPage.findViewById(R.id.v_dot1));
        dots.add(mViewPage.findViewById(R.id.v_dot2));
        dots.add(mViewPage.findViewById(R.id.v_dot3));
        dots.add(mViewPage.findViewById(R.id.v_dot4));
    }

    @SuppressLint("HandlerLeak")
    public void initChangePicHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                if (msg.what == 1002) {

                }
            }

            ;
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
    /**
     *
     */




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;

        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}



