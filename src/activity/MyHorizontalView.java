package activity;

import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
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
    public static  int mMenuWidth;
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
			   this.smoothScrollTo(mMenuWidth, 0);//这是表示菜单部分滑到最左边
		   }
		   else{
			   this.smoothScrollTo(0, 0);  //这是表示菜单部分滑到最右边
		   }
		  return true;
	case MotionEvent.ACTION_DOWN:
		   int x1=getScrollX();
		   float x2=ev.getX();
		   if(x1==0&&x2>mMenuWidth)
		   {   
			   this.smoothScrollTo(mMenuWidth, 0);
		   }
		  return true;
	    
          }
	  return super.onTouchEvent(ev); 
}
@Override
protected void onScrollChanged(int l, int t, int oldl, int oldt) 
{   float scale=l*1.0f/mMenuWidth;    //1~0，当菜单在左边的时候，l是为0的，scale是为0的，
                                      //当左侧菜单完整滑出的时候，l就为0  
	ViewHelper.setAlpha(mMenu,(1-scale)*0.7f+0.3f);  
    ViewHelper.setTranslationX(mMenu, l*((1-scale)*0.7f+0.3f));//当左侧菜单完全显示，偏移量就为0，这个就是使指定的控件在X方向移动多少的距离啊，你不设置，菜单会慢慢画出来，因为外层继承了HorizontalScrollView，默认menu会慢慢向右滑出，ViewHelper.setTranslationX（menu,L）L不断减小，它每次都是移动到相同的位置，就是正屏幕位置。
	super.onScrollChanged(l, t, oldl, oldt);
}
   
  
}
