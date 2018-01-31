package activity;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import com.amap.api.mapcore2d.u;
import com.sharefriend.app.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class mobileVerifyAct extends baseActivity {
    
	private String mobile;    //手机号
	private EditText editText1;   //手机号编辑框
	private EditText editText2;
	
	
	private Button button1;       //发送按钮
	private Button button2;        //验证按钮
	
    private boolean fasong=true;    //注册的时候自动发送标志,true表示可以发送
    private Timer timer=new Timer();
    
    private final int UPDATE_TEXT=0;
    private final int TOFASONG=1;
    
    private int daojishi=30; 
    
    private TimerTask task;
    
    private ImageButton imageButton;
    
    private AlertDialog dialog1;
    
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mobileverify);
		editText1=(EditText)findViewById(R.id.editText_account);
		editText2=(EditText)findViewById(R.id.editText_secret);
		
		button1=(Button)findViewById(R.id.fasong);
		button2=(Button)findViewById(R.id.verify);
		
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
        button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mobile=editText1.getText().toString();
				if(fasong)
		      {	 if(loginAct.isMobileNO(mobile))
				{
				  BmobSMS.requestSMSCode( mobile,"短信验证",new QueryListener<Integer>() {
					
					@Override
					public void done(Integer smsId, BmobException ex) {
						if(ex==null)
						{
							Toast.makeText(mobileVerifyAct.this,"发送成功",Toast.LENGTH_SHORT).show();
							button1.setEnabled(false);
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
						}else if(ex.getErrorCode()==10010){
							Toast.makeText(mobileVerifyAct.this,"该手机号发送短信达到限制，"+ex.getMessage() ,Toast.LENGTH_SHORT).show();
						}else if(ex.getErrorCode()==10011){
							Toast.makeText(mobileVerifyAct.this,"该账户无可用的发送短信条数，"+ex.getMessage() ,Toast.LENGTH_SHORT).show();
						}else if(ex.getErrorCode()==10012){
							Toast.makeText(mobileVerifyAct.this,"身份信息必须审核通过才能使用该功能，"+ex.getMessage() ,Toast.LENGTH_SHORT).show();
						}
						
					}
				   });
				}
				else if(mobile.isEmpty())
				{
					Toast.makeText(mobileVerifyAct.this,"请输入您的手机号" ,Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(mobileVerifyAct.this,"请输入正确的手机号码格式", Toast.LENGTH_SHORT).show();
				}
		       
			}
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 String smsCode=editText2.getText().toString();
				 
				 if(!smsCode.equals("")){
				 
				 BmobSMS.verifySmsCode(mobile, smsCode,new UpdateListener() {
					
					@Override
					public void done(BmobException e) {
					    if(e==null){//短信验证码已验证成功
					    	Toast.makeText(mobileVerifyAct.this,"验证通过", Toast.LENGTH_SHORT).show();
					    	
					    	MyUser user=new MyUser();
					    	user.setMobilePhoneNumber(mobile);
					    	user.setMobilePhoneNumberVerified(true);
					    	MyUser cur=MyUser.getCurrentUser(MyUser.class);
					    	user.update(cur.getObjectId(),new UpdateListener() {
								
								@Override
								public void done(BmobException e) {
									if(e==null){
										
										  finish();
										  
									}else if(e.getErrorCode()==209){
										   AlertDialog.Builder builder=new AlertDialog.Builder(mobileVerifyAct.this);
											
											builder.setMessage("系统检测到您当前手机号已经注册过，请用该手机号登录使用共享功能");
											builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialoginterface, int which) {
													finish();
												}
											});
											
											builder.setCancelable(false);
									        dialog1=builder.create();
									        if(!dialog1.isShowing())
										    	dialog1.show();
										   
										   
										 
									}else {
										 Toast.makeText(mobileVerifyAct.this,"失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
									}
									
								}
							});
					    	
				        }else{
				            Toast.makeText(mobileVerifyAct.this,"验证失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
				        }
						
					}
				});
				
			   }else {
				 Toast.makeText(mobileVerifyAct.this,"验证码不能为空", Toast.LENGTH_SHORT).show();
			    }
			}
		});
		
	}

   
}
