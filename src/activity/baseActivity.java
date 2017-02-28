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
import android.util.Log;

public class baseActivity extends Activity {
	private String bmobObjectId=(String)MyUser.getObjectByKey("objectId");
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//�Զ���½״̬�¼���Ƿ��������豸��½
		checkLogin();
	}
	@Override
	protected void onResume() {

		super.onResume();
		//����״̬�µļ��
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
					Log.d("Main", "web="+webinstallationId);
					Log.d("Main","loc="+loginAct.installationId);
					if(!webinstallationId.equals(loginAct.installationId)){
						MyUser.logOut();
						Log.d("Main","1");
						MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
						Log.d("Main","2");
						showLogOutDialog();
						Log.d("Main","3");
					}
				} catch (JSONException e) {
					Log.d("Main","e.gt"+e.getMessage());
					e.printStackTrace();
				}
			    
			}
			
			@Override
			public void onConnectCompleted(Exception e) {
				Log.d("Main", "���ӳɹ�:"+rtd.isConnected());
				 if(rtd.isConnected()){
				    	rtd.subRowUpdate("_User", bmobObjectId);
				    }
			}
		});
	   
	}
	public void showLogOutDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(baseActivity.this);
		
		builder.setMessage("����ǰ�˻��������豸�ϵ�¼����������");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("Main","4"); 
				Intent intent=new Intent(getApplicationContext(),loginAct.class);
				Log.d("Main","5"); 
				startActivity(intent);
				 finish();
					Log.d("Main","6");
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
}
