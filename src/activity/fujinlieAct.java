package activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import model.location;
import myCustomView.DividerItemDecoration;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.sharefriend.app.R;

import db.fujinlieDB;

import Util.Http;
import Util.HttpCallbackListener;
import adapter.OnRecyclerViewListener;
import adapter.fujinAdapter;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.math.BigDecimal;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class fujinlieAct extends baseActivity {
    private int count;
    private double lat;
    private double lon;
    private fujinAdapter fujinadapter;
    
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private TextView textView;
    
    private List<location> locationLists;
    private RecyclerView recyclerView;
    
    public static  fujinlieDB fujinliedb;
    
    private int num;  //查询的次数
    
    private MyUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fujin_yonghu_lie);
		
		currentUser=MyUser.getCurrentUser(MyUser.class);
		
		RelativeLayout relative=(RelativeLayout)findViewById(R.id.relative);
		CustomFontTextView textView1=(CustomFontTextView)findViewById(R.id.text);
		
		fujinliedb=fujinlieDB.getInstance(fujinlieAct.this);
		
		recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
		
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
	    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		
	    progressBar=(ProgressBar)findViewById(R.id.progressBar);
	    relativeLayout=(RelativeLayout)findViewById(R.id.relativelayout1);
	    textView=(TextView)findViewById(R.id.textDeng);
	    
	    SharedPreferences pre=PreferenceManager.getDefaultSharedPreferences(fujinlieAct.this);
	    
	    int designNum=pre.getInt("design", 0);
		if(designNum==4){
			relative.setBackgroundColor(Color.parseColor("#051C3D"));
			textView1.setTextColor(Color.parseColor("#A2C0DE"));
			
		}
	    
		lat=pre.getFloat("lat", 39);
		lon=pre.getFloat("lon",116);
		
		BmobQuery<location> bmobQuery=new BmobQuery<location>();
		bmobQuery.addWhereNotEqualTo("myUser",currentUser);
		bmobQuery.setLimit(50);
		bmobQuery.order("-updateAt");
		bmobQuery.findObjects(new FindListener<location>() {
			
			@Override
			public void done(List<location> list, BmobException e) {
               
				if(e==null){
					count=list.size();
					locationLists=list;	
					
					for (int i = 0; i < locationLists.size(); i++) {
						 fujinliedb.saveLatAndLon(locationLists.get(i).getMyUser().getObjectId(), String.valueOf(locationLists.get(i).getMyLocation().getLatitude()),String.valueOf(locationLists.get(i).getMyLocation().getLongitude()));
						 
					}   //先存储用户objectId和它的经纬度信息
					
					if(locationLists!=null&&locationLists.size()>0){
					
					      for (int i = 0; i < locationLists.size(); i++) {
					    	
					    	  BmobQuery<MyUser> query=new BmobQuery<MyUser>();
						      query.getObject(locationLists.get(i).getMyUser().getObjectId(),new QueryListener<MyUser>() {
								
								@Override
								public void done(MyUser myUser, BmobException e) {
									if(e==null) {
									      num++;
									      
									      float distance=AMapUtils.calculateLineDistance(new LatLng(lat, lon), new LatLng(fujinliedb.getLatbyId(myUser.getObjectId())[0], fujinliedb.getLatbyId(myUser.getObjectId())[1]));
										  float distanceofAbs=Math.abs(distance);
										  int distanceofNew=Math.round(distanceofAbs);
										  
										  fujinliedb.saveEverythingById(myUser.getObjectId(), myUser.getTouXiangUrl(), myUser.getNick(), myUser.getSex(), String.valueOf(distanceofNew));
									      
									   if(num==count){
										   
										    progressBar.setVisibility(View.GONE);
											relativeLayout.setVisibility(View.GONE);
											textView.setVisibility(View.GONE);
										   
										    List<MyUser> userLists=fujinliedb.getMyUsers();    //获取userlist列表
											fujinadapter=new fujinAdapter( count, fujinlieAct.this,userLists);
											recyclerView.setAdapter(fujinadapter);
											
											fujinadapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
												
												@Override
												public boolean onItemLongClick(int position) {
													// TODO Auto-generated method stub
													return false;
												}
												
												@Override
												public void onItemClick(int position) {
													Intent intent =new Intent(fujinlieAct.this,searchResultAct.class);
													intent.putExtra("nick", fujinadapter.getItem(position).getNick());
													intent.putExtra("touxiang", fujinadapter.getItem(position).getTouXiangUrl());
													intent.putExtra("objectId", fujinadapter.getItem(position).getObjectId());
													intent.putExtra("lat", fujinliedb.getLatbyId(fujinadapter.getItem(position).getObjectId())[0]);
													intent.putExtra("lon", fujinliedb.getLatbyId(fujinadapter.getItem(position).getObjectId())[1]);
													startActivity(intent);
												}
											});
									   
									   }
									}
									else {
									   Toast.makeText(fujinlieAct.this, "出现错误,"+e.getErrorCode()+e.getMessage(), Toast.LENGTH_SHORT).show();
									}
									
									
								}
							});
						}
					      
					      
					}else if(locationLists!=null&&locationLists.size()>0){
						 Toast.makeText(fujinlieAct.this, "暂无结果", Toast.LENGTH_SHORT).show();
					}
				
					
					
				}else if(e.getErrorCode()!=9015){

					count=0;
					Toast.makeText(fujinlieAct.this, "出现错误,"+e.getErrorCode()+e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		
		
	}
	@Override
	protected void onDestroy() {
		fujinliedb.deleteAll();
		super.onDestroy();
	}

   
}
