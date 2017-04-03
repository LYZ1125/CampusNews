package com.baibian.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.R;
import com.baibian.tool.UI_Tools;

/**
 * 登录界面的活动
 */
public class Login4Activity extends Activity {
    protected TextView imageView11;//第一行第一个图片
    protected ImageView imageView12;
    protected ImageView imageView13;
    protected ImageView imageView21;
    protected ImageView imageView22;
    protected ImageView imageView31;
    protected ImageView imageView32;
    protected EditText login4AccountEdit;
    protected EditText login4passwordEdit;
    protected Button login4loginbutton;
    protected Button login4RegisterButton;
    protected Button login4ForgetButton;
    protected String account;
    protected String password;
    private LinearLayout login4_all_layout;

    private final int LOGIN4_REQUEST=11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login4_layout);
        initview();
        ImageViewAnimation(); //这个方法负责初始化时，7张图片的动画效果
        UI_Tools ui_tools=new UI_Tools();
        ui_tools.CancelFocusOne(this,login4_all_layout,login4AccountEdit);
        //登录Button响应事件

        login4loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account=login4AccountEdit.getText().toString();
                password=login4passwordEdit.getText().toString();
                 if(account.length()<=0||account.length()>20){
                    Toast.makeText(Login4Activity.this, R.string.please_account,Toast.LENGTH_SHORT).show();
                }
                else if (password.length()>16||password.length()<7){
                    Toast.makeText(Login4Activity.this, R.string.please7to16,Toast.LENGTH_SHORT).show();
                }
                else{
                     Intent intent1=new Intent();
                     setResult(LOGIN4_REQUEST,intent1);
                     finish();
                 }

            }
        });
        //注册Button响应事件
        login4RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login4RegisterButton.setTextColor(0xFF003399);
                Intent intent=new Intent(Login4Activity.this,registerActivity.class);
                startActivity(intent);
             //   Toast.makeText(Login4Activity.this, R.string.REGISTER,Toast.LENGTH_SHORT).show();

            }
        });

        //忘记密码Button响应事件
        login4ForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login4ForgetButton.setTextColor(0xFF003399);
                Toast.makeText(Login4Activity.this, R.string.forget_password,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void initview(){
        imageView11=(TextView) findViewById(R.id.login4img11);
        imageView12=(ImageView) findViewById(R.id.login4img12);
        imageView13=(ImageView) findViewById(R.id.login4img13);
        imageView21=(ImageView) findViewById(R.id.login4img21);
        imageView22=(ImageView) findViewById(R.id.login4img22);
        imageView31=(ImageView) findViewById(R.id.login4img31);
        imageView32=(ImageView) findViewById(R.id.login4img32);
        login4loginbutton=(Button) findViewById(R.id.login4loginButton);
        login4RegisterButton=(Button) findViewById(R.id.login4registerbutton);
        login4ForgetButton=(Button) findViewById(R.id.login4forgetbutton);
        login4AccountEdit=(EditText) findViewById(R.id.login4AccountEdit);
        login4passwordEdit=(EditText) findViewById(R.id.login4passwordEdit);
        login4_all_layout=(LinearLayout) findViewById(R.id.login4_all_layout);
    }
    //这个方法负责初始化时，7张图片的动画效果
    protected void  ImageViewAnimation(){
        AlphaAnimation aa11=new AlphaAnimation(0,1);
        aa11.setDuration(500);
        AlphaAnimation aa12=new AlphaAnimation(0,1);
        aa12.setDuration(500);
        aa12.setStartOffset(500);
        AlphaAnimation aa13=new AlphaAnimation(0,1);
        aa13.setDuration(500);
        aa13.setStartOffset(500*2);
        AlphaAnimation aa21=new AlphaAnimation(0,1);
        aa21.setDuration(300);
        aa21.setStartOffset(500*2+300);
        AlphaAnimation aa22=new AlphaAnimation(0,1);
        aa22.setDuration(300);
        aa22.setStartOffset(500*2+300*2);
        AlphaAnimation aa31=new AlphaAnimation(0,1);
        aa31.setDuration(300);
        aa31.setStartOffset(500*2+300*3);
        AlphaAnimation aa32=new AlphaAnimation(0,1);
        aa32.setDuration(300);
        aa32.setStartOffset(500*2+300*4);
        imageView11.startAnimation(aa11);
        imageView12.startAnimation(aa12);
        imageView13.startAnimation(aa13);
        imageView21.startAnimation(aa21);
        imageView22.startAnimation(aa22);
        imageView31.startAnimation(aa31);
        imageView32.startAnimation(aa32);
    }
}
