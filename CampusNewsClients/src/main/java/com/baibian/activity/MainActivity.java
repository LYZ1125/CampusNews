package com.baibian.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.R;
import com.baibian.activity.login.Login4Activity;
import com.baibian.adapter.Forums_Integration_Refresh_FootAdapter;
import com.baibian.adapter.NewsFragmentPagerAdapter;
import com.baibian.bean.ChannelItem;
import com.baibian.fragment.forums.IntegrationFragment;
import com.baibian.fragment.main.FindFragment;
import com.baibian.fragment.main.ForumsFragment;
import com.baibian.fragment.main.HomepageFragment;
import com.baibian.fragment.main.PeriodicalsFragment;
import com.baibian.tool.BaseTools;
import com.baibian.view.ColumnHorizontalScrollView;
import com.baibian.view.SlidingDrawerView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 *  模拟还原今日头条 --新闻阅读器
 * author:XZY && RA
 */
public class MainActivity extends FragmentActivity implements OnClickListener{

    /**
     * 四个碎片布局声明的变量
     */
    private HomepageFragment homepageFragment;//第一个碎片
    private ForumsFragment forumsFragment;//第二个碎片
    private FindFragment findFragment;//第三个碎片
    private PeriodicalsFragment periodicalsFragment;//第四个碎片
    private View fragmentLayout1;
    private View fragmentLayout2;
    private View fragmentLayout3;
    private View fragmentLayout4;
    private ImageView fragmentImage1;
    private ImageView fragmentImage2;
    private ImageView fragmentImage3;
    private ImageView fragmentImage4;
    private FragmentManager fragmentManager;
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
     * 用户选择的新闻分类列表
     */
    private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /**
     * 当前选中的栏目
     */
    private int columnSelectIndex = 0;
    /**
     * 左阴影部分
     */
    public ImageView shade_left;
    /**
     * 右阴影部分
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
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    public SlidingMenu side_drawer;
    /**
     * head 头部 的中间的loading
     */
    private ProgressBar top_progress;
    /**
     * head 头部 中间的刷新按钮
     */
    private ImageView top_refresh;
    /**
     * head 头部 的左侧菜单 按钮
     */
    private ImageView top_head;
    /**
     * head 头部 的右侧菜单 按钮
     */
    private ImageView top_more;
    /**
     * 头部的中间文字
     */
    private TextView top_text;
    /**
     * 请求CODE
     */
    public final static int CHANNELREQUEST = 1;
    /**
     * 调整返回的RESULTCODE
     */
    public final static int CHANNELRESULT = 10;
//    private static final int STATE_REFRESHING = 3;

    private NewsFragmentPagerAdapter mAdapter;
    private int position1 = 0;

    /**
     * 实现登陆后更改UI为圆形头像
     */
    private ImageView baibian_btn;//通过百辩账号登录
    private final int LOGIN4_REQUEST=11;//进入login4activity的请求码
    private LinearLayout logout_layout_not_login;//未登录布局
    private RelativeLayout login_layout;//带有圆形头像的布局，用来更换原来的布局

    /**
     * 引导界面添加内容
     */
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;//判断是否是第一次登陆使用
    private GestureDetector gestureDetector;
    private SwipeRefreshLayout integration_swiperefreshlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.setThreadPolicy(new
//                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(
//                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        setContentView(R.layout.main);
        View integrationFragment=View.inflate(getApplicationContext(),R.layout.integration_fragment_layout,null);
        integration_swiperefreshlayout=(SwipeRefreshLayout) integrationFragment.findViewById(R.id.integration_swiperefreshlayout);
        final Forums_Integration_Refresh_FootAdapter adapter=new Forums_Integration_Refresh_FootAdapter(getApplicationContext());
        gestureDetector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onDoubleTap(MotionEvent event)
            {
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
                 Toast.makeText(MainActivity.this, R.string.reflesh, Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(event);
            }
        });
        mScreenWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mScreenWidth / 4;// ???Item?????????1/4

        initSlidingMenu();

//        init_guide();//引导界面的初始化

        initViews();
        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);




    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        fragmentLayout1 = findViewById(R.id.fragment1_layout);
        fragmentLayout2 = findViewById(R.id.fragment2_layout);
        fragmentLayout3 = findViewById(R.id.fragment3_layout);
        fragmentLayout4 = findViewById(R.id.fragment4_layout);
        fragmentImage1 = (ImageView) findViewById(R.id.fragment1_img);
        fragmentImage2 = (ImageView) findViewById(R.id.fragment2_img);
        fragmentImage3 = (ImageView) findViewById(R.id.fragment3_img);
        fragmentImage4 = (ImageView) findViewById(R.id.fragment4_img);
        fragmentLayout1.setOnClickListener(this);
        fragmentLayout2.setOnClickListener(this);
        fragmentLayout3.setOnClickListener(this);
        fragmentLayout4.setOnClickListener(this);
        top_text=(TextView) findViewById(R.id.top_text);

        top_head = (ImageView) findViewById(R.id.top_head);
        top_more = (ImageView) findViewById(R.id.top_more);
        top_refresh = (ImageView) findViewById(R.id.top_refresh);
       top_progress = (ProgressBar) findViewById(R.id.top_progress);
        top_head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (side_drawer.isMenuShowing()) {
                    side_drawer.showContent();
                } else {
                    side_drawer.showMenu();
                }
            }
        });
        top_more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * if (side_drawer.isSecondaryMenuShowing()) {
                 side_drawer.showContent();
                 } else {
                 side_drawer.showSecondaryMenu();
                 }
                 */
                // TODO Auto-generated method stub

            }
        });
        fragmentLayout2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment1_layout:
                // 当点击了消息tab时，选中第1个tab
                InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(fragmentLayout1.getWindowToken(), 0);//这行代码隐藏软键盘
                top_text.setText(R.string.LunXun);
                setTabSelection(0);
                break;
            case R.id.fragment2_layout:
                // 当点击了联系人tab时，选中第2个tab
                InputMethodManager imm2 = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(fragmentLayout1.getWindowToken(), 0);//这行代码隐藏软键盘
                setTabSelection(1);
                top_text.setText(R.string.LunTan);

                break;
            case R.id.fragment3_layout:
                // 当点击了动态tab时，选中第3个tab
                InputMethodManager imm3 = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(fragmentLayout1.getWindowToken(), 0);//这行代码隐藏软键盘
                setTabSelection(2);
                top_text.setText(R.string.LunBa);

                break;
            case R.id.fragment4_layout:
                // 当点击了动态tab时，选中第3个tab
                InputMethodManager imm4 = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(fragmentLayout1.getWindowToken(), 0);//这行代码隐藏软键盘
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                fragmentImage1.setImageResource(R.mipmap.black_mipmap);
                fragmentLayout1.setBackgroundColor(getResources().getColor(R.color.main_layout_selected));
                if (homepageFragment == null) {
                    homepageFragment = new HomepageFragment();
                    transaction.add(R.id.content, homepageFragment);
                } else {
                    transaction.show(homepageFragment);
                }
                break;
            case 1:

                fragmentImage2.setImageResource(R.mipmap.black_mipmap);
                fragmentLayout2.setBackgroundColor(getResources().getColor(R.color.main_layout_selected));
                if (forumsFragment == null) {
                    forumsFragment = new ForumsFragment();
                    transaction.add(R.id.content, forumsFragment);
                } else {
                    transaction.show(forumsFragment);
                }
                break;
            case 2:
                fragmentImage3.setImageResource(R.mipmap.black_mipmap);
                fragmentLayout3.setBackgroundColor(getResources().getColor(R.color.main_layout_selected));
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    transaction.add(R.id.content, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case 3:
                fragmentImage4.setImageResource(R.drawable.periodicalsfragment_selected);
                fragmentLayout4.setBackgroundColor(getResources().getColor(R.color.main_layout_selected));
                if (periodicalsFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    periodicalsFragment = new PeriodicalsFragment();
                    transaction.add(R.id.content, periodicalsFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(periodicalsFragment);
                }
                break;
        }


        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        fragmentImage1.setImageResource(R.mipmap.black_mipmap);
//        fragmentLayout1.setBackgroundColor(getResources().getColor(R.color.main_layout1_unselected));
     //   fragmentLayout1.setBackgroundColor(getResources().getColor(
      //          R.color.mainFragment_background
      //  ));
        fragmentImage2.setImageResource(R.mipmap.black_mipmap);
//        fragmentLayout2.setBackgroundColor(getResources().getColor(R.color.main_layout1_unselected));
        fragmentImage3.setImageResource(R.mipmap.black_mipmap);
//        fragmentLayout3.setBackgroundColor(getResources().getColor(R.color.main_layout1_unselected));
        fragmentImage4.setImageResource(R.drawable.periodicalsfragment);
        fragmentLayout4.setBackgroundColor(getResources().getColor(R.color.main_layout1_unselected));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homepageFragment != null) {
            transaction.hide( homepageFragment);
        }
        if (forumsFragment != null) {
            transaction.hide(forumsFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (periodicalsFragment != null) {
            transaction.hide(periodicalsFragment);
        }
    }
//
//
//
//    /**
//     * 引导界面初始化部分
//     */
//    private void init_guide(){
//        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
//        if (preferences.getBoolean("firststart", true)) {
//            editor = preferences.edit();
//            //将登录标志位设置为false，下次登录时不在显示首次登录界面
//            editor.putBoolean("firststart", false);
//            editor.commit();
//            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//以后移走
    protected void initSlidingMenu() {
        side_drawer = new SlidingDrawerView(this).initSlidingMenu();

        /**
         * 登录切换顶部布局
         */
       login_layout=(RelativeLayout) side_drawer.findViewById(R.id.login_layout);
        baibian_btn=(ImageView) side_drawer.findViewById(R.id.baibian_btn);//百辩登录按钮
        logout_layout_not_login=(LinearLayout) side_drawer.findViewById(R.id.logout_layout_not_login);//未登录布局

        baibian_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_baibian_btn=new Intent(MainActivity.this,Login4Activity.class);
                startActivityForResult(intent_baibian_btn,LOGIN4_REQUEST);
            }
        });
        login_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,UsersImformationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (side_drawer.isMenuShowing() || side_drawer.isSecondaryMenuShowing()) {
                side_drawer.showContent();
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, R.string.Press_again_to_exit,
                            Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }
        //拦截MENU按钮事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {

            case LOGIN4_REQUEST:
                if(resultCode==LOGIN4_REQUEST){
                    /**
                     * 登录切换布局部分
                     */
                    logout_layout_not_login.setVisibility(View.GONE);//旧布局消失
                    login_layout.setVisibility(View.VISIBLE);//新布局出现
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
    * ?????????Scrollview?????????????????????   public boolean onTouchEvent(MotionEvent event){
     switch (event.getAction()){
     case MotionEvent.ACTION_DOWN:
     //????
     break;
     case MotionEvent.ACTION_MOVE:
     //???
     break;
     case MotionEvent.ACTION_UP:
     //???
     int y=(int) event.getY();
     if (y>=30d){
     imformation_viewPager.setVisibility(View.GONE);
     }
     if(y<=30d&&(imformation_viewPager.getVisibility()==View.VISIBLE)){
     imformation_viewPager.setVisibility(View.VISIBLE);
     }
     break;
     }
     return true;
     }
    */

}