package com.baibian.fragment.users_imformation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baibian.R;
import com.baibian.view.SwitchButton;


public class BaiBianImformationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view=inflater.inflate(R.layout.bb_imformation_layout,container, false);
        return view;
    }
}
