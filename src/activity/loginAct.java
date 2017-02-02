package activity;



import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.c.i;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.a.a.in;
import com.amap.api.mapcore2d.p;
import com.amap.api.services.a.br;
import com.squareup.okhttp.OkHttpClient;
import com.uniqueweather.app.R;



import Util.MD5Util;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
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
    private TextView account;       //帐号
    private TextView password;       //密码
    
    private EditText editText1;     //第一行
    private EditText editText2;     //第二行
    private Button button4;    //  发送验证码
    
    private boolean onceE=true;     //邮箱验证一次
    private boolean onceM=true;      //手机短信验证一次，verify一次
    private boolean bangzhufasongM=true;   //手机号帮助发送一次
    private boolean bangzhufasongE=true;   //邮箱验证帮助发送一次
    private String input;      //输入数据
    private String passwordString;   //密码

    private boolean fasong=true;    //注册的时候自动发送标志,true表示可以发送
    private ImageView imageView;
    private boolean eye=false;     //眼睛关上
    private static final int UPDATE_TEXT=0;
    private static final int TOFASONG=1;
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
	    customFont=(CustomFont)findViewById(R.id.text_EndRain);
	    caihong=(myCaihong)findViewById(R.id.caihong);
	    text1=(TextView)findViewById(R.id.wangjimima);
	    text2=(TextView)findViewById(R.id.xiantiyan);
	    text1.setText(Html.fromHtml("<u>"+"忘记密码"+"</u>"));
	    text2.setText(Html.fromHtml("<u>"+"先体验"+"</u>"));
	    account=(TextView)findViewById(R.id.account);
	    password=(TextView)findViewById(R.id.password);
	    
	    imageView=(ImageView)findViewById(R.id.kekanmima);
	    editText1=(EditText)findViewById(R.id.editText_account);
	    editText2=(EditText)findViewById(R.id.editText_secret);
	    button4=(Button)findViewById(R.id.sendYanzhengma);
	    button2.setBackgroundColor(0);
	    button3.setBackgroundColor(0);
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
				  if(flag3)
					 button3.setBackgroundColor(0);
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
				   if(isMobileNO(input)||isEmail(input))
				   {
					   bu.setUsername(input);
					   bu.setPassword(MD5Util.getMD5String(passwordString));
					   bu.login(loginAct.this,new SaveListener() 
					   {@Override
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
				   }
				   flag1=true;
				   flag2=false;
				   flag3=false;
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
					 if(flag3)
						 button3.setBackgroundColor(0);
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
					   flag3=false;
					}
				  
				if(action==MotionEvent.ACTION_UP)
					button2.setBackgroundColor(Color.parseColor("#00FF00"));
				return false;
			}
		});
	    button3.setOnTouchListener(new OnTouchListener() {
	    
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) 
			{
                
				int action=motionEvent.getAction();
				if(action==MotionEvent.ACTION_DOWN)
					{
					 input=editText1.getText().toString();
					 passwordString=editText2.getText().toString();
					 button3.setBackgroundColor(Color.parseColor("#006400"));
					 if (flag1) 
					    button1.setBackgroundColor(0);
					 if(flag2)
						 button2.setBackgroundColor(0);
					   account.setText("帐号");
					   account.setTextSize(20);
					   editText1.setHint("手机号或者邮箱");
					   if(!flag3)
					   { password.setText("密码");
					     editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
					   }
					   password.setTextSize(20);
					   imageView.setVisibility(View.VISIBLE);
					   button4.setVisibility(View.GONE);
					   if((input.isEmpty()||passwordString.isEmpty())&&flag3)
							  Toast.makeText(loginAct.this, "帐号或密码不能为空", Toast.LENGTH_SHORT).show();
					   if(isMobileNO(input)||isEmail(input))
					   {     button4.setVisibility(View.VISIBLE);
					         imageView.setVisibility(View.GONE);
						     bu.setUsername(input);
						     bu.setPassword(MD5Util.getMD5String(passwordString));
						     if(isEmail(input))
						     { bu.setEmail(input);	      
						       if(onceE)
						       {   bu.setEmailVerified(false);
						    	   onceE=false;
						       }
						       bu.signUp(loginAct.this,new SaveListener()
						    		   {@Override
										public void onFailure(int arg0,
												String arg1) {
											Toast.makeText(loginAct.this,"注册失败，"+arg1, Toast.LENGTH_SHORT).show();
										}

										@Override
										public void onSuccess() {
										     Toast.makeText(loginAct.this,"注册成功，请验证",Toast.LENGTH_SHORT).show();
										     if(bangzhufasongE)
									    	 {    BmobUser.requestEmailVerify(loginAct.this,input,new EmailVerifyListener(){

												@Override
												public void onFailure(int arg0, String arg1) {
													Toast.makeText(loginAct.this,"发送验证邮箱失败，"+arg1,Toast.LENGTH_SHORT).show();
													
												}

												@Override
												public void onSuccess() {
													Toast.makeText(loginAct.this,"发送验证邮箱成功",Toast.LENGTH_SHORT).show();
									    		    fasong=false;
									    		    editText2.setText("");
									    			timer.schedule(task, 1000);
												} 
												});
									    	      bangzhufasongE=false;
									    	 }
									       
										}   
						    		   
						    		   }
						    		   );
						      
						     }
						     if(isMobileNO(input))
						     {    bu.setMobilePhoneNumber(input);
						    	 if(onceM)
						          { bu.setMobilePhoneNumberVerified(false);
						            onceM=false;
						          }
						    	 if(bangzhufasongM)
						    	 {   editText2.setText("");
						    	     passwordString=editText2.getText().toString();
						    		 BmobSMS.requestSMSCode(loginAct.this,input,"短信验证",new RequestSMSCodeListener()
						    	   {
						    		 @Override
						    		 public void done(Integer smsId,BmobException ex)
						    		 {    password.setText("验证码");
					    			      editText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
						    			 if(ex==null)
						    			 { Toast.makeText(loginAct.this,"发送验证短信成功，"+smsId,Toast.LENGTH_SHORT).show();
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
						    			 else {
											Toast.makeText(loginAct.this,"发送验证短信失败，"+ex.getMessage(), Toast.LENGTH_SHORT).show();
											Log.d("Main",ex.getMessage());
										}
						    		 }
						    	  }
						    			 );
						    	      bangzhufasongM=false;
						    	 }
						    	 if(!passwordString.equals(""))
						    	 {  Log.d("Main","代码是"+passwordString);
						    		bu.signOrLogin(loginAct.this,passwordString, new SaveListener()
						    		{   @Override
										public void onFailure(int arg0,
												String arg1) {
											Toast.makeText(loginAct.this,"注册失败，"+arg1,Toast.LENGTH_SHORT).show();
											
										}

										@Override
										public void onSuccess() {
											Toast.makeText(loginAct.this,"注册成功,正在转入", Toast.LENGTH_SHORT).show();
											  bu.login(loginAct.this,new SaveListener() 
											   {@Override
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
										}
						    		}	
						    		);
						    	 }else {
									Toast.makeText(loginAct.this,"验证码不能为空", Toast.LENGTH_SHORT).show();
								}
						    	  
						     }
						     if(isEmail(passwordString))  
						        if(fasong)
						    	 BmobUser.requestEmailVerify(loginAct.this,input,new EmailVerifyListener() {
									
									@Override
									public void onSuccess() 
									{   
										Toast.makeText(loginAct.this, "验证邮件发送成功",Toast.LENGTH_SHORT).show();
										fasong=false;
										timer=new Timer();
									    task=new TimerTask(){
								 		
								 		@Override
								 		public void run() 
								 		{   daojishi--;
								 		    Message message=new Message();
								 			message.what=UPDATE_TEXT;
								 			handler.sendMessage(message);
								 			Log.d("Main","数字为"+daojishi);
								 			if(daojishi==0)
								 			{   
								 			    message=new Message();
								 			    message.what=TOFASONG;
								 			    handler.sendMessage(message);
								 			    timer.cancel();
								 			    task.cancel();
								 			    daojishi=30;
								 			    Log.d("Main", "2");
								 			}
								 		}
								 	};
										timer.schedule(task, 1000,1000);	
									}
									
									@Override
									public void onFailure(int arg0, String arg1) 
									{
										Toast.makeText(loginAct.this,"验证邮件发送失败",Toast.LENGTH_SHORT).show();
									}
								});
						     
					   }
					   else {
						Toast.makeText(loginAct.this,"请输入正确的手机号或邮箱", Toast.LENGTH_SHORT);
					}
					   flag1=false;
					   flag2=false;
					   flag3=true;
					}
				if(action==MotionEvent.ACTION_UP)
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
					 			Log.d("Main","数字为"+daojishi);
					 			if(daojishi==0)
					 			{   
					 			    message=new Message();
					 			    message.what=TOFASONG;
					 			    handler.sendMessage(message);
					 			    timer.cancel();
					 			    task.cancel();
					 			    daojishi=30;
					 			    Log.d("Main", "2");
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
		        if(isEmail(input)){
		        	BmobUser.requestEmailVerify(loginAct.this,input,new EmailVerifyListener() {
						
						@Override
						public void onSuccess() 
						{
							Toast.makeText(loginAct.this, "验证邮件发送成功",Toast.LENGTH_SHORT).show();
							fasong=false;
							timer=new Timer();
						    task=new TimerTask(){
					 		
					 		@Override
					 		public void run() 
					 		{   daojishi--;
					 		    Message message=new Message();
					 			message.what=UPDATE_TEXT;
					 			handler.sendMessage(message);
					 			Log.d("Main","数字为"+daojishi);
					 			if(daojishi==0)
					 			{   
					 			    message=new Message();
					 			    message.what=TOFASONG;
					 			    handler.sendMessage(message);
					 			    timer.cancel();
					 			    task.cancel();
					 			    daojishi=30;
					 			    Log.d("Main", "2");
					 			}
					 		}
					 	};
							timer.schedule(task, 1000,1000);	
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							
							Toast.makeText(loginAct.this,"验证邮件发送失败",Toast.LENGTH_SHORT).show();
						}
				    	});
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
