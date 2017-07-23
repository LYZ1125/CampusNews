package com.baibian.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.baibian.R;

import java.io.ByteArrayInputStream;

public class LargeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large);
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", MODE_PRIVATE);
        String saveImageShared = sharedPreferences.getString("image", "");
        byte[] bytes = Base64.decode(saveImageShared, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        ImageView largeView = (ImageView) findViewById(R.id.large_view);
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
