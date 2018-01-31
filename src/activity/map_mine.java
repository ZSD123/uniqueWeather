package activity;

import mapAct.share_historyAct;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.sharefriend.app.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class map_mine extends baseActivity {
    private ImageButton imageButton;  //���Ͻǵ��˳���ť
    private TextView text_credit;    //��������ָ��textview
    private Integer credit;      //��������ָ��
    private MyUser currentUser;   //��������weather_info.user����null�������¿�һ����
    private LinearLayout shareHistoryLayout;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_mine);
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		text_credit=(TextView)findViewById(R.id.text_credit);
		
		shareHistoryLayout=(LinearLayout)findViewById(R.id.share_history);
		
		currentUser=MyUser.getCurrentUser(MyUser.class);
		
		if(currentUser!=null&&currentUser.getCredit()==null){   //�����֮ǰע����˻����ͻ����null
	         credit=100;
	         MyUser myUser=new MyUser();
	         myUser.setCredit(100);
	         myUser.update(weather_info.objectId,new UpdateListener() {
				
				@Override
				public void done(BmobException e) {
					if(e==null){
						
					}else {
						Toast.makeText(map_mine.this,"���ִ���"+e.getMessage(), Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	    }else if(currentUser!=null&&currentUser.getCredit()!=null){
			 credit=currentUser.getCredit();
		}
		
		if(credit!=null) 
	       text_credit.setText(""+credit);
	    
		
		shareHistoryLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(map_mine.this,share_historyAct.class);
				startActivity(intent);
			}
		});
		
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
    
	 

}
