package activity;



import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.c.i;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

import com.a.a.in;
import com.uniqueweather.app.R;



import Util.MD5Util;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Type;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class loginAct extends Activity {

    private CustomFont customFont;
    private myCaihong caihong;
    private int []location=new int[2];
    
    private int widthPixels;
    private int heightPixels;
    
    
    private boolean kedenglu=false;  //邮箱验证登录
    
    private Button button1;  //登录按钮
    private Button button2;  //手机验证登录
    private Button button3;  //快速注册
    
    private TextView text1;  //先体验
    private TextView text2;  //忘记密码
    private boolean flag1=true;    //button1亮色，true表示启动
    private boolean flag2=false;    //button2亮色
    private TextView account;       //帐号
    private TextView password;       //密码
    
    private EditText editText1;     //第一行
    private EditText editText2;     //第二行
    private Button button4;    //  发送验证码
    
    private String input;      //输入数据
    private String passwordString;   //密码

    private boolean fasong=true;    //注册的时候自动发送标志,true表示可以发送
    private ImageView imageView;
    private boolean eye=false;     //眼睛关上
    private static final int UPDATE_TEXT=0;
    private static final int TOFASONG=1;
    
    private CheckBox checkBox;       //我同意服务条款checkbox
    
    private int daojishi=30;   
    private Message message;
    private Timer timer=new Timer();
    private Handler handler=new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case UPDATE_TEXT:
    			button4.setText(daojishi+"s后再发送");
    			break;
    		case TOFASONG:
    			button4.setText("发送验证码");
    			fasong=true;
    		default:
    			break;
    		}
    	}
    };
    private TimerTask task;
    @Override
	public void onCreate(Bundle savedInstanceState)
     {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
	    
		Bmob.initialize(this, "f3065817051f7c298d2e49d9329a2a6b");
		final MyUser bu=new MyUser();
	    
		button1=(Button)findViewById(R.id.login);
		button2=(Button)findViewById(R.id.shoujidenglu);
		button3=(Button)findViewById(R.id.register);
		button3.setBackgroundColor(0);
	    customFont=(CustomFont)findViewById(R.id.text_EndRain);
	    caihong=(myCaihong)findViewById(R.id.caihong);
	    text1=(TextView)findViewById(R.id.wangjimima);
	    text2=(TextView)findViewById(R.id.xiantiyan);
	    text1.setText(Html.fromHtml("<u>"+"忘记密码"+"</u>"));
	    text2.setText(Html.fromHtml("<u>"+"先体验"+"</u>"));
	    account=(TextView)findViewById(R.id.account);
	    password=(TextView)findViewById(R.id.password);
	    
	    checkBox=(CheckBox)findViewById(R.id.checkbox);
	    checkBox.setChecked(true);
	    
	    imageView=(ImageView)findViewById(R.id.kekanmima);
	    editText1=(EditText)findViewById(R.id.editText_account);
	    editText2=(EditText)findViewById(R.id.editText_secret);
	    
	    editText1.setInputType(InputType.TYPE_CLASS_TEXT);
	    button4=(Button)findViewById(R.id.sendYanzhengma);
	    button2.setBackgroundColor(0);
		MyUser userInfo=MyUser.getCurrentUser(loginAct.this,MyUser.class);
		if(userInfo!=null){
			  Intent intent=new Intent(loginAct.this,weather_info.class);
			  startActivity(intent);
			  finish();
		}
	    button1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				int action=motionEvent.getAction();
				if(action==MotionEvent.ACTION_DOWN)
				{  
					input=editText1.getText().toString();
					passwordString=editText2.getText().toString();
					button1.setBackgroundColor(Color.parseColor("#006400"));
				  if (flag2) 
				     button2.setBackgroundColor(0);
				   account.setText("帐号");
				   account.setTextSize(20);
				   editText1.setHint("手机号或者邮箱");
				   password.setText("密码");
				   editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
				   password.setTextSize(20);
				   imageView.setVisibility(View.VISIBLE);
				   button4.setVisibility(View.GONE);
				   if((input.isEmpty()||passwordString.isEmpty())&&flag1)
					  Toast.makeText(loginAct.this, "帐号或密码不能为空", Toast.LENGTH_SHORT).show();
				   else if(isMobileNO(input)||isEmail(input))
				   {   
					   bu.setUsername(input);
					   bu.setPassword(MD5Util.getMD5String(passwordString));			  
					   
					   if(isEmail(input)){
					   BmobQuery<MyUser> query=new BmobQuery<MyUser>("_User");
					   query.addWhereEqualTo("username",input);
					   query.findObjects(loginAct.this, new FindCallback() {
						
						@Override
						public void onFailure(int a, String b) {
							if(a==9016)
							    Toast.makeText(loginAct.this,"连接失败，请稍后再试，无网络连接，请检查您的手机网络", Toast.LENGTH_SHORT).show();
							else {
								Toast.makeText(loginAct.this,"连接失败，请稍后再试，"+b,Toast.LENGTH_SHORT).show();
							}
						}
						
						@Override
						public void onSuccess(JSONArray array) {
						    try {
								JSONObject jsonObject=array.getJSONObject(0);
								kedenglu=jsonObject.getBoolean("emailVerified");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						    if(kedenglu){
						    	 bu.login(loginAct.this,new SaveListener() {  
										@Override
										public void onSuccess() 
										{     
											  Toast.makeText(loginAct.this, "登录成功", Toast.LENGTH_SHORT).show();
											  Intent intent=new Intent(loginAct.this,weather_info.class);
											  startActivity(intent);
											  finish();
										}
										
										@Override
										public void onFailure(int arg0, String arg1) 
										{   
											Toast.makeText(loginAct.this,"登录失败,"+arg1,Toast.LENGTH_SHORT ).show();
										}
									});
						    }else {
								Toast.makeText(loginAct.this,"请先验证您的邮箱",Toast.LENGTH_SHORT).show();
							}
						    
						}
					});
					   }else if(isMobileNO(input)){
						 bu.login(loginAct.this,new SaveListener() {  
								@Override
								public void onSuccess() 
								{     
									  Toast.makeText(loginAct.this, "登录成功", Toast.LENGTH_SHORT).show();
									  Intent intent=new Intent(loginAct.this,weather_info.class);
									  startActivity(intent);
									  finish();
								}
								
								@Override
								public void onFailure(int arg0, String arg1) 
								{   if(arg0==9016)
									  Toast.makeText(loginAct.this,"登录失败，无网络连接，请检查您的手机网络",Toast.LENGTH_SHORT ).show();
								else {
									 Toast.makeText(loginAct.this,"登录失败，"+arg1, Toast.LENGTH_SHORT).show();
								}
								   
								}
							});
					}
					  
					 
				   }else {
					Toast.makeText(loginAct.this,"请输入正确的手机号或者邮箱", Toast.LENGTH_SHORT).show();
				}
				   flag1=true;
				   flag2=false;
				}
				if(action==MotionEvent.ACTION_UP)
					button1.setBackgroundColor(Color.parseColor("#00FF00"));
				return false;
			}
		});
	    button2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) 
			{
				int action=motionEvent.getAction();
				if(action==MotionEvent.ACTION_DOWN)
					{
					 input=editText1.getText().toString();
					 passwordString=editText2.getText().toString();
					 button2.setBackgroundColor(Color.parseColor("#006400"));
					 if (flag1) 
					    button1.setBackgroundColor(0);
					   account.setText("手机号");
					   account.setTextSize(20);
					   editText1.setHint("11位手机号");
					   password.setText("验证码");
					   editText2.setText("");
					   editText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
					   password.setTextSize(20);
					   imageView.setVisibility(View.GONE);
					   button4.setVisibility(View.VISIBLE);
					   if(passwordString.isEmpty()&&flag2)
						   Toast.makeText(loginAct.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
					   else if(flag2&&!passwordString.isEmpty())
					    {
						    BmobUser.loginBySMSCode(loginAct.this,input, passwordString, new LogInListener<BmobUser>()
						   {		

						           @Override
					        	    public void done(BmobUser user, BmobException e) 
						           {
							             if(user!=null&&e==null)
							             { Toast.makeText(loginAct.this,"登录成功",Toast.LENGTH_SHORT).show();
							                 Intent intent=new Intent(loginAct.this,weather_info.class);
										     startActivity(intent);
										     finish();
							             }
							             else {
											  Toast.makeText(loginAct.this,"登录失败，"+e,Toast.LENGTH_SHORT).show();
										}
						            }
						   }
						    		);
					    }
					   flag1=false;
					   flag2=true;
					}
				  
				if(action==MotionEvent.ACTION_UP)
					button2.setBackgroundColor(Color.parseColor("#00FF00"));
				return false;
			}
		});
	    button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				 
				   Intent intent=new Intent(loginAct.this,register.class);
				   startActivity(intent);
				
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
       button4.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View view) 
			{   
				input=editText1.getText().toString();
				passwordString=editText2.getText().toString();
				if(fasong)
		      {	 if(isMobileNO(input))
				{
				  BmobSMS.requestSMSCode(loginAct.this, input,"短信验证",new RequestSMSCodeListener() {
					
					@Override
					public void done(Integer smsId, BmobException ex) {
						if(ex==null)
						{
							Toast.makeText(loginAct.this,"发送成功",Toast.LENGTH_SHORT).show();
							fasong=false;
							timer=new Timer();
						    task=new TimerTask(){
					 		
					 		@Override
					 		public void run() 
					 		{   daojishi--;
					 		    Message message=new Message();
					 			message.what=UPDATE_TEXT;
					 			handler.sendMessage(message);
					 
					 			if(daojishi==0)
					 			{   
					 			    message=new Message();
					 			    message.what=TOFASONG;
					 			    handler.sendMessage(message);
					 			    timer.cancel();
					 			    task.cancel();
					 			    daojishi=30;
					 	
					 			}
					 		}
					 	};
							timer.schedule(task, 1000,1000);	
						}
					}
				   });
				}
				else if(input.isEmpty())
				{
					Toast.makeText(loginAct.this,"请输入您的手机号" ,Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(loginAct.this,"请输入正确的手机号码格式", Toast.LENGTH_SHORT).show();
				}
		       
			}
			}
		});
       imageView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View view) {
		     if(eye){
		    	 editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
		    	 imageView.setImageResource(R.drawable.eyeclose);
		    	 eye=false;
		    	 
		     }else {
				editText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				imageView.setImageResource(R.drawable.eyeopen);
				eye=true;
			}
			
		}
	});
       checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				button1.setEnabled(true);
				button2.setEnabled(true);
			}else{
				button1.setEnabled(false);
				button2.setEnabled(false);
			}
			
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
	public static boolean isMobileNO(String mobiles)
	{  

	   Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");  

	   Matcher m = p.matcher(mobiles);  

		    return m.matches();  

	}
	public boolean isEmail(String email) 
	{

		Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配  

		Matcher m = p.matcher(email);
        return m.matches();

		}
	
	 
  }
