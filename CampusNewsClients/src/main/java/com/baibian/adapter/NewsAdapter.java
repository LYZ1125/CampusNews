package com.baibian.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.baibian.R;
import com.baibian.bean.NewsEntity;
import com.baibian.tool.Constants;
import com.baibian.tool.DateTools;
import com.baibian.tool.Options;
import com.baibian.view.HeadListView.HeaderAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsAdapter extends BaseAdapter implements SectionIndexer, HeaderAdapter {
    ArrayList<NewsEntity> newsList;
    Activity activity;
    LayoutInflater inflater = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
       /**
     * 弹出更多选择框
     */
    private PopupWindow popupWindow;
    private View popDislike;
    private View popFavor;
    private TextView popTextFavor;
    private int currentItem;
    private View view;
    //    private List<Integer> isCollect;
//    private View view;

    public NewsAdapter(Activity activity, ArrayList<NewsEntity> newsList) {
        this.activity = activity;
        this.newsList = newsList;
//        this.isCollect = isCollect;
        inflater = LayoutInflater.from(activity);
        options = Options.getListOptions();
        initPopWindow();
        initDateHead();
    }

    /* 是不是城市频道，true是，false不是*/
    public boolean isCityChannel = false;
    /* 是不是第一个ITEM，true：是，false:不是*/
    public boolean isFirst = true;
    private List<Integer> mPositions;
    private List<String> mSections;

    private void initDateHead() {
        mSections = new ArrayList<String>();
        mPositions = new ArrayList<Integer>();
        for (int i = 0; i < newsList.size(); i++) {
            if (i == 0) {
                mSections.add(DateTools.getSection(newsList.get(i).getRefreshTime()));
                mPositions.add(i);
                continue;
            }
            if (i != newsList.size()) {
                if (newsList.get(i).getRefreshTime() != newsList.get(i - 1).getRefreshTime()) {
                    mSections.add(DateTools.getSection(newsList.get(i).getRefreshTime()));
                    mPositions.add(i);
                }
            }
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public NewsEntity getItem(int position) {
        // TODO Auto-generated method stub
        if (newsList != null && newsList.size() != 0 && position >= 0) {
            return newsList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder;
        view = convertView;
//        Log.d("position", String.valueOf(position));
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
            mHolder = new ViewHolder();
            mHolder.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
            mHolder.comment_layout = (RelativeLayout) view.findViewById(R.id.comment_layout);
            mHolder.item_title = (TextView) view.findViewById(R.id.item_title);
            mHolder.item_source = (TextView) view.findViewById(R.id.item_source);
            mHolder.list_item_local = (TextView) view.findViewById(R.id.list_item_local);
            mHolder.comment_count = (TextView) view.findViewById(R.id.comment_count);
            mHolder.publish_time = (TextView) view.findViewById(R.id.publish_time);
            mHolder.item_abstract = (TextView) view.findViewById(R.id.item_abstract);
            mHolder.alt_mark = (ImageView) view.findViewById(R.id.alt_mark);
            mHolder.right_image = (ImageView) view.findViewById(R.id.right_image);
            mHolder.item_image_layout = (LinearLayout) view.findViewById(R.id.item_image_layout);
            mHolder.item_image_0 = (ImageView) view.findViewById(R.id.item_image_0);
            mHolder.item_image_1 = (ImageView) view.findViewById(R.id.item_image_1);
            mHolder.item_image_2 = (ImageView) view.findViewById(R.id.item_image_2);
            mHolder.large_image = (ImageView) view.findViewById(R.id.large_image);
            mHolder.popicon = (ImageView) view.findViewById(R.id.popicon);
            mHolder.comment_content = (TextView) view.findViewById(R.id.comment_content);
            mHolder.right_padding_view = (View) view.findViewById(R.id.right_padding_view);
            //头部的日期部分
            mHolder.layout_list_section = (LinearLayout) view.findViewById(R.id.layout_list_section);
            mHolder.section_text = (TextView) view.findViewById(R.id.section_text);
            mHolder.section_day = (TextView) view.findViewById(R.id.section_day);

            view.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        //获取position对应的数据
        NewsEntity news = getItem(position);
        mHolder.item_title.setText(news.getTitle());
        mHolder.item_source.setText(news.getSource());
        mHolder.comment_count.setText("评论" + news.getCommentNum());
//        String strTime_hm = DateTools.refreshTime(activity.getSharedPreferences("publishTime", Context.MODE_PRIVATE).getLong("lastTime", -1) - news.getPublishTime());
//        Log.d("time!!!", String.valueOf(news.getPublishTime() - activity.getSharedPreferences("publishTime", Context.MODE_PRIVATE).getLong("lastTime", -1)));
        mHolder.publish_time.setText(news.getPublishTime());
        List<String> imgUrlList = news.getPicList();

        mHolder.popicon.setVisibility(View.VISIBLE);
        mHolder.comment_count.setVisibility(View.VISIBLE);
        mHolder.right_padding_view.setVisibility(View.VISIBLE);
        if (imgUrlList != null && imgUrlList.size() != 0) {
//            System.out.println(imgUrlList.get(0));

            if (imgUrlList.size() == 1) {
//                mHolder.item_image_layout.setVisibility(View.GONE);
                //是否是大图
                if (news.getIsLarge()) {
                    mHolder.large_image.setVisibility(View.VISIBLE);
                    mHolder.right_image.setVisibility(View.GONE);
                    imageLoader.displayImage(imgUrlList.get(0), mHolder.large_image, options);
                    mHolder.popicon.setVisibility(View.GONE);
                    mHolder.comment_count.setVisibility(View.GONE);
                    mHolder.right_padding_view.setVisibility(View.GONE);
                    mHolder.item_image_layout.setVisibility(View.GONE);
                } else {
                    mHolder.large_image.setVisibility(View.GONE);
                    mHolder.right_image.setVisibility(View.VISIBLE);
                    mHolder.item_image_layout.setVisibility(View.GONE);
                    imageLoader.displayImage(imgUrlList.get(0), mHolder.right_image, options);
                }
            } else {
                mHolder.large_image.setVisibility(View.GONE);
                mHolder.right_image.setVisibility(View.GONE);
                mHolder.item_image_layout.setVisibility(View.VISIBLE);
//                mHolder.item_image_layout.setVisibility(View.GONE);
                imageLoader.displayImage(imgUrlList.get(0), mHolder.item_image_0, options);
                imageLoader.displayImage(imgUrlList.get(1), mHolder.item_image_1, options);
                imageLoader.displayImage(imgUrlList.get(2), mHolder.item_image_2, options);
            }
        } else {
            mHolder.right_image.setVisibility(View.GONE);
            mHolder.item_image_layout.setVisibility(View.GONE);
        }
        int markResID = getAltMarkResID(news.getMark(), position);
        if (markResID != -1) {
            mHolder.alt_mark.setVisibility(View.VISIBLE);
            mHolder.alt_mark.setImageResource(markResID);
        } else {
            mHolder.alt_mark.setVisibility(View.GONE);
        }
        //判断该新闻概述是否为空
        if (!TextUtils.isEmpty(news.getNewsAbstract())) {
            mHolder.item_abstract.setVisibility(View.VISIBLE);
            mHolder.item_abstract.setText(news.getNewsAbstract());
        } else {
            mHolder.item_abstract.setVisibility(View.GONE);
        }
        //判断该新闻时候是特殊标记的，推广等，为空就是新闻
        if (!TextUtils.isEmpty(news.getLocal())) {
            mHolder.list_item_local.setVisibility(View.VISIBLE);
            mHolder.list_item_local.setText(news.getLocal());
        } else {
            mHolder.list_item_local.setVisibility(View.GONE);
        }
        //判断评论子弹是否为空，不为空显示对应布局
        if (!TextUtils.isEmpty(news.getComment())) {
            //news.getLocal() != null &&
            mHolder.comment_layout.setVisibility(View.VISIBLE);
            mHolder.comment_content.setText(news.getComment());
        } else {
            mHolder.comment_layout.setVisibility(View.GONE);
        }
        //判断该新闻是否已读
//        if (!news.getReadStatus()) {
////            mHolder.item_layout.setSelected(true);
//        } else {
//            mHolder.item_title.setTextColor(Color.GRAY);
////            mHolder.item_layout.setSelected(false);
//        }
        Log.d("thisPosition", String.valueOf(getItem(position).getId()));
        if (!activity.getSharedPreferences("publishTime", Context.MODE_PRIVATE).getBoolean(String.valueOf(getItem(position).getId()), false)) {
            mHolder.item_title.setTextColor(Color.BLACK);
        }else{
            mHolder.item_title.setTextColor(Color.GRAY);
        }
        //设置+按钮点击效果
        mHolder.popicon.setOnClickListener(new popAction(position));
        //头部的相关东西
        int section = getSectionForPosition(position);
        if (getPositionForSection(section) == position) {
            mHolder.layout_list_section.setVisibility(View.VISIBLE);
//			head_title.setText(news.getDate());
            mHolder.section_text.setText(mSections.get(section));
//            mHolder.section_text.setText("2016.11.12");
            mHolder.section_day.setText("今天");
        } else {
            mHolder.layout_list_section.setVisibility(View.GONE);
        }
        return view;
    }

    static class ViewHolder {
        LinearLayout item_layout;
        //title
        TextView item_title;
        //图片源
        TextView item_source;
        //类似推广之类的标签
        TextView list_item_local;
        //评论数量
        TextView comment_count;
        //发布时间
        TextView publish_time;
        //新闻摘要
        TextView item_abstract;
        //右上方TAG标记图片
        ImageView alt_mark;
        //右边图片
        ImageView right_image;
        //3张图片布局
        LinearLayout item_image_layout; //3张图片时候的布局
        ImageView item_image_0;
        ImageView item_image_1;
        ImageView item_image_2;
        //大图的图片的话布局
        ImageView large_image;
        //pop按钮
        ImageView popicon;
        //评论布局
        RelativeLayout comment_layout;
        TextView comment_content;
        //头部的日期部分
        View right_padding_view;

        //ͷ�������ڲ���
        LinearLayout layout_list_section;
        TextView section_text;
        TextView section_day;
    }

    /**
     * 根据属性获取对应的资源ID
     */
    public int getAltMarkResID(int mark, int position) {
        if (activity.getSharedPreferences("collect", Context.MODE_PRIVATE).getBoolean(String.valueOf(getItem(position).getId()), false)) {
//            Log.d("mark", String.valueOf(mark));
            return R.drawable.ic_mark_favor;
        }
        switch (mark) {
            case Constants.mark_recom:
                return R.drawable.ic_mark_recommend;
            case Constants.mark_hot:
                return R.drawable.ic_mark_hot;
            case Constants.mark_frist:
                return R.drawable.ic_mark_first;
            case Constants.mark_exclusive:
                return R.drawable.ic_mark_exclusive;
            case Constants.mark_favor:
                return R.drawable.ic_mark_favor;
            default:
                break;
        }
        return -1;
    }

    /**
     * popWindow 关闭按钮
     */
    private ImageView btn_pop_close;

    /**
     *初始化弹出的pop
     * //     * @param position
     */
    private void initPopWindow() {
        View popView = inflater.inflate(R.layout.list_item_pop, null);
        popupWindow = new PopupWindow(popView, AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
        popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
        btn_pop_close = (ImageView) popView.findViewById(R.id.btn_pop_close);
        popDislike = popView.findViewById(R.id.ll_pop_dislike);
        popFavor = popView.findViewById(R.id.ll_pop_favor);
        popTextFavor = (TextView) popView.findViewById(R.id.tv_pop_favor);
        Log.d("popposition",String.valueOf(getItem(currentItem).getId()));
//        Log.d("popPosition",String.valueOf(currentItem));
        if ((activity.getSharedPreferences("collect", Context.MODE_PRIVATE).getBoolean(String.valueOf(getItem(currentItem).getId()), false))) {
            Drawable drawable = popFavor.getResources().getDrawable(R.drawable.listpage_more_like_seleted_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            popTextFavor.setCompoundDrawables(drawable, null, null, null);
            popTextFavor.setText("���ղ�");
        }else{
            Drawable drawable = popFavor.getResources().getDrawable(R.drawable.listpage_more_like_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            popTextFavor.setCompoundDrawables(drawable, null, null, null);
            popTextFavor.setText("已收藏");
        }
    }

    /**
     * 显示popWindow
     */
    public void showPop(View parent, int x, int y, final int currentItem) {
        initPopWindow();
        //设置popwindow显示位置
        popupWindow.showAtLocation(parent, 0, x, y);
        //获取popwindow焦点
        popupWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        if (popupWindow.isShowing()) {

        }
        btn_pop_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                popupWindow.dismiss();
            }
        });

        popFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("newsId3", String.valueOf(getItem(position)));

//                isCollect.add(getItem(position).getId());
//                Log.d("itemId1111", String.valueOf(getItem(currentItem).getId()));
                if (!activity.getSharedPreferences("collect", Context.MODE_PRIVATE).getBoolean(String.valueOf(getItem(currentItem).getId()), false)) {
//                    ��������޷���Ч
//                    getItem(currentItem).setCollectStatus(true);
                    activity.getSharedPreferences("collect", Context.MODE_PRIVATE).edit().putBoolean(String.valueOf(getItem(currentItem).getId()), true).commit();
                    Drawable drawable = popFavor.getResources().getDrawable(R.drawable.listpage_more_like_seleted_normal);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    popTextFavor.setCompoundDrawables(drawable, null, null, null);
                    popTextFavor.setText("已收藏");
                } else {
                    //��������޷���Ч
//                    getItem(currentItem).setCollectStatus(false);
                    activity.getSharedPreferences("collect", Context.MODE_PRIVATE).edit().putBoolean(String.valueOf(getItem(currentItem).getId()), false).commit();
                    Drawable drawable = popFavor.getResources().getDrawable(R.drawable.listpage_more_like_normal);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    popTextFavor.setCompoundDrawables(drawable, null, null, null);
                    popTextFavor.setText("�ղ�");
//                    notifyDataSetChanged();
                }
            }
        });

        popDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left);
//                view.startAnimation(animation);
                newsList.remove(getItem(currentItem));
                popupWindow.dismiss();

                notifyDataSetChanged();
            }
        });
    }

    /**
     * 每个ITEM中more按钮对应的点击动作
     */
    public class popAction implements View.OnClickListener {
        int position;

        public popAction(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int[] arrayOfInt = new int[2];
            //获取点击按钮的坐标

            v.getLocationOnScreen(arrayOfInt);
//            int indexOf = newsList.indexOf(v.getParent());
//            Log.d("index", String.valueOf(position));
            currentItem = position;
            int x = arrayOfInt[0];
            int y = arrayOfInt[1];
            showPop(v, x, y, currentItem);
        }
    }

    /*
      * 设置是不是特殊的频道（城市频道）
      */
    public void setCityChannel(boolean iscity) {
        isCityChannel = iscity;
    }


    @Override
    public int getHeaderState(int position) {
        // TODO Auto-generated method stub
        int realPosition = position;
        if (isCityChannel) {
            if (isFirst) {
                return HEADER_GONE;
            }
        }
        if (realPosition < 0 || position >= getCount()) {
            return HEADER_GONE;
        }
        int section = getSectionForPosition(realPosition);
        int nextSectionPosition = getPositionForSection(section + 1);
        if (nextSectionPosition != -1
                && realPosition == nextSectionPosition - 1) {
            return HEADER_PUSHED_UP;
        }
        return HEADER_VISIBLE;
    }

    @Override
    public void configureHeader(View header, int position, int alpha) {
        int realPosition = position;
        int section = getSectionForPosition(realPosition);
        String title = (String) getSections()[section];
        ((TextView) header.findViewById(R.id.section_text)).setText(title);
        ((TextView) header.findViewById(R.id.section_day)).setText("����");
    }

    @Override
    public Object[] getSections() {
        // TODO Auto-generated method stub
        return mSections.toArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= mPositions.size()) {
            return -1;
        }
        return mPositions.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= getCount()) {
            return -1;
        }
        int index = Arrays.binarySearch(mPositions.toArray(), position);
        return index >= 0 ? index : -index - 2;
    }
}
