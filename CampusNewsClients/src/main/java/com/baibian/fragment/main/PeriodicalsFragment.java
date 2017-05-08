package com.baibian.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baibian.R;


public class PeriodicalsFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View PeriodicalsFragment = inflater.inflate(R.layout.periodicals_layout, container, false);
        return PeriodicalsFragment;
    }
}
