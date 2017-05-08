package com.baibian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.baibian.R;

import java.util.List;


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
        //?????????¦Ë??????
        if (position ==mList.size()-1) {
            convertView = mInflater.inflate(R.layout.edit_information_listitem_bottom, null);
            return convertView;
        } else {
            convertView = mInflater.inflate(R.layout.edit_information_listitem, null);
            ImageView remove_btn=(ImageView) convertView.findViewById(R.id.remove_btn);
            remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mList.remove(i);
                }
            });
        }
        return convertView;
    }



}