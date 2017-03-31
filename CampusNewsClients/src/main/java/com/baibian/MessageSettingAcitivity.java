package com.baibian;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baibian.base.BaseActivity;


public class MessageSettingAcitivity extends BaseActivity {
    private Button choise_direction_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_setting_layout);
        initview();
        choise_direction_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initview(){
        choise_direction_back=(Button) findViewById(R.id.choise_direction_back);
    }
}

