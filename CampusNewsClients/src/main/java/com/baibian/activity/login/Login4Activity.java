package com.baibian.activity.login;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.baibian.base.BaseActivity;
import com.baibian.tool.HttpTool;
import com.baibian.tool.ToastTools;
import com.baibian.tool.UI_Tools;
import com.squareup.okhttp.Response;

/**
 * 登录界面的活动
 */
public class Login4Activity extends BaseActivity {
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
    private final int LOGIN4_REQUEST = 11;
    private Response signInResponse;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login4_layout);
        imageView11 = (TextView) findViewById(R.id.login4img11);
        imageView12 = (ImageView) findViewById(R.id.login4img12);
        imageView13 = (ImageView) findViewById(R.id.login4img13);
        imageView21 = (ImageView) findViewById(R.id.login4img21);
        imageView22 = (ImageView) findViewById(R.id.login4img22);
        imageView31 = (ImageView) findViewById(R.id.login4img31);
        imageView32 = (ImageView) findViewById(R.id.login4img32);
        login4loginbutton = (Button) findViewById(R.id.login4loginButton);
        login4RegisterButton = (Button) findViewById(R.id.login4registerbutton);
        login4ForgetButton = (Button) findViewById(R.id.login4forgetbutton);
        login4AccountEdit = (EditText) findViewById(R.id.login4AccountEdit);
        login4passwordEdit = (EditText) findViewById(R.id.login4passwordEdit);
        login4_all_layout=(LinearLayout) findViewById(R.id.login4_all_layout);
        UI_Tools ui_tools = new UI_Tools();
        ui_tools.CancelFocusOne(this, login4_all_layout, login4AccountEdit);
        ImageViewAnimation(); //这个方法负责初始化时，7张图片的动画效果
        //登录Button响应事件

        login4loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = login4AccountEdit.getText().toString();
                password = login4passwordEdit.getText().toString();
                if (account.length() != 11) {
                    Toast.makeText(Login4Activity.this, R.string.please_account, Toast.LENGTH_SHORT).show();
                } else if (password.length() > 16 || password.length() < 6) {
                    Toast.makeText(Login4Activity.this, R.string.please6to16, Toast.LENGTH_SHORT).show();
                } else {
                    //用于接受登录、注册子线程的返回数据
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            signInResponse = (Response) msg.obj;
                            if (signInResponse.code() == 200) {
                                System.out.println("返回值为200");
                                ToastTools.ToastShow(getString(R.string.signIn_OK));
                                //开启新线程，并将response传过去。
                                Intent intent1 = new Intent();
                                intent1.putExtra("response", String.valueOf(signInResponse));
                                setResult(LOGIN4_REQUEST, intent1);
                                finish();
                            } else if (signInResponse.code() == 401) {
                                System.out.println("返回值为401");
                                login4passwordEdit.setText("");
                                ToastTools.ToastShow(getString(R.string.account_or_password_error));
                            }else{
                                ToastTools.ToastShow(getString(R.string.timeout));
                            }
                        }
                    };
                    signIn(account, password);
                }

            }
        });
        //注册Button响应事件
        login4RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login4RegisterButton.setTextColor(0xFF003399);
                Intent intent = new Intent(Login4Activity.this, registerActivity.class);
                startActivity(intent);
                //   Toast.makeText(Login4Activity.this, R.string.REGISTER,Toast.LENGTH_SHORT).show();

            }
        });

        //忘记密码Button响应事件
        login4ForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login4Activity.this, R.string.forget_password, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void get() {
        final String[] htmlStr = new String[1];
        new Thread() {
            public void run() {
                htmlStr[0] = HttpTool.doGetOkHttp(" ", "GET", "/api/users/1");
                Log.e("net", String.valueOf(htmlStr));
            }
        }.start();
    }
    /*
    * 登录方法
    * */
    private Response signIn(String account, String password) {
        String user = HttpTool.getSignInJson(account, password);
        Response response = sign("/api/signin", user);
//        System.out.println("response" + response);
//        System.out.println("responsecode" + response.body());

        return response;
    }

    public Response sign(final String path, final String user){
        final Response[] post = new Response[1];
        new Thread() {
            public void run() {
                System.out.println("123" + user);
                post[0] = HttpTool.post(user, path);
                Message message = Message.obtain();
                message.obj = post[0];
                handler.sendMessage(message);
            }
        }.start();
        return post[0];
    }

    //这个方法负责初始化时，7张图片的动画效果
    protected void ImageViewAnimation() {
        AlphaAnimation aa11 = new AlphaAnimation(0, 1);
        aa11.setDuration(500);
        AlphaAnimation aa12 = new AlphaAnimation(0, 1);
        aa12.setDuration(500);
        aa12.setStartOffset(500);
        AlphaAnimation aa13 = new AlphaAnimation(0, 1);
        aa13.setDuration(500);
        aa13.setStartOffset(500 * 2);
        AlphaAnimation aa21 = new AlphaAnimation(0, 1);
        aa21.setDuration(300);
        aa21.setStartOffset(500 * 2 + 300);
        AlphaAnimation aa22 = new AlphaAnimation(0, 1);
        aa22.setDuration(300);
        aa22.setStartOffset(500 * 2 + 300 * 2);
        AlphaAnimation aa31 = new AlphaAnimation(0, 1);
        aa31.setDuration(300);
        aa31.setStartOffset(500 * 2 + 300 * 3);
        AlphaAnimation aa32 = new AlphaAnimation(0, 1);
        aa32.setDuration(300);
        aa32.setStartOffset(500 * 2 + 300 * 4);
        imageView11.startAnimation(aa11);
        imageView12.startAnimation(aa12);
        imageView13.startAnimation(aa13);
        imageView21.startAnimation(aa21);
        imageView22.startAnimation(aa22);
        imageView31.startAnimation(aa31);
        imageView32.startAnimation(aa32);
    }
}