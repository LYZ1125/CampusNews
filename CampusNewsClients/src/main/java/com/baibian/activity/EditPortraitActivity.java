package com.baibian.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;

import com.baibian.R;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPortraitActivity extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView userPortrait;
    private String saveImageShared;
    final private String[] items = {"Scoop", "Capture", "Chosen from album"};
    public String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_portrait);
        userPortrait = (CircleImageView) findViewById(R.id.user_portrait);

        /**
         * To initialize the user's portrait in advance
         */
        path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.png";
        userPortrait.setImageBitmap(getSaveImageShared());

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
             */
            case 2:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = bundle.getParcelable("data");
                    setSaveImageShared(bitmap);
                    userPortrait.setImageBitmap(bitmap);
                } else {
                    return;
                }
                break;
            case 3:
                if (data != null) {
                    Uri uri = data.getData();
                    getImg(uri);
                } else {
                    return;
                }
                break;

        }
    }

    private void setSaveImageShared(Bitmap mBitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        saveImageShared = Base64.encodeToString(bytes, Base64.DEFAULT);
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", saveImageShared);
        editor.apply();
    }
    private Bitmap getSaveImageShared(){
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", MODE_PRIVATE);
        saveImageShared = sharedPreferences.getString("image", "");
        byte[] bytes = Base64.decode(saveImageShared, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        return bitmap;
    }
    private void getImg(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            //从输入流中解码位图
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            //保存位图
            setSaveImageShared(bitmap);
            userPortrait.setImageBitmap(bitmap);

            //关闭流
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
