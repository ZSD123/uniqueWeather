package activity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.sharefriend.app.R;

import Util.MD5Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class resetPassword1 extends Activity {
     private ImageView imageView1;  //��һ���۾�
     private ImageView imageView2;  //�ڶ����۾�
     private EditText password1; 
     private EditText password2;
     private EditText yazhengma;   //��֤��
     private boolean eye1=false;   //�۾��ر�
     private boolean eye2=false;  
     private Button button;   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.resetpassword1);
		imageView1=(ImageView)findViewById(R.id.kekanmima1);
		imageView2=(ImageView)findViewById(R.id.kekanmima2);
		password1=(EditText)findViewById(R.id.password1);
		password2=(EditText)findViewById(R.id.password2);
		button=(Button)findViewById(R.id.ensure);
		yazhengma=(EditText)findViewById(R.id.yanzhengma);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pass1=password1.getText().toString();
				String pass2=password2.getText().toString();
				if(yazhengma.getText().toString().isEmpty()){
					Toast.makeText(resetPassword1.this,"��֤�벻��Ϊ��",Toast.LENGTH_SHORT).show();
				}else if(!pass1.isEmpty()&&!pass2.isEmpty()){
					if(pass1.equals(pass2)){
						MyUser.resetPasswordBySMSCode(yazhengma.getText().toString(), MD5Util.getMD5String(pass1), new UpdateListener(){

							@Override
							public void done(BmobException e) {
							   if(e==null){
								   Toast.makeText(resetPassword1.this,"�������óɹ�,��ʹ���������¼",Toast.LENGTH_SHORT).show();
								   Intent intent=new Intent(resetPassword1.this,loginAct.class);
								   startActivity(intent);
								   finish();
							   }else {
								   Toast.makeText(resetPassword1.this,"����ʧ��,"+e.getMessage(),Toast.LENGTH_SHORT).show();
							     }
								
							}
							
						});
					}else {
						Toast.makeText(resetPassword1.this,"�������벻һ�������ٴ�ȷ��",Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(resetPassword1.this,"�����벻��Ϊ��",Toast.LENGTH_SHORT).show();
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
	     
	    
	}
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder=new AlertDialog.Builder(resetPassword1.this);
		final AlertDialog alertDialog=builder.create();
		builder.setTitle("�뿪����");
		builder.setMessage("ȷ���뿪����ҳ����");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				  finish();
				  
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(alertDialog.isShowing()){
					alertDialog.dismiss();
				}
			}
		});
		
		builder.show();
	}
     
}
