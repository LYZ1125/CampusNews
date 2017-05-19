package com.baibian.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baibian.R;
import com.baibian.adapter.Users_Viwepager_Adapter;
import com.baibian.tool.HttpTool;
import com.baibian.tool.ToastTools;
import com.baibian.tool.UI_Tools;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersImformationActivity extends Activity implements View.OnClickListener{
    private ImageView user_information_edit;
    private Button BB_state_btn;
    private Button BB_imformation_btn;
    private Button choise_direction_back;
    private ViewPager users_imformation_pager;
    private Users_Viwepager_Adapter fAdapter;                               //����adapter
    private List<View> viewList;
    private LinearLayout user_information_all;
    private EditText personalized_signature_edit;
    private Response informationResponse;
    private Handler handler;
    private TextView username;
    private final int EDIT_REQUEST=1;
    private String UserInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.usersimformationlayout);
        //下面这部分代码强制使用在主线程中网络请求，但是我真是日了狗了，，我明明实在子线程中调用的网络请求，，非要说我是主线程中调用，mmp
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initview();
        init_viewpager();//�ײ�viewpager�ĳ�ʼ��
    }

    private void initview() {
        choise_direction_back = (Button) findViewById(R.id.choise_direction_back);
        users_imformation_pager = (ViewPager) findViewById(R.id.users_imformation_pager);
        user_information_edit=(ImageView) findViewById(R.id.user_information_edit);
         BB_state_btn=(Button) findViewById(R.id.BB_state_btn);
        BB_imformation_btn=(Button) findViewById(R.id.BB_imformation_btn);
        user_information_all=(LinearLayout)findViewById(R.id.user_information_all);
        personalized_signature_edit=(EditText) findViewById(R.id.personalized_signature_edit);
        username=(TextView) findViewById(R.id.username);
        choise_direction_back.setOnClickListener(this);
        user_information_edit.setOnClickListener(this);
        UI_Tools ui_tools=new UI_Tools();
        ui_tools.CancelFocusOne(this,user_information_all,personalized_signature_edit);
        init_information();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.choise_direction_back://����˷��ؼ�
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.user_information_edit://�򿪸������ϱ༭�
                Intent intent=new Intent(UsersImformationActivity.this,Edit_Information_Activity.class);
                intent.putExtra("responseString", UserInformation);
                startActivityForResult(intent,EDIT_REQUEST);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 1:
//               String result_usersString= data.getExtras().getString("result_usersString");
////                Log.d("传过去后",result_usersString);
//                ToastTools.ToastShow(result_usersString);
//                parseResultJSONObject(result_usersString);
                init_information();;
                break;
            default:
                break;
        }
    }

    private void init_information(){
        SharedPreferences preferences=getSharedPreferences("usersimformation",MODE_PRIVATE);
        String  id =preferences.getString("id","");//��ȡ��ǰ��¼�û���id
        Log.d("idΪ",id);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                informationResponse = (Response) msg.obj;
                if (informationResponse.code() == 200) {
                    UserInformation="";
                    try{
                 UserInformation= informationResponse.body().string();//��ȡ��Ϣ
                  }catch (IOException e){
                        e.printStackTrace();
                   }
                    UserInformation =ChangeToJSON(UserInformation);
                    parseJSONObject(UserInformation);
                    Log.d("response.body.string:" ,UserInformation);
                    //�������̣߳�����response����ȥ��
                } else{
                    ToastTools.ToastShow(getString(R.string.timeout));
                }
            }
        };
        getin(id);
    }
    private Response getin(String id ) {
        String path="/api/users/"+id+"";
        Log.d("idpath",path);
        Response response = GetUserImformation(path,id);
//        System.out.println("response" + response);
//        System.out.println("responsecode" + response.body());

        return response;
    }

    public Response GetUserImformation(final String path,final String id ){
        final Response[] post = new Response[1];
        new Thread() {
            public void run() {
                post[0] = HttpTool.doGetOkHttpResponse(path);
                Message message = Message.obtain();
                message.obj = post[0];
                handler.sendMessage(message);
            }
        }.start();
        return post[0];
    }

    private String ChangeToJSON(String string){
        string =string.replace("{\"user\":","");
        string=string.replace("}}","}");
        string="["+string+"]";
        return string;
    }
    private void parseResultJSONObject(String josnData) {
//        Log.d("josndata", josnData);
        try {
            JSONArray jsonArray = new JSONArray(josnData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nickname = "";
                nickname = jsonObject.getString("nickname");
                username.setText(nickname);
                Log.d("nickname", nickname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseJSONObject(String josnData) {
        Log.d("josndata", josnData);
        try {
            JSONArray jsonArray = new JSONArray(josnData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nickname = "";

                nickname = jsonObject.getString("nickname");
                username.setText(nickname);
                Log.d("nickname", nickname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override//���·��ؼ�
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private void init_viewpager() {
        viewList=new ArrayList<View>();
        /**
         * 2��View�ֱ��Ӧ��������
         */
        View view1=View.inflate(this, R.layout.bb_statement_layout,null);
        View view2=View.inflate(this, R.layout.bb_imformation_layout,null);
        viewList.add(view1);
        viewList.add(view2);
        fAdapter=new Users_Viwepager_Adapter(viewList,UsersImformationActivity.this);
        //viewpager����adapter
        users_imformation_pager.setAdapter(fAdapter);
        users_imformation_pager.setOffscreenPageLimit(0);

        /**
         * ����д��ֻ�а취֧������Button�������������button��ʱ���뻻һ��ʹ�÷���
         */
        BB_state_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users_imformation_pager.arrowScroll(1);//��ǰ��ҳ
            }
        });
        BB_imformation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users_imformation_pager.arrowScroll(2);//���ҳ
            }
        });
        users_imformation_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){

                    BB_state_btn.setBackground(getResources().getDrawable(R.drawable.yellow_rect));
                    BB_imformation_btn.setBackground(getResources().getDrawable(R.drawable.white_rect));
                }
                else if(position==1){
                    BB_imformation_btn.setBackground(getResources().getDrawable(R.drawable.yellow_rect));
                    BB_state_btn.setBackground(getResources().getDrawable(R.drawable.white_rect));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
