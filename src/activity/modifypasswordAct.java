package activity;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.uniqueweather.app.R;

import Util.MD5Util;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class modifypasswordAct extends baseActivity {
    private ImageView imageView1;  //第一个眼睛
    private ImageView imageView2;  //第二个眼睛
    private ImageView imageView3;  //第三个眼睛
    private EditText password1;
    private EditText password2; 
    private EditText password3;
    private boolean eye1=false;   //眼睛关闭
    private boolean eye2=false;  
    private boolean eye3=false;
    private Button button;   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.modifypassword);
		imageView1=(ImageView)findViewById(R.id.kekanmima1);
		imageView2=(ImageView)findViewById(R.id.kekanmima2);
		imageView3=(ImageView)findViewById(R.id.kekanmima3);
		button=(Button)findViewById(R.id.ensure);
		password1=(EditText)findViewById(R.id.password1);
		password2=(EditText)findViewById(R.id.password2);
		password3=(EditText)findViewById(R.id.password3);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String oldPassword=password1.getText().toString();
				String newPassword1=password2.getText().toString();
				String newPassword2=password3.getText().toString();
				if(!oldPassword.isEmpty()&&!newPassword1.isEmpty()&&!newPassword2.isEmpty()){
					if(newPassword1.equals(newPassword2)){
					    String username=(String)BmobUser.getCurrentUser(MyUser.class).getUsername();
					      if(loginAct.isEmail(username)){
					    	  MyUser.updateCurrentUserPassword(oldPassword,newPassword1, new UpdateListener(){
     
								@Override
								public void done(BmobException e) {
									if(e==null){
										 Toast.makeText(modifypasswordAct.this,"密码修改成功，可以用新密码登录啦",Toast.LENGTH_SHORT).show();
									}else if(e.getErrorCode()==210){
										 Toast.makeText(modifypasswordAct.this,"失败:旧密码不正确"+e.getMessage(),Toast.LENGTH_SHORT).show();
									}else {
										 Toast.makeText(modifypasswordAct.this,"失败:"+e.getMessage(),Toast.LENGTH_SHORT).show();
									}
									
								}});
					      }else if(loginAct.isMobileNO(username)){
					       	  MyUser.updateCurrentUserPassword(MD5Util.getMD5String(oldPassword),MD5Util.getMD5String(newPassword1), new UpdateListener(){

									@Override
									public void done(BmobException e) {
										if(e==null){
											 Toast.makeText(modifypasswordAct.this,"密码修改成功，可以用新密码登录啦",Toast.LENGTH_SHORT).show();
										}else if(e.getErrorCode()==210){
											 Toast.makeText(modifypasswordAct.this,"失败:旧密码不正确"+e.getMessage(),Toast.LENGTH_SHORT).show();
										}else {
											 Toast.makeText(modifypasswordAct.this,"失败:"+e.getMessage(),Toast.LENGTH_SHORT).show();
										}
										
									}});
					      }
					}else {
					 Toast.makeText(modifypasswordAct.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
					}
				}else {
				   	Toast.makeText(modifypasswordAct.this,"三项任意一项不能为空",Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		   imageView1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
				     if(eye1){
				    	 password1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
				    	 imageView1.setImageResource(R.drawable.eyeclose);
				    	 eye1=false;
				    	 
				     }else {
						password1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
						imageView1.setImageResource(R.drawable.eyeopen);
						eye1=true;
					}
					
				}
			});
		    imageView2.setOnClickListener(new OnClickListener() {
				
		 			@Override
		 			public void onClick(View view) {
		 			     if(eye2){
		 			    	 password2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
		 			    	 imageView2.setImageResource(R.drawable.eyeclose);
		 			    	 eye2=false;
		 			    	 
		 			     }else {
		 					password2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
		 					imageView2.setImageResource(R.drawable.eyeopen);
		 					eye2=true;
		 				}
		 				
		 			}
		 		});
		    imageView3.setOnClickListener(new OnClickListener() {
				
	 			@Override
	 			public void onClick(View view) {
	 			     if(eye3){
	 			    	 password3.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
	 			    	 imageView3.setImageResource(R.drawable.eyeclose);
	 			    	 eye3=false;
	 			    	 
	 			     }else {
	 					password3.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_TEXT);
	 					imageView3.setImageResource(R.drawable.eyeopen);
	 					eye3=true;
	 				}
	 				
	 			}
	 		});
		     
	}
     
}
