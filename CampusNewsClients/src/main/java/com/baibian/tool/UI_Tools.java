package com.baibian.tool;


import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

/**
 * 这个工具类用来处理一些通用的UI部分的东西
 */
public class UI_Tools {
    private List<ViewGroup> listViewGroup;
    private List<EditText> listEditText;
    public int i = 0;

    public UI_Tools() {

    }

    /**
     * 这个方法用来使得EditText,,点击其他地方的时候，取消焦点并且退出软键盘。。在有EditView的地方都应该调用这个方法。
     * 传入的三个参数，第一个是活动，第二个是取消焦点要点击的地方，比如背景layout，listview之类的，第三个参数是其中一个edittext，只需要传入一个就可以达到效果
     */
    public void CancelFocus(final Activity activity, final List<ViewGroup> listViewGroup, final EditText edittext) {
        System.out.print(listViewGroup.size());
        for (; i < listViewGroup.size(); i++) {
            listViewGroup.get(i)
                    .setFocusable(true);
            listViewGroup.get(i)
                    .setFocusableInTouchMode(true);
            listViewGroup.get(i)
                    .setFocusable(true);
            listViewGroup.get(i)
                    .setFocusableInTouchMode(true);
            listViewGroup.get(i)
                    .requestFocus();
            listViewGroup.get(i)
                    .setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            // TODO Auto-generated method stub
                            InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                            //  imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//这行代码可以弹出软键盘，但是在这个地方没有屁用
                            imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);//这行代码隐藏软键盘
                            return false;
                        }
                    });

        }
    }
    //一个布局的取消焦点
    public void CancelFocusOne(final Activity activity, final ViewGroup viewGroup, final EditText edittext) {

            viewGroup
                    .setFocusable(true);
        viewGroup                    .setFocusableInTouchMode(true);
        viewGroup                    .setFocusable(true);
        viewGroup                    .setFocusableInTouchMode(true);
        viewGroup                    .requestFocus();
        viewGroup                    .setOnTouchListener(new View.OnTouchListener() {

                        public boolean onTouch(View v, MotionEvent event) {
                            // TODO Auto-generated method stub

                            InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                            //  imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//这行代码可以弹出软键盘，但是在这个地方没有屁用
                            imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);//这行代码隐藏软键盘
                            return false;
                        }
                    });


    }
}
