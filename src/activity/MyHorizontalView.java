package activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class MyHorizontalView extends HorizontalScrollView {
    private int mScreenWidth;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private LinearLayout mWapper;
    private int mScreenRight=50;
    private int mMenuWidth;
    private boolean once=false;
	public MyHorizontalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth=outMetrics.widthPixels;
		mScreenRight=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,context.getResources().getDisplayMetrics());
	}
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
	{   if(!once)
		{mWapper=(LinearLayout)getChildAt(0);
		mMenu=(ViewGroup)mWapper.getChildAt(0);
		mContent=(ViewGroup)mWapper.getChildAt(1);
		mMenuWidth=mMenu.getLayoutParams().width=mScreenWidth-mScreenRight;
		mContent.getLayoutParams().width=mScreenWidth;
		once=true;
		}
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
   @Override
   public void onLayout(boolean changed,int l,int t,int r,int b)
   {   super.onLayout(changed, l, t, r, b);
	   if(changed)
	   {
		   this.scrollTo(mMenuWidth, 0);
	   }
   }
   @Override
   public boolean onTouchEvent(MotionEvent ev)
   {
	   int motion=ev.getAction();
	   switch (motion) {
	   case MotionEvent.ACTION_UP:
		   int x=getScrollX();
		   if(x>=mMenuWidth/2)
		   {
			   this.smoothScrollTo(mMenuWidth, 0);
		   }
		   else{
			   this.smoothScrollTo(0, 0);
		   }
		  return true;
	    
   }
	  return super.onTouchEvent(ev); 
}
}
