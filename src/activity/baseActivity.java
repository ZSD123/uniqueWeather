package activity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.ValueEventListener;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public  class baseActivity extends Activity {
	private String bmobObjectId;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		//自动登陆状态下检测是否在其他设备登陆
		   bmobObjectId=weather_info.objectId;
		   checkLogin();
		  
		
	}
	@Override
	protected void onResume() {

		super.onResume();
		//锁屏状态下的检测
		checkLogin();
	}
	
	public void checkLogin() {
		final BmobRealTimeData rtd=new BmobRealTimeData();
	    rtd.start(new ValueEventListener() {
			
			@Override
			public void onDataChange(JSONObject data) {
			    try {
					JSONObject jsonObject=data.getJSONObject("data");
					String webinstallationId=jsonObject.getString("installationId");
					
					    
					if(!webinstallationId.equals(loginAct.installationId)){
						MyUser.logOut();
						MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
						if(dialog==null)
					    	showLogOutDialog();
					} else{
						BmobQuery<MyUser> bmobQuery=new BmobQuery<MyUser>();   //这里改动添加因为有时候即使数据改了返回的是原来的数据
					     bmobQuery.getObject(bmobObjectId, new QueryListener<MyUser>() {
							
						@Override
						public void done(MyUser myUser, BmobException e) {
							if(e==null){
								if(!myUser.getInstallationId().equals(loginAct.installationId)){
									MyUser.logOut();
									MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
									if(dialog==null)
									   showLogOutDialog();
								}
							}
							
						}
				    	});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			    
			}
			
			@Override
			public void onConnectCompleted(Exception e) {
				 if(rtd.isConnected()){
				    	rtd.subRowUpdate("_User", bmobObjectId);
				    }
			}
		});
	   
	}
	@SuppressWarnings("unused")
	public void showLogOutDialog(){
	
		AlertDialog.Builder builder=new AlertDialog.Builder(baseActivity.this);
		
		builder.setMessage("您当前账户在其他设备上登录，即将下线");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent=new Intent(getApplicationContext(),loginAct.class);
				startActivity(intent);
				if(baseActivity.this.dialog!=null){
					baseActivity.this.dialog.dismiss();
				}
				finish();

			}
		});
        builder.setCancelable(false);
        dialog=builder.create();
        if(!dialog.isShowing())
	    	dialog.show();
	}
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
	   public Bundle getBundle() {
	        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
	            return getIntent().getBundleExtra(getPackageName());
	        else
	            return null;
	    }
	    public void hideSoftInputView() {
	        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
	        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
	            if (getCurrentFocus() != null)
	                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	        }
	    }


}
