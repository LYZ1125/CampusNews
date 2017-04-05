package com.baibian;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import com.baibian.base.BaseActivity;

public class ChoiceDirectionActivity extends BaseActivity {
    private Button choise_direction_back;
    private FrameLayout frameLayout21;
    private FrameLayout frameLayout22;
    private FrameLayout frameLayout31;
    private FrameLayout frameLayout32;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choise_direction_layout);
        initview();
        ImageViewAnimation();
        choise_direction_back.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initview(){
        choise_direction_back=(Button) findViewById(R.id.choise_direction_back);
        frameLayout21=(FrameLayout) findViewById(R.id.framelayoute21);
        frameLayout22=(FrameLayout) findViewById(R.id.framelayoute22);
        frameLayout31=(FrameLayout) findViewById(R.id.framelayoute31);
        frameLayout32=(FrameLayout) findViewById(R.id.framelayoute32);
    }
    protected void  ImageViewAnimation(){
        /**
         * ����߽����
         */
        TranslateAnimation translateAnimationleft=new TranslateAnimation(-400,0,0,0);
        translateAnimationleft.setDuration(1000);
        frameLayout21.startAnimation(translateAnimationleft);//��ʼ����
        frameLayout31.startAnimation(translateAnimationleft);//��ʼ����

        /**
         * ���ұ߽����
         */
        TranslateAnimation translateAnimationright=new TranslateAnimation(400,0,0,0);
        translateAnimationright.setDuration(1000);
        frameLayout22.startAnimation(translateAnimationright);//��ʼ����
        frameLayout32.startAnimation(translateAnimationright);//��ʼ����
    }
}
