package com.baibian.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.baibian.R;
import com.baibian.adapter.CityAdapter;
import com.baibian.base.BaseActivity;
import com.baibian.bean.CityEntity;
import com.baibian.tool.Constants;
import com.baibian.view.HeadListView;

import java.util.ArrayList;

public class CityListActivity extends BaseActivity {
	private TextView title;
	private HeadListView mListView;
	private ArrayList<CityEntity> cityList;
	private CityAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		initView();
		initData();
	}
	
	private void initView() {
		title = (TextView) findViewById(R.id.title);
		mListView = (HeadListView)findViewById(R.id.cityListView);
	}
	
	private void initData() {
		title.setText("???????-????");
		cityList = Constants.getCityList();
		mAdapter = new CityAdapter(this, cityList);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mAdapter);
		mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(R.layout.city_item_section, mListView, false));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Intent intent = new Intent(getApplicationContext(), newsDetailsActivity.class);
//				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}
}
