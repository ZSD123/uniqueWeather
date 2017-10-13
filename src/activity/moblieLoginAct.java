package activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import com.sharefriend.app.R;

import Util.MD5Util;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class moblieLoginAct extends Activity {
    private int daojishi=30;   
    private final int UPDATE_TEXT=0;
    private final int TOFASONG=1;
    private RelativeLayout relativeRoot;
    private ProgressBar progressBar;
    private boolean fasong=true;    //ע���ʱ���Զ����ͱ�־,true��ʾ���Է���
    private Timer timer=new Timer();
    private TextView textView;
    
    private RelativeLayout relativeLayout;
    
    private Handler handler=new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case UPDATE_TEXT:
    			button1.setText(daojishi+"s���ٷ���");
    			break;
    		case TOFASONG:
    			button1.setText("������֤��");
    			button1.setEnabled(true);
    			fasong=true;
    		default:
    			break;
    		}
    	}
    };
    private TimerTask task;
    private ImageButton imageButton;
	private Button button1;   //������֤��
	
	private TextView fuwu;
	
    private CheckBox checkBox;       //��ͬ���������checkbox
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.moblielogin);
		final EditText editText1=(EditText)findViewById(R.id.editText_account);
		final EditText editText2=(EditText)findViewById(R.id.editText_secret);
        button1=(Button)findViewById(R.id.sendYanzhengma);
		final Button button2=(Button)findViewById(R.id.yanzhenglogin);
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		relativeRoot=(RelativeLayout)findViewById(R.id.relativeRoot);
		progressBar=(ProgressBar)findViewById(R.id.progressBar);
		
	   checkBox=(CheckBox)findViewById(R.id.checkbox);
	   checkBox.setChecked(true);
		
		
		fuwu=(TextView)findViewById(R.id.fuwu);
	    progressBar.setVisibility(View.GONE);
	    relativeLayout=(RelativeLayout)findViewById(R.id.relativelayout1);
	    relativeLayout.setVisibility(View.GONE);
	    textView=(TextView)findViewById(R.id.textDeng);
	    textView.setVisibility(View.GONE);
	  
	    
	    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
				     button2.setEnabled(true);
				}else{
					button2.setEnabled(false);
				}
				
			}
		   });
	    
       fuwu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(moblieLoginAct.this,fuwuAct.class);
				startActivity(intent);
				
			}
		});
	    
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String input=editText1.getText().toString();
				if(fasong)
		      {	 if(loginAct.isMobileNO(input))
				{
				  BmobSMS.requestSMSCode( input,"������֤",new QueryListener<Integer>() {
					
					@Override
					public void done(Integer smsId, BmobException ex) {
						if(ex==null)
						{
							Toast.makeText(moblieLoginAct.this,"���ͳɹ�",Toast.LENGTH_SHORT).show();
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
							Toast.makeText(moblieLoginAct.this,"���ֻ��ŷ��Ͷ��Ŵﵽ���ƣ�"+ex.getMessage() ,Toast.LENGTH_SHORT).show();
						}else if(ex.getErrorCode()==10011){
							Toast.makeText(moblieLoginAct.this,"���˻��޿��õķ��Ͷ���������"+ex.getMessage() ,Toast.LENGTH_SHORT).show();
						}else if(ex.getErrorCode()==10012){
							Toast.makeText(moblieLoginAct.this,"�����Ϣ�������ͨ������ʹ�øù��ܣ�"+ex.getMessage() ,Toast.LENGTH_SHORT).show();
						}
						
					}
				   });
				}
				else if(input.isEmpty())
				{
					Toast.makeText(moblieLoginAct.this,"�����������ֻ���" ,Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(moblieLoginAct.this,"��������ȷ���ֻ������ʽ", Toast.LENGTH_SHORT).show();
				}
		       
			}
			}
		});
		
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
					final String  input=editText1.getText().toString();
					final String passwordString=editText2.getText().toString();
					   if(passwordString.isEmpty())
						   Toast.makeText(moblieLoginAct.this, "��֤�벻��Ϊ��", Toast.LENGTH_SHORT).show();
					   else if(!passwordString.isEmpty())
					    {   
						   BmobQuery<MyUser> query=new BmobQuery<MyUser>();
						   query.addWhereEqualTo("username",input);
						   query.findObjects(new FindListener<MyUser>() {

							@Override
							public void done(List<MyUser> object,
									BmobException e) {
								if(e==null){
									if(object.size()>0){
										  BmobUser.signOrLoginByMobilePhone(input, passwordString, new LogInListener<BmobUser>()
												   {		
						                                  
												           @Override
											        	    public void done(BmobUser user, BmobException e) 
												           {
													             if(user!=null&&e==null)
													             {   loginAct.installationId=MyBmobInstallation.getInstallationId(moblieLoginAct.this);
													            	 Toast.makeText(moblieLoginAct.this,"��¼�ɹ�",Toast.LENGTH_SHORT).show();
													                 Intent intent=new Intent(moblieLoginAct.this,weather_info.class);
													                 intent.putExtra("login", 1);
																     startActivity(intent);
																     loginAct.application.delete();
																     finish();
													             }
													             else {
													            	    if(e.getErrorCode()==207)
																	  Toast.makeText(moblieLoginAct.this,"��¼ʧ�ܣ���֤�����"+e.getMessage(),Toast.LENGTH_SHORT).show();
																	  if(relativeRoot.getAlpha()==0.3f){
																			progressBar.setVisibility(View.GONE);
																			relativeRoot.setAlpha(1);
																			relativeLayout.setVisibility(View.GONE);
																			textView.setVisibility(View.GONE);
																		}
																}
												            }
												   });
									}else {
										MyUser user=new MyUser();
										user.setMobilePhoneNumber(input);
										user.setUsername(input);
										user.setNick(input);
										user.setPassword(MD5Util.getMD5String(input));
										user.setMobilePhoneNumber(input);
										user.setMobilePhoneNumberVerified(false);
										user.setCanCall(false);
										loginAct.installationId=MyBmobInstallation.getInstallationId(moblieLoginAct.this);
										user.signOrLogin(passwordString,new SaveListener<MyUser>() {

											@Override
											public void done(MyUser user,
													BmobException e) {
											if(e==null){
												  loginAct.installationId=MyBmobInstallation.getInstallationId(moblieLoginAct.this);
												  Toast.makeText(moblieLoginAct.this,"ע��ɹ�����ʼ����Ϊ�����ֻ��ţ����μ�",Toast.LENGTH_LONG).show();
												  Intent intent=new Intent(moblieLoginAct.this,weather_info.class);
												  startActivity(intent);
												  loginAct.application.delete();
												  finish();
											}else{
												Toast.makeText(moblieLoginAct.this,"ע��ʧ�ܣ�"+e.getMessage(),Toast.LENGTH_LONG).show();
												 if(relativeRoot.getAlpha()==0.3f){
														progressBar.setVisibility(View.GONE);
														relativeRoot.setAlpha(1);
														relativeLayout.setVisibility(View.GONE);
														textView.setVisibility(View.GONE);
													}
											}
												
											}
										} );
									}
								
								
								}else{
									Toast.makeText(moblieLoginAct.this,"�������",Toast.LENGTH_SHORT).show();
									if(relativeRoot.getAlpha()==0.3f){
										progressBar.setVisibility(View.GONE);
										relativeRoot.setAlpha(1);
										relativeLayout.setVisibility(View.GONE);
										textView.setVisibility(View.GONE);
									}
								}
							}
						   });
						    progressBar.setVisibility(View.VISIBLE);
							relativeLayout.setVisibility(View.VISIBLE);
							relativeLayout.setOnClickListener(null);
							relativeRoot.setAlpha(0.3f);
							textView.setVisibility(View.VISIBLE);
					    }

			
				
			}
		});
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	@Override
	public void onBackPressed() {
		
		
		
		
		if(relativeRoot.getAlpha()==0.3f){
			progressBar.setVisibility(View.GONE);
			relativeRoot.setAlpha(1);
			relativeLayout.setVisibility(View.GONE);
			textView.setVisibility(View.GONE);
		}
		else {
			super.onBackPressed();
		}
	}
	 
      
}
