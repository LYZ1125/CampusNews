package com.baibian.activity;

import android.content.Intent;
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
    private String fileName;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large);
        Intent intent = getIntent();
        fileName = intent.getStringExtra("file_name");
        ImageView largeView = (ImageView) findViewById(R.id.large_view);
        bitmap = EditPortraitActivity.getSaveImageShared(fileName);
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
