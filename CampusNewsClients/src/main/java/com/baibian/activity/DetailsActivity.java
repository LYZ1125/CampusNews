package com.baibian.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baibian.R;
import com.baibian.base.BaseActivity;
import com.baibian.tool.UI_Tools;
import com.like.LikeButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by XZY on 2017/4/13.
 * ?????????????/???
 * ?????????????????????Natural%20Right??????????????????????????????????????????????
 * ???е????????????????????????????????????????????????????????????????????????
 * ?????????????????????%20???????Щ??????????????????????????????????????????
 * ?????????????????????????????????????У??????????????У???????
 * ????????????????壬????ν????????????????????%20?????????е?????????????????????
 * ????????????????????????????????????????????????????????????????
 */

public class DetailsActivity extends BaseActivity {
    public View view3 ;//细分视图
    private Context context;
    ViewPager pager = null;
    PagerTabStrip tabStrip = null;
    ArrayList<View> viewContainter = new ArrayList<View>();
    ArrayList<String> titleContainer = new ArrayList<String>();
    public String TAG = "tag";
    Button backBt = null;
    private LinearLayout forums_details_all_layout;
    private TagContainerLayout mTagContainerLayout1, mTagContainerLayout2, mTagContainerLayout3, mTagContainerLayout4;
    private Button addTag1;
    private EditText editTag;
    private ViewPager viewPager;
    List<Fragment> viewPagerList;
    private LinearLayout forums_details_itemlayout1;
    private RelativeLayout forums_details_itemlayout2;
    private Activity activity;
    private TextView begin_discussion;

    private ImageButton backToChoice;
    private FloatingActionButton popupInput;
    private LinkedList<FrameLayout> frameLayouts = new LinkedList<>();
    private LinkedList<Button> buttons = new LinkedList<>();
    /**
     * Create five GridLayouts to hold Buttons
     * Create grids to get GridLayout randomly
     * Create previousTexts to save last button that has been set in one GridLayout previously
     * Create columns and column1,2,3 to simulate a Matrix and by deleting them one by one to achieve setting Buttons randomly
     * --WangYY 2017.5.16
     */
    private GridLayout gridFirst;
    private GridLayout gridSecond;
    private GridLayout gridThird;
    private GridLayout gridFourth;
    private LinkedList<GridLayout> grids = new LinkedList<>();
    private LinkedList<TextView> previousTexts = new LinkedList<>();
    private LinkedList<LinkedList<Integer>> columns = new LinkedList<>();
    private String items[] = { "高数", "线代", "大物", "马克思", "工程制图", "C语言", "军事理论课", "思修", "职业发展教育", "恕我直言", "这些", "统统都是"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forums_details);
        context=getApplicationContext();
        activity=this;
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        initData();

        initView();
        initViewPager();
//        viewpager????????
//        viewPagerList=new ArrayList<Fragment>();
//        viewPagerList.add(new overviewFragment());
//        viewPagerList.add(new subdivideFragment());
//        viewPager =(ViewPager) findViewById(R.id.pager);
//        fgViewPagerAdapter adapter=new fgViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(adapter);


//      ?????????
//        addTag1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editTag.setText(addTag1.getText());
//            }
//        });

    }

    private void initData() {
        List<String> list1 = new ArrayList<String>();
        list1.add("Java");
        list1.add("C++");
        list1.add("Python");
        list1.add("Swift");
        list1.add("???????????TAG?????????????TAG?????????????TAG?????????????TAG??");
        list1.add("PHP");
        list1.add("JavaScript");
        list1.add("Html");
        list1.add("Welcome to use AndroidTagView!");

        List<String> list2 = new ArrayList<String>();
        list2.add("China");
        list2.add("USA");
        list2.add("Austria");
        list2.add("Japan");
        list2.add("Sudan");
        list2.add("Spain");
        list2.add("UK");
        list2.add("Germany");
        list2.add("Niger");
        list2.add("Poland");
        list2.add("Norway");
        list2.add("Uruguay");
        list2.add("Brazil");

        String[] list3 = new String[]{"Persian", "?????", "?????", "Hello", "???", "????"};
        String[] list4 = new String[]{"Adele", "Whitney Houston"};

        mTagContainerLayout1 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);
        mTagContainerLayout2 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout2);
        mTagContainerLayout3 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout3);
        mTagContainerLayout4 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout4);

        // Set custom click listener
        mTagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Toast.makeText(DetailsActivity.this, "click-position:" + position + ", text:" + text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                AlertDialog dialog = new AlertDialog.Builder(DetailsActivity.this)
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayout1.removeTag(position);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }

            @Override
            public void onTagCrossClick(int position) {
//                mTagContainerLayout1.removeTag(position);
                Toast.makeText(DetailsActivity.this, "Click TagView cross! position = " + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Custom settings
//        mTagContainerLayout1.setTagMaxLength(4);

        // Set the custom theme
//        mTagContainerLayout1.setTheme(ColorFactory.PURE_CYAN);

        // If you want to use your colors for TagView, remember set the theme with ColorFactory.NONE
//        mTagContainerLayout1.setTheme(ColorFactory.NONE);
//        mTagContainerLayout1.setTagBackgroundColor(Color.TRANSPARENT);
//        mTagContainerLayout1.setTagTextDirection(View.TEXT_DIRECTION_RTL);

        // support typeface
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "iran_sans.ttf");
//        mTagContainerLayout.setTagTypeface(typeface);

        // adjust distance baseline and descent
//        mTagContainerLayout.setTagBdDistance(4.6f);

        // After you set your own attributes for TagView, then set tag(s) or add tag(s)
        mTagContainerLayout1.setTags(list1);
        mTagContainerLayout2.setTags(list2);
        mTagContainerLayout3.setTags(list3);
        mTagContainerLayout4.setTags(list4);

        final EditText text = (EditText) findViewById(R.id.text_tag);
        Button btnAddTag = (Button) findViewById(R.id.btn_add_tag);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagContainerLayout1.addTag(text.getText().toString());
                // Add tag in the specified position
//                mTagContainerLayout1.addTag(text.getText().toString(), 4);
            }
        });

//        mTagContainerLayout1.setMaxLines(1);


        // test in RecyclerView
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setVisibility(View.VISIBLE);
//        TagRecyclerViewAdapter adapter = new TagRecyclerViewAdapter(this, list3);
//        adapter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Click on TagContainerLayout", Toast.LENGTH_SHORT).show();
//            }
//        });
//        recyclerView.setAdapter(adapter);
    }

    /**
     * There is something improper with the parameters of the following method
     * I wanna drag the method out of the outer method, "initViewPager", while I have no idea dealing with the variables in the inner class passed to this method
     * thus I have no other way but to add some specific final parameters "LinearLayout" and "RelativeLayout"
     * If forums_details_itemlayout1or2 can be initiated in the "initView" then the problem can be tackled probably from my own perspective-WangYY
     * @param flag
     * @param forums_details_itemlayout1
     * @param forums_details_itemlayout2
     */
    private void setFrameLayoutAnimation(Boolean flag, final  LinearLayout forums_details_itemlayout1, final RelativeLayout forums_details_itemlayout2) {
        setFrameLayouts(forums_details_itemlayout1);
        if (flag) {
            TranslateAnimation tempTranslateAnimation = translateAnimationGenerator(0, -400, 1000, 0);
            for (int i = 0; i < frameLayouts.size(); i++) {
                if (i == 2 || i == 4) {
                    frameLayouts.get(i).startAnimation(tempTranslateAnimation);
                } else {
                    frameLayouts.get(i).startAnimation(translateAnimationGenerator(0, 400, 1000, 0));
                }
            }
            frameLayouts.clear();
            tempTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    forums_details_itemlayout1.setVisibility(View.GONE);
                    forums_details_itemlayout2.setVisibility(View.VISIBLE);
                    forums_details_itemlayout2.startAnimation(translateAnimationGenerator(800, 0, 1000, 0));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else {
            TranslateAnimation tempTranslateAnimation = translateAnimationGenerator(0, 800, 1000, 0);
            forums_details_itemlayout2.startAnimation(tempTranslateAnimation);
            tempTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    for (int i = 0; i < frameLayouts.size(); i++){
                        if (i == 2 || i == 4){
                            frameLayouts.get(i).startAnimation(translateAnimationGenerator(-400, 0, 1000, 1000));
                        }else {
                            frameLayouts.get(i).startAnimation(translateAnimationGenerator(400, 0, 1000, 1000));
                        }
                    }
                    frameLayouts.clear();
                    forums_details_itemlayout2.setVisibility(View.GONE);
                    forums_details_itemlayout1.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void initViewPager() {
        viewPager.setAdapter(new PagerAdapter() {

            //viewpager?е????????
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //?????л?????????????????
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //??λ????????????????
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                if (position==1){

                    final FrameLayout framelayoute21=(FrameLayout) findViewById(R.id.framelayoute21);
                    backToChoice = (ImageButton) findViewById(R.id.btn_back_choice);
                    backToChoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setFrameLayoutAnimation(false, forums_details_itemlayout1, forums_details_itemlayout2);
                        }
                    });
                    forums_details_itemlayout1=(LinearLayout) findViewById(R.id.forums_details_itemlayout1);
                    forums_details_itemlayout2=(RelativeLayout)findViewById(R.id.forums_details_itemlayout2);
                    begin_discussion=(TextView)findViewById(R.id.begin_discussion);
                    editTag = (EditText) findViewById(R.id.tag_edit);

                    begin_discussion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, DiscussionActivity.class));
                        }
                    });

                    initGridThings();

                    GridLayout.Spec rowTest = GridLayout.spec(0);
                    float ratio = 1;
                    for (int i = 0; i < items.length; i++){
                        int randomRow = getRandomIn(columns.size());
                        while (columns.get(randomRow).size() == 0){
                            randomRow = getRandomIn(columns.size());
                        }
                        int randomColumn = getRandomIn(columns.get(randomRow).size());
                        final TextView temp = new TextView(DetailsActivity.this);
                        temp.setText(items[i]);
                        temp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        temp.setPadding(30, 30, 30, 30);
                        temp.setTextColor(Color.WHITE);
                        temp.setBackgroundResource(R.drawable.detalis_tag_background);
                        temp.setElevation(5.0f);
                        temp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editTag.append(temp.getText() + " ");
                            }
                        });
                        if (previousTexts.get(randomRow).getText() == "") {
                            previousTexts.set(randomRow, temp);
                        }
                        ratio = getRatio(temp, previousTexts.get(randomRow));
                        GridLayout.Spec columnTest = GridLayout.spec(columns.get(randomRow).get(randomColumn), ratio);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowTest, columnTest);
                        grids.get(randomRow).addView(temp, params);
                        if (columns.get(randomRow).size() != 0){
                            columns.get(randomRow).remove(randomColumn);
                        }
                    }
                    framelayoute21.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                            forums_details_itemlayout1.setVisibility(View.GONE);
                            forums_details_itemlayout2.setVisibility(View.VISIBLE);*/
                            /**
                             * Here the method is set to Animate those four Square FrameLayouts
                             */
                            setFrameLayoutAnimation(true, forums_details_itemlayout1, forums_details_itemlayout2);
                                 UI_Tools ui_tools=new UI_Tools();
                                  ui_tools.CancelFocusOne(activity,forums_details_all_layout,editTag);
                            ui_tools.CancelFocusOne(activity,container,editTag);
                        }

                    });

//                    TextView textViewbegin=(TextView) findViewById(R.id.begin_discussion);
//                    textViewbegin.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent=new Intent(DetailsActivity.this,DiscussionActivity.class);
//                            startActivity(intent);
//                        }
//                    });
                }
                return viewContainter.get(position);
            }

            /**
             替换原来的view
             */
            private void updateViewPagerItem(ViewGroup container,View view,int index){
                viewContainter.remove(index);
                view3 = LayoutInflater.from(viewPager.getContext()).inflate(R.layout.forums_details_overview, null);
                viewContainter.add(index,view3);
                viewPager.getAdapter().notifyDataSetChanged();
                // findViewById(getResources().getIdentifier("sysset_button"+(index+1), "id", "com.jzbyapp.suzhou")).requestFocus();
            }
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                SpannableStringBuilder ssb = new SpannableStringBuilder(" "
                        + titleContainer.get(position)); // space added before text for
                ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.baibian_back_color));//???????????????
                ssb.setSpan(fcs, 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//???????????
                ssb.setSpan(new RelativeSizeSpan(1.2f), 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return ssb;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                Log.e(TAG, "--------changed:" + arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.e(TAG, "-------scrolled arg0:" + arg0);
                Log.e(TAG, "-------scrolled arg1:" + arg1);
                Log.e(TAG, "-------scrolled arg2:" + arg2);
            }

            @Override
            public void onPageSelected(int arg0) {
                Log.e(TAG, "------selected:" + arg0);
            }
        });
    }

    private void initGridThings() {
        /**
         * Initiate  five GridLayouts
         */
        gridFirst = (GridLayout) findViewById(R.id.forums_details_grid_first);
        gridSecond = (GridLayout) findViewById(R.id.forums_details_grid_second);
        gridThird = (GridLayout) findViewById(R.id.forums_details_grid_third);
        gridFourth = (GridLayout) findViewById(R.id.forums_details_grid_fourth);

        /**
         * Add GridLayouts to grids
         */
        grids.add(gridFirst);
        grids.add(gridSecond);
        grids.add(gridThird);
        grids.add(gridFourth);

        /**
         * Initiate a 2-dimension list to make up a Matrix-alike
         */
        for (int i = 0; i < 4; i++){
            LinkedList<Integer> tempColumn = new LinkedList<>();
            for (int j = 0; j < 3; j++){
                tempColumn.add(j);
            }
            columns.add(tempColumn);
        }
        /**
         * add textViews to avoid exception
         */
        for (int i = 0; i < items.length; i++){
            previousTexts.add(new TextView(DetailsActivity.this));
        }
    }

    private void initView() {
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        tabStrip = (PagerTabStrip) this.findViewById(R.id.tab_strip);
        backBt = (Button) this.findViewById(R.id.forums_details_back);
//        addTag1 = (Button) this.findViewById(R.id.button4);
        frameLayouts = new LinkedList<>();
        popupInput = (FloatingActionButton) findViewById(R.id.adding_button);

        forums_details_all_layout=(LinearLayout) findViewById(R.id.forums_details_all_layout);

        List<String> tags = new ArrayList<String>();

        tags.add("123");
        tags.add("134455");
//        TagContainerLayout  mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
//        mTagContainerLayout.addTag(tags.get(0));
        //???tab??????????
        tabStrip.setDrawFullUnderline(false);
        //????tab??????
        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.white));
        //??????tab????????????
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.baibian_back_color));
        tabStrip.setTextSpacing(200);

        View view1 = LayoutInflater.from(this).inflate(R.layout.forums_details_subdivide, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.forums_details_overview, null);//大方向分类


        //viewpager??????view
        viewContainter.add(view1);
        viewContainter.add(view2);
//        viewContainter.add(view3);
        //????
        titleContainer.add(getString(R.string.debatetopic));
        titleContainer.add(getString(R.string.detaildivide));

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //????????????????????
//        mTagContainerLayout.setTags(tags);


    }


    public class TagRecyclerViewAdapter
            extends RecyclerView.Adapter<TagRecyclerViewAdapter.TagViewHolder> {

        private Context mContext;
        private String[] mData;
        private View.OnClickListener mOnClickListener;

        public TagRecyclerViewAdapter(Context context, String[] data) {
            this.mContext = context;
            this.mData = data;
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        @Override
        public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TagViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.view_recyclerview_item, parent, false), mOnClickListener);
        }

        @Override
        public void onBindViewHolder(TagViewHolder holder, int position) {
            holder.tagContainerLayout.setTags(mData);
            holder.button.setOnClickListener(mOnClickListener);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            this.mOnClickListener = listener;
        }

        class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TagContainerLayout tagContainerLayout;
            View.OnClickListener clickListener;
            Button button;

            public TagViewHolder(View v, View.OnClickListener listener) {
                super(v);
                this.clickListener = listener;
//                tagContainerLayout = (TagContainerLayout) v.findViewById(R.id.tagcontainerLayout);
                button = (Button) v.findViewById(R.id.button);
//                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
            }
        }
    }

    /**
     * to generate translateAnimation on X ray only, specifically it can generate animation with random duration.-WangYY
     * @param fromX
     * @param toX
     * @param duration
     * @param random
     * @return
     */
    private TranslateAnimation translateAnimationGenerator(int fromX, int toX, int duration, int random){
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, 0, 0);
        translateAnimation.setDuration(duration + (int) (random * Math.random()));
        return translateAnimation;
    }

    /**
     * this method is set to find all the FrameLayouts under a ViewGroup while needed specific way of layout.-WangYY
     * @param viewGroup
     */
    private void setFrameLayouts(ViewGroup viewGroup){
        for (int i = 0; i < viewGroup.getChildCount(); i ++){
            if (viewGroup.getChildAt(i) instanceof FrameLayout){
                frameLayouts.add((FrameLayout) viewGroup.getChildAt(i));
            }else {
                setFrameLayouts((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    /**
     * a better way to go through ViewGroup(Though not used yet :<)
     * @param root
     */
    private void setButtons(View root){
        if (root instanceof ViewGroup){
            int childCount = ((ViewGroup) root).getChildCount();
            for (int i = 0; i < childCount; i++){
                setButtons(((ViewGroup) root).getChildAt(i));
            }
        }else if (root instanceof View){
            if (root instanceof Button){
                buttons.add((Button) root);
            }
        }
    }

    /**
     * to get a random number
     * @param size
     * @return
     */
    private int getRandomIn(int size){
        return (int) (size * Math.random());
    }

    /**
     * to get ratio between two buttons by textWidth
     * @param one
     * @param two
     * @return
     */
    private float getRatio(TextView one, TextView two){
        float ratio = getTextWidth(one.getText().toString(), one.getTextSize())/getTextWidth(two.getText().toString(), two.getTextSize());
        return ratio;
    }

    /**
     * to get the width of a button with a specific size
     * @param text
     * @param size
     * @return
     */
    private float getTextWidth(String text, float size){
        if (null == text || "".equals(text)){
            return 0;
        }else {
            Paint paint = new Paint();
            paint.setTextSize(size);
            float width = paint.measureText(text);
            return width;
        }
    }
    //????Fragment??ViewPager????????
    private class fgViewPagerAdapter extends FragmentPagerAdapter {

        public fgViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return viewPagerList.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return viewPagerList.size();
        }

    }
}