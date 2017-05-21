package com.baibian.view;

/**
 * Created by 邹特强 on 2017/5/20.
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baibian.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.path;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.content.ContentValues.TAG;
/**
 * Created by ����ǿ on 2017/5/12.
 */
/*���Ʒ�qq�������ֹ������Ĺ�����*/
public class TipView extends View {

    private static final int STATUS_DOWN = 1;
    private static final int STATUS_UP = 2;
    // ��ʼ���ڵ�Item��ʾ���Ϸ�
    private int mStatus = STATUS_UP;


    // �ָ�Item֮�����
    private int mSeparateLineColor ;

    // �������������ı߽ǰ뾶��С
    private int mCorner = dip2px(6);

    private Paint mPaint; // ����������ֵĻ���
    private Paint doPaint1; //�������С����ȫ��ĳ�������ڣ�����doPaint1
    private Paint doPaint2;// �����ǵĻ���//�ֱ��������С�������

    private Path mPath; // ���Ƶ�·��

    private int mBorderMargin = dip2px(5); // ��ʾ��������Ļ�������֣�����С����
    private int mItemWidth = dip2px(50); // ����Item�Ŀ��
    private int mItemHeight = dip2px(48); // ����Item�ĸ߶�
    private int mTriangleTop = dip2px(50); // ����С��Ķ���
    private int mHalfTriangleWidth = dip2px(6); // ����С��İ��
    private int mItemBorder; // ����С���봰��Item���ٽ�
    private int realLeft;//���ڵ�left
    private int chooseItemleft;//ѡ�е�item�����Ե
    private int chooseItemright;// ѡ�е�item���ұ�Ե
    private int mTriangleleft;//����С����󶥵�
    private int mTriangleright;//����С����Ҷ���//�����ζ��Ǹպ���45�ȣ�������̫����
    private boolean lstate=false;//��¼����С����󶥵��״̬
    private boolean rstate=false;//�Ҷ���

    private List<TipItem> mItemList = new ArrayList<>(); // �洢ÿ��Item����Ϣ
    private List<Rect> mItemRectList = new ArrayList<>(); // �洢ÿ������


    private OnItemClickListener onItemClickListener; // Item����ӿ�
    private int choose = -1; // �Ƿ���Item�����£���ΪItem����ţ���Ϊ-1//���㿪ʼ
    private int x; // �������x����
    private int y; // ��紫���y����
    private Bitmap disprove;//反驳
    private Bitmap ask;//提问
    private Bitmap copy;//复制
    private Bitmap report;//举报
    private Bitmap share;//分享
    private Rect mSrcRect;//bitmap放置的区域
    private Rect mDesRect;//bitmap显示的区域

    public TipView(Context context, ViewGroup rootView,int x,int y,List<TipItem> mItemList) {
        super(context);

        this.x = x; // ���ô��������x������
        this.y = y;  // ���ô��������y������
        // x��y�����������±��λ��
        mTriangleleft=x-mHalfTriangleWidth;
        mTriangleright=x+mHalfTriangleWidth;//��ʼ��
        initPaint(); // ��ʼ������
        setTipItemList(mItemList); // ��ʼ��Item���ϣ�����Item���ַ������Ƚ��д���
        //�����ַ�������ʱ��ʹ�ú��ʡ�Դ�����xxx...����
        addView(rootView); // ��TipViewʵ����ӵ������rootView�С�//rootview����tipview��ʾ�����ϵĲ���
        // ע��:rootView��ҪFrameLayout/RelativeLayout��������//rootView��TipView������

        initView(); // ���ݴ���ĵ������x��y����״̬�жϺ�����λ�õĴ���
    }

    private void initPaint() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//�������
        mPaint.setStyle(Paint.Style.FILL);//���ÿ��ģ�Ĭ��FILL
        mPaint.setTextSize(sp2px(10));//setTextsize����spΪ��λ�������д���

        doPaint1 = new Paint();
        doPaint2=new Paint();
        doPaint1.setAntiAlias(true);
        doPaint2.setAntiAlias(true);
        doPaint1.setStyle(Paint.Style.FILL);
        doPaint2.setStyle(Paint.Style.FILL);
        doPaint1.setColor(Color.rgb(28,185,253));//ͨ��������õ���ɫת��������
        doPaint2.setColor(Color.rgb(28,185,253));
    }

    private void initView() {

        // ��ȡ��Ļ���
        int mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        //获取bitmap实例
        disprove= BitmapFactory.decodeResource(this.getResources(),R.mipmap.message);
        ask=BitmapFactory.decodeResource(this.getResources(),R.mipmap.ask);
        copy=BitmapFactory.decodeResource(this.getResources(),R.mipmap.copy);
        report= BitmapFactory.decodeResource(this.getResources(),R.mipmap.report);
        share= BitmapFactory.decodeResource(this.getResources(), R.mipmap.share);
        // �������������y�Ƚ�Сʱ�������λ�ñȽ����棩
        if (y/2<mItemHeight) {
            mStatus = STATUS_DOWN; // ������·���ʾ
            mTriangleTop = y + dip2px(6); // ���������±�Ķ���
            mItemBorder = mTriangleTop + dip2px(7); // ���������±�ʹ��ڷ���Ľ��紦
        } else {
            mStatus = STATUS_UP;   // ͬ������
            mTriangleTop = y - dip2px(6);
            mItemBorder = mTriangleTop - dip2px(7);
        }//ͨ�����ϴ������ʵ���˹�������ѡ��ĳ���

        // ��ȡ��Item�������߶Գ�ʱ�����ڵ�Itemֵ��
        //���������˾���ͦ���
        realLeft = x - (mItemWidth * mItemList.size()) / 2;//������ʵ������ô�鷳��ֱ�ӷ���QQ,���ƫ���ʹItem����Ļ���Ե���룬ƫ�Ҿ��Ҷ��룬����˼���ǽ������޶���һ����Χ��ֻ������������ָ�ƶ�
        if (realLeft < 0) {
            // �ܳ���Ļ��ߵĻ�����ֵΪ����ֵ
            realLeft = mBorderMargin;
            // ��ֹ�����±��뷽�����
            if(x-mCorner<=realLeft) x = realLeft+mCorner*2;
        } else if (realLeft + (mItemWidth*mItemList.size()) > mScreenWidth) {
            // �ܳ���Ļ�ұߵĻ������ȥ����Ŀ�ȣ��ټ�ȥ����ֵ
            realLeft -= realLeft + (mItemWidth*mItemList.size())-mScreenWidth+mBorderMargin;
            // ��ֹ�����±��뷽�����
            if(x+mCorner>=realLeft+mItemWidth*mItemList.size()) x =  realLeft+mItemWidth*mItemList.size()-mCorner*2;
        }

    }


    private void addView(ViewGroup rootView) {//ViewGroup�Ǹ�����
        rootView.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));//LayoutParams���ڶ�̬����ViewGroup����Ĳ��ֵ��ӿؼ�
    }

    @Override
    protected void onDraw(Canvas canvas) {//����������ڸ�����ͼ����������//��choose....��������������Ϊ�˻�ȡ���µ�choose
        super.onDraw(canvas);
        chooseItemleft=realLeft+choose*mItemWidth;
        chooseItemright=realLeft+(choose+1)*mItemWidth;//�����ˢ�º�choose��Ϊѡ�е�item
        setState();
        drawBackground(canvas); // ���Ϊ͸��//��������Ըı䱳����ɫ
        switch(mStatus)
        {
            case STATUS_DOWN: drawItemDown(canvas);// ���»��ƴ���
                break;
            case STATUS_UP:drawItemUp(canvas);
            default:break;
        }
    }

    private void drawItemDown(Canvas canvas) {

        // �����鼯��
        mItemRectList.clear();

        mPath.reset(); // ����·��

        // ���������±꣺�����Ƿ���Item��������Ʋ�ͬ��ɫ//���ϸ�ڴ������ѧһ��
        //��������һ�����⣬����ʵ������С����ɫ���ʵ��仯��Ӧ��ֻ��������С�����ڵ��Ǹ����鱻���ʱ�Ÿı���ɫ
        if (choose != -1)//��item������ʱ�������������������һ�飬����Ϊ��ʵ��qq��������Ч����������������ۣ���������ܶ࣬���ظ�����ܶ�
        {
            if (lstate && (!rstate))//���ڶ��Ҳ���
            {
                if (x < chooseItemright)//������Ե��item���ұ߿�
                {
                    doPaint2.setColor(Color.rgb(28,185,253));
                    doPaint1.setColor(Color.rgb(23,124,176));
                    mPath.moveTo(chooseItemright,mItemBorder-mTriangleright+chooseItemright);
                    mPath.lineTo(mTriangleright,mItemBorder);
                    mPath.lineTo(chooseItemright,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint2);
                    mPath.reset();//���һ��Ҫ�У����򻭱���㲻��䣬canava��ѡ�Ļ���Ҳ�����
                    mPath.moveTo(chooseItemright,mItemBorder);
                    mPath.lineTo(mTriangleleft,mItemBorder);
                    mPath.lineTo(x,mTriangleTop);
                    mPath.lineTo(chooseItemright,mItemBorder-mTriangleright+chooseItemright);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint1);
                } else {
                    doPaint2.setColor(Color.rgb(28,185,253));//doPaint1��������С����󲿷�
                    doPaint1.setColor(Color.rgb(23,124,176));
                    mPath.moveTo(chooseItemright,mItemBorder-chooseItemright+mTriangleleft);//������ѧ��ϵ����������
                    mPath.lineTo(mTriangleleft,mItemBorder);//������벿��
                    mPath.lineTo(chooseItemright,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint1);
                    mPath.reset();
                    mPath.moveTo(chooseItemright,mItemBorder-chooseItemright+mTriangleleft);
                    mPath.lineTo(x,mTriangleTop);
                    mPath.lineTo(mTriangleright,mItemBorder);
                    mPath.lineTo(chooseItemright,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint2);
                }
            } else if ((!lstate) && rstate) {
                if (x >=chooseItemleft) {
                    doPaint1.setColor(Color.rgb(28,185,253));//doPaint1��������С����󲿷�
                    doPaint2.setColor(Color.rgb(23,124,176));
                    mPath.moveTo(chooseItemleft,mItemBorder-chooseItemleft+mTriangleleft);//������ѧ��ϵ����������
                    mPath.lineTo(mTriangleleft,mItemBorder);//������벿��
                    mPath.lineTo(chooseItemleft,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint1);
                    mPath.reset();
                    mPath.moveTo(chooseItemleft,mItemBorder-chooseItemleft+mTriangleleft);
                    mPath.lineTo(x,mTriangleTop);
                    mPath.lineTo(mTriangleright,mItemBorder);
                    mPath.lineTo(chooseItemleft,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint2);
                } else {
                    doPaint1.setColor(Color.rgb(28,185,253));
                    doPaint2.setColor(Color.rgb(23,124,176));
                    mPath.moveTo(mTriangleleft,mItemBorder);
                    mPath.lineTo(chooseItemleft,mItemBorder);
                    mPath.lineTo(chooseItemleft,mItemBorder+chooseItemleft-mTriangleright);
                    mPath.lineTo(x,mTriangleTop);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint1);
                    mPath.reset();
                    mPath.moveTo(chooseItemleft,mItemBorder+chooseItemleft-mTriangleright);
                    mPath.lineTo(mTriangleright,mItemBorder);
                    mPath.lineTo(chooseItemleft,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint2);
                }
            } else if (lstate && rstate) {//����С����ȫ�������������
                doPaint1.setColor(Color.rgb(23,124,176));//��ʱ����Ϊ���
                mPath.moveTo(x, mTriangleTop);//
                mPath.lineTo(x - mHalfTriangleWidth, mItemBorder);
                mPath.lineTo(x + mHalfTriangleWidth, mItemBorder);
                canvas.drawPath(mPath, doPaint1);
            }
            else//�����������С�겻�������������
            {
                doPaint1.setColor(Color.rgb(28,185,253));
                mPath.moveTo(x, mTriangleTop);//
                mPath.lineTo(x - mHalfTriangleWidth, mItemBorder);
                mPath.lineTo(x + mHalfTriangleWidth, mItemBorder);
                canvas.drawPath(mPath, doPaint1);
            }
        }
        else//û�б����
        {
            doPaint1.setColor(Color.rgb(28,185,253));
            mPath.moveTo(x, mTriangleTop);//
            mPath.lineTo(x - mHalfTriangleWidth, mItemBorder);
            mPath.lineTo(x + mHalfTriangleWidth, mItemBorder);
            canvas.drawPath(mPath, doPaint1);

        }


        for (int i = 0; i < mItemList.size(); i++) {//ע�������ϸ�ڴ����ȰѲ�ͬ����µ���ɫ�趨�ã����趨���๫���Ĳ���

            // ���з���Item�����ʱ�����Ʋ�ͬ��ɫ
            if(choose==i){
                mPaint.setColor(Color.rgb(23,124,176));
                //如果对应方块被点击，则切换不同背景颜色的照片
                switch(i)
                {
                    case 0:disprove=BitmapFactory.decodeResource(this.getResources(),R.mipmap.message2);break;
                    case 1: ask=BitmapFactory.decodeResource(this.getResources(),R.mipmap.ask2);break;
                    case 2:copy=BitmapFactory.decodeResource(this.getResources(),R.mipmap.copy2);break;
                    case 3:report= BitmapFactory.decodeResource(this.getResources(),R.mipmap.report2);break;
                    case 4:   share= BitmapFactory.decodeResource(this.getResources(),R.mipmap.share2);break;
                    default:break;
                }
            }else {
                mPaint.setColor(Color.rgb(28,185,253));
            }

            // ���Ƶ�һ������
            if (i == 0) {
                mPath.reset();
                mPath.moveTo(realLeft+mItemWidth  , mItemBorder);//�ı������������λ��
                mPath.lineTo(realLeft+mCorner ,   mItemBorder);//���ߣ�Ĭ�������(0,0)��Ļ���Ͻ�
                mPath.quadTo(realLeft,mItemBorder
                        , realLeft,mItemBorder+mCorner);//ǰ���������ǿ��Ƶ����꣬�������������յ����꣬���������moveToָ��
                mPath.lineTo(realLeft,mItemBorder+mItemHeight-mCorner);
                mPath.quadTo(realLeft,mItemBorder+mItemHeight
                        ,realLeft+mCorner,mItemBorder+mItemHeight);
                mPath.lineTo(realLeft+mItemWidth,mItemBorder+mItemHeight);
                canvas.drawPath(mPath, mPaint);
                mSrcRect=new Rect(31, 31, report.getWidth()-31, report.getHeight()-31);//对图片进行裁剪，剪掉空白部分
                mDesRect=new Rect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+35, mItemBorder+10
                        , realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth-40, mItemBorder+mItemHeight-55);//截取的图显示的区域
                canvas.drawBitmap(disprove,mSrcRect,mDesRect,mPaint);
                // ���Ƶ�һ���ָ���
                mPaint.setColor(mSeparateLineColor);
                canvas.drawLine(realLeft+mItemWidth, mItemBorder
                        ,realLeft+mItemWidth, mItemBorder+mItemHeight , mPaint);

            }else if (i == mItemList.size() - 1) {
                // �������һ������
                mPath.reset();
                mPath.moveTo(realLeft +mItemWidth*(mItemList.size()-1) , mItemBorder);
                mPath.lineTo(realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth - mCorner, mItemBorder);
                mPath.quadTo(realLeft +mItemWidth*(mItemList.size()-1)+ + mItemWidth, mItemBorder
                        , realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth, mItemBorder  + mCorner);
                mPath.lineTo( realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth,mItemBorder+mItemHeight-mCorner);
                mPath.quadTo(realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth,mItemBorder+mItemHeight,
                        realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth-mCorner,mItemBorder+mItemHeight);
                mPath.lineTo(realLeft +mItemWidth*(mItemList.size()-1),mItemBorder+mItemHeight);
                canvas.drawPath(mPath, mPaint);
                // ���һ�����鲻��Ҫ���Ʒָ���
                mSrcRect=new Rect(31, 31, report.getWidth()-31, report.getHeight()-31);//对图片进行裁剪，剪掉空白部分
                mDesRect=new Rect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+35, mItemBorder+10
                        , realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth-40, mItemBorder+mItemHeight-55);//截取的图显示的区域
                canvas.drawBitmap(share,mSrcRect,mDesRect,mPaint);
            }else {
                mSrcRect=new Rect(31, 31, report.getWidth()-31, report.getHeight()-31);//对图片进行裁剪，剪掉空白部分
                mDesRect=new Rect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+35, mItemBorder+10
                        , realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth-40, mItemBorder+mItemHeight-55);//截取的图显示的区域
                // �����м䷽��
                canvas.drawRect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth, mItemBorder
                        , realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth, mItemBorder+mItemHeight, mPaint);
                // ���������ָ���
                switch(i){
                    case 1:canvas.drawBitmap(ask,mSrcRect,mDesRect,mPaint);break;
                    case 2:canvas.drawBitmap(copy,mSrcRect,mDesRect,mPaint);break;
                    case 3:canvas.drawBitmap(report,mSrcRect,mDesRect,mPaint);break;
                    default:break;
                }
                mPaint.setColor(mSeparateLineColor);
                canvas.drawLine(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth, mItemBorder
                        ,realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth, mItemBorder+mItemHeight , mPaint);


            }
            // ��ӷ��鵽���鼯��
            mItemRectList.add(new Rect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth,mItemBorder,realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() -(i+1)) * mItemWidth+mItemWidth,mItemBorder+mItemHeight));


        }

        // ���Ʒ���������
        drawTitle(canvas);


    }
    // �ɲο�drawItemDown
    private void drawItemUp(Canvas canvas) {
        mItemRectList.clear();

        mPath.reset();
        if (choose != -1)//��item������ʱ�������������������һ�飬����Ϊ��ʵ��qq��������Ч����������������ۣ���������ܶ࣬���ظ�����ܶ�
        {
            if (lstate && (!rstate))//���ڶ��Ҳ���
            {
                if (x < chooseItemright)//������Ե��item���ұ߿�
                {
                    doPaint2.setColor(Color.rgb(28,185,253));
                    doPaint1.setColor(Color.rgb(23,124,176));
                    mPath.moveTo(chooseItemright,mItemBorder+mTriangleright-chooseItemright);
                    mPath.lineTo(mTriangleright,mItemBorder);
                    mPath.lineTo(chooseItemright,mItemBorder);
                    mPath.close();//���Ӷ˵�
                    canvas.drawPath(mPath,doPaint2);
                    mPath.reset();
                    mPath.moveTo(chooseItemright,mItemBorder);
                    mPath.lineTo(mTriangleleft,mItemBorder);
                    mPath.lineTo(x,mTriangleTop);
                    mPath.lineTo(chooseItemright,mItemBorder+mTriangleright-chooseItemright);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint1);
                } else {
                    doPaint2.setColor(Color.rgb(28,185,253));//doPaint1��������С����󲿷�
                    doPaint1.setColor(Color.rgb(23,124,176));
                    mPath.moveTo(chooseItemright,mItemBorder+chooseItemright-mTriangleleft);//������ѧ��ϵ����������
                    mPath.lineTo(mTriangleleft,mItemBorder);//������벿��
                    mPath.lineTo(chooseItemright,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint1);
                    mPath.reset();
                    mPath.moveTo(chooseItemright,mItemBorder+chooseItemright-mTriangleleft);
                    mPath.lineTo(x,mTriangleTop);
                    mPath.lineTo(mTriangleright,mItemBorder);
                    mPath.lineTo(chooseItemright,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint2);
                }
            } else if ((!lstate) && rstate) {
                if (x >=chooseItemleft) {
                    doPaint1.setColor(Color.rgb(28,185,253));//doPaint1��������С����󲿷�
                    doPaint2.setColor(Color.rgb(23,124,176));
                    mPath.moveTo(chooseItemleft,mItemBorder+chooseItemleft-mTriangleleft);//������ѧ��ϵ����������
                    mPath.lineTo(mTriangleleft,mItemBorder);//������벿��
                    mPath.lineTo(chooseItemleft,mItemBorder);//mPath.moveTo(mTriangleleft,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint1);
                    mPath.reset();
                    mPath.moveTo(chooseItemleft,mItemBorder+chooseItemleft-mTriangleleft);
                    mPath.lineTo(x,mTriangleTop);
                    mPath.lineTo(mTriangleright,mItemBorder);
                    mPath.lineTo(chooseItemleft,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint2);
                } else {
                    doPaint1.setColor(Color.rgb(28,185,253));
                    doPaint2.setColor(Color.rgb(23,124,176));
                    mPath.moveTo(mTriangleleft,mItemBorder);
                    mPath.lineTo(chooseItemleft,mItemBorder);
                    mPath.lineTo(chooseItemleft,mItemBorder-chooseItemleft+mTriangleright);
                    mPath.lineTo(x,mTriangleTop);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint1);
                    mPath.reset();//���������ɫ��ʾ�����������
                    mPath.moveTo(chooseItemleft,mItemBorder-chooseItemleft+mTriangleright);
                    mPath.lineTo(mTriangleright,mItemBorder);
                    mPath.lineTo(chooseItemleft,mItemBorder);
                    mPath.close();
                    canvas.drawPath(mPath,doPaint2);
                }
            } else if (lstate && rstate) {//����С����ȫ�������������
                doPaint1.setColor(Color.rgb(23,124,176));//��ʱ����Ϊ���
                mPath.moveTo(x, mTriangleTop);//
                mPath.lineTo(x - mHalfTriangleWidth, mItemBorder);
                mPath.lineTo(x + mHalfTriangleWidth, mItemBorder);
                canvas.drawPath(mPath, doPaint1);
                Log.d(TAG,"��ȫ�ڷ�����");
            }
            else//�����������С�겻�������������
            {
                doPaint1.setColor(Color.rgb(28,185,253));
                mPath.moveTo(x, mTriangleTop);//
                mPath.lineTo(x - mHalfTriangleWidth, mItemBorder);
                mPath.lineTo(x + mHalfTriangleWidth, mItemBorder);
                canvas.drawPath(mPath, doPaint1);
            }
        }
        else//û�б����
        {
            doPaint1.setColor(Color.rgb(28,185,253));
            mPath.moveTo(x, mTriangleTop);//
            mPath.lineTo(x - mHalfTriangleWidth, mItemBorder);
            mPath.lineTo(x + mHalfTriangleWidth, mItemBorder);
            canvas.drawPath(mPath, doPaint1);
            Log.d(TAG,"û�б����");//����
        }
        for (int i = 0; i < mItemList.size(); i++) {

            if(choose==i){
                mPaint.setColor(Color.rgb(23,124,176));//�����õ����Ϊǳ��ɫ
                //如果对应方块被点击，则切换不同背景颜色的照片
                switch(i)
                {
                    case 0:disprove=BitmapFactory.decodeResource(this.getResources(),R.mipmap.message2);break;
                    case 1: ask=BitmapFactory.decodeResource(this.getResources(),R.mipmap.ask2);break;
                    case 2:copy=BitmapFactory.decodeResource(this.getResources(),R.mipmap.copy2);break;
                    case 3:report= BitmapFactory.decodeResource(this.getResources(),R.mipmap.report2);break;
                    case 4:   share= BitmapFactory.decodeResource(this.getResources(),R.mipmap.share2);break;
                    default:break;
                }
            }else {
                mPaint.setColor(Color.rgb(28,185,253));//�ҵ��ˣ�����ԭ������������ı���ɫ��Դͷ
            }



            if (i == 0) {
                mPath.reset();
                mPath.moveTo(realLeft+mItemWidth  ,       mItemBorder-mItemHeight);
                mPath.lineTo(realLeft+mCorner ,   mItemBorder-mItemHeight);
                mPath.quadTo(realLeft,mItemBorder-mItemHeight
                        , realLeft,mItemBorder-mItemHeight+mCorner);
                mPath.lineTo(realLeft,mItemBorder-mCorner);
                mPath.quadTo(realLeft,mItemBorder
                        ,realLeft+mCorner,mItemBorder);
                mPath.lineTo(realLeft+mItemWidth,mItemBorder);
                canvas.drawPath(mPath, mPaint);
                mSrcRect=new Rect(31, 31, report.getWidth()-31, report.getHeight()-31);//对图片进行裁剪，剪掉空白部分
                mDesRect=new Rect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+35, mItemBorder - mItemHeight+10
                        , realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth-40, mItemBorder-55);//截取的图显示的区域
                canvas.drawBitmap(disprove,mSrcRect,mDesRect,mPaint);
                mPaint.setColor(mSeparateLineColor);
                canvas.drawLine(realLeft+mItemWidth, mItemBorder - mItemHeight
                        ,realLeft+mItemWidth, mItemBorder , mPaint);

            }else if (i == mItemList.size() - 1) {
                mPath.reset();
                mPath.moveTo(realLeft +mItemWidth*(mItemList.size()-1) , mItemBorder - mItemHeight);
                mPath.lineTo(realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth - mCorner, mItemBorder - mItemHeight);
                mPath.quadTo(realLeft +mItemWidth*(mItemList.size()-1)+ + mItemWidth, mItemBorder - mItemHeight
                        , realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth, mItemBorder - mItemHeight + mCorner);
                mPath.lineTo( realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth,mItemBorder-mCorner);
                mPath.quadTo(realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth,mItemBorder,
                        realLeft +mItemWidth*(mItemList.size()-1) + mItemWidth-mCorner,mItemBorder);
                mPath.lineTo(realLeft +mItemWidth*(mItemList.size()-1),mItemBorder);
                canvas.drawPath(mPath, mPaint);
                mSrcRect=new Rect(31, 31, report.getWidth()-31, report.getHeight()-31);//对图片进行裁剪，剪掉空白部分
                mDesRect=new Rect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+35, mItemBorder - mItemHeight+10
                        , realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth-40, mItemBorder-55);//截取的图显示的区域
                canvas.drawBitmap(share,mSrcRect,mDesRect,mPaint);
            }else {
                mSrcRect=new Rect(31, 31, report.getWidth()-31, report.getHeight()-31);//对图片进行裁剪，剪掉空白部分
                mDesRect=new Rect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+35, mItemBorder - mItemHeight+10
                        , realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth-40, mItemBorder-55);//截取的图显示的区域
                canvas.drawRect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth, mItemBorder - mItemHeight
                        , realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth, mItemBorder, mPaint);//drawRect����:left,top,right,bottom.Paint��leftֵrightֵ��ʾ����xֵ�ķ�Χ,top��bottom��ʾ����yֵ�ķ�Χ
                switch(i){
                    case 1:canvas.drawBitmap(ask,mSrcRect,mDesRect,mPaint);break;
                    case 2:canvas.drawBitmap(copy,mSrcRect,mDesRect,mPaint);break;
                    case 3:canvas.drawBitmap(report,mSrcRect,mDesRect,mPaint);break;
                    default:break;
                }
                mPaint.setColor(mSeparateLineColor);
                canvas.drawLine(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth, mItemBorder - mItemHeight
                        ,realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth+ mItemWidth, mItemBorder , mPaint);

            }

            mItemRectList.add(new Rect(realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() - (i+1)) * mItemWidth,mItemBorder-mItemHeight,realLeft +mItemWidth*(mItemList.size()-1)-(mItemList.size() -(i+1)) * mItemWidth+mItemWidth,mItemBorder));



        }

        drawTitle(canvas);

    }

// ���ƴ����ϵ�����


    private void drawTitle(Canvas canvas) {
        TipItem tipItem;
        for(int i = 0;i<mItemRectList.size();i++) {
            tipItem = mItemList.get(i);
            mPaint.setColor(tipItem.getTextColor());//ԭ��tipitem��ɫ�ı�ı�����view��paint,canvas�ĸı�
            if (mStatus == STATUS_UP) {
                canvas.drawText(tipItem.getTitle(), mItemRectList.get(i).left +mItemWidth/2- getTextWidth(tipItem.getTitle(), mPaint) / 2, mItemBorder - mItemHeight / 2 +  getTextHeight(mPaint)/2+57, mPaint);
            } else if (mStatus == STATUS_DOWN) {
                canvas.drawText(tipItem.getTitle(), mItemRectList.get(i).left +mItemWidth/2- getTextWidth(tipItem.getTitle(), mPaint) / 2, mItemRectList.get(i).bottom - mItemHeight / 2 +  getTextHeight(mPaint)/2+57, mPaint);
            }//��ν�Ļ�������ԭ��Ҳֻ���ڻ����ϻ��ƣ��䱾��û�л��ƹ���
        }

    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
    }//�����ҵú�����,�ð����������ı���



    public void setTipItemList(final List<TipItem> list) {
        mItemList.clear();
        List<TipItem> items = list;
        for (TipItem item : items) {
            if (!TextUtils.isEmpty(item.getTitle())) {
                item.setTitle(updateTitle(item.getTitle()));
            } else {
                item.setTitle("");
            }

            mItemList.add(item);
        }
    }

    private String updateTitle(String title) {
        int textLength = title.length();
        String suffix = "";
        while (getTextWidth(title.substring(0, textLength) + "...", mPaint) > mItemWidth - dip2px(10)) {
            textLength--;
            suffix = "...";
        }
        return title.substring(0, textLength) + suffix;
    }

    public void setSeparateLineColor(int separateLineColor) {
        mSeparateLineColor = separateLineColor;
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }




    private float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.bottom-fontMetrics.descent-fontMetrics.ascent;
    }


    private float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < mItemRectList.size(); i++) {
                    if (onItemClickListener != null && isPointInRect(new PointF(event.getX(), event.getY()), mItemRectList.get(i))) {//���Ǹ����涼������Ȼ����˴���λ���Ƿ��ھ�����
                        // ������ʱ��chooseֵΪ��ǰ����Item���
                        choose = i;
                        // ������ͼ//������ͼʱ�������е�Ӧ����draw��
                        postInvalidate(mItemRectList.get(i).left,mItemRectList.get(i).top,mItemRectList.get(i).right,mItemRectList.get(i).bottom);
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < mItemRectList.size(); i++) {
                    if (onItemClickListener != null && isPointInRect(new PointF(event.getX(), event.getY()), mItemRectList.get(i))) {
                        // ����������������������
                        onItemClickListener.onItemClick(mItemList.get(i).getTitle(),i);
                        choose = -1;
                    }
                }

                if (onItemClickListener != null) {
                    onItemClickListener.dismiss(); // ��������
                }

                removeView(); // �Ƴ�TipView

                return true;
        }
        return true;
    }

    private void removeView() {
        ViewGroup vg = (ViewGroup) this.getParent();
        if (vg != null) {
            vg.removeView(this);
        }
    }

    private boolean isPointInRect(PointF pointF, Rect targetRect) {//����ͨ������һ���򵥵ķ�������˵����item���ĸ�
        if (pointF.x < targetRect.left) {
            return false;
        }
        if (pointF.x > targetRect.right) {
            return false;
        }
        if (pointF.y < targetRect.top) {
            return false;
        }
        if (pointF.y > targetRect.bottom) {
            return false;
        }
        return true;
    }

    private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String name,int position);
        void dismiss();
    }



    public static class Builder {

        private OnItemClickListener onItemClickListener;
        private Context mContext;
        private ViewGroup mRootView;
        private List<TipItem> mTipItemList = new ArrayList<>();
        private int mSeparateLineColor =Color.rgb(28,185,253);//��������ʱ����Ϊblue����Ч��
        private int x ,y;

        public Builder(Context context, ViewGroup rootView,int x,int y) {
            mContext = context;
            mRootView = rootView;
            this.x = x;
            this.y = y;
        }

        public Builder addItem(TipItem tipItem) {
            mTipItemList.add(tipItem);
            return this;
        }

        public Builder addItems(List<TipItem> list) {
            mTipItemList.addAll(list);
            return this;
        }

        public Builder setSeparateLineColor(int color) {
            mSeparateLineColor = color;
            return this;
        }

        public Builder setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public TipView create() {
            TipView flipShare = new TipView(mContext, mRootView,x,y,mTipItemList);
            flipShare.setOnItemClickListener(onItemClickListener);
            flipShare.setSeparateLineColor(mSeparateLineColor);//���÷ֽ�����ɫ
            return flipShare;
        }

    }
    public void setState()//��������򵥵ķ�װʵ������������״̬�Ĵ洢
    {
        if(mTriangleleft>=chooseItemleft&&mTriangleleft<=chooseItemright)
            lstate=true;
        else lstate=false;
        if(mTriangleright>=chooseItemleft&&mTriangleright<=chooseItemright)
            rstate=true;
        else rstate=false;
    }

}

