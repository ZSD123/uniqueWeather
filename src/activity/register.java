package activity;


import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.a.a.in;
import com.amap.api.services.a.bu;
import com.uniqueweather.app.R;

import Util.MD5Util;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
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
import android.widget.Toast;

public class register extends Activity {
    private ImageButton imageButton; //����ImageButton
	private Button button;    //ע��Button
	private Button button1;   //��֤��Button
	
	private ImageView imageView;//�۾�ImageView
	private ImageView imageView1;  //�۾�ImageView1
    
	private boolean eye=false;     //�۾�����
	private boolean eye1=false;    //�۾�1����
    private EditText editText1;   //�ʺ�EditText
    private EditText editText2;   //����EditText
    private EditText editText4;   //ȷ������EditText
    
    private EditText editText3;   //��֤��EditText
    private String input;        //�˻�
    private String passwordString;   //����
    private boolean bangzhufasongE=true;  //�����������
    private boolean bangzhufasongM=true;  //�ֻ���������
    private boolean fasong=true;    
    private static final int UPDATE_TEXT=0;
    private static final int TOFASONG=1; 
    
    private int daojishi=30;   
    private Message message;
    private Timer timer;
    private Handler handler=new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case UPDATE_TEXT:
    			button1.setText(daojishi+"s���ٷ���");
    			break;
    		case TOFASONG:
    			button1.setText("������֤��");
    			fasong=true;
    		default:
    			break;
    		}
    	}
    };
    private TimerTask task;
    private CheckBox checkBox;    //��ͬ���������
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
					Toast.makeText(register.this,"�ʺŻ����벻��Ϊ��", Toast.LENGTH_SHORT).show();
				if(!editText2.getText().toString().equals(editText4.getText().toString()))
				{		
				   Toast.makeText(register.this,"�������벻һ��",Toast.LENGTH_SHORT).show();
				}else {
					if(isMobileNO(input)||isEmail(input))
					   {
						
						if(isEmail(input)){
							MyUser bu=new MyUser();
							bu.setUsername(input);
							bu.setPassword(MD5Util.getMD5String(passwordString));
							bu.setEmail(input);
						    bu.setEmailVerified(false);
						    bu.signUp(register.this, new SaveListener() {
								
								@Override
								public void onSuccess() {
									  Toast.makeText(register.this,"ע��ɹ���������������֤",Toast.LENGTH_SHORT).show();
									  if(bangzhufasongE){
										  MyUser.requestEmailVerify(register.this,input,new EmailVerifyListener() {
											
											@Override
											public void onSuccess() {
												Toast.makeText(register.this,"������֤����ɹ�",Toast.LENGTH_SHORT).show();
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
											
											@Override
											public void onFailure(int arg0, String arg1) {
												Toast.makeText(register.this,"������֤����ʧ�ܣ�"+arg1, Toast.LENGTH_SHORT).show();
												
											}
										});
									  }
								}
								
								@Override
								public void onFailure(int arg0, String arg1) {
									Toast.makeText(register.this,"ע��ʧ�ܣ�"+arg1, Toast.LENGTH_SHORT).show();
									
								}
							});
						}
						if(isMobileNO(input)){
							MyUser bu=new MyUser();
							bu.setUsername(input);
							bu.setPassword(MD5Util.getMD5String(passwordString));
							bu.setMobilePhoneNumber(input);
							bu.setMobilePhoneNumberVerified(false);
							if(bangzhufasongM){
								BmobSMS.requestSMSCode(register.this,input,"������֤",new RequestSMSCodeListener() {
									
									@Override
									public void done(Integer smsId, BmobException ex) {
										if(ex==null){
											 Toast.makeText(register.this,"������֤���ųɹ���"+smsId,Toast.LENGTH_SHORT).show();
											 fasong=false;
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
										}else {
											Toast.makeText(register.this, "������֤����ʧ�ܣ�"+ex.getMessage(), Toast.LENGTH_SHORT).show();
										}
										
									}
								});
								bangzhufasongM=false;
							}
							if(!editText3.getText().toString().equals("")){
								bu.signOrLogin(register.this,editText3.getText().toString(),new SaveListener() {
									
									@Override
									public void onSuccess() {
										Toast.makeText(register.this,"ע��ɹ�������ת��", Toast.LENGTH_SHORT).show();
										Intent intent=new Intent(register.this,weather_info.class);
										startActivity(intent);
										finish();
									}
									
									@Override
									public void onFailure(int arg0, String arg1) {
										Toast.makeText(register.this,"ע��ʧ�ܣ�"+arg1, Toast.LENGTH_SHORT).show();
										
									}
								});
							}else{
								Toast.makeText(register.this,"��������֤��", Toast.LENGTH_SHORT).show();
							}
						}
				      	}else{
				      		Toast.makeText(register.this,"������ֻ��Ż��������ʽ����", Toast.LENGTH_SHORT).show();
				      	}
				}
			}
		});
		   button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(fasong){
					if(isMobileNO(input)){
				   	BmobSMS.requestSMSCode(register.this,input,"������֤",new RequestSMSCodeListener() {
						
						@Override
						public void done(Integer smsId, BmobException ex) {
							if(ex==null){
								 Toast.makeText(register.this,"������֤���ųɹ���"+smsId,Toast.LENGTH_SHORT).show();
								 fasong=false;
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
							}else {
								Toast.makeText(register.this, "������֤����ʧ�ܣ�"+ex.getMessage(), Toast.LENGTH_SHORT).show();
							}
							
						}
					});
					}else if(isEmail(input)){
						MyUser.requestEmailVerify(register.this, input,new EmailVerifyListener() {
							
							@Override
							public void onSuccess() {
								Toast.makeText(register.this,"������֤�ʼ��ɹ�", Toast.LENGTH_SHORT).show();
								 fasong=false;
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
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(register.this,"ʧ�ܣ�"+arg1,Toast.LENGTH_SHORT).show();
								
							}
						});
					}else {
						Toast.makeText(register.this,"�ֻ��Ż��������ʽ����", Toast.LENGTH_SHORT).show();
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

		Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//����ƥ��  

		Matcher m = p.matcher(email);
        return m.matches();

		}
	
	 


}