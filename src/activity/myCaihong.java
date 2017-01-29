package activity;

import com.uniqueweather.app.R;


import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class myCaihong extends View {
	 private Bitmap mBitmap;
	 private Rect src;
	 private RectF dst;
	 private ValueAnimator mAnimator;
	 private float mProgress=0;
	 private Paint mPaint;
	 private Rect mBitmapRect;
	 private RectF mRectF;
	 
     private int textWidth;
     private int textHeight;
	 public   int caihongx;//起始横坐标
	 public int  caihongy;//起始纵坐标
	 public   float w;
	 public   float h;
	 
	 private int pixelWidth;
	 private int pixelHeight;
     public myCaihong(Context context) 
       {   
	       super(context); 
	       initialize();   
	   } 
     public myCaihong(Context context,AttributeSet attris)
     {
    	 super(context, attris);
    	 initialize();
     }
	     private void initialize() 
	   {   
	       Bitmap bmp = ((BitmapDrawable)getResources().getDrawable(R.drawable.caihong)).getBitmap();   
	       mBitmap = bmp;  
	       
	       mPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
	       mRectF=new RectF();
	       mBitmapRect=new Rect();
	     
	       w=mBitmap.getWidth();
	       h=mBitmap.getHeight();
	       mBitmapRect.set(0, 0, (int)w, (int)h); 
           src=new Rect();
	       dst=new RectF();
	       mAnimator=ValueAnimator.ofFloat(0f,1f);
	       mAnimator.addUpdateListener(new AnimatorUpdateListener() 
	       {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) 
			{
		       invalidate();
			}
		});
	       beginAnimation(2000);
	       
	      
	   }   
	   @Override 
	   protected void onDraw(Canvas canvas) 
	   {   
	       super.onDraw(canvas);  //当然，如果界面上还有其他元素需要绘制，只需要将这句话写上就行了。   
	       mRectF.set(caihongx-pixelWidth/320*30,caihongy-pixelHeight/480*60,caihongx+textWidth,caihongy+textHeight-pixelHeight/480*10);
	       mProgress=(Float) mAnimator.getAnimatedValue();
	       src.set(mBitmapRect.left,mBitmapRect.top, (int)(mBitmapRect.right * mProgress+0.5f), mBitmapRect.bottom);//彩虹位图
	       dst.set(mRectF.left,mRectF.top,mRectF.right*mProgress,mRectF.bottom);
	       canvas.drawBitmap(mBitmap,src,dst,mPaint); 
	       
	   } 

	    public void beginAnimation(int duration)
	    {    
	    	mAnimator.cancel(); 
	    	mAnimator.setDuration(duration).start(); 
	    }
	
        public void getTextLocation(int location[])
        {
        	
        	caihongx=location[0];
        	caihongy=location[1];
        }
	
	  public int getCaihongx()
	  {
		  return caihongx;
	  }
	  public int getCaihongy()
	  {
		  return caihongy;
	  }
	  public float getW()
	  {
		  return w;
	  }
	  public float getH()
	  {
		  return h;
	  }
      public void getTextWidth(int width)
      {
    	  textWidth=width;
      }
      public void getTextHeight(int height)
      {
    	  textHeight=height;
      }
      public void getPiexlWidth(int width)
      {
    	  pixelWidth=width;
      }
      public void getPiexlHeight(int height)
      {
    	  pixelHeight=height;
      }
      
}
