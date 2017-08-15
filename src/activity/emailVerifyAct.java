package activity;


import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.sharefriend.app.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class emailVerifyAct extends baseActivity {
    private Timer timer;
    private TimerTask task;
    private final int UPDATE_TEXT=0;
    private final int TOFASONG=1; 
    private Button button;
    private int daojishi=30;   
    private Handler handler=new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case UPDATE_TEXT:
    			button.setText(daojishi+"s后再发送");
    			break;
    		case TOFASONG:
    			button.setText("发送验证码");
    			button.setEnabled(true);
    		default:
    			break;
    		}
    	}
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.emailverify);
        button=(Button)findViewById(R.id.fasong);
		final EditText editText=(EditText)findViewById(R.id.editText_account);
		MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
		if(currentUser.getEmail()!=null&&!currentUser.getEmail().equals("")){
		editText.setText(currentUser.getEmail());
		editText.setEnabled(false);
		}
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  final String account=editText.getText().toString();
				    if(account.isEmpty()){
				    	Toast.makeText(emailVerifyAct.this, "账号不能为空",Toast.LENGTH_SHORT).show();
				    }else if(loginAct.isEmail(account)){
				    	  MyUser.requestEmailVerify(account,new UpdateListener(){

								@Override
								public void done(BmobException e) {
									if(e==null){
										Toast.makeText(emailVerifyAct.this,"发送验证邮箱成功",Toast.LENGTH_SHORT).show();
										button.setEnabled(false);
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
										Toast.makeText(emailVerifyAct.this,"发送验证邮箱失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
									}
									
								}
								  
							  }
							   );
					 }else {
						 Toast.makeText(emailVerifyAct.this,"不符合邮箱格式，请仔细检查", Toast.LENGTH_SHORT).show();
					 }
				
				
			}
		});
		
	}
     
}
