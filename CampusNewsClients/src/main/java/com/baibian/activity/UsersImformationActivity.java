package com.baibian.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.R;
import com.baibian.adapter.Users_Viwepager_Adapter;
import com.baibian.bean.PeriodicalItem;
import com.baibian.listener.OnPeriodicalItemClickListener;
import com.baibian.listener.ReceiverImageLoadingHelper;
import com.baibian.receiver.ImageLoadReceiver;
import com.baibian.tool.HttpTool;
import com.baibian.tool.SpaceItemDecoration;
import com.baibian.tool.ToastTools;
import com.baibian.view.TipView;
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

public class UsersImformationActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    private static final int CHANGE_IMAGE = 1;
    private static final int FROM_CAMERA = 2;
    private static final int FROM_ALBUM = 3;
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
    private TextView backNav;
    private Toolbar toolbar;

    private RecyclerView periodicalRecyView;
    private PeriodicalAdapter periodicalAdapter;
    private LinearLayoutManager llm;
    private List<PeriodicalItem> periodicalItems;
    private int lastVisibleItem;

    private LinearLayout personalInformationDebateLayout;
    private LinearLayout userInformationSwitchBtns;
    private LinearLayout honorLayout;

    private TextView switchToSwitches;
    private TextView switchBack;

    private Switch debateTopicSwitch;
    private Switch debatePointSwitch;
    private Switch debatePresentationSwitch;

    private RelativeLayout myTopicLayout;
    private RelativeLayout myPointLayout;
    private RelativeLayout myPresentationLayout;

    private SharedPreferences isSwitchCheckedPreferences;
    private String saveImageShared;
    final private String[] items = {"Scoop", "Capture", "Chosen from album"};

    private Bitmap mBitmap;
    private Handler imageLoadHandler;
    private ImageLoadReceiver receiver;

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
        /*init_viewpager();//???viewpager??????*/
    }

    /**
     * Set the states of switches before entering the activity
     */
     private void initPersonalInfoCheckedVisible() {

        isSwitchCheckedPreferences = getSharedPreferences("IS_SWITCH_CHECKED", MODE_PRIVATE);

        if (!isSwitchCheckedPreferences.getBoolean("is_debate_topic_checked", false)){
            myTopicLayout.setVisibility(View.GONE);
            debateTopicSwitch.setChecked(false);
        }
        if (!isSwitchCheckedPreferences.getBoolean("is_debate_point_checked", false)){
            myPointLayout.setVisibility(View.GONE);
            debatePointSwitch.setChecked(false);
        }
        if (!isSwitchCheckedPreferences.getBoolean("is_debate_presentation_checked", false)){
            myPresentationLayout.setVisibility(View.GONE);
            debatePresentationSwitch.setChecked(false);
        }
    }

    class PeriodicalAdapter extends RecyclerView.Adapter<PeriodicalAdapter.MyViewHolder> {

        private OnPeriodicalItemClickListener mItemListener;

        public void setOnClickItemListener(OnPeriodicalItemClickListener listener){
            mItemListener = listener;
        }
        @Override
        public PeriodicalAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.periodical_cover_view_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(final PeriodicalAdapter.MyViewHolder myViewHolder,  int position) {
            myViewHolder.tv.setText(periodicalItems.get(position).getTextContent());
            if (mItemListener != null){
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = myViewHolder.getLayoutPosition();
                        mItemListener.onItemClick(v, position);
                    }
                });
            }
            //TODO Glide with context load pictures into iv;
        }

        @Override
        public int getItemCount() {
            return periodicalItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tv;
            ImageView iv;

            MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.periodical_text_content);
                iv = (ImageView) itemView.findViewById(R.id.periodical_image_cover);
            }

        }
    }

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


        initRecyItemsData();
        initVariousViews();
        initRecyclerView();
        initListenersOnView();

        /**
         * Here to support my toolbar I switch AppTheme to NoActionbar in AndroidManifest.
         * In any case if this switch will make bugs unknown to me presently in the future
         * I would turn toolbar into RelativeLayout as the previous one.
         */
        initToolBar();
        initUserPortrait();
        init_information();
        initPersonalInfoCheckedVisible();

        /*        UI_Tools ui_tools=new UI_Tools();
        ui_tools.CancelFocusOne(this,user_information_all,personalized_signature_edit);*/

    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * To initialize the user's portrait in advance
     */
    private void initUserPortrait() {
        imageLoadHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case CHANGE_IMAGE:
                        userPortrait.setImageBitmap(mBitmap);
                        break;
                }
            }
        };
        /**
         * To receive broadcast from EditPortraitActivity that notify the change of image.
         */
        receiver = new ImageLoadReceiver();
        receiver.setImageLoadingHelper(new ReceiverImageLoadingHelper() {
            @Override
            public void doTasks() {
                Log.d("load_image", "LoadImage");
                mBitmap = EditPortraitActivity.getSaveImageShared();
                userPortrait.setImageBitmap(mBitmap);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.baibian.image_change");
        registerReceiver(receiver, filter);

        new Thread(){
            @Override
            public void run() {
                mBitmap = EditPortraitActivity.getSaveImageShared();
                imageLoadHandler.sendEmptyMessage(CHANGE_IMAGE);
            }
        }.start();
    }

    private void initListenersOnView() {
        debateTopicSwitch.setOnCheckedChangeListener(this);
        debatePointSwitch.setOnCheckedChangeListener(this);
        debatePresentationSwitch.setOnCheckedChangeListener(this);
        switchToSwitches.setOnClickListener(this);
        switchBack.setOnClickListener(this);
        backNav.setOnClickListener(this);
        userPortrait.setOnClickListener(this);
        editPersonalSignal.setOnClickListener(this);
    }

    private void initVariousViews() {
        userPortrait = (CircleImageView) findViewById(R.id.user_portrait);
        editPersonalSignal = (TextView) findViewById(R.id.edit_personal_signal);
        backNav = (TextView) findViewById(R.id.back_nav_toolbar);
        toolbar = (Toolbar) findViewById(R.id.user_tool_bar);
        personalInformationDebateLayout = (LinearLayout) findViewById(R.id.personal_information_on_debate_layout);
        userInformationSwitchBtns = (LinearLayout) findViewById(R.id.user_information_toggle_btn_layout);
        switchToSwitches = (TextView) findViewById(R.id.switch_to_toggle_buttons);
        switchBack = (TextView) findViewById(R.id.switch_back_to_information);
        debateTopicSwitch = (Switch) findViewById(R.id.debate_topic_switch);
        debatePointSwitch = (Switch) findViewById(R.id.debate_point_switch);
        debatePresentationSwitch = (Switch) findViewById(R.id.debate_presentation_switch);
        myTopicLayout = (RelativeLayout) findViewById(R.id.my_topic_holder_layout);
        myPointLayout = (RelativeLayout) findViewById(R.id.my_point_holder_layout);
        myPresentationLayout = (RelativeLayout) findViewById(R.id.my_presentation_holder_layout);
        periodicalRecyView = (RecyclerView) findViewById(R.id.periodical_recycler_view) ;
        honorLayout = (LinearLayout) findViewById(R.id.user_honor_layout);

        addHonorImageView(R.drawable.ic_action_favor);

    }

    private void addHonorImageView(int resID) {
        ImageView tempImage = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tempImage.setImageResource(resID);
        honorLayout.addView(tempImage, params);
//        tempImage.setPadding(0, 0, R.dimen.bg_row_padding_right);
    }

    private void initRecyclerView() {
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        periodicalAdapter = new PeriodicalAdapter();
        periodicalAdapter.setOnClickItemListener(new OnPeriodicalItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastTools.ToastShow("Test" + position);
            }
        });
        periodicalRecyView.setLayoutManager(llm);
        periodicalRecyView.setAdapter(periodicalAdapter);
        periodicalRecyView.addItemDecoration(new SpaceItemDecoration(15));
        periodicalRecyView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= llm.getItemCount()){
                    for (int i = 0; i < 3; i++){
                        PeriodicalItem tempItem = new PeriodicalItem();
                        tempItem.setTextContent("ruaruaraurauraururauraurauraurau");
                        periodicalItems.add(tempItem);
                    }
                    ToastTools.ToastShow("loading more");
                    periodicalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = llm.findLastVisibleItemPosition();
            }
        });
    }

    private void initRecyItemsData() {
        periodicalItems = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            PeriodicalItem tempPeriodicalItem = new PeriodicalItem();
            //TODO with text contents and image source of the item
            tempPeriodicalItem.setTextContent("hiahiahiahiahiahiahiahiahiahihaihaihaihaihiahiahiahiahai");
            periodicalItems.add(tempPeriodicalItem);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SharedPreferences.Editor editor = isSwitchCheckedPreferences.edit();
        /**
         * To test the result I make the switches switch on the user interface.
         * (originally they should switch the interface that others see rather than the user's)
         */
        switch (buttonView.getId()){
            case R.id.debate_topic_switch:
                if (isChecked){
                    myTopicLayout.setVisibility(View.VISIBLE);
                }else {
                    myTopicLayout.setVisibility(View.GONE);
                }
                editor.putBoolean("is_debate_topic_checked", isChecked);
                editor.apply();
                break;
            case R.id.debate_point_switch:
                if (isChecked){
                    myPointLayout.setVisibility(View.VISIBLE);
                }else {
                    myPointLayout.setVisibility(View.GONE);
                }
                editor.putBoolean("is_debate_point_checked", isChecked);
                editor.apply();
                break;
            case R.id.debate_presentation_switch:
                if (isChecked){
                    myPresentationLayout.setVisibility(View.VISIBLE);
                }else {
                    myPresentationLayout.setVisibility(View.GONE);
                }
                editor.putBoolean("is_debate_presentation_checked", isChecked);
                editor.apply();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
/*
            case R.id.choise_direction_back://?????????
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
*//*
            case R.id.like_button:

                break;*/

            case R.id.switch_to_toggle_buttons:
                personalInformationDebateLayout.setVisibility(View.GONE);
                userInformationSwitchBtns.setVisibility(View.VISIBLE);
                break;
            case R.id.switch_back_to_information:
                userInformationSwitchBtns.setVisibility(View.GONE);
                personalInformationDebateLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.back_nav_toolbar:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.edit_personal_signal://????????????
                Intent intent=new Intent(UsersImformationActivity.this,Edit_Information_Activity.class);
                intent.putExtra("responseString", UserInformation);
                startActivityForResult(intent, EDIT_REQUEST);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.user_portrait:
                Intent intentToEditPortrait = new Intent(UsersImformationActivity.this, EditPortraitActivity.class);
                startActivity(intentToEditPortrait);
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
        saveImageShared = Base64.encodeToString(bytes, Base64.DEFAULT);
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
