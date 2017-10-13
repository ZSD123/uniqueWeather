package activity;


import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


import com.amap.api.services.a.bu;
import com.sharefriend.app.R;

import Util.MD5Util;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class register extends Activity {
    private ImageButton imageButton; //返回ImageButton
	private Button button;    //注册Button
	private Button button1;   //验证码Button
	
	private ImageView imageView;//眼睛ImageView
	private ImageView imageView1;  //眼睛ImageView1
    
	private boolean eye=false;     //眼睛关上
	private boolean eye1=false;    //眼睛1关上
    private EditText editText1;   //帐号EditText
    private EditText editText2;   //密码EditText
    private EditText editText4;   //确认密码EditText
    
    private EditText editText3;   //验证码EditText
    private String input;        //账户
    private String passwordString;   //密码
    private boolean bangzhufasongE=true;  //邮箱帮助发送
    private boolean bangzhufasongM=true;  //手机帮助发送
    private boolean fasong=true;    
    private final int UPDATE_TEXT=0;
    private final int TOFASONG=1; 
    
    private int daojishi=30;   
    private Timer timer;
    private Handler handler=new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case UPDATE_TEXT:
    			button1.setText(daojishi+"s后再发送");
    			break;
    		case TOFASONG:
    			button1.setText("发送验证码");
    			button1.setEnabled(true);
    			fasong=true;
    		default:
    			break;
    		}
    	}
    };
    private TimerTask task;
    private CheckBox checkBox;    //我同意服务条款
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		button=(Button)findViewById(R.id.zhuce);
		button1=(Button)findViewById(R.id.sendYanzhengma);
		
		imageView=(ImageView)findViewById(R.id.kekanmima);
		imageView1=(ImageView)findViewById(R.id.kekanmima1);
		
		editText1=(EditText)findViewById(R.id.editText_account);
		editText2=(EditText)findViewById(R.id.editText_secret);
		editText3=(EditText)findViewById(R.id.yanzhengma);
		editText4=(EditText)findViewById(R.id.editText_querensecret);
		
		checkBox=(CheckBox)findViewById(R.id.checkbox);
		checkBox.setChecked(true);		
		TextView fuwu=(TextView)findViewById(R.id.fuwu);
		fuwu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(register.this,fuwuAct.class);
				startActivity(intent);
				
				
			}
		});
		
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				finish();
				
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
		 imageView1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
				     if(eye1){
				    	 editText4.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
				    	 imageView1.setImageResource(R.drawable.eyeclose);
				    	 eye1=false;
				    	 
				     }else {
						editText4.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						imageView1.setImageResource(R.drawable.eyeopen);
						eye1=true;
					}
					
				}
			});
		 checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					button.setEnabled(true);
				}else {
					button.setEnabled(false);
				}
				
			}
		});
		 button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				input=editText1.getText().toString();
				passwordString=editText2.getText().toString();
				if(input.isEmpty()||passwordString.isEmpty())
					Toast.makeText(register.this,"帐号或密码不能为空", Toast.LENGTH_SHORT).show();
				if(!editText2.getText().toString().equals(editText4.getText().toString()))
				{		
				   Toast.makeText(register.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
				}else {
					if(isMobileNO(input)||isEmail(input))
					   {
						
						if(isEmail(input)){
							MyUser bu=new MyUser();
							bu.setUsername(input);
							bu.setNick(input);
							bu.setPassword(passwordString);
							bu.setEmail(input);
						    bu.setEmailVerified(false);
						    bu.setCanCall(false);
						    bu.signUp(new SaveListener<MyUser>() {

								@Override
								public void done(MyUser myUser, BmobException e) {
									if(e==null){
										  Toast.makeText(register.this,"注册成功，请在邮箱里验证",Toast.LENGTH_SHORT).show();
										  if(bangzhufasongE){
											  MyUser.requestEmailVerify(input,new UpdateListener(){

												@Override
												public void done(BmobException e) {
													if(e==null){
														Toast.makeText(register.this,"发送验证邮箱成功",Toast.LENGTH_SHORT).show();
														fasong=false;
														button1.setEnabled(false);
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
													}else {
														Toast.makeText(register.this,"发送验证邮箱失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
													}
													
												}
												  
											  }
											   );
											  bangzhufasongE=false;
										  }
									}else if(e.getErrorCode()==202||e.getErrorCode()==203){
										Toast.makeText(register.this,"该用户已存在，请直接登录，"+e.getMessage(), Toast.LENGTH_SHORT).show();
									}else {
										Toast.makeText(register.this,"注册失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
									}
									
								}
							});
						}
						if(isMobileNO(input)){
							MyUser bu=new MyUser();
							bu.setUsername(input);
							bu.setNick(input);
							bu.setCanCall(false);
							bu.setPassword(MD5Util.getMD5String(passwordString));
							bu.setMobilePhoneNumber(input);
							bu.setMobilePhoneNumberVerified(false);
							if(bangzhufasongM){
								BmobSMS.requestSMSCode(input,"短信验证",  new QueryListener<Integer>() {

									@Override
									public void done(Integer arg0,BmobException e) {
										if(e==null){
											 Toast.makeText(register.this,"发送成功，请验证", Toast.LENGTH_SHORT).show();
											 fasong=false;
											 button1.setEnabled(false);
											 timer=new Timer();
											 task=new TimerTask() {
												
												@Override
												public void run() {
													daojishi--;
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
										}else if(e.getErrorCode()==10010){
											Toast.makeText(register.this,"该手机号发送短信达到限制，"+e.getMessage() ,Toast.LENGTH_SHORT).show();
										}else if(e.getErrorCode()==10011){
											Toast.makeText(register.this,"该账户无可用的发送短信条数，"+e.getMessage() ,Toast.LENGTH_SHORT).show();
										}else if(e.getErrorCode()==10012){
											Toast.makeText(register.this,"身份信息必须审核通过才能使用该功能，"+e.getMessage() ,Toast.LENGTH_SHORT).show();
										}else {
											Toast.makeText(register.this, "发送验证短信失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
										}
										
									}
									
								});
								bangzhufasongM=false;
							}
							if(!editText3.getText().toString().equals("")){
								
								bu.signOrLogin(editText3.getText().toString(),new SaveListener<MyUser>() {
									 @Override
									    public void done(MyUser user, BmobException e) {
									        if(user!=null){
									        	loginAct.installationId=MyBmobInstallation.getInstallationId(register.this);
									        	Toast.makeText(register.this,"注册成功，正在转入", Toast.LENGTH_SHORT).show();
												Intent intent=new Intent(register.this,weather_info.class);
												startActivity(intent);
												loginAct.application.delete();
												finish();
									        }else if(e!=null){
									        	if(e.getErrorCode()==207)
									        		Toast.makeText(register.this,"注册失败，验证码错误"+e.getMessage(), Toast.LENGTH_SHORT).show();
									        	else 
									        	    Toast.makeText(register.this,"注册失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
									        }
									    }
								});
							}else{
								Toast.makeText(register.this,"请输入验证码", Toast.LENGTH_SHORT).show();
							}
						}
				      	}else{
				      		Toast.makeText(register.this,"输入的手机号或者邮箱格式不对", Toast.LENGTH_SHORT).show();
				      	}
				}
			}
		});
		   button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(fasong){
					input=editText1.getText().toString();
					if(isMobileNO(input)){
						BmobSMS.requestSMSCode(input,"短信验证",  new QueryListener<Integer>() {

							@Override
							public void done(Integer arg0,BmobException e) {
								if(e==null){
									 Toast.makeText(register.this,"发送成功，请验证", Toast.LENGTH_SHORT).show();
									 fasong=false;
									 button1.setEnabled(false);
									 timer=new Timer();
									 task=new TimerTask() {
										
										@Override
										public void run() {
											daojishi--;
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
								}else if(e.getErrorCode()==10010){
									Toast.makeText(register.this,"该手机号发送短信达到限制，"+e.getMessage() ,Toast.LENGTH_SHORT).show();
								}else if(e.getErrorCode()==10011){
									Toast.makeText(register.this,"该账户无可用的发送短信条数，"+e.getMessage() ,Toast.LENGTH_SHORT).show();
								}else if(e.getErrorCode()==10012){
									Toast.makeText(register.this,"身份信息必须审核通过才能使用该功能，"+e.getMessage() ,Toast.LENGTH_SHORT).show();
								}else {
									Toast.makeText(register.this, "发送验证短信失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
								}
								
								
								
							}
							
						});
					}else if(isEmail(input)){
						 MyUser.requestEmailVerify(input,new UpdateListener(){

								@Override
								public void done(BmobException e) {
									if(e==null){
										Toast.makeText(register.this,"发送验证邮箱成功",Toast.LENGTH_SHORT).show();
										fasong=false;
										button1.setEnabled(false);
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
									}else if(e.getErrorCode()==205){
										Toast.makeText(register.this,"失败，没有找到此邮件的用户，请先点击下面的注册按钮", Toast.LENGTH_SHORT).show();
									}else {
										Toast.makeText(register.this,"失败，"+e.getMessage(),Toast.LENGTH_SHORT).show();
									}
									
								}
								  
							  }
							   );
					}else {
						Toast.makeText(register.this,"手机号或者邮箱格式不对", Toast.LENGTH_SHORT).show();
					}
						
					
				}
				
			}
		});
		 
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
