package activity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ValueEventListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class baseFragmentActivity extends FragmentActivity {
	private String bmobObjectId;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
     
		
		super.onCreate(savedInstanceState);
		//自动登陆状态下检测是否在其他设备登陆
	
			bmobObjectId=(String)MyUser.getObjectByKey("objectId");
		
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
							showLogOutDialog();
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
		Activity activity=baseFragmentActivity.this;
		if(activity==null){
			if(activity.getParent()!=null){
				activity=activity.getParent();
			}
		}
		AlertDialog.Builder builder=new AlertDialog.Builder(activity);
		builder.setMessage("您当前账户在其他设备上登录，即将下线");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 Intent intent=new Intent(getApplicationContext(),loginAct.class);
				 startActivity(intent);
				 finish();
			}
		});
		builder.setCancelable(false);
		 dialog=builder.create();
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
