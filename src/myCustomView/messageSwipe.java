package myCustomView;

import activity.fragmentChat;
import android.R.integer;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem.Flags;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class messageSwipe extends SwipeRefreshLayout {
	private float mPrevX;
	public messageSwipe(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mPrevX=event.getX();
				break;
			case MotionEvent.ACTION_MOVE :
				fragmentChat.canScroll=false;
				final float eventX=event.getX();
				float xDiff=Math.abs(eventX-mPrevX);
                
				if(xDiff>200){
					fragmentChat.canScroll=true;
				}
				break;
			case MotionEvent.ACTION_UP:
				fragmentChat.canScroll=true;
				break;
		}
		return super.onInterceptTouchEvent(event);
	}
	

}
