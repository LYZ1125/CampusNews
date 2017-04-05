package com.baibian.tool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baibian.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个用来代替ListView的一个布局，里面内置了方法有，
 * 初始化数据，获取所有editview的字符串
 */
public class LinearLayout_Inflaterable {
    private LayoutInflater layoutInflater;
    private Context context;
    private LinearLayout linearLayout;
    private View view;//点击添加的视图view
    private List<String> edit_string_list=null;//文本框的字符串list
    private List<String> data;//传入的数据源
    private List<View> layoutlist;
    private int maxChildCount;
    public LinearLayout_Inflaterable(Context context, LinearLayout linearLayout, View view , List<String> data,int maxChildCount){
        this.context=context;
        this.linearLayout=linearLayout;
        this.view=view;
        this.data=data;
        this.maxChildCount=maxChildCount-1;
        layoutlist=new ArrayList<>();

        layoutInflater= LayoutInflater.from(context);
    }
    public void initlayout(){
        for (int i =0;i<data.size();i++){
            init_add_layout(data.get(i));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_layout();
            }
        });
    }
    //获取所有edittext的数据
    public List<String> getEdit_String_List(){
        edit_string_list=new ArrayList<String>();
        for(int i=0;i<linearLayout.getChildCount();i++){
            EditText editText=(EditText) linearLayout.getChildAt(i).findViewById(R.id.edit_text);
            String string= editText.getText().toString();
            edit_string_list.add(string);
        }
        return edit_string_list;
    }
    //初始化时有元素传入的添加布局
    private void init_add_layout(String string ){
       final View textlayout=layoutInflater.inflate(R.layout.edit_information_listitem,null);
        layoutlist.add(textlayout);

        linearLayout.addView(textlayout);
        View remove_btn=(View) textlayout.findViewById(R.id.remove_btn);
         EditText textView=(EditText) textlayout.findViewById(R.id.edit_text);
        textView.setText(string);

        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeView(textlayout);
            }});
    }
    //点击增加的无数据传入的时候使用添加布局
    private void add_layout(){
        if (linearLayout.getChildCount()<=maxChildCount){
            final View textlayout=layoutInflater.inflate(R.layout.edit_information_listitem, null);
            layoutlist.add(textlayout);
            linearLayout.addView(textlayout);
            View remove_btn=(View) textlayout.findViewById(R.id.remove_btn);
            EditText textView=(EditText) textlayout.findViewById(R.id.edit_text);
            remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayout.removeView(textlayout);
                }
            });
        }else {
            int realmaxChildCount =maxChildCount+1;
            Toast.makeText(context,context.getString(R.string.maxadd)+ realmaxChildCount  +context.getString(R.string.maxadd2),Toast.LENGTH_SHORT).show();
        }

    }
    //获取linearlayout子布局数量
    public int getChildCount(){
        return linearLayout.getChildCount();
    }
}
