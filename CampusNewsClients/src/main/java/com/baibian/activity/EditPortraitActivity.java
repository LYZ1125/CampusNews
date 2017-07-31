package com.baibian.activity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.baibian.R;
import com.baibian.tool.ToastTools;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPortraitActivity extends AppCompatActivity implements View.OnClickListener{

    private Handler imageLoadHandler;

    private Bitmap bitmap = null;
    private static final int CHANGE_IMAGE = 1;
    private static final int FROM_CAMERA = 2;
    private static final int FROM_ALBUM = 3;
    private CircleImageView userPortrait;
    final private String[] items = {"Scoop", "Capture", "Chosen from album"};
    final private String fileName = "head_portrait";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_portrait);
        userPortrait = (CircleImageView) findViewById(R.id.user_portrait);
        userPortrait.setOnClickListener(this);
        initUserPortraitInAdvance();
    }

    private void initUserPortraitInAdvance() {

        imageLoadHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case CHANGE_IMAGE:
                        Log.d("Handler", "Handling1");
                        userPortrait.setImageBitmap(bitmap);
                        break;
                    case FROM_CAMERA:
                        Log.d("Handler", "Handling2");
                        userPortrait.setImageBitmap(bitmap);
                        break;
                    case FROM_ALBUM:
                        Log.d("Handler", "Handling3");
                        userPortrait.setImageBitmap(bitmap);
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = CHANGE_IMAGE;
                bitmap = getSaveImageShared();
                imageLoadHandler.sendMessage(msg);
                Log.d("thread_test", "Thread Testing");
            }
        }.start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_portrait:

                final ActionSheetDialog dialog = new ActionSheetDialog(EditPortraitActivity.this, items, v);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (items[position]){
                            case "Capture":
                                Intent intent=new Intent();
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,2);
                                dialog.dismiss();
                                break;
                            case "Chosen from album":
                                Intent intent1=new Intent();
                                intent1.setType("image/*");
                                intent1.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent1,3);
                                dialog.dismiss();
                                break;
                            case "Scoop":
                                Intent forLarge = new Intent(EditPortraitActivity.this, LargeActivity.class);
                                startActivity(forLarge);
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            /**
             * case 2 and 3 are used for accepting captures and photos from albums to set portrait
             */ case 2:
                if (data != null) {
                    final Bundle bundle = data.getExtras();
                    final Message camMeg = new Message();
                    new Thread(){
                        @Override
                        public void run() {
                            bitmap = bundle.getParcelable("data");
                            setSaveImageShared(bitmap);
                            camMeg.what = FROM_CAMERA;
                            imageLoadHandler.sendMessage(camMeg);
                        }
                    }.start();
                } else {
                    return;
                }
                break;
            case 3:
                if (data != null) {
                    final Uri uri = data.getData();
                    new Thread(){
                        @Override
                        public void run() {
                            bitmap = getImg(uri);
                            setSaveImageShared(bitmap);
                            Message alMsg = new Message();
                            alMsg.what = FROM_ALBUM;
                            imageLoadHandler.sendMessage(alMsg);
                        }
                    }.start();
                } else {
                    return;
                }
                break;

        }
    }

    private void setSaveImageShared(Bitmap mBitmap){
        File appDir = new File(Environment.getExternalStorageDirectory(), "LunDao");
        if (!appDir.exists()){
            appDir.mkdir();
        }
        File f = new File(appDir, fileName + ".jpg");
        if (f.exists()){
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Notify that the image has been changed.
         */
        Intent intent = new Intent();
        intent.setAction("com.baibian.image_change");
        sendBroadcast(intent);
    }
    public static Bitmap getSaveImageShared(){
        File appDir = new File(Environment.getExternalStorageDirectory(), "LunDao");
        if (!appDir.exists()){
            appDir.mkdir();
        }
        File f = new File(appDir, "head_portrait" + ".jpg");
        Bitmap mBitmap = null;
        try {
            FileInputStream stream = new FileInputStream(f);
            mBitmap = BitmapFactory.decodeStream(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }
    private Bitmap getImg(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            //从输入流中解码位图
            bitmap = BitmapFactory.decodeStream(inputStream);
            //保存位图
            //关闭流
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
