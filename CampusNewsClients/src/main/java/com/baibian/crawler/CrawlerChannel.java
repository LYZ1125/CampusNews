package com.baibian.crawler;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.baibian.bean.ChannelManage;
import com.baibian.bean.NewsEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XZY on 2016/11/8.
 */

public class CrawlerChannel {

    private static final String BASE_URL = "http://112.74.107.202";
    private static final String BASE_URL1 = "http://blog.csdn.net";
    private static ArrayList<String> channelList = new ArrayList<String>();
    private static ArrayList<Elements> items = new ArrayList<Elements>();
    private static Map firstItems = new HashMap();
    private Activity channelActivity;
    //    因为会预加载下一页，此处不能用静态
    private int refreshPage = 1;
    private static Elements item;
    private static String channelURL;
//    private int lastIndex;

    public CrawlerChannel(final Activity channelActivity) {
        this.channelActivity = channelActivity;
    }

    public void getItem() {
//        lastIndex = 0;
        final String[] htmlStr = new String[1];
        new Thread() {
            public void run() {

                //暂时注销
//                htmlStr[0] = HttpTool.doGet(BASE_URL);


//                System.out.println(htmlStr[0]);
                Document doc = Jsoup.parse(htmlStr[0]);

//        String titleStr = nav2.text();

                //        doc.getElementById("");
//        doc.getelem
//        Elements loaded = doc.getElementsByClass("hb-loaded");
//                Elements body = doc.getElementsByTag("body");
//                System.out.println(body);
//                Elements body1 = doc.getElementsByClass("blog_category");
//        System.out.println(body1);
                Elements li = doc.getElementsByTag("li");
                //                Elements nav2 = nav.get(0).getElementsByClass("nav2");
//                Elements ul = nav2.get(0).getElementsByTag("ul");
//                Elements li = ul.get(0).getElementsByTag("li");
                int num = 1;
//-----------------------------如果当前没有网络连接的情况还没做
                if (li.size() == 0) {
//                    Toast.makeText(channelActivity, "��ǰû����������", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 8; i < 13; i++) {
                        Element child = li.get(i).child(0);
                        //                  Element child1 = li.get(i).child(1);
//                    System.out.println(child.attr("href"));
//                    String text2 = child.text();
                        String text = child.text();
                        String href = child.attr("href");
                        channelActivity.getSharedPreferences("channel", Context.MODE_PRIVATE).edit().putString(String.valueOf(num), text).commit();
//                    channelActivity.getSharedPreferences("channelSource", Context.MODE_PRIVATE).edit().putString(String.valueOf(num), BASE_URL1 + href).commit();
                        channelList.add(BASE_URL1 + href);
                        num++;
                        //                 text = child1.text();
                        //                 href = child1.attr("href");
                        channelActivity.getSharedPreferences("channel", Context.MODE_PRIVATE).edit().putString(String.valueOf(num), text).commit();
//                    channelActivity.getSharedPreferences("channelSource", Context.MODE_PRIVATE).edit().putString(String.valueOf(num), BASE_URL1 + href).commit();
                        channelList.add(BASE_URL1 + href);
                        num++;
                    }
//__________________在获得完整列表之后再初始化channel
                    new ChannelManage(channelActivity);

                    for (int i = 0; i < channelList.size(); i++) {
                        Log.d("1111", String.valueOf(111111));
                        channelURL = channelList.get(i);

                        //暂时注销
//                        htmlStr[0] = HttpTool.doGet(channelURL + "?&page=" + 1);

                        doc = Jsoup.parse(htmlStr[0]);
                        items.add(doc.getElementsByTag("dl"));
                    }
                }
            }
        }.start();
    }

    public void getChannel() {
//        lastIndex = 0;
        final String[] htmlStr = new String[1];
        new Thread() {
            public void run() {

                //暂时注销
//                htmlStr[0] = HttpTool.doGet(BASE_URL);

                Document doc = Jsoup.parse(htmlStr[0]);
                Elements li = doc.getElementsByTag("li");
                int num = 1;
                for (int i = 8; i < 13; i++) {
                    Element child = li.get(i).child(0);
                    Element child1 = li.get(i).child(1);
                    String text = child.text();
                    String href = child.attr("href");
                    channelActivity.getSharedPreferences("channel", Context.MODE_PRIVATE).edit().putString(String.valueOf(num), text).commit();
                    channelList.add(BASE_URL1 + href);
                    num++;
                    text = child1.text();
                    href = child1.attr("href");
                    channelActivity.getSharedPreferences("channel", Context.MODE_PRIVATE).edit().putString(String.valueOf(num), text).commit();
                    channelList.add(BASE_URL1 + href);
                    num++;
                }
            }
        }.start();
    }

    public interface OnRefreshListener {
        public void refreshItem(int i);
        public void pullRefreshItem(int i);
    }

    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }


    public NewsEntity ConstantsAdapter(final int itemId, final int channelId, final NewsEntity news) {
        final String[] htmlStr = new String[1];
        final int index = channelId - 1;
        Log.d("img11", String.valueOf(itemId));
        if (itemId % 30 == 0 && itemId != 0) {
            refreshPage++;
        }
        new Thread() {
            public void run() {
              /*等待直到items加载出了前两页*/
                while (items.size() <= 7) {
//                    Log.d("1111", String.valueOf(111111));
                }
                item = items.get(index);
                List<String> url_list = new ArrayList<String>();
                //                以下if内用于加载下一个网页
                if (itemId % 30 == 0 && itemId != 0) {
//                    refreshPage++;
//                    Log.d("itemsize", String.valueOf(item.size() - item.size()%30));
//                    Log.d("url", channelList.get(index) + "?&page=" + refreshPage);

                    //暂时注销
//                    htmlStr[0] = HttpTool.doGet(channelList.get(index) + "?&page=" + refreshPage);

                    Document doc = Jsoup.parse(htmlStr[0]);

                    item.addAll(item.size() - item.size() % 30, doc.getElementsByTag("dl"));
//                    Log.d("itemsize1", String.valueOf(item.size()));
                }

//                    int indexOf = refreshItem.indexOf(item.get(0));
//                  /*如果原有的第一个item在新的RefreshItem中不存在，那就彻底初始化重来*/
//                    if (indexOf != -1) {
//                        item = refreshItem;
//                    }
//                      /*如果原有的第一个item在新的RefreshItem中存在，那就把新多出来的item放进苑item的头部*/
//                    else {
//                        for (int i = 0; i <= refreshItem.size()-indexOf; i++) {
//                            refreshItem.remove(indexOf);
//                        }
//                        item.addAll(0, refreshItem);
//                        mListener.refreshItem(0);
//                    }
//                } else {
                   /*下拉了30个item之后，等待，直到item加载完了一个完整的有30个item的网页，
                    * 如果不等，可能下一页的item还没加上，有的item的初始化已经到到这里
                    * */
//                    Log.d("1@@@1", String.valueOf(refreshPage));
                while (item.size() / 30 < refreshPage && itemId >= 30 * (refreshPage - 1)) {
                }
                Element element = item.get(itemId);
                Elements a = element.getElementsByTag("a");

                url_list.add(a.get(0).getElementsByTag("img").get(0).attr("src"));
//                Log.d("img", a.get(0).attr("src"));
//                news.setPicOne(a.get(0).attr("src"));
                news.setPicList(url_list);

//                Log.d("img222", String.valueOf(itemId));
                news.setSource_url(a.get(2).attr("href"));

                news.setNewsAbstract(element.getElementsByClass("blog_list_c").text());
                Elements label = element.getElementsByTag("label");
                //                    如果是第一个项目，将其title存下，用于后面比较是否需要刷新
                if (itemId == 0) {
                    firstItems.put(index, a.get(2).text());
                }
                if (label.size() == 0) {
                    news.setTitle(a.get(1).text());
                    news.setPublishTime(" ");
                } else {
                    news.setSource(a.get(1).text());
                    news.setTitle(a.get(2).text());
                    news.setPublishTime(String.valueOf(label.get(0).text()));
                }
                Elements em = element.getElementsByTag("em");
                if (em.size() == 0) {
                    news.setCommentNum(0);
                } else {
                    news.setCommentNum(Integer.valueOf(element.getElementsByTag("em").get(0).text()));
                }
//                Log.d("img", String.valueOf(index));
//                    某些时候mlistener可能为空，必须判断。具体原因还不清楚
//                每当获得一个newsItem之后就回调刷新一次
                if (mListener != null) {
                    mListener.refreshItem(1);
                }
            }
//                }
//                    System.out.println(item);
//                for (int i = 0; i < item.size(); i++) {
//                    System.out.println(item.get(i));
//                    System.out.println("-----------------------"+ i + "-----------------------------------");
//                }
//            }
        }.start();
        return news;
    }

    //                下拉刷新功能模块
    public void pullToRefresh(final int channelId) {
        final String[] htmlStr = new String[1];
        final int index = channelId - 1;

        new Thread() {
            public void run() {
                ArrayList<String> titleList = new ArrayList<String>();

//                    重新获得一次第一页的item列表

//                 //暂时注销
//                htmlStr[0] = HttpTool.doGet(channelList.get(index) + "?&page=" + 1);


                Document doc = Jsoup.parse(htmlStr[0]);
                Elements refreshItem = doc.getElementsByTag("dl");
                for (int i = 0; i < refreshItem.size(); i++) {
                    Element element = refreshItem.get(i);
                    Elements a = element.getElementsByTag("a");
                    Elements label = element.getElementsByTag("label");
                    if (label.size() != 0) {
                        titleList.add(a.get(2).text());
                    }
                }
//                Log.d("refresh", String.valueOf(titleList));
//                Log.d("index", String.valueOf(index));
//                Log.d("item", String.valueOf(firstItems.get(index)));
//                一下这句无效！因为viewpager预加载了下一页，这里的item不再是第一页的item了，而是第二页的。所以
//                不论怎样都是-1
//                int indexOf = refreshItem.indexOf(item.get(0));
//                ��Ϊ��
                int indexOf = titleList.indexOf(firstItems.get(index));
                items.get(index).clear();
                items.get(index).addAll(0, refreshItem);
                Log.d("indexOf", String.valueOf(indexOf));
                if (indexOf != -1){
                    mListener.pullRefreshItem(indexOf);
                } else {
                    mListener.pullRefreshItem(30);
                }
            }
        }.start();
    }

}