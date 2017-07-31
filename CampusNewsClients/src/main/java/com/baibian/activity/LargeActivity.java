package com.baibian.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.baibian.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LargeActivity extends AppCompatActivity {

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large);
        ImageView largeView = (ImageView) findViewById(R.id.large_view);
        bitmap = EditPortraitActivity.getSaveImageShared();
        largeView.setImageBitmap(bitmap);
        largeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        Glide.with(LargeActivity.this).load(bytes).into(largeView);
    }
}
