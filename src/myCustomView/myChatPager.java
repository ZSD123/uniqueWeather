package myCustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class myChatPager extends ViewPager {
	 public  myChatPager(Context context) {
	        super(context);
	    }
     public myChatPager(Context context,AttributeSet attrs){
    	 super(context, attrs);
     }
	
	    @Override
	    public boolean onTouchEvent(MotionEvent arg0) {
	        return false;
	    }

	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent arg0) {
	        return false;
	    }
}
