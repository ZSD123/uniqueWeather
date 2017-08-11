package activity;

//һ��Ҫ�����󾫣�Ŭ��������ã�Ϊ��ᴴ���ֵ��

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import message.myMessageHandler;
import model.BmobIMApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.command.e;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;


import com.amap.api.services.a.bu;
import com.uniqueweather.app.R;

import Util.MD5Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.renderscript.Type;
import android.text.Html;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
public class loginAct extends Activity{

    private CustomFontTextView customFont;
    private myCaihong caihong;
    private int []location=new int[2];
    
    private int widthPixels;
    private int heightPixels;
    
    
    private Button button1;  //��¼��ť
    private Button button2;  //�ֻ���֤��¼
    private Button button3;  //����ע��
    
    private TextView text1;  //������
    private TextView account;       //�ʺ�
    private TextView password;       //����
    
    private EditText editText1;     //��һ��
    private EditText editText2;     //�ڶ���
    
    private String input;      //��������
    private String passwordString;   //����

    private ImageView imageView;
    private boolean eye=false;     //�۾�����

    
    private CheckBox checkBox;       //��ͬ���������checkbox
    
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private RelativeLayout relativeRoot;
    private TextView textView;

    public static String installationId;
    
    public static BmobIMApplication application;

    @Override
	public void onCreate(Bundle savedInstanceState)
     {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		application=BmobIMApplication.INSTANCE();
		
		TextView fuwu=(TextView)findViewById(R.id.fuwu);
	 //   BmobIM.init(this);
	  
	    
		final MyUser bu=new MyUser();
	    
        final MyUser userInfo=BmobUser.getCurrentUser(MyUser.class);
		
		if(userInfo!=null)
		{   
			installationId=MyBmobInstallation.getInstallationId(loginAct.this);
			Intent intent=new Intent(loginAct.this,weather_info.class);
			intent.putExtra("login", 0);
			startActivity(intent);
			finish();
		}
		
		button1=(Button)findViewById(R.id.login);
		button2=(Button)findViewById(R.id.shoujidenglu);
		button3=(Button)findViewById(R.id.register);
		button3.setBackgroundColor(0);
	    customFont=(CustomFontTextView)findViewById(R.id.text_EndRain);
	    caihong=(myCaihong)findViewById(R.id.caihong);
	    text1=(TextView)findViewById(R.id.wangjimima);
	    text1.setText(Html.fromHtml("<u>"+"��������"+"</u>"));
	    account=(TextView)findViewById(R.id.account);
	    password=(TextView)findViewById(R.id.password);
	    
	    checkBox=(CheckBox)findViewById(R.id.checkbox);
	    checkBox.setChecked(true);
	    
	   
	    imageView=(ImageView)findViewById(R.id.kekanmima);
	    editText1=(EditText)findViewById(R.id.editText_account);
	    editText2=(EditText)findViewById(R.id.editText_secret);
	    
	    editText1.setInputType(InputType.TYPE_CLASS_TEXT);
	    
	    progressBar=(ProgressBar)findViewById(R.id.progressBar);
	    progressBar.setVisibility(View.GONE);
	    relativeLayout=(RelativeLayout)findViewById(R.id.relativelayout1);
	    relativeLayout.setVisibility(View.GONE);
	    relativeRoot=(RelativeLayout)findViewById(R.id.relativeRoot);
	    textView=(TextView)findViewById(R.id.textDeng);
	    textView.setVisibility(View.GONE);
	    
	    button2.setBackgroundColor(0);
	    
	    fuwu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(loginAct.this,fuwuAct.class);
				startActivity(intent);
			}
		});
	    
	    text1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
					Intent intent=new Intent(loginAct.this,resetPassword.class);
					startActivity(intent);
			
			}
		});
	    
	      button1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				int action=motionEvent.getAction();
				if(action==MotionEvent.ACTION_DOWN)
				{  
					input=editText1.getText().toString();
					passwordString=editText2.getText().toString();
					button1.setBackgroundColor(Color.parseColor("#006400"));
				   account.setText("�ʺ�");
				   account.setTextSize(20);
				   editText1.setHint("�ֻ��Ż�������");
				   password.setText("����");
				   editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
				   password.setTextSize(20);
				   imageView.setVisibility(View.VISIBLE);
				   if((input.isEmpty()||passwordString.isEmpty()))
					  Toast.makeText(loginAct.this, "�ʺŻ����벻��Ϊ��", Toast.LENGTH_SHORT).show();
				   else if((isMobileNO(input)||isEmail(input)))
				   {   
					   progressBar.setVisibility(View.VISIBLE);
					   relativeLayout.setVisibility(View.VISIBLE);
					   relativeLayout.setOnClickListener(null);
					   relativeRoot.setAlpha(0.3f);
					   textView.setVisibility(View.VISIBLE);
					   
					   bu.setUsername(input);			  
					   
					   if(isEmail(input)){
				       bu.setPassword(passwordString);
					   BmobQuery<MyUser> query=new BmobQuery<MyUser>("_User");
					   query.addWhereEqualTo("username",input);
				       query.findObjects(new FindListener<MyUser>() {
						
						@Override
						public void done(List<MyUser> object, BmobException e) {
						     if(e==null){
						    	if(object.size()==0){
						    		Toast.makeText(loginAct.this, "�Բ�������ǰ�˻���δע�ᣬ����ȷ��һ��", Toast.LENGTH_SHORT).show();
						    		 if(relativeRoot.getAlpha()==0.3f){
											progressBar.setVisibility(View.GONE);
											relativeRoot.setAlpha(1);
											relativeLayout.setVisibility(View.GONE);
											textView.setVisibility(View.GONE);
										}
						    	}else{
						    	 for(MyUser myUser : object){
						    		 if(myUser.getEmailVerified()){
						    			 bu.login(new SaveListener<MyUser>(){
                                           @Override
											public void done(MyUser user,BmobException e) {
												
                                        	   if(e==null){
													  Toast.makeText(loginAct.this, "��¼�ɹ�", Toast.LENGTH_SHORT).show();
													  installationId=MyBmobInstallation.getInstallationId(loginAct.this);
													  Intent intent=new Intent(loginAct.this,weather_info.class);
													  intent.putExtra("login", 1);
													  startActivity(intent);
													  finish();
												}else {
													 if(e.getErrorCode()==101)
												      	Toast.makeText(loginAct.this,"��¼ʧ��,�˻������������"+e.getMessage(),Toast.LENGTH_SHORT ).show();
													 else if(e.getErrorCode()==9016)
													    Toast.makeText(loginAct.this,"��¼ʧ�ܣ����������ӣ����������ֻ����磬"+e.getMessage(),Toast.LENGTH_SHORT ).show();
													 else{
														 Toast.makeText(loginAct.this,"��¼ʧ��,"+e.getMessage(),Toast.LENGTH_SHORT ).show();
													 }
														 
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
						    			 Toast.makeText(loginAct.this,"������֤�������䣬����ʼ���ɾ������ע��ҳ�������������䣬���������֤",Toast.LENGTH_SHORT).show();
									}
						    	 }
						    	}
						     }else if(e.getErrorCode()==9016){
						    	  Toast.makeText(loginAct.this,"����ʧ�ܣ����Ժ����ԣ����������ӣ����������ֻ����磬"+e.getMessage(), Toast.LENGTH_SHORT).show();
						     } else{
								Toast.makeText(loginAct.this,"����ʧ�ܣ����Ժ����ԣ�"+e.getMessage(), Toast.LENGTH_SHORT).show();
							}
							
						}
						
						 
					});
					   }else if(isMobileNO(input)){
						   bu.setPassword(MD5Util.getMD5String(passwordString));
						    bu.login(new SaveListener<MyUser>(){

                                @Override
								public void done(MyUser myUser, BmobException e) {
									if(e==null){
										  installationId=MyBmobInstallation.getInstallationId(loginAct.this);
                                          Toast.makeText(loginAct.this, "��¼�ɹ�", Toast.LENGTH_SHORT).show();
										  Intent intent=new Intent(loginAct.this,weather_info.class);
										  intent.putExtra("login", 1);
										  startActivity(intent);
										  finish();
									}else {
										 if(e.getErrorCode()==101)
										      	Toast.makeText(loginAct.this,"��¼ʧ��,�˻������������"+e.getMessage(),Toast.LENGTH_SHORT ).show();
										 else if(e.getErrorCode()==9016)
											  Toast.makeText(loginAct.this,"��¼ʧ�ܣ����������ӣ����������ֻ����磬"+e.getMessage(),Toast.LENGTH_SHORT ).show();
										else {
											 Toast.makeText(loginAct.this,"��¼ʧ�ܣ�"+e.getMessage(), Toast.LENGTH_SHORT).show();
									  	}
										 if(relativeRoot.getAlpha()==0.3f){
												progressBar.setVisibility(View.GONE);
												relativeRoot.setAlpha(1);
												relativeLayout.setVisibility(View.GONE);
												textView.setVisibility(View.GONE);
											}
									}
									
								}
	
						    });
					   }
					  
				
				   }else {
					Toast.makeText(loginAct.this,"��������ȷ���ֻ��Ż�������", Toast.LENGTH_SHORT).show();
				  }
				}
				if(action==MotionEvent.ACTION_UP){
					
					button1.setBackgroundColor(Color.parseColor("#00FF00"));
					
				}
				return false;
			}
		});
	    button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(loginAct.this,moblieLoginAct.class);
				startActivity(intent);
				application.add(loginAct.this);
			 }
		});
	    button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				 
				   Intent intent=new Intent(loginAct.this,register.class);
				   startActivity(intent);
				   application.add(loginAct.this);
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
    
       imageView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View view) {
		     if(eye){
		    	 editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
		    	 imageView.setImageResource(R.drawable.eyeclose);
		    	 eye=false;
		    	 
		     }else {
				editText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
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
    public String getStringData(int id){
    	return getResources().getString(id);
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
	public static boolean isEmail(String email) 
	{

		Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//����ƥ��  

		Matcher m = p.matcher(email);
        return m.matches();

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
