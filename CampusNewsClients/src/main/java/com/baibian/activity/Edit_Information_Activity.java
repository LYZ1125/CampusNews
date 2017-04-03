package com.baibian.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baibian.R;
import com.baibian.adapter.EditImformation_ListView_Adapter;
import com.baibian.tool.UI_Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑个人信息的activity
 */

public class Edit_Information_Activity extends Activity implements View.OnClickListener{
    private ImageView edit_information_back;//个人信息返回键
    private LinearLayout edit_information_all_layout;//全布局
    private ListView house_list;//居住地
    private ListView advantage_viewlist;//擅长领域
    private List<String> data;//测试数据源
    private EditText user_name_edittext;
    private int i ;
    public EditImformation_ListView_Adapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_informationlayout);
        initview();
        UI_Tools ui_tools=new UI_Tools();
        ui_tools.CancelFocusOne(this,edit_information_all_layout,user_name_edittext);
        edit_information_back.setOnClickListener(this);

       data = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            /**
             * ������ˢ�µĲ������ݵ�����
             */
            int index = i + 1;
            data.add("new �û�" + index);
        }
         listAdapter=new EditImformation_ListView_Adapter(this,data);

        house_list.setAdapter(listAdapter);
        //��������ĵ���¼�
        house_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private int i;
            private  void btnDelete(View view,int position){
                data.remove(position);
                listAdapter.notifyDataSetChanged();
            }
            private  void btnAdd(View view){
                data.add("2231");
                listAdapter.notifyDataSetChanged();;
                house_list.setSelection(data.size()-1);
            }

            @Override
            public void onItemClick(AdapterView<?> parent,  View view,  int position, long id) {
                i=position;
                if (position==house_list.getChildCount()-1){
                    btnAdd(house_list);//最后一个的时候进行调用
                }
              /*  else {
                    ImageView remove_btn=(ImageView) view.findViewById(R.id.remove_btn);
                    remove_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnDelete(house_list,i);
                        }
                    });

                }*/
            }
        });

        advantage_viewlist.setAdapter(listAdapter);
        advantage_viewlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String a=""+i;
                Toast.makeText(Edit_Information_Activity.this,a,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initview(){
        edit_information_back=(ImageView) findViewById(R.id.edit_information_back);
        edit_information_all_layout=(LinearLayout) findViewById(R.id.edit_information_all_layout);
        user_name_edittext=(EditText) findViewById(R.id.user_name_edittext);
        house_list=(ListView) findViewById(R.id.house_viewlist);
        advantage_viewlist=(ListView) findViewById(R.id.advantage_viewlist);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_information_back://����˷��ؼ�
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }
    /***
     *这个adpater定义在这内部，因为这里要调用这个活动创建的adapter。
     */
    public class EditImformation_ListView_Adapter extends BaseAdapter {
        int i ;
        private Context mContext;
        private LayoutInflater mInflater;
        public List<String> mList;

        public EditImformation_ListView_Adapter(Context context, List<String> list) {

            this.mContext = context;
            this.mInflater = LayoutInflater.from(mContext);
            this.mList = list;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }


        @Override
        public int getItemViewType(int position) {
            return position > 0 ? 0 : 1 ;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            i=position;
            //�����޸Ķ�Ӧλ�õĲ���
            if (position ==mList.size()-1) {
                convertView = mInflater.inflate(R.layout.edit_information_listitem_bottom, null);
                return convertView;
            } else {
                convertView = mInflater.inflate(R.layout.edit_information_listitem, null);
                ImageView remove_btn=(ImageView) convertView.findViewById(R.id.remove_btn);
                remove_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mList.remove(i);//移除数据
                        listAdapter.notifyDataSetChanged();//刷新数据

                    }
                });
            }
            return convertView;
        }



    }
}

