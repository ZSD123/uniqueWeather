package activity;



import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.uniqueweather.app.R;


import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
public class loginAct extends Activity {

    private CustomFont customFont;
    private myCaihong caihong;
    private int []location=new int[2];
    
    private int widthPixels;
    private int heightPixels;
    
    private Button button1;  //登录按钮
    private Button button2;  //手机验证登录
    private Button button3;  //快速注册
    private TextView text1;  //先体验
    private TextView text2;  //忘记密码
    private boolean flag1=true;    //button1亮色，true表示启动
    private boolean flag2=false;    //button2亮色
    private boolean flag3=false;    //button3亮色
	@Override
	public void onCreate(Bundle savedInstanceState)
     {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
	  //Bmob.initialize(this, "f3065817051f7c298d2e49d9329a2a6b");
		button1=(Button)findViewById(R.id.login);
		button2=(Button)findViewById(R.id.shoujidenglu);
		button3=(Button)findViewById(R.id.register);
	    customFont=(CustomFont)findViewById(R.id.text_EndRain);
	    caihong=(myCaihong)findViewById(R.id.caihong);
	    text1=(TextView)findViewById(R.id.wangjimima);
	    text2=(TextView)findViewById(R.id.xiantiyan);
	    text1.setText(Html.fromHtml("<u>"+"忘记密码"+"</u>"));
	    text2.setText(Html.fromHtml("<u>"+"先体验"+"</u>"));
	    button2.setBackgroundColor(0);
	    button3.setBackgroundColor(0);
	 //   BmobUser bu=new BmobUser();

	    button1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				int action=motionEvent.getAction();
				if(action==motionEvent.ACTION_DOWN)
				{  button1.setBackgroundColor(Color.parseColor("#006400"));
				  if (flag2) 
				  { 
				    button2.setBackgroundColor(0);
				  }
				  if(flag3)
					 button3.setBackgroundColor(0);
				   flag1=true;
				}
				if(action==motionEvent.ACTION_UP)
					button1.setBackgroundColor(Color.parseColor("#00FF00"));
				return false;
			}
		});
	    button2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) 
			{
				int action=motionEvent.getAction();
				if(action==motionEvent.ACTION_DOWN)
					{button2.setBackgroundColor(Color.parseColor("#006400"));
					 if (flag1) 
					    button1.setBackgroundColor(0);
					 if(flag3)
						 button3.setBackgroundColor(0);
					   flag2=true;
					}
				if(action==motionEvent.ACTION_UP)
					button2.setBackgroundColor(Color.parseColor("#00FF00"));
				return false;
			}
		});
	    button3.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) 
			{
                int action=motionEvent.getAction();
				if(action==motionEvent.ACTION_DOWN)
					{button3.setBackgroundColor(Color.parseColor("#006400"));
					 if (flag1) 
					    button1.setBackgroundColor(0);
					 if(flag2)
						 button2.setBackgroundColor(0);
					   flag3=true;
					}
				if(action==motionEvent.ACTION_UP)
					button3.setBackgroundColor(Color.parseColor("#00FF00"));
				return false;
			}
		});
        caihong.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) 
		{
				if(motionEvent.getX()<caihong.getCaihongx()+customFont.getWidth()&&motionEvent.getX()>caihong.getCaihongx()-widthPixels/320*30
							&&motionEvent.getY()<caihong.getCaihongy()+customFont.getHeight()-heightPixels/480*10&&motionEvent.getY()>caihong.getCaihongy()-heightPixels/480*60)
			   {   
					caihong.beginAnimation(2000);
				
				}
				return false;
			}
		});
	 }
	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
		super.onWindowFocusChanged(hasFocus);
	   customFont.getLocationInWindow(location);
	    caihong.getTextLocation(location);
	   caihong.getTextWidth(customFont.getWidth());
	    caihong.getTextHeight(customFont.getHeight());
	    DisplayMetrics metrics=new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthPixels=metrics.widthPixels;
	    heightPixels=metrics.heightPixels;
	    caihong.getPiexlWidth(widthPixels);
	    caihong.getPiexlHeight(heightPixels);
	 }
	
  }
