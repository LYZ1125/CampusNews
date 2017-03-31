package com.baibian;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PushSetting extends Activity {
    private Button choise_direction_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_layout);
        initview();
        choise_direction_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void  initview(){
        choise_direction_back=(Button) findViewById(R.id.choise_direction_back);
    }

}