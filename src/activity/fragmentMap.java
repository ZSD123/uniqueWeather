package activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import message.AddFriendMessage;
import myCustomView.MapCircleImageView;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearch.NearbyListener;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.amap.api.services.nearby.NearbySearch.NearbyQuery;
import com.sharefriend.app.R;

import db.yonghuDB;
import Util.Http;
import Util.HttpCallbackListener;
import Util.SensorEventHelper;
import Util.Utility;
import Util.download;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class fragmentMap extends Fragment implements AMapLocationListener,LocationSource, OnCameraChangeListener, OnMarkerClickListener, NearbyListener {
	private volatile int zmarkNum=0;//直接添加用户头像的Marker个数
    public static RelativeLayout fuzhiMap;
    private ImageButton zujiBtn;
    
	public static  yonghuDB yongbDb;  //周围用户数据表
    private Circle mCircle;
    public double lat;
    public double lon;
    
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pre;
    
    public float zoom;
    private LatLng latLng1;
    public static MapView mMapView;
    
	private Marker mLocMarker;    //定位Marker
	private TextView yonghuString;
	private ImageButton refreshBtn;  //地图刷新按钮
	  public static AMap aMap;
	
	  public NearbySearch mNearbySearch;
	  private SensorEventHelper mSensorEventHelper;
	  
	  public static MapCircleImageView userPicture1;  //地图上userPicture
    
	  private static Context context;
	  private LatLonPoint latlng;
	  public static CameraUpdate cameraUpdate;
	  public NearbyQuery query;  
      
      public int bitmapNum=0;       //其他用户头的个数
      private Timer timer;
      private TimerTask task;
      public List<String> objStrings =new ArrayList<String>();//加载过其他用户的obj
      public LatLng searchLatLng;   //同上
      public volatile int chucuoonce=0;
  	  private boolean mFirstFix = false;
	  private OnLocationChangedListener mListener;
      private boolean zuji=true;
      public AMapLocationClient mLocationClient;
      private Marker mLoveMarker;    //当前Marker
      public static View  yonghuDataView;  //附近用户资料卡View
      private List<Marker> mFujinMarker=new ArrayList<Marker>(); //没有设置头像直接添加头像的附近用户Marker
      private Bitmap bitmap11;
  	  public fragmentMap(){
	 	 context=getContext();
	  }
	@Override

	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		View view=null;
		
		view=inflater.inflate(R.layout.map,container,false);
		ImageButton locButton;
		
		fuzhiMap=(RelativeLayout)view;
		
		
		zujiBtn=(ImageButton)view.findViewById(R.id.zujibtn);
		if(context==null)
			context=getActivity();
		editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		pre=PreferenceManager.getDefaultSharedPreferences(context);
		
		
		lat=pre.getFloat("lat", 39);
		lon=pre.getFloat("lon",116);
		
		
		zoom=pre.getFloat("zoom", 18);
		latLng1=new LatLng(lat, lon);
		mMapView=(MapView)view.findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		locButton=(ImageButton)view.findViewById(R.id.locationButton);
		yonghuString=(TextView)view.findViewById(R.id.yonghuString);
		refreshBtn=(ImageButton)view.findViewById(R.id.refreshbtn);
		
		if(context==null)
			context=(Context)getActivity();
		
		View mapView=LayoutInflater.from(context).inflate(R.layout.mapcircleimageview, null);
		userPicture1=(MapCircleImageView)mapView.findViewById(R.id.mapCircle);
		LayoutParams layoutParams=new LayoutParams(Utility.dip2px(context,100),Utility.dip2px(context, 100));
		userPicture1.setLayoutParams(layoutParams);
		yongbDb=yonghuDB.getInstance(context);

		if(aMap==null)
		{   
			aMap=mMapView.getMap();
			if(aMap==null)
				Toast.makeText(context, "初始化失败，请退出后再试一次", Toast.LENGTH_LONG).show();
		}
		else {
			aMap.clear();
			aMap=mMapView.getMap();     // Fragment嵌套高德地图，当再次进入Fragment的时候，                           //会出现奇怪的现象。嵌套的地图会出现无法定位的现象。                           //这个问题出现的原因在于，fragment在被移除时，不会执行onDestroy（）方法，而是执行onDestroyView（）方法。fragment中的数据已经在第一次操作时完成了初始化了，所以以下代码中，aMap不为null,无法执行getMap()正常初始化，故无法正常开启定位功能。
			if(aMap==null)
				Toast.makeText(context, "初始化失败，请退出后再试一次", Toast.LENGTH_LONG).show();
		   }
	    aMap.setLocationSource(this);
	    aMap.setMyLocationEnabled(true); 
	    aMap.setOnCameraChangeListener(this);
	    aMap.setOnMarkerClickListener(this);
	    
	    UiSettings uiSettings;     //AMap的设置
	    
	    uiSettings=aMap.getUiSettings();
	    uiSettings.setAllGesturesEnabled(true);
	    if(context==null)
	        context=(Context)getActivity();
	    mSensorEventHelper = new SensorEventHelper(context);
		if (mSensorEventHelper != null) {
			mSensorEventHelper.registerSensorListener();
		}
	    
		cameraUpdate=CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng1,zoom,0,0));
        aMap.moveCamera(cameraUpdate);       //初始化到相应的位置
        aMap.invalidate();

        mNearbySearch=NearbySearch.getInstance(context);
        mNearbySearch.startUploadNearbyInfoAuto(new UploadInfoCallback() {
        	//设置自动上传数据和上传的间隔时间
        	@Override
        	public UploadInfo OnUploadInfoCallback() {
        	       UploadInfo loadInfo = new UploadInfo();
        	       loadInfo.setCoordType(NearbySearch.AMAP);
        	       //位置信息
        	       loadInfo.setPoint(latlng);
        	       //用户id信息
                   loadInfo.setUserID((String)MyUser.getObjectByKey("objectId")); 
  
        	       return loadInfo;
        	}
        	},10000);
        
        mNearbySearch.addNearbyListener(this);
      //设置搜索条件
        query = new NearbyQuery();
        //设置搜索的中心点
        query.setCenterPoint(new LatLonPoint(lat, lon));
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        query.setRadius(500);
        //设置查询的时间
        query.setTimeRange(10000);
        //调用异步查询接口
        mNearbySearch.searchNearbyInfoAsyn(query);
        
        timer=new Timer();     //这里是搜查10s，每10s搜查一次
		task=new TimerTask(){
	 		@Override
		 		public void run() 
	 		     
		 		{   int daojishi=10;     //倒计时刷新
	 			    daojishi--;
		 		    if(daojishi==0)
		 			{   
		 		    	if(mNearbySearch!=null){
						query.setCenterPoint(new LatLonPoint(searchLatLng.latitude, searchLatLng.longitude));
					    mNearbySearch.searchNearbyInfoAsyn(query);
	 			        daojishi=10;
                          chucuoonce=0;
		    	}
                
	 			}
	 		}
	 	};
	   timer.schedule(task, 1000,1000);
        
	    zujiBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(zuji==true){

					zuji=false;
					zujiBtn.setBackgroundResource(R.drawable.shoenoprint);
					String objectId=(String)MyUser.getObjectByKey("objectId");
					if(objectId!=null){
						mNearbySearch.stopUploadNearbyInfoAuto();
						NearbySearch.getInstance(context).setUserID(objectId);
						NearbySearch.getInstance(context).clearUserInfoAsyn();
						Toast.makeText(context, "当前状态:去掉地理位置信息，停止上传地理位置信息", Toast.LENGTH_LONG).show();
                      }
				}else if(zuji==false){
					zuji=true;
					zujiBtn.setBackgroundResource(R.drawable.shoeprints);
					mNearbySearch.startUploadNearbyInfoAuto(new UploadInfoCallback() {
			            	//设置自动上传数据和上传的间隔时间
			            	@Override
			            	public UploadInfo OnUploadInfoCallback() {
			            	       UploadInfo loadInfo = new UploadInfo();
			            	       loadInfo.setCoordType(NearbySearch.AMAP);
			            	       //位置信息
			            	       loadInfo.setPoint(latlng);
			            	       //用户id信息
			                       loadInfo.setUserID((String)MyUser.getObjectByKey("objectId")); 
			            	       return loadInfo;
			            	}
			            	},10000);
					
					Toast.makeText(context, "当前状态:上传地理位置信息", Toast.LENGTH_SHORT).show();
					
					
				}
				
			}
		});
		
	    locButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{    lat=pre.getFloat("lat", 39);
				 lon=pre.getFloat("lon", 116);
				 zoom=pre.getFloat("zoom",18);
				 latLng1=new LatLng(lat, lon);
				 cameraUpdate=CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng1,zoom,0,0));
                 aMap.animateCamera(cameraUpdate);
                 aMap.invalidate();
			}
		});
	   refreshBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			query.setCenterPoint(new LatLonPoint(searchLatLng.latitude, searchLatLng.longitude));
		    mNearbySearch.searchNearbyInfoAsyn(query);
			RotateAnimation animation=new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(1000);
			refreshBtn.startAnimation(animation);
		}
	});
	    
	

	return view;
		
	}
	  
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		
	    AMapLocationClientOption mLocationClientOption;
		
		if (mLocationClient == null) {
		mLocationClient=new AMapLocationClient(context);
		mLocationClientOption=new AMapLocationClientOption();
		mLocationClient.setLocationListener(this);
		
		mLocationClientOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		mLocationClientOption.setInterval(2000);
		mLocationClientOption.setMockEnable(true);
		mLocationClient.setLocationOption(mLocationClientOption);
		mLocationClient.startLocation();
		}
	}
	@Override
	public void deactivate() {
		mListener = null;
		if (mLocationClient != null) {
			mLocationClient.stopLocation();
			mLocationClient.onDestroy();
		}
		mLocationClient = null;
			
	}
	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		    LatLng yuanLatLng = null;   //原来的LatLng,即搜寻
		      if(yuanLatLng==null){
	        	  yuanLatLng=cameraPosition.target;
	          }
	          searchLatLng=cameraPosition.target;
	          if(AMapUtils.calculateLineDistance(yuanLatLng, searchLatLng)>1000)
	          {   yuanLatLng=searchLatLng;
	        	  query.setCenterPoint(new LatLonPoint(searchLatLng.latitude, searchLatLng.longitude));
			      mNearbySearch.searchNearbyInfoAsyn(query);
	          }
	
		   
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		
		if(yonghuDataView==null){
		    addYonghuDataView(marker);
			}else {
			    fuzhiMap.removeView(yonghuDataView);
			    addYonghuDataView(marker);
			}
			return false; 
	}
	
	private boolean cunzai=false;
	@Override
	public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
		 if(resultCode == 1000){
			    
			    if (nearbySearchResult != null
			        && nearbySearchResult.getNearbyInfoList() != null
			        && nearbySearchResult.getNearbyInfoList().size() > 0)
			    	
			    {   
			    	if(benyonghucunzai(nearbySearchResult.getNearbyInfoList())==1){
			    		  yonghuString.setText("当前附近用户有:"+(nearbySearchResult.getTotalNum()-1));
			    	}else{
			    	
			    		yonghuString.setText("当前附近用户有:"+(nearbySearchResult.getTotalNum()));
			    	}
			    	
			    	
			    	
			        for (int i = 0; i < nearbySearchResult.getNearbyInfoList().size(); i++) 
			       {  
			            cunzai=false;
			        	if(!nearbySearchResult.getNearbyInfoList().get(i).getUserID().equals((String)MyUser.getCurrentUser().getObjectId()))  {
			                List<String> list=yongbDb.loadObjectId();  //查询数据表中所有的objectId
			                if(list.size()==0){
			                	yongbDb.saveObjectIdandlatlon(nearbySearchResult.getNearbyInfoList().get(i).getUserID(), nearbySearchResult.getNearbyInfoList().get(i).getPoint().getLatitude()+"",  nearbySearchResult.getNearbyInfoList().get(i).getPoint().getLongitude()+"");
			                  
			                }else {
			                	for (int j = 0; j < list.size(); j++) {
									   if(nearbySearchResult.getNearbyInfoList().get(i).getUserID().equals(list.get(j))){
										   cunzai=true;
										   yongbDb.updateData(nearbySearchResult.getNearbyInfoList().get(i).getUserID(), nearbySearchResult.getNearbyInfoList().get(i).getPoint().getLatitude()+"",  nearbySearchResult.getNearbyInfoList().get(i).getPoint().getLongitude()+"");
									   }
									  
								  }
			                	 if(!cunzai){
			                			yongbDb.saveObjectIdandlatlon(nearbySearchResult.getNearbyInfoList().get(i).getUserID(), nearbySearchResult.getNearbyInfoList().get(i).getPoint().getLatitude()+"",  nearbySearchResult.getNearbyInfoList().get(i).getPoint().getLongitude()+"");
					              }
							}
			                
			             }
			       }    
			       
			            addSanMarker1();
			     
			   } 
			        else if(nearbySearchResult.getTotalNum()==0){
			              
			                     yonghuString.setText("当前附近用户有:"+(nearbySearchResult.getTotalNum()));
			                     
			                  
			              }
			}
			     else if(chucuoonce==0){
			             Toast.makeText(context,"周边搜索出现异常，异常码为："+resultCode ,Toast.LENGTH_LONG).show();
			             chucuoonce=1;
			         }

		
		
	}
	@Override
	public void onNearbyInfoUploaded(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onUserInfoCleared(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLocationChanged(AMapLocation amaplocation) {
		if(amaplocation!=null)
		  {  
			
			if(amaplocation.getErrorCode()==0)
			   {
			    lat=amaplocation.getLatitude();
			    lon=amaplocation.getLongitude();	
				editor.putFloat("lat",(float) lat);
				editor.putFloat("lon", (float)lon);
				editor.putFloat("zoom", aMap.getCameraPosition().zoom);
				editor.commit();
				latlng=new LatLonPoint(lat, lon);
				latLng1=new LatLng(lat, lon);
				if (!mFirstFix) {
					mFirstFix = true;
					addCircle(latLng1, amaplocation.getAccuracy());//添加定位精度圆
					addMarker(latLng1);//添加定位图标
					mSensorEventHelper.setCurrentMarker(mLocMarker);//定位图标旋转
	
				} else {
					mCircle.setCenter(latLng1);
					mCircle.setRadius(amaplocation.getAccuracy());
					mLocMarker.setPosition(latLng1);
				    }
			   }
			else if(chucuoonce==0)
		     	{
			  	Toast.makeText(context, "errorcode:"+amaplocation.getErrorCode()+",errorInfo:"+amaplocation.getErrorInfo(), Toast.LENGTH_LONG).show();
				chucuoonce=1;
		     	}
		  }
		
		
		
	}
	private void addCircle(LatLng latlng, double radius) {
		final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
		 final int FILL_COLOR = Color.argb(10, 0, 0, 180);
		 
		CircleOptions options = new CircleOptions();
		options.strokeWidth(1f);
		options.fillColor(FILL_COLOR);
		options.strokeColor(STROKE_COLOR);
		options.center(latlng);
		options.radius(radius);
		mCircle = aMap.addCircle(options);
	}
	private void addMarker(LatLng latlng) {
		if(mLocMarker!=null){
			mLocMarker.setPosition(latlng);
		}

		BitmapFactory.Options options1=new BitmapFactory.Options();
		options1.inSampleSize=2;
		Bitmap bitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.zhuanxiang, options1);
		BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bitmap);
		
		MarkerOptions options = new MarkerOptions();
		options.icon(des);
		options.anchor(0.5f, 0.5f);
		options.position(latlng);
		mLocMarker = aMap.addMarker(options);
	}
	private void addLoveding(CameraPosition cameraPosition){
		if(mLoveMarker!=null){
			mLoveMarker.setPosition(cameraPosition.target);
		}
		else {
		BitmapFactory.Options options1=new BitmapFactory.Options();
		options1.inSampleSize=2;
		Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.loveding, options1);
		BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bitmap);
		MarkerOptions options = new MarkerOptions();
		options.icon(des);
		options.anchor(0.5f, 1f);
		options.position(cameraPosition.target);
		mLoveMarker=aMap.addMarker(options);
		}
	}
	String nizhen;
	String touXiang;
	String userName;
	private void addYonghuDataView(final Marker marker){
	      
	      RelativeLayout fujinData;
          yonghuDataView=LayoutInflater.from(context).inflate(R.layout.fujin_yonghu_data, null);
		  fujinData=(RelativeLayout)yonghuDataView.findViewById(R.id.fujin_relative);
		  RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	      layoutParams.addRule(RelativeLayout.ABOVE, R.id.refreshbtn);
	      layoutParams.setMarginStart(Utility.dip2px(context, 15));
	      layoutParams.setMarginEnd(Utility.dip2px(context, 15));
	      fujinData.setLayoutParams(layoutParams);
		  ImageView imageView=(ImageView)fujinData.findViewById(R.id.yonghu_data_image);
		  ImageView imageCha=(ImageView)fujinData.findViewById(R.id.cha);
		  ImageView imageTalk=(ImageView)fujinData.findViewById(R.id.chattu);
		  ImageView callTo=(ImageView)fujinData.findViewById(R.id.calltu);
		  
		  BitmapDescriptor descriptor=marker.getIcons().get(0);
		  imageView.setImageBitmap(descriptor.getBitmap());
		  
		  Bundle bundle=new Bundle(yongbDb.loadData((String) marker.getObject()));
		  if(bundle!=null){
			  nizhen=bundle.getString("nickName");
			  String sex=bundle.getString("sex");
			  String age=bundle.getString("age");
			  String zhiye=bundle.getString("zhiye");
			  touXiang=bundle.getString("touxiangUrl");
			  userName=bundle.getString("userName");
			  TextView nizhenText=(TextView)fujinData.findViewById(R.id.yonghu_data_nick);
			  TextView sexText=(TextView)fujinData.findViewById(R.id.yonghu_data_sex);
			  TextView ageText=(TextView)fujinData.findViewById(R.id.yonghu_data_age);
			  TextView zhiyeText=(TextView)fujinData.findViewById(R.id.yonghu_data_zhiye);
            nizhenText.setText(nizhen);
            sexText.setText(sex);
            ageText.setText(age);
            zhiyeText.setText(zhiye);
		  }
		 // final BmobIMUserInfo bmobIMUserInfo=;
		  imageCha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(yonghuDataView!=null){
					fuzhiMap.removeView(yonghuDataView);
					yonghuDataView=null;
				}
				
			}
		  });
		  imageTalk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int i=fragmentChat.converdb.getIsFriend((String) marker.getObject());
				if(i==0||i==1){
					final BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
					bmobIMUserInfo.setUserId((String) marker.getObject());
					bmobIMUserInfo.setName(nizhen);
					bmobIMUserInfo.setAvatar(touXiang);
			     	BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo ,new ConversationListener() {
						
						@Override
						public void done(BmobIMConversation c, BmobException e) {
							if(e==null){
								Bundle bundle=new Bundle();
								bundle.putSerializable("c",c);
								bundle.putSerializable("userInfo",bmobIMUserInfo);
								Intent intent=new Intent(getActivity(),ChatActivity.class);
								intent.putExtra("bundle", bundle);
								startActivity(intent);
								
							}else {
                                   Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
							}
							
						}
					});
				
			
				}else if(i==2){
					 Toast.makeText(getActivity(),"对方已加入黑名单",Toast.LENGTH_SHORT).show();
				  }else if(i==3){
					  Toast.makeText(getActivity(),"对方拒绝接收",Toast.LENGTH_SHORT).show();
				  }
				
			}
		  });
		  
		  callTo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int i=fragmentChat.converdb.getIsFriend((String) marker.getObject());
				if(i==0||i==1){
					if(loginAct.isMobileNO(userName)){
						Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+userName));
						startActivity(intent);
					}else {
						  Toast.makeText(getActivity(),"对方未留手机号",Toast.LENGTH_SHORT).show();
					}
				}else if(i==2){
					  Toast.makeText(getActivity(),"对方已加入黑名单",Toast.LENGTH_SHORT).show();
				 }else if(i==3){
					  Toast.makeText(getActivity(),"对方拒绝接收",Toast.LENGTH_SHORT).show();
				 }
				
			}
		  });
		  ImageView jiaImage=(ImageView)fujinData.findViewById(R.id.jiatu);
		  jiaImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int i=fragmentChat.converdb.getIsFriend((String) marker.getObject());
				if(i==0){  //只有当对方是陌生人的时候才可以添加
			  BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();   //这里出现的问题把我折磨死了，我查了好久，折腾好久，原来就是String的nizhen没办法加在setName中
			  bmobIMUserInfo.setUserId((String) marker.getObject());
			  bmobIMUserInfo.setAvatar(yongbDb.loadUserTouxiangUrl((String)marker.getObject()));
			  if(nizhen==null||nizhen.equals("")){
				  bmobIMUserInfo.setName((String) marker.getObject());
			  }else{
			     bmobIMUserInfo.setName(nizhen);
			  }
			  sendAddFriendMessage(bmobIMUserInfo);
				}else if(i==1){
					Toast.makeText(context, "对方已经是您的好友，无需添加", Toast.LENGTH_SHORT).show();
				}else if(i==2){
					Toast.makeText(context, "对方已加入黑名单", Toast.LENGTH_SHORT).show();
				}else if(i==3){
					Toast.makeText(context, "对方拒绝了您的请求", Toast.LENGTH_SHORT).show();
				}
			}
		});
		  fuzhiMap.addView(fujinData);
    }

    private void sendAddFriendMessage(BmobIMUserInfo bmobIMUserInfo){    //发送添加好友请求
   	 BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo, true,new ConversationListener() {
			
			@Override
			public void done(BmobIMConversation arg0, BmobException e) {
				if(e==null){
					
				}else {
					Toast.makeText(context, "失败，"+e.getMessage(),Toast.LENGTH_SHORT).show();

				}
				
			}
		});
   
   		 BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
	
   	 AddFriendMessage msg=new AddFriendMessage();
        MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
        msg.setContent("很高兴认识你，可以加个好友吗？");
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name", currentUser.getNick());
        map.put("avatar", currentUser.getTouXiangUrl());
        map.put("uid", currentUser.getObjectId());
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
			
			@Override
			public void done(BmobIMMessage msg, BmobException e) {
				if(e==null){
					Toast.makeText(context, "好友请求发送成功，等待验证",Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(context, "发送失败，"+e.getMessage(),Toast.LENGTH_SHORT).show();

				}
				
			}
		});
    }
    private int benyonghucunzai(List<NearbyInfo> list){   //返回1的时候表示有本用户
   	 for (int i = 0; i < list.size(); i++) {
			   if(list.get(i).getUserID().equals((String)MyUser.getCurrentUser().getObjectId())){
				   return 1;
			   }
			
		}
   	  return 0;     //返回0的时候表示没有本用户
   	 
    }
	private  void addSanMarker1() 
	{    
		
		List<String>  objectIdList=yongbDb.loadobjectIdWhereUrlemp();//加载没有Url的objectId
	    if(objectIdList.size()>0){
	    	for (int i = 0; i < objectIdList.size(); i++) {
	    		BmobQuery<MyUser> query=new BmobQuery<MyUser>();
	    		query.getObject(objectIdList.get(i), new QueryListener<MyUser>() {
					
					@Override
					public void done(MyUser object, BmobException e) {
						if(e==null){
							yongbDb.saveUserNameandTouxiangUrl(object.getObjectId(), object.getNick(),object.getTouXiangUrl());
							yongbDb.saveData(object.getObjectId(),object.getNick(),object.getSex(), object.getAge(), object.getZhiye(),object.getUsername());
							if(object.getTouXiangUrl()!=null){      //当存在有相应的Url值
							     checkJiaZai(object.getObjectId(),object.getTouXiangUrl());
							}
							 else {
								 if(!markXianShi(object.getObjectId()))  {//这里修改为了判断mark是否加载
							 	    addSanMarkerDir(object.getObjectId(),null);     //如果touxiangUrl为空的话，直接加载
								 }else{                                         //如果等于1，就是加载完毕，这个设置它最新的地理位置坐标                    
									for (int j = 0; j < zmarkNum; j++) {
										if(mFujinMarker.get(j).getObject().equals(object.getObjectId())){
											double la=yongbDb.loadLatbyId(object.getObjectId())[0];
											double lo=yongbDb.loadLatbyId(object.getObjectId())[1];
											mFujinMarker.get(j).setPosition(new LatLng(la,lo));
										}
									} 
								   }
							 }
						}else if(e.getErrorCode()!=9015){
							Toast.makeText(context,"失败，"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(context,"失败，"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			   
			}
	    }else {             //修正:当url都存在的时候，也无法判断它是否已经在地图上加载显示了，先判断是否加载显示，再设置地理位置
	    	List<String> obList=new ArrayList<String>();
			obList=yongbDb.loadObjectId();
			boolean cun=false;
	    	for (int i = 0; i < obList.size(); i++) {
	    		cun=false;
				for (int j = 0; j < zmarkNum; j++) {
					if(obList.get(i).equals(mFujinMarker.get(j).getObject())&&mFujinMarker.get(j).isVisible()){
						double la=yongbDb.loadLatbyId(obList.get(i))[0];
						double lo=yongbDb.loadLatbyId(obList.get(i))[1];
						mFujinMarker.get(j).setPosition(new LatLng(la,lo));
						cun=true;
					}
				}
				if(!cun){
					JiaZai(obList.get(i), yongbDb.loadUserTouxiangUrl(obList.get(i)));
				}
			}
		}
	
	   
		
	}
    
	   private void checkJiaZai(String obString,String touxiangUrl){
		   if(!markXianShi(obString)){
			   JiaZai(obString, touxiangUrl);
		   }else {
				for (int j = 0; j < zmarkNum; j++) {
					if(mFujinMarker.get(j).getObject().equals(obString)){
						double la=yongbDb.loadLatbyId(obString)[0];
						double lo=yongbDb.loadLatbyId(obString)[1];
						mFujinMarker.get(j).setPosition(new LatLng(la,lo));
					}
				} 
		}
	   }
	   public boolean markXianShi(String objectId){
		   boolean ti=false;
		   if(zmarkNum>0) {
			   for (int i = 0; i < zmarkNum; i++) {
					if(mFujinMarker.get(i).getObject().equals(objectId)){
						 if(mFujinMarker.get(i).isVisible())
							 ti=true;
					}
			   }
		   }
	    	return ti;
	   }
	   private void addSanMarkerDir(String objectId,Bitmap bitmap1){
		   Bitmap bitmap2;
		   if(bitmap1==null){
		        bitmap2=BitmapFactory.decodeResource(getResources(),R.drawable.userpicture);
		    }else {
				bitmap2=bitmap1;
			}
	                if((ViewGroup)userPicture1.getParent()!=null)
				        ((ViewGroup)userPicture1.getParent()).removeView(userPicture1);
				    userPicture1.setImageBitmap(bitmap2);
				    BitmapDescriptor descriptor=BitmapDescriptorFactory.fromView(userPicture1);
			    	MarkerOptions options=new MarkerOptions();
			    	options.icon(descriptor);
			    	options.anchor(0.5f, 1f);
			    	double [] yonghuLatlon=new double [2];
			    	yonghuLatlon=yongbDb.loadLatbyId(objectId);
			    	options.position(new LatLng(yonghuLatlon[0], yonghuLatlon[1]));
			    	mFujinMarker.add(zmarkNum, aMap.addMarker(options));
			    	mFujinMarker.get(zmarkNum).setObject(objectId);
			    	yongbDb.updateJiaZai1(objectId);
			    	zmarkNum++;
		   
	   }

	   private void JiaZai(final String obString,final String touxiangUrl){ 
	        File file=new File(Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+obString+".jpg_");
	        if(file.exists()){
            bitmap11=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+obString+".jpg_");
            addSanMarkerDir(obString,bitmap11);     
            new Thread(new Runnable() {
 	            @Override
					public void run() {
					     
				    	 bitmap11=Utility.getTouxiangBitmap(touxiangUrl, context,yongbDb);
				
				    	 if(bitmap11!=null){
					     	download.saveYonghuPic(bitmap11,obString);
						    bitmapNum++;
						    objStrings.add(obString);   //objStrings列表是加载了自己图片的用户的列表obj
						}	
					((Activity)context).runOnUiThread(new Runnable() {  
						
						@Override
						public void run() {
								addSanMarkerRefresh(obString,getYonghuPic(obString));
						}
					});
					}
				}).start();
                //有头像Url的objectId加载过了的话就设置position
            
	        }else {
	        	 new Thread(new Runnable() {
     	            @Override
						public void run() {
						     
					    	 bitmap11=Utility.getTouxiangBitmap(touxiangUrl, context,yongbDb);
					
					    	 if(bitmap11!=null){
						     	download.saveYonghuPic(bitmap11,obString);
							    bitmapNum++;
							    objStrings.add(obString);   //objStrings列表是加载了自己图片的用户的列表obj
							}	
						((Activity)context).runOnUiThread(new Runnable() {  //加载了所有bitmap就可以放在地图上了
							
							@Override
							public void run() {
									addSanMarkerDir(obString,getYonghuPic(obString));
							}
						});
						}
					}).start();
                    //有头像Url的objectId加载过了的话就设置position
			}
	       
			    
		    
	   }


		  public Bitmap getYonghuPic(String obj){
			  Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+obj+".jpg_");
			  return bitmap;
		  }
		  public void addSanMarkerRefresh(String objectId,Bitmap bitmap){
			   if(zmarkNum>0) {
				   for (int i = 0; i < zmarkNum; i++) {
						if(mFujinMarker.get(i).getObject().equals(objectId)){
							  if((ViewGroup)userPicture1.getParent()!=null)
							        ((ViewGroup)userPicture1.getParent()).removeView(userPicture1);
							    userPicture1.setImageBitmap(bitmap);
							    BitmapDescriptor descriptor=BitmapDescriptorFactory.fromView(userPicture1);
						    	mFujinMarker.get(i).setIcon(descriptor);
						}
				   }
			   }
		   }
		@Override
		public void onCameraChange(CameraPosition cameraPosition) {
			addLoveding(cameraPosition);
			
		}
		@Override
		public void onDestroy() {
			super.onDestroy();
			if (mMapView!=null) 
				  mMapView.onDestroy();
				mMapView=null;
				if(null != mLocationClient)
				{   mLocationClient.stopLocation();
					mLocationClient.onDestroy();
				}
	           zmarkNum=0;
	           NearbySearch.destroy();
	           if(timer!=null)
	              timer.cancel();
	           if(task!=null)
	              task.cancel();
	            Log.d("Main","onDestroy");
		}
		@Override
		public void onPause() {
			super.onPause();
			if(mMapView!=null)
			   mMapView.onPause();
			if (mSensorEventHelper != null) {
				mSensorEventHelper.unRegisterSensorListener();
				mSensorEventHelper.setCurrentMarker(null);
				mSensorEventHelper = null;
			}
			deactivate();
			mFirstFix=false;

		}

		@Override
		public void onResume() {
			super.onResume();
			if(mMapView!=null)
			  mMapView.onResume();
			if (mSensorEventHelper != null) {
				mSensorEventHelper.registerSensorListener();
			}
		}

		
		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			if(mMapView!=null)
			  mMapView.onSaveInstanceState(outState);
		}
	
	      
	
}
