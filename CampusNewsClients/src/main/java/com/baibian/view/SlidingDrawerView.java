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
	private  TextView left_search_text;//左侧搜索的字体
	private TextView left_favorite_text;
	private TextView message_text;
	private  TextView offline_btn_text;
	private TextView app_activity_text;
	private TextView left_drawer_setting_text;
	private TextView left_drawer_feedback_text;
	private TextView left_drawer_appstore_text;



	private final Activity activity;
	SlidingMenu localSlidingMenu;
	private ImageView baibian_btn;//左侧侧滑菜单中百辩登录的按钮
	private LinearLayout left_top_layout;
	private SwitchButton night_mode_btn;
	private TextView night_mode_text;
	private RelativeLayout setting_btn;
	private Button exit_btn;
	private AlertDialog alert;
	private AlertDialog.Builder builder;
    private ViewPager profile_drawer_right_viewpager;
	private List<View> viewList;
	private Button buttonTest;
	private ActionSheetDialog actionSheetDialog;
	String[] items;

	private LinearLayout logout_layout_not_login;//未登录布局
	private LinearLayout login_layout;//带有圆形头像的布局，用来更换原来的布局



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
		exit_btn=(Button) localSlidingMenu.findViewById(R.id.exit_btn);
		baibian_btn=(ImageView) localSlidingMenu.findViewById(R.id.baibian_btn);
		night_mode_btn = (SwitchButton)localSlidingMenu.findViewById(R.id.night_mode_btn);
		night_mode_text = (TextView)localSlidingMenu.findViewById(R.id.night_mode_text);
		left_top_layout=(LinearLayout)localSlidingMenu .findViewById(R.id.left_top_layout);
		left_drawer_all=(LinearLayout)localSlidingMenu.findViewById(R.id.left_drawer_all);
		left_drawer_ScrollView=(LinearLayout) localSlidingMenu.findViewById(R.id.left_drawer_ScrollView);
		left_drawer_top_text=(TextView) localSlidingMenu.findViewById(R.id.left_drawer_top_text);
		left_search_text=(TextView) localSlidingMenu.findViewById(R.id.left_search_text);
		left_favorite_text=(TextView)localSlidingMenu.findViewById(R.id.left_favorite_text);
		message_text =(TextView) localSlidingMenu.findViewById(R.id.message_text);
		offline_btn_text=(TextView) localSlidingMenu.findViewById(R.id.offline_btn_text);
		app_activity_text =(TextView) localSlidingMenu.findViewById(R.id.app_activity_text);
		left_drawer_setting_text=(TextView) localSlidingMenu.findViewById(R.id.left_drawer_setting_text);
		 left_drawer_feedback_text=(TextView) localSlidingMenu.findViewById(R.id.left_drawer_feedback_text);
		 left_drawer_appstore_text=(TextView) localSlidingMenu.findViewById(R.id.left_drawer_appstore_text);


		/**
		 * 右侧Viewpager部分
		 *鉴于要直接布局出来，舍弃使用viewpager减少占用空间的方法。
		 viewList=new ArrayList<View>();
		 View view1=View.inflate(activity,R.layout.profile_drawers_right_pagerview1,null);
		 View view2=View.inflate(activity,R.layout.profile_drawers_right_pagerview2,null);
		 viewList.add(view1);
		 viewList.add(view2);
		 profile_drawer_right_viewpager=(ViewPager)localSlidingMenu.findViewById(R.id.profile_drawer_right_viewpager);//这个地方一定要在Adapter实例化之前  否则会崩溃
		 Profile_Drawer_Right_ViewPager_Adapter myPagerAdapter=new Profile_Drawer_Right_ViewPager_Adapter(viewList,activity);
		 profile_drawer_right_viewpager.setAdapter(myPagerAdapter);
		 */


		night_mode_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					//日/夜间模式切换按钮的响应
					night_mode_text.setText(activity.getResources().getString(R.string.action_night_mode));
					night_mode_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					//背景颜色和中间的srollview的背景颜色切换
					left_drawer_top_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_no_login_tip_text));//登陆后推荐更多内容
					left_drawer_all.setBackgroundColor(activity.getResources().getColor(R.color.left_drawer_item_bg_normal));
					left_drawer_ScrollView.setBackgroundColor(activity.getResources().getColor(R.color.left_drawer_item_bg_normal));
					//Srollview部分
					left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					left_favorite_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					message_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					offline_btn_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					app_activity_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					left_drawer_setting_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					left_drawer_feedback_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
					left_drawer_appstore_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));

				}else{
					night_mode_text.setText(activity.getResources().getString(R.string.action_day_mode));
					night_mode_text.setTextColor(activity.getResources().getColor(R.color.black));
					left_drawer_top_text.setTextColor(activity.getResources().getColor(R.color.black));
					left_drawer_all.setBackgroundColor(activity.getResources().getColor(R.color.left_drawer_itme_bg_normal_day));
					left_drawer_ScrollView.setBackgroundColor(activity.getResources().getColor(R.color.white));
					//Srollview部分
					left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					left_favorite_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					message_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					offline_btn_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					app_activity_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					left_drawer_setting_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					left_drawer_feedback_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
					left_drawer_appstore_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));

				}
			}
		});
		night_mode_btn.setChecked(false);
		if(night_mode_btn.isChecked()){
			night_mode_text.setText(activity.getResources().getString(R.string.action_night_mode));
			night_mode_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			left_drawer_top_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_no_login_tip_text));
			left_drawer_all.setBackgroundColor(activity.getResources().getColor(R.color.left_drawer_item_bg_normal));
			left_drawer_ScrollView.setBackgroundColor(activity.getResources().getColor(R.color.left_drawer_item_bg_normal));
			//Srollview部分
			left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			left_favorite_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			message_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			offline_btn_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			app_activity_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			left_drawer_setting_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			left_drawer_feedback_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
			left_drawer_appstore_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_night_mode_text));
		}else{
			night_mode_text.setText(activity.getResources().getString(R.string.action_day_mode));
			night_mode_text.setTextColor(activity.getResources().getColor(R.color.black));
			night_mode_text.setTextColor(activity.getResources().getColor(R.color.black));
			left_drawer_top_text.setTextColor(activity.getResources().getColor(R.color.black));
			left_drawer_all.setBackgroundColor(activity.getResources().getColor(R.color.left_drawer_itme_bg_normal_day));
			left_drawer_ScrollView.setBackgroundColor(activity.getResources().getColor(R.color.white));
			//Srollview部分
			left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
			left_favorite_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
			message_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
			offline_btn_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
			app_activity_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
			left_drawer_setting_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
			left_drawer_feedback_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
			left_search_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
			left_drawer_appstore_text.setTextColor(activity.getResources().getColor(R.color.left_drawer_item_text));
		}
		
		setting_btn =(RelativeLayout)localSlidingMenu.findViewById(R.id.setting_btn);
		setting_btn.setOnClickListener(this);
		baibian_btn.setOnClickListener(this);
		exit_btn.setOnClickListener(this);

	}


	/**
	 * 退出/注销 按钮的弹窗事件的设置
	 */
	private void init_exit_btn(){
		items=new String[2];
		items[0]=activity.getString(R.string.close_Baibai);
		items[1]=activity.getString(R.string.exit_not_account);
		actionSheetDialog=new ActionSheetDialog(activity,items,exit_btn);
		actionSheetDialog.setOnOperItemClickL(new OnOperItemClickL() {
			@Override
			public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
				actionSheetDialog.dismiss();
				switch (position) {
					case 0://退出事件
						AlertDialog.Builder builder=new AlertDialog.Builder(activity);
						builder.setMessage(R.string.sure_exit)
								.setIcon(R.drawable.icon2)
								.setTitle(R.string.frindly_reminder)
								.setCancelable(false)
								.setPositiveButton(R.string.sure,new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog,int id ){
										//确认后的事件
										activity.finish();
									}
								})
								.setNegativeButton(R.string.cancel,new  DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog,int id ){
										dialog.cancel();//取消后的事件
									}

								} );
						AlertDialog alert=builder.create();
						alert.show();
						break;
					case 1://注销事件
						login_layout=(LinearLayout) activity.findViewById(R.id.login_layout);
						logout_layout_not_login=(LinearLayout) activity.findViewById(R.id.logout_layout_not_login);
						if ((logout_layout_not_login.getVisibility()==View.GONE)&&(login_layout.getVisibility()==View.VISIBLE)){

							login_layout.setVisibility(View.GONE);
							logout_layout_not_login.setVisibility(View.VISIBLE);
						}
						else if ((logout_layout_not_login.getVisibility()==View.VISIBLE)){
							Toast.makeText(activity,"您尚未登录，无法注销",Toast.LENGTH_SHORT).show();
						}
						else{
							Toast.makeText(activity,"注销失败",Toast.LENGTH_SHORT).show();
						}


						break;
					default:
						break;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.baibian_btn:

			Intent intent_baibian_btn=new Intent(activity,Login4Activity.class);
			activity.startActivityForResult(intent_baibian_btn,REQUESTCODE);
			break;
		case R.id.setting_btn:
			break;
		case R.id.exit_btn:
			init_exit_btn();
			actionSheetDialog.isTitleShow(false).show();
			break;
		default:
			break;
		}
	}
}
