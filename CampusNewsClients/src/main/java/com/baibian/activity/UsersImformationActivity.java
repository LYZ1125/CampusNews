package com.baibian.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.R;
import com.baibian.adapter.Users_Viwepager_Adapter;
import com.baibian.tool.HttpTool;
import com.baibian.tool.ToastTools;
import com.baibian.tool.UI_Tools;
import com.baibian.view.PeriodicalCoverView;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersImformationActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView user_information_edit;
    private Button BB_state_btn;
    private Button BB_imformation_btn;
    private Button choise_direction_back;
    private ViewPager users_imformation_pager;
    private Users_Viwepager_Adapter fAdapter;                               //????adapter
    private List<View> viewList;
    private LinearLayout user_information_all;
    private EditText personalized_signature_edit;
    private Response informationResponse;
    private Handler handler;
    private TextView username;
    private final int EDIT_REQUEST=1;
    private String UserInformation;

    private CircleImageView userPortrait;
    private TextView editPersonalSignal;
    private ImageView likeButton;
    private TextView backNav;
    private Toolbar toolbar;
    private LinearLayout shareLinearLayout;
    private String saveImageShared;
    public String path;
    final private String[] items = {"Scoop", "Capture", "Chosen from album"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.usersimformationlayout);
        //下面这部分代码强制使用在主线程中网络请求，但是我真是日了狗了，，我明明实在子线程中调用的网络请求，，非要说我是主线程中调用，mmp
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initview();
        /*init_viewpager();//???viewpager??????
    */}

    private void initview() {
        /*choise_direction_back = (Button) findViewById(R.id.choise_direction_back);
        users_imformation_pager = (ViewPager) findViewById(R.id.users_imformation_pager);
        user_information_edit=(ImageView) findViewById(R.id.user_information_edit);
         BB_state_btn=(Button) findViewById(R.id.BB_state_btn);
        BB_imformation_btn=(Button) findViewById(R.id.BB_imformation_btn);
        user_information_all=(LinearLayout)findViewById(R.id.user_information_all);
        personalized_signature_edit=(EditText) findViewById(R.id.personalized_signature_edit);
        username=(TextView) findViewById(R.id.username);
        choise_direction_back.setOnClickListener(this);
        user_information_edit.setOnClickListener(this);
*//*
        user_information_all = (LinearLayout) findViewById(R.id.user_information_all);*/
        userPortrait = (CircleImageView) findViewById(R.id.user_portrait);
        editPersonalSignal = (TextView) findViewById(R.id.edit_personal_signal);
        likeButton = (ImageView) findViewById(R.id.like_button);
        backNav = (TextView) findViewById(R.id.back_nav_toolbar);
        toolbar = (Toolbar) findViewById(R.id.user_tool_bar);
        shareLinearLayout = (LinearLayout) findViewById(R.id.share_linear_layout);
        /**
         * Here to support my toolbar I switch AppTheme to NoActionbar in AndroidManifest.
         * In any case if this switch will make bugs unknown to me presently in the future
         * I would turn toolbar into RelativeLayout as the previous one.
         */
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
        }
        backNav.setOnClickListener(this);
        userPortrait.setOnClickListener(this);
        editPersonalSignal.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.png";
        userPortrait.setImageBitmap(getSaveImageShared());
        /**
         * Temporarily add view in this way since I don't know in what way would the "periodical" be loaded
         * maybe methods will be written for changing image, title and subtitle.
         * Wrong margin.
         */
        PeriodicalCoverView coverTemp1 = new PeriodicalCoverView(this);
        PeriodicalCoverView coverTemp2 = new PeriodicalCoverView(this);
        shareLinearLayout.addView(coverTemp1);
        shareLinearLayout.addView(coverTemp2);
/*        UI_Tools ui_tools=new UI_Tools();
        ui_tools.CancelFocusOne(this,user_information_all,personalized_signature_edit);*/
        init_information();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
/*
            case R.id.choise_direction_back://?????????
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
*/
            case R.id.like_button:

                break;
            case R.id.back_nav_toolbar:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.edit_personal_signal://????????????
                Intent intent=new Intent(UsersImformationActivity.this,Edit_Information_Activity.class);
                intent.putExtra("responseString", UserInformation);
                startActivityForResult(intent,EDIT_REQUEST);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.user_portrait:
                final ActionSheetDialog dialog = new ActionSheetDialog(UsersImformationActivity.this, items, view);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (items[position]){
                            case "Capture":
                                Intent intent=new Intent();
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,2);
                                dialog.dismiss();
                                break;
                            case "Chosen from album":
                                Intent intent1=new Intent();
                                intent1.setType("image/*");
                                intent1.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent1,3);
                                dialog.dismiss();
                                break;
                            case "Scoop":
                                Intent forLarge = new Intent(UsersImformationActivity.this, LargeActivity.class);
                                startActivity(forLarge);
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
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
                init_information();
                break;
            /**
             * case 2 and 3 are used for accepting captures and photos from albums to set portrait
             */
            case 2:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = bundle.getParcelable("data");
                    setSaveImageShared(bitmap);
                    userPortrait.setImageBitmap(bitmap);
                } else {
                    return;
                }
                break;
            case 3:
                if (data != null) {
                    Uri uri = data.getData();
                    getImg(uri);
                } else {
                    return;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * Click listeners for items in the toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.user_share:
                Toast.makeText(this, "Share Something", Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_write:
                Toast.makeText(this, "Modify Something", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
    private void init_information(){
        SharedPreferences preferences=getSharedPreferences("usersimformation",MODE_PRIVATE);
        String  id =preferences.getString("id","");//??????????????id
        Log.d("id?",id);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                informationResponse = (Response) msg.obj;
                if (informationResponse != null && informationResponse.code() == 200) {
                    UserInformation="";
                    try{
                        UserInformation= informationResponse.body().string();//??????
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    UserInformation =ChangeToJSON(UserInformation);
                    parseJSONObject(UserInformation);
                    Log.d("response.body.string:" ,UserInformation);
                    //??????????????response???????
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



    @Override//???·????
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private void init_viewpager() {
        viewList=new ArrayList<View>();
        /**
         * 2??View?????????????
         */
        View view1=View.inflate(this, R.layout.bb_statement_layout,null);
        View view2=View.inflate(this, R.layout.bb_imformation_layout,null);
        viewList.add(view1);
        viewList.add(view2);
        fAdapter=new Users_Viwepager_Adapter(viewList,UsersImformationActivity.this);
        //viewpager????adapter
        users_imformation_pager.setAdapter(fAdapter);
        users_imformation_pager.setOffscreenPageLimit(0);

        /**
         * ????д????а????????Button?????????????button????????????÷???
         */
        BB_state_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users_imformation_pager.arrowScroll(1);//??????
            }
        });
        BB_imformation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users_imformation_pager.arrowScroll(2);//????
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

    /**
     * the following three methods are all used for saving capture
     * @param mBitmap
     */
    private void setSaveImageShared(Bitmap mBitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        saveImageShared = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", saveImageShared);
        editor.apply();
    }
    private Bitmap getSaveImageShared(){
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", MODE_PRIVATE);
        saveImageShared = sharedPreferences.getString("image", "");
        byte[] bytes = Base64.decode(saveImageShared, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        return bitmap;
    }

    private void getImg(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            //从输入流中解码位图
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            //保存位图
            setSaveImageShared(bitmap);
            userPortrait.setImageBitmap(bitmap);

            //关闭流
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
