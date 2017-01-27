package activity;

import com.uniqueweather.app.R;

import android.animation.ObjectAnimator;
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

public class myCaihong extends View {
	 private Bitmap mBitmap;
	 private Rect src;
	 private RectF dst;
	 private ValueAnimator mAnimator;
	 private float mProgress=0;
	 private Paint mPaint;
	 private Rect mBitmapRect;
	 private RectF mRectF;
	 //private float yuanProgress=0;
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
	       float w=mBitmap.getWidth();
	       float h=mBitmap.getHeight();
	       mBitmapRect.set(0, 0, (int)w, (int)h); 
	       mRectF.set(0,1000,720, 1255);
	       src=new Rect();
	       dst=new RectF();
	       mAnimator=ValueAnimator.ofFloat(0f,1f);
	       mAnimator.addUpdateListener(new AnimatorUpdateListener() {
			
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
	       mProgress=(Float) mAnimator.getAnimatedValue();
	       src.set(mBitmapRect.left,mBitmapRect.top, (int)(mBitmapRect.right * mProgress+0.5f), mBitmapRect.bottom);//彩虹位图
	       dst.set(mRectF.left,mRectF.top,mRectF.right*mProgress,mRectF.bottom);
	       Log.d("Main","left="+mRectF.left);
	       Log.d("Main","top="+mRectF.top);
	       Log.d("Main","right="+mRectF.right);
	       Log.d("Main","bottom="+mRectF.bottom);
	       Log.d("Main","mBitmapRect.right="+(mBitmapRect.right * mProgress+0.5f));
	       canvas.drawBitmap(mBitmap,src,dst,mPaint); 
	       Log.d("Main","progress="+mProgress);
	   } 

	    public void beginAnimation(int duration)
	    {    
	    	 mAnimator.setDuration(duration).start(); 
	    }
        
}
