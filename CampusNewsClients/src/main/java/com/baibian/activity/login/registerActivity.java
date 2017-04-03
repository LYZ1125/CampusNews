package com.baibian.activity.login;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baibian.R;
import com.baibian.tool.UI_Tools;

import java.util.HashMap;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * 注册界面的活动
 */
public class registerActivity extends Activity  implements View.OnClickListener {
    protected ImageView register_imageView;//头像部分
    protected EditText register_AccountEdit;//账号
    protected EditText register_first_passwords;//第一次输入的密码
    protected EditText register_confirm_editText;//再次确认时输入的密码
    protected Button regiter_loginButton;//登录按钮
    protected Button register_or_login_button;//注册界面和登录界面切换的按钮
    protected String account;//账号
    protected String password1;//第一次输入的密码
    protected String password2;//再次输入的密码
    private String phone;//电话号码
    private LinearLayout register_all_layout;//注册界面的全布局
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_layout);
        initview();
        UI_Tools ui_tools=new UI_Tools();
        ui_tools.CancelFocusOne(this,register_all_layout,register_confirm_editText);
        /**
         * SMS SDK调用。。。
         */
        SMSSDK.initSDK(this, "1c56a693870cb", "2849dd663a585497395d6e5d8da70e2e");//俩个参数
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
// 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                     phone = (String) phoneMap.get("phone");
                    account=phone;
// 提交用户信息（此方法可以不调用）
                    //    registerUser(country, phone);
                }
            }
        });
        registerPage.show(this);
        register_AccountEdit.setText(account);


    }
    private void initview(){
        register_imageView = (ImageView) findViewById(R.id.register_imageView);
        register_AccountEdit = (EditText) findViewById(R.id.register_AccountEdit);
        register_first_passwords = (EditText) findViewById(R.id.register_first_passwords);
        register_confirm_editText = (EditText) findViewById(R.id.register_confirm_editText);
        regiter_loginButton = (Button) findViewById(R.id.regiter_loginButton);
        register_or_login_button = (Button) findViewById(R.id.register_or_login_button);
        register_all_layout=(LinearLayout) findViewById(R.id.register_all_layout);
        regiter_loginButton.setOnClickListener(this);
        register_or_login_button.setOnClickListener(this);
    }
/*

 */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regiter_loginButton:
                account = register_AccountEdit.getText().toString();
                password1 = register_first_passwords.getText().toString();
                password2 = register_confirm_editText.getText().toString();
                if (account.length() <= 0 || account.length() > 20) {
                    Toast.makeText(registerActivity.this, "R.string.please_account", Toast.LENGTH_SHORT).show();
                } else if (password1.length() > 16 || password1.length() < 7) {
                    Toast.makeText(registerActivity.this, "R.string.please7to16", Toast.LENGTH_SHORT).show();
                } else if (password2.length() > 16 || password2.length() < 7) {
                    Toast.makeText(registerActivity.this, "R.string.please7to16", Toast.LENGTH_SHORT).show();
                } else if (!password1.equals(password2)) {
                    Toast.makeText(registerActivity.this, R.string.different_password, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(registerActivity.this, "R.string.login_succeed", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(registerActivity.this, Login4Activity.class);
                    startActivity(intent1);
                    finish();
                }
                break;
            case R.id.register_or_login_button:
                register_or_login_button.setTextColor(0xFF003399);
                Intent intent2 = new Intent(registerActivity.this, Login4Activity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
}
