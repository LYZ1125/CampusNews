package com.baibian.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.activity.setting.SettingsActivity;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.baibian.activity.login.Login4Activity;
import com.baibian.R;

import java.util.List;

/**
 * 自定义SlidingMenu侧拉菜单类
 * */
public class SlidingDrawerView implements OnClickListener{

	/**
	 * 设置颜色更改时定义
	 */
	private  LinearLayout left_drawer_ScrollView;
	private LinearLayout left_drawer_all;//左侧侧滑菜单的全布局，用这个来设置背景颜色
	private TextView left_drawer_top_text;//左侧顶部的 登录后，将推荐给你更多感兴趣的文章  字体
	/**
	 * 左侧侧滑菜单Srollview的item
	 */
	//字体部分



	private final Activity activity;
	SlidingMenu localSlidingMenu;
	private ImageView baibian_btn;//左侧侧滑菜单中百辩登录的按钮
	private LinearLayout left_top_layout;
	private AlertDialog alert;
	private AlertDialog.Builder builder;
    private ViewPager profile_drawer_right_viewpager;
	private List<View> viewList;
	private ActionSheetDialog actionSheetDialog;
	String[] items;
	private LinearLayout logout_layout_not_login;//未登录布局
	private LinearLayout login_layout;//带有圆形头像的布局，用来更换原来的布局
	private RelativeLayout setting_btn_layout;



	protected final int REQUESTCODE=11;//baibian_btn请求码
	public SlidingDrawerView(Activity activity) {
		this.activity = activity;
	}

	public SlidingMenu initSlidingMenu() {
		localSlidingMenu = new SlidingMenu(activity);
		localSlidingMenu.setMode(SlidingMenu.LEFT);//设置左右滑菜单
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//设置要使菜单滑动，触碰屏幕的范围
//		localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//设置了这个会获取不到菜单里面的焦点，所以先注释掉
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);//设置阴影图片的宽度
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);//设置阴影图片
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu划出时主页面显示的剩余宽度
		localSlidingMenu.setFadeDegree(0.35F);//SlidingMenu滑动时渐变程度
		localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);//使SlidingMenu附加在Activity右边
//		localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
//		1
		localSlidingMenu.setMenu(R.layout.left_drawer_fragment);//设置menu布局文件
//		localSlidingMenu.toggle();//动态判断自动关闭或开启SlidingMenu
//		2
//		localSlidingMenu.setSecondaryMenu(R.layout.profile_drawer_right);
//		localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		/**
		 * 退出对话框
		 */




		localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
					public void onOpened() {
						
					}
				});
		localSlidingMenu.setOnClosedListener(new OnClosedListener() {
			
			@Override
			public void onClosed() {
				// TODO Auto-generated method stub
				
			}
		});
		initView();
		return localSlidingMenu;
	}
	private void initView() {
		baibian_btn=(ImageView) localSlidingMenu.findViewById(R.id.baibian_btn);
		left_top_layout=(LinearLayout)localSlidingMenu .findViewById(R.id.left_top_layout);
		left_drawer_all=(LinearLayout)localSlidingMenu.findViewById(R.id.left_drawer_all);
		left_drawer_ScrollView=(LinearLayout) localSlidingMenu.findViewById(R.id.left_drawer_ScrollView);
		setting_btn_layout=(RelativeLayout) localSlidingMenu.findViewById(R.id.setting_btn_layout);
		left_drawer_top_text=(TextView) localSlidingMenu.findViewById(R.id.left_drawer_top_text);
		baibian_btn.setOnClickListener(this);
		setting_btn_layout.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.baibian_btn:
			Intent intent_baibian_btn=new Intent(activity,Login4Activity.class);
			activity.startActivityForResult(intent_baibian_btn,REQUESTCODE);
			break;
			case R.id.setting_btn_layout:
				Intent intent_setting_btn_layout=new Intent(activity, SettingsActivity.class);
				activity.startActivity(intent_setting_btn_layout);
		default:
			break;
		}
	}
}
