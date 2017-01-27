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
	private boolean mlnit;  //是否layout完成
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
    	mPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);//绘制类拥有如何绘制几何、文本和位图的风格和颜色信息；第1个值绘制的时候抗锯齿，保真，第2个值在缩放的位图进行双线性取样
    	mRectF=new RectF();
    	mBitmapRect=new Rect();
    	mDrawableRectF=new RectF();
    	mSrc=new Rect();
    	mDst=new RectF();
    	mProgress=0;
    	mAnimator=ObjectAnimator.ofFloat(this,"progress",1);//这个ValueAnimator的父类提供了对目标对象的动画属性支持，这个类的构造函数使用参数来定义将被动画化的目标对象以及将被动画化的属性的名称。单一的值表示动画达到的值
    	                                                    //属性将被动画化的对象，这个对象应该在setname()有个公共方法，名字是后面的参数
    	                                                   //被动画化的属性的名称
    	                                                   //动画将随时间变化的一组值
    	mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());//用于计算这个动画的经过的片段的时间插值器，插值器确定动画是线性还是非线性，如加速和减速
    	super.setOnClickListener(this);                                                                 //一个内插器，它的开始和结束改变速度慢，但是通过中间的时候加速
    }
    @Override
    protected void onLayout(boolean changed,int left,int top,int right,int bottom)//当视图分配大小和位置给每个子类的时候从布局调用。带有子类的派生类应该重写这个方法，并调用每个子类的布局
    {
    	super.onLayout(changed, left, top, right, bottom);
    	if(changed)
    	{
    		doInit(getViewRect(this));   //这里视图是除下拉栏之外的视图
    	}
    }
    public static RectF getViewRect(View view)
    { int vLeft, vTop; 
      int location[] = new int[2]; 
      view.getLocationInWindow(location); //在窗口中计算该视图的坐标，参数为两个整数的数组。在方法返回后，数组包含x和y的位置
      vLeft = location[0]; vTop = location[1]; 
      RectF mViewRect = new RectF(vLeft, vTop, vLeft + view.getWidth(), vTop + view.getHeight()); 
      return mViewRect; 
    }
    @Override
    protected void onDraw(Canvas canvas) 
    {
      super.onDraw(canvas);
      if(mlnit && mCurBitmap != null)
       { mSrc.set(mBitmapRect.left, mBitmapRect.top, (int)(mBitmapRect.right * mProgress + 0.5f), mBitmapRect.bottom);//彩虹位图
         mDst.set(mDrawableRectF.left, mDrawableRectF.top, mDrawableRectF.right * mProgress, mDrawableRectF.bottom);//表示完全展开RectF，边际贴着坐标0
         canvas.drawBitmap(mCurBitmap, null, mDst, mPaint);//一为彩虹位图，要绘制的位图
       }                                                        
    } 
    private void doInit(RectF rectF)
    {
    	 if(!mlnit || !mRectF.equals(rectF))//如果刚开始初始化或者mRectF不等于rectF
    	 {
    	   mRectF.set(rectF);   //rectF为这里视图是除下拉栏之外的视图矩形
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
    	   mCurBitmap = bitmap;  //彩虹位图
    	  if (mCurBitmap != null && mlnit) 
    	    {
    	    setInformation();
    	    }
    	 }
    public void setInformation()
    { float w = mCurBitmap.getWidth(); //mCurBitmap为彩虹位图
      float h = mCurBitmap.getHeight(); 
      float viewW = mRectF.width(); //mRectF为这里视图是除下拉栏之外的视图矩形
      float viewH = mRectF.height(); 
      mBitmapRect.set(0, 0, (int)w, (int)h); 
      if(w / h > viewW / viewH)
        { //顶宽 
    	  float curH = h * viewW / w; 
    	  mDrawableRectF.set(0, (viewH - curH) / 2, viewW, (viewH - curH) / 2 + curH); 
    	} 
       else
         {//顶高 
    	  float curW = w * viewH / h; 
    	  mDrawableRectF.set((viewW - curW) / 2, 0, (viewW - curW) / 2 + curW, viewH); 
    	 } 
          beginAnimation(DEFAULT_ANIMATION_DURATION); 
      }
    public void beginAnimation(int duration)
    {    
    	 setProgress(0);
    	 mAnimator.cancel();//取消动画
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
