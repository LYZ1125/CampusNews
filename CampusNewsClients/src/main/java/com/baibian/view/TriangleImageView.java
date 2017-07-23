package com.baibian.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.baibian.R;
import com.like.Utils;
import com.squareup.okhttp.internal.Util;

/**
 * Created by ����ǿ on 2017/7/18.
 */

public class TriangleImageView extends ImageView {
    private Path clippath;
    public TriangleImageView(Context context){super(context);}
    public TriangleImageView(Context  context, AttributeSet attrs){super(context,attrs);}
    public TriangleImageView(Context context,AttributeSet attrs,int defStyle){super(context,attrs,defStyle);}
    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable drawable=getDrawable();
        //Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();//获取bitmap
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.img_forums_praise);
        Bitmap cpBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas cpCanvas=new Canvas(cpBitmap);
        Paint mPaint=new Paint();
        mPaint.setAntiAlias(true);
        Path path=new Path();
        path.moveTo(0,0);
        path.lineTo(getWidth(),0);
        path.lineTo(0,getHeight());
        path.close();
        canvas.drawPath(path,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        cpCanvas.drawBitmap(bitmap,0,0,mPaint);
    }
}
