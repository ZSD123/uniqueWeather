package adapter;

import java.util.List;

import model.qqUser;

import org.json.JSONException;
import org.json.JSONObject;

import activity.MyBmobInstallation;
import activity.MyUser;
import activity.loginAct;
import activity.weather_info;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.amap.api.mapcore2d.bm;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class userInfoListener implements IUiListener{   //QQ用户资料获取接口
	
    private Context mContext;
    private qqUser qUser;  
    private String mOpenid;
    
    private  ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private RelativeLayout relativeRoot;
    private  TextView textView;
	@Override
	public void onCancel() {
		 Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onComplete(Object object) {
		
		doComplete((JSONObject)object);
		
	}
	
    public userInfoListener(Context context,String openid,ProgressBar mprogressBar,RelativeLayout mrelativeLayout,RelativeLayout mrelativeRoot,TextView mtextView){
    	mContext=context;
    	mOpenid=openid;
    	
        progressBar=mprogressBar;
   	    relativeLayout=mrelativeLayout;
   	    relativeRoot=mrelativeRoot;
   	    textView=mtextView;
    }
    
	@Override
	public void onError(UiError e) {
		 Toast.makeText(mContext, "发生错误，"+e.errorMessage, Toast.LENGTH_SHORT).show();
		 if(relativeRoot.getAlpha()==0.3f){
				progressBar.setVisibility(View.GONE);
				relativeRoot.setAlpha(1);
				relativeLayout.setVisibility(View.GONE);
				textView.setVisibility(View.GONE);
			}
		
	}
	   protected void doComplete(JSONObject values) {
		   qUser=new qqUser();
	       try {
	    	qUser.setOpenid(mOpenid);
			qUser.setNickname(values.getString("nickname"));
			qUser.setFigureurl_qq_2(values.getString("figureurl_qq_2"));
			qUser.setCity(values.getString("city"));
			qUser.setProvince(values.getString("province"));
			qUser.setGender(values.getString("gender"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	       BmobQuery<MyUser> bmobQuery=new BmobQuery<MyUser>();
	       bmobQuery.addWhereEqualTo("username", "qq"+qUser.getOpenid());
	       bmobQuery.findObjects(new FindListener<MyUser>() {
			
			@Override
			public void done(List<MyUser> list, BmobException e) {
				if(e==null){
					if(list.size()>0){
						MyUser user=new MyUser();
						user.setUsername("qq"+mOpenid);
						user.setPassword(mOpenid);
						user.login(new SaveListener<MyUser>() {

							@Override
							public void done(MyUser user, BmobException e) {
								if(e==null){
									Toast.makeText(mContext, "成功，正在转入", Toast.LENGTH_SHORT).show();
									loginAct.installationId=MyBmobInstallation.getInstallationId(mContext);
									Intent intent=new Intent(mContext,weather_info.class);
									intent.putExtra("login", 1);
									mContext.startActivity(intent);
								    ((Activity)mContext).finish();
								}else {
									Toast.makeText(mContext, "哎呀，不好意思，出错了，"+e.getMessage(), Toast.LENGTH_SHORT).show();
									 if(relativeRoot.getAlpha()==0.3f){
											progressBar.setVisibility(View.GONE);
											relativeRoot.setAlpha(1);
											relativeLayout.setVisibility(View.GONE);
											textView.setVisibility(View.GONE);
										}
							 	}
								
							}
							
						});
					}else {                 //没有相应的QQ用户,注册一个
						MyUser bu=new MyUser();
						bu.setUsername("qq"+qUser.getOpenid());
						bu.setPassword(qUser.getOpenid());
						bu.setNick(qUser.getNickname());
						bu.setTouXiangUrl(qUser.getFigureurl_qq_2());
						bu.setSuozaidi(qUser.getProvince()+qUser.getCity());
						bu.setSex(qUser.getGender());
						bu.setQQ(true);
						bu.setCanCall(false);
						bu.signUp(new SaveListener<MyUser>() {

							@Override
							public void done(MyUser user, BmobException e) {
								 if(e==null){
									 Toast.makeText(mContext, "成功，正在转入", Toast.LENGTH_SHORT).show();
									 loginAct.installationId=MyBmobInstallation.getInstallationId(mContext);
									 Intent intent=new Intent(mContext,weather_info.class);
									 intent.putExtra("login", 1);
									 intent.putExtra("isQQ",1);
									 mContext.startActivity(intent);
									 ((Activity)mContext).finish();
								 }else {
									 Toast.makeText(mContext, "哎呀，不好意思，出错了，"+e.getMessage(), Toast.LENGTH_SHORT).show();
									 if(relativeRoot.getAlpha()==0.3f){
											progressBar.setVisibility(View.GONE);
											relativeRoot.setAlpha(1);
											relativeLayout.setVisibility(View.GONE);
											textView.setVisibility(View.GONE);
										}
								}
								
							}
						});
				   	}
				}else {
					 Toast.makeText(mContext, "哎呀，不好意思，出错了，"+e.getMessage(), Toast.LENGTH_SHORT).show();
					 if(relativeRoot.getAlpha()==0.3f){
							progressBar.setVisibility(View.GONE);
							relativeRoot.setAlpha(1);
							relativeLayout.setVisibility(View.GONE);
							textView.setVisibility(View.GONE);
						}
				 }
				
			}
		});
	        
	   }
	   public qqUser getQQUser(){
		   return qUser;
	   }

}
