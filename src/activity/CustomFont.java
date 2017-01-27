package activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFont extends TextView {
	public CustomFont(Context context)
    {
    	super(context);
    	init(context);
    }
    public CustomFont(Context context,AttributeSet attrs)
    {
    	super(context, attrs);
    	init(context);
    }
    public CustomFont(Context context,AttributeSet attrs,int defStyle)
    {
    	super(context, attrs, defStyle);
    	init(context);
    }
    private void init (Context context)
    {
    	AssetManager assertMgr=context.getAssets();
        Typeface font=Typeface.createFromAsset(assertMgr,"fonts/newEnglish.ttf");
        setTypeface(font);
    }
}
