package com.baibian.activity.login;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.util.Log;
        import android.view.View;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import com.baibian.R;
        import com.baibian.activity.MainActivity;
        import com.baibian.tool.HttpTool;
        import com.baibian.tool.ToastTools;
        import com.baibian.tool.UI_Tools;
        import com.squareup.okhttp.Response;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.util.HashMap;

        import cn.smssdk.EventHandler;
        import cn.smssdk.SMSSDK;
        import cn.smssdk.gui.RegisterPage;

/**
 * 注册界面的活动
 */
public class registerActivity extends Activity implements View.OnClickListener {
    //protected ImageView register_imageView;//头像部分
    protected EditText register_AccountEdit;//账号
    protected EditText register_password1;//第一次输入的密码
    protected EditText register_password2;//再次确认时输入的密码
    protected Button register_loginButton;//登录按钮
    protected Button register_or_login_button;//注册界面和登录界面切换的按钮
    protected String account;//账号
    protected String password1;//第一次输入的密码
    protected String password2;//再次输入的密码
    private String phone;//电话号码
    private Response signInResponse;
    private Handler handler;
    private boolean isSignUpOK;
    private boolean isAccountHava;
    private String responseBody;
    private LinearLayout register_all_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_layout);
        initView();
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
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    phone = (String) phoneMap.get("phone");
                    account = phone;
// 提交用户信息（此方法可以不调用）
                    //    registerUser(country, phone);
                }
            }
        });
        registerPage.show(this);
        register_AccountEdit.setText(account);


    }

    private void initView() {
      //  register_imageView = (ImageView) findViewById(R.id.register_imageView);
        register_AccountEdit = (EditText) findViewById(R.id.register_AccountEdit);
        register_password1 = (EditText) findViewById(R.id.register_first_passwords);
        register_password2 = (EditText) findViewById(R.id.register_confirm_editText);
        register_loginButton = (Button) findViewById(R.id.register_loginButton);
        register_or_login_button = (Button) findViewById(R.id.register_or_login_button);
        register_all_layout=(LinearLayout)findViewById(R.id.register_all_layout);
        UI_Tools ui_tools = new UI_Tools();
        ui_tools.CancelFocusOne(this, register_all_layout, register_AccountEdit);
        register_loginButton.setOnClickListener(this);
        register_or_login_button.setOnClickListener(this);
    }

    /*

     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_loginButton:
                account = register_AccountEdit.getText().toString();
                password1 = register_password1.getText().toString();
                password2 = register_password2.getText().toString();
                if (account.length() != 11) {
                    ToastTools.ToastShow(getString(R.string.please_account));
                } else if (password1.length() > 16 || password1.length() < 6) {
                    Toast.makeText(registerActivity.this, R.string.please6to16, Toast.LENGTH_SHORT).show();
                } else if (password2.length() > 16 || password2.length() < 6) {
                    Toast.makeText(registerActivity.this, R.string.please6to16, Toast.LENGTH_SHORT).show();
                } else if (!password1.equals(password2)) {
                    Toast.makeText(registerActivity.this, R.string.different_password, Toast.LENGTH_SHORT).show();
                } else {
                    //用于接受登录、注册子线程的返回数据
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            signInResponse = (Response) msg.obj;
                            //response监测点
//                            try {
//                                System.out.println("response!!!" + signInResponse.body().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            try {
                                responseBody = signInResponse.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isSignUpOK = responseBody.startsWith("{\"user\"");
                            isAccountHava = responseBody.startsWith("{\"error\"");
                            //response监测点
/*                            System.out.println("body"+responseBody);
                            System.out.println("1"+isSignUpOK);
                            System.out.println("2"+isAccountHava);*/

                            if (isSignUpOK) {
                                System.out.println(isSignUpOK);
                                ToastTools.ToastShow(getString(R.string.signUp_OK));
                                //开启新线程，并将response传过去。
                                Intent intent1 = new Intent(registerActivity.this, MainActivity.class);
                                intent1.putExtra("response", String.valueOf(signInResponse));
                                startActivity(intent1);
                                finish();
                            }
                            //帐号已存在
                            else if (isAccountHava) {
                                ToastTools.ToastShow(getString(R.string.haved_account));
//                                register_password1.setText("");
//                                register_password2.setText("");
                            } else {
                                ToastTools.ToastShow(getString(R.string.timeout));
                            }
                        }
                    };
                    signUp(account, password1);
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

    /*
* 注册方法
* */
    private Response signUp(String account, String password) {
        String user = HttpTool.getSignUpJson(account, password);
        Response response = sign("/api/signup", user);
        return response;
    }

    public Response sign(final String path, final String user) {
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
}
