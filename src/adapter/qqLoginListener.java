package adapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class qqLoginListener implements IUiListener {
  
  private String openid;
  private String access_token;
  private String expires_in;
  private Context mContext;
  private Tencent mTencent;
  
  private  ProgressBar progressBar;
  private RelativeLayout relativeLayout;
  private RelativeLayout relativeRoot;
  private  TextView textView;
  
   public String getOpenid() {
	return openid;
  }
  public String getAccess_token() {
	return access_token;
  }
  public String getExpires_in() {
	return expires_in;
  }
  public qqLoginListener(Context context,Tencent mTen,ProgressBar mprogressBar,RelativeLayout mrelativeLayout,RelativeLayout mrelativeRoot,TextView mtextView){
	  mContext=context;
	  mTencent=mTen;
	  
	  progressBar=mprogressBar;
	  relativeLayout=mrelativeLayout;
	  relativeRoot=mrelativeRoot;
	  textView=mtextView;
  }
  @Override
  public void onComplete(Object response) {
     //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
	   progressBar.setVisibility(View.VISIBLE);
	   relativeLayout.setVisibility(View.VISIBLE);
	   relativeLayout.setOnClickListener(null);
	   relativeRoot.setAlpha(0.3f);
	   textView.setVisibility(View.VISIBLE);
	   
       Log.d("Main", "QQ登录成功");
       doComplete((JSONObject) response);
   }
   protected void doComplete(JSONObject values) {
        try {
			 openid=values.getString("openid");
			 access_token=values.getString("access_token");
			 expires_in=values.getString("expires_in");
			 SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(mContext).edit();
			 
		     long time= System.currentTimeMillis() + Long.parseLong(expires_in) * 1000;
			 
			 editor.putString("openid", openid);
			 editor.putString("access_token", access_token);
			 editor.putLong("expires_in", time);
			 editor.commit();
			   
		     mTencent.setAccessToken(access_token, expires_in);     //要先设置它的token和expires还有下面那个参数，不然得不到相应的userInfo
		     mTencent.setOpenId(openid);
				
			 UserInfo userInfo=new UserInfo(mContext, mTencent.getQQToken());
			 userInfoListener listener2=new userInfoListener(mContext,openid,progressBar,relativeLayout,relativeRoot,textView);
			 userInfo.getUserInfo(listener2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
   }
   @Override
   public void onError(UiError e) {
	   Toast.makeText(mContext, "登录错误，"+e.errorMessage, Toast.LENGTH_SHORT).show();
      
    }  
   @Override
   public void onCancel() {
      Toast.makeText(mContext, "登录取消", Toast.LENGTH_SHORT).show();
   }

   
}
