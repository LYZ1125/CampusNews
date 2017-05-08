package com.baibian.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baibian.R;
import com.baibian.adapter.ImagePagerAdapter;
import com.baibian.base.BaseActivity;
import com.baibian.view.imageshow.ImageShowViewPager;

import java.util.ArrayList;

/*
 * ????
 */
public class ImageShowActivity extends BaseActivity {
	/** ???? */
	private ImageShowViewPager image_pager;
	private TextView page_number;
	/** ???????? */
	private ImageView download;
	/** ???งา? */
	private ArrayList<String> imgsUrl;
	/** PagerAdapter */
	private ImagePagerAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_imageshow);
		initView();
		initData();
		initViewPager();
	}

	private void initData() {
		imgsUrl = getIntent().getStringArrayListExtra("infos");
		page_number.setText("1" + "/" + imgsUrl.size());
	}

	private void initView() {
		image_pager = (ImageShowViewPager) findViewById(R.id.image_pager);
		page_number = (TextView) findViewById(R.id.page_number);
		download = (ImageView) findViewById(R.id.download);
		image_pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				page_number.setText((arg0 + 1) + "/" + imgsUrl.size());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initViewPager() {
		if (imgsUrl != null && imgsUrl.size() != 0) {
			mAdapter = new ImagePagerAdapter(getApplicationContext(), imgsUrl);
			image_pager.setAdapter(mAdapter);
		}
	}
}
