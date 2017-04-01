package com.baibian.fragment.forums;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baibian.R;

/**
 * 论坛碎片中的分类碎片
 */

public class RealTimeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.real_time_fragment_layout, container, false);
        return view;
    }
}
