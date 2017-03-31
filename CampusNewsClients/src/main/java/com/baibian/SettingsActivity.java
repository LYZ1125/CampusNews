package com.baibian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baibian.base.BaseActivity;
import com.baibian.fragment.IntegrationFragment;

public class SettingsActivity extends BaseActivity {
private RelativeLayout message_setting;
	private Button choise_direction_back;
	private RelativeLayout push_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		initView();
		initData();
		message_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(SettingsActivity.this,MessageSettingAcitivity.class);
				startActivity(intent);
			}
		});
		choise_direction_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		push_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(SettingsActivity.this,PushSetting.class);
				startActivity(intent);
			}
		});
	}
	
	private void initView() {
		message_setting=(RelativeLayout) findViewById(R.id.message_setting);
		choise_direction_back=(Button) findViewById(R.id.choise_direction_back);
		push_layout=(RelativeLayout) findViewById(R.id.push_layout);
	}

	private void initData() {
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
//		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}


}
