package com.baibian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.baibian.R;


public class Edit_Information_Activity extends Activity implements View.OnClickListener{
    private ImageView edit_information_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_informationlayout);
        initview();
        edit_information_back.setOnClickListener(this);
    }
    private void initview(){
        edit_information_back=(ImageView) findViewById(R.id.edit_information_back);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_information_back://µã»÷ÁË·µ»Ø¼ü
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }
}

