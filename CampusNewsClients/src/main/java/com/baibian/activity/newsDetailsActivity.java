package com.baibian.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baibian.R;
import com.baibian.base.BaseActivity;
import com.baibian.bean.NewsEntity;
import com.baibian.service.NewsDetailsService;

import java.util.ArrayList;

@SuppressLint("JavascriptInterface")
public class newsDetailsActivity extends BaseActivity {
    private TextView title;
    private ProgressBar progressBar;
    private FrameLayout customview_layout;
    private String news_url;
    private String news_title;
    private String news_source;
//    private String news_date;
    private NewsEntity news;
    private TextView action_comment_count;
    WebView webView;
    private ImageButton toolFavor;
    private ImageView toolRepost;
    private ImageView toolShare;
    private ImageView toolViewComment;
    private ImageView toolWriteComment;
//    private boolean isSelected = false;
//    private boolean collectStatus;
    private final static  int COLLECTED = 1;
    private final static  int NON_COLLECTED = 0;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        setNeedBackGesture(true);//������Ҫ���Ƽ���
        getData();
        initView();
        initWebView();
    }

    /* 获取传递过来的数据 */
    private void getData() {
        news = (NewsEntity) getIntent().getSerializableExtra("news");
//        collectStatus = getIntent().getBooleanExtra("collectStatus", false);
        itemId = getIntent().getIntExtra("itemId", -1);
        news_url = news.getSource_url();
        news_title = news.getTitle();
        news_source = news.getSource();
//        news_date = DateTools.getNewsDetailsDate(String.valueOf(news.getPublishTime()));
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.wb_details);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (!TextUtils.isEmpty(news_url)) {
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);//设置可以运行JS脚本
//			settings.setTextZoom(120);//Sets the text zoom of the page in percent. The default is 100.
            settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//			settings.setUseWideViewPort(true); //打开页面时， 自适应屏幕
//			settings.setLoadWithOverviewMode(true);//打开页面时， 自适应屏幕
            settings.setSupportZoom(false);// 用于设置webview放大
            settings.setBuiltInZoomControls(false);
            webView.setBackgroundResource(R.color.transparent);
            //添加js交互接口类，并起别名 imagelistner
            webView.addJavascriptInterface(new JavascriptInterface(getApplicationContext()), "imagelistner");
            webView.setWebChromeClient(new MyWebChromeClient());
            webView.setWebViewClient(new MyWebViewClient());
            webView.loadUrl(news_url);
//            new MyAsnycTask().execute(news_url, news_title, news_source + " " + news_date);
        }
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        progressBar = (ProgressBar) findViewById(R.id.ss_htmlprogessbar);
        customview_layout = (FrameLayout) findViewById(R.id.customview_layout);
        //底部栏目
        action_comment_count = (TextView) findViewById(R.id.action_comment_count);

        toolFavor = (ImageButton) findViewById(R.id.action_favor);
        toolRepost = (ImageView) findViewById(R.id.action_report);
        toolShare = (ImageView) findViewById(R.id.action_share);
        toolViewComment = (ImageView) findViewById(R.id.action_view_comment);
        toolWriteComment = (ImageView) findViewById(R.id.action_write_comment);
//        Log.d("boolean11", String.valueOf(news.getCollectStatus()));
        Log.d("newsId2", String.valueOf(news));

        if (getSharedPreferences("collect", Context.MODE_PRIVATE).getBoolean(String.valueOf(itemId), false)) {
            toolFavor.setImageResource(R.drawable.ic_action_favor_on_normal);
        }else{
            toolFavor.setImageResource(R.drawable.ic_action_favor_pressed);
        }
//收藏功能还没有关联起来！！！
        toolFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("boolean", String.valueOf(news.getCollectStatus()));
//                Log.d("newsId", String.valueOf(news));

                if (getSharedPreferences("collect", Context.MODE_PRIVATE).getBoolean(String.valueOf(itemId), false)) {
//                    news.setCollectStatus(false);
                    getSharedPreferences("collect", Context.MODE_PRIVATE).edit().putBoolean(String.valueOf(itemId), false).commit();
                    setResult(NON_COLLECTED);
                    toolFavor.setImageResource(R.drawable.ic_action_favor_pressed);
                }else{
//                    news.setCollectStatus(true);
                    getSharedPreferences("collect", Context.MODE_PRIVATE).edit().putBoolean(String.valueOf(itemId), true).commit();
                    setResult(COLLECTED);
                    toolFavor.setImageResource(R.drawable.ic_action_favor_on_normal);
                }
//                Log.d("boolean", String.valueOf(news.getCollectStatus()));
            }
        });

        toolWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        toolViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        toolShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        toolRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        progressBar.setVisibility(View.VISIBLE);
        title.setTextSize(13);
        title.setVisibility(View.VISIBLE);
        title.setText(news_url);
        action_comment_count.setText(String.valueOf(news.getCommentNum()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class MyAsnycTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            String data = NewsDetailsService.getNewsDetails(urls[0], urls[1], urls[2]);
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
        }
    }

    //注入js函数监听
    private void addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\");"
                + "var imgurl=''; " + "for(var i=0;i<objs.length;i++)  " + "{"
                + "imgurl+=objs[i].src+',';"
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imagelistner.openImage(imgurl);  "
                + "    }  " + "}" + "})()");
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        public void openImage(String img) {

            //
            String[] imgs = img.split(",");
            ArrayList<String> imgsUrl = new ArrayList<String>();
            for (String s : imgs) {
                imgsUrl.add(s);
                Log.i("图片的URL>>>>>>>>", s);
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra("infos", imgsUrl);
            intent.setClass(context, ImageShowActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    // ����
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            // html�������֮����Ӽ���ͼƬ�ĵ��js����
            addImageClickListener();
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            progressBar.setVisibility(View.GONE);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // TODO Auto-generated method stub
            if (newProgress != 100) {
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
