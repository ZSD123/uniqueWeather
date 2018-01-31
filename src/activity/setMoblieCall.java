package activity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.sharefriend.app.R;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class setMoblieCall extends baseActivity {
     CheckBox checkBox;
     private SharedPreferences pre;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setmobliecall);
		
		pre=PreferenceManager.getDefaultSharedPreferences(this);
		
		LinearLayout linearLayout=(LinearLayout)findViewById(R.id.lin);
		checkBox=(CheckBox)findViewById(R.id.check);
		
		int designNum=pre.getInt("design", 0);
		if(designNum==4){
			linearLayout.setBackgroundColor(Color.parseColor("#051C3D"));
			checkBox.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		
		MyUser myUser=BmobUser.getCurrentUser(MyUser.class);
		checkBox.setChecked(!myUser.isCanCall());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			       MyUser myUser=new MyUser();
			       myUser.setCanCall(!isChecked);
			       myUser.update(weather_info.objectId, new UpdateListener() {
					
					@Override
					public void done(BmobException e) {
					     if(e!=null){
					    	 Toast.makeText(setMoblieCall.this, "����ʧ�ܣ�"+e.getMessage(), Toast.LENGTH_SHORT).show();
					     }else {
					    	 Toast.makeText(setMoblieCall.this, "���³ɹ�", Toast.LENGTH_SHORT).show();
					     }
					    	 
						
					}
				});
				
			}
		});
	}
     
}
