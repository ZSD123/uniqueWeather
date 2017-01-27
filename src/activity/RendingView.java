package activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class RendingView extends View implements View.OnClickListener {
	private boolean mlnit;  //�Ƿ�layout���
	private Paint mPaint;
	private RectF mRectF;
	
	private float mProgress;  //0~1
	
	private Bitmap mCurBitmap;
	private Rect mBitmapRect;
	private RectF mDrawableRectF;
	
    private Rect mSrc;
    private RectF mDst;
    
    private static final int DEFAULT_ANIMATION_DURATION=1000;
    
    private OnClickListener mClickListener;
    
    private ObjectAnimator mAnimator;
    
    public RendingView(Context context)
    {
    	this(context,null);
    }
    public RendingView(Context context,AttributeSet attrs)
    {
        this(context,attrs,0);
    }
    public RendingView(Context context,AttributeSet attrs,int defStyle)
    {
    	super(context,attrs,defStyle);
    	init();
    }
    private void init()
    {
    	mlnit=false;
    	mPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);//������ӵ����λ��Ƽ��Ρ��ı���λͼ�ķ�����ɫ��Ϣ����1��ֵ���Ƶ�ʱ�򿹾�ݣ����棬��2��ֵ�����ŵ�λͼ����˫����ȡ��
    	mRectF=new RectF();
    	mBitmapRect=new Rect();
    	mDrawableRectF=new RectF();
    	mSrc=new Rect();
    	mDst=new RectF();
    	mProgress=0;
    	mAnimator=ObjectAnimator.ofFloat(this,"progress",1);//���ValueAnimator�ĸ����ṩ�˶�Ŀ�����Ķ�������֧�֣������Ĺ��캯��ʹ�ò��������彫����������Ŀ������Լ����������������Ե����ơ���һ��ֵ��ʾ�����ﵽ��ֵ
    	                                                    //���Խ����������Ķ����������Ӧ����setname()�и����������������Ǻ���Ĳ���
    	                                                   //�������������Ե�����
    	                                                   //��������ʱ��仯��һ��ֵ
    	mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());//���ڼ�����������ľ�����Ƭ�ε�ʱ���ֵ������ֵ��ȷ�����������Ի��Ƿ����ԣ�����ٺͼ���
    	super.setOnClickListener(this);                                                                 //һ���ڲ��������Ŀ�ʼ�ͽ����ı��ٶ���������ͨ���м��ʱ�����
    }
    @Override
    protected void onLayout(boolean changed,int left,int top,int right,int bottom)//����ͼ�����С��λ�ø�ÿ�������ʱ��Ӳ��ֵ��á����������������Ӧ����д���������������ÿ������Ĳ���
    {
    	super.onLayout(changed, left, top, right, bottom);
    	if(changed)
    	{
    		doInit(getViewRect(this));   //������ͼ�ǳ�������֮�����ͼ
    	}
    }
    public static RectF getViewRect(View view)
    { int vLeft, vTop; 
      int location[] = new int[2]; 
      view.getLocationInWindow(location); //�ڴ����м������ͼ�����꣬����Ϊ�������������顣�ڷ������غ��������x��y��λ��
      vLeft = location[0]; vTop = location[1]; 
      RectF mViewRect = new RectF(vLeft, vTop, vLeft + view.getWidth(), vTop + view.getHeight()); 
      return mViewRect; 
    }
    @Override
    protected void onDraw(Canvas canvas) 
    {
      super.onDraw(canvas);
      if(mlnit && mCurBitmap != null)
       { mSrc.set(mBitmapRect.left, mBitmapRect.top, (int)(mBitmapRect.right * mProgress + 0.5f), mBitmapRect.bottom);//�ʺ�λͼ
         mDst.set(mDrawableRectF.left, mDrawableRectF.top, mDrawableRectF.right * mProgress, mDrawableRectF.bottom);//��ʾ��ȫչ��RectF���߼���������0
         canvas.drawBitmap(mCurBitmap, null, mDst, mPaint);//һΪ�ʺ�λͼ��Ҫ���Ƶ�λͼ
       }                                                        
    } 
    private void doInit(RectF rectF)
    {
    	 if(!mlnit || !mRectF.equals(rectF))//����տ�ʼ��ʼ������mRectF������rectF
    	 {
    	   mRectF.set(rectF);   //rectFΪ������ͼ�ǳ�������֮�����ͼ����
    	   if(mCurBitmap != null)
    	    {
    	     setInformation();
    	    }
    	   mlnit = true;
    	 }
    }
    public float getProgress() {
    	 return mProgress;
    	 }
    public void setProgress(float progress) {
    	 this.mProgress = progress;
    	 invalidate();
    	 }
    public void setImageBitmap(Bitmap bitmap){
    	   mCurBitmap = bitmap;  //�ʺ�λͼ
    	  if (mCurBitmap != null && mlnit) 
    	    {
    	    setInformation();
    	    }
    	 }
    public void setInformation()
    { float w = mCurBitmap.getWidth(); //mCurBitmapΪ�ʺ�λͼ
      float h = mCurBitmap.getHeight(); 
      float viewW = mRectF.width(); //mRectFΪ������ͼ�ǳ�������֮�����ͼ����
      float viewH = mRectF.height(); 
      mBitmapRect.set(0, 0, (int)w, (int)h); 
      if(w / h > viewW / viewH)
        { //���� 
    	  float curH = h * viewW / w; 
    	  mDrawableRectF.set(0, (viewH - curH) / 2, viewW, (viewH - curH) / 2 + curH); 
    	} 
       else
         {//���� 
    	  float curW = w * viewH / h; 
    	  mDrawableRectF.set((viewW - curW) / 2, 0, (viewW - curW) / 2 + curW, viewH); 
    	 } 
          beginAnimation(DEFAULT_ANIMATION_DURATION); 
      }
    public void beginAnimation(int duration)
    {    
    	 setProgress(0);
    	 mAnimator.cancel();//ȡ������
    	 mAnimator.setDuration(duration).start(); 
    }
    @Override
    public void setOnClickListener(OnClickListener l) 
    {
    mClickListener = l;
    }  
    @Override
    public void onClick(View v) 
    {
       beginAnimation(DEFAULT_ANIMATION_DURATION);
       if(mClickListener != null)
       {
         mClickListener.onClick(v);
        }
    }
}
