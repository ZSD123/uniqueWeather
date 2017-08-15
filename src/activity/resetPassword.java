package activity;


import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;

import com.sharefriend.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class resetPassword extends Activity {
    private Button button;
    private EditText editText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.resetpassword);
		 button=(Button)findViewById(R.id.fasong);
	     editText=(EditText)findViewById(R.id.editText_account);
		ImageButton imageButton=(ImageButton)findViewById(R.id.imagebutton);
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				 if(loginAct.isEmail(s.toString())){
					  button.setText("发送邮件");
				 }else if(loginAct.isMobileNO(s.toString())){
					  button.setText("发送短信");
				 }
			}
		});
		 button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				    final String account=editText.getText().toString();
				    if(account.isEmpty()){
				    	Toast.makeText(resetPassword.this, "账号不能为空",Toast.LENGTH_SHORT).show();
				    }else if(loginAct.isEmail(account)){
						MyUser.resetPasswordByEmail(account,new UpdateListener() {
							
							@Override
							public void done(BmobException e) {
								if(e==null){
									Toast.makeText(resetPassword.this, "重置密码请求成功，请到"+account+"邮箱进行密码重置操作",Toast.LENGTH_LONG).show();
								}else {
									Toast.makeText(resetPassword.this, "失败:"+e.getMessage(),Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					 }else if(loginAct.isMobileNO(account)){
						 BmobSMS.requestSMSCode(account,"短信验证",new QueryListener<Integer>() {
							
							@Override
							public void done(Integer smsId, BmobException e) {
								if(e==null){
									Toast.makeText(resetPassword.this, "发送成功，等待接收",Toast.LENGTH_SHORT).show();
								     Intent intent=new Intent(resetPassword.this,resetPassword1.class);
								     startActivity(intent);
								}else {
									Toast.makeText(resetPassword.this, "失败:"+e.getMessage(),Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					 }
				}
			});
		
		
	}
       
}
