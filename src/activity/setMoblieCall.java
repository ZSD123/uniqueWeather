package activity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.sharefriend.app.R;

import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class setMoblieCall extends baseActivity {
     CheckBox checkBox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setmobliecall);
		checkBox=(CheckBox)findViewById(R.id.check);
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
					    	 Toast.makeText(setMoblieCall.this, "更新失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
					     }else {
					    	 Toast.makeText(setMoblieCall.this, "更新成功", Toast.LENGTH_SHORT).show();
					     }
					    	 
						
					}
				});
				
			}
		});
	}
     
}
