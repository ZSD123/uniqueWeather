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
	private volatile int zmarkNum=0;//ֱ������û�ͷ���Marker����
    public static RelativeLayout fuzhiMap;
    private ImageButton zujiBtn;
    
	public static  yonghuDB yongbDb;  //��Χ�û����ݱ�
    private Circle mCircle;
    public double lat;
    public double lon;
    
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pre;
    
    public float zoom;
    private LatLng latLng1;
    public static MapView mMapView;
    
	private Marker mLocMarker;    //��λMarker
	private TextView yonghuString;
	private ImageButton refreshBtn;  //��ͼˢ�°�ť
	  public static AMap aMap;
	
	  public NearbySearch mNearbySearch;
	  private SensorEventHelper mSensorEventHelper;
	  
	  public static MapCircleImageView userPicture1;  //��ͼ��userPicture
    
	  private static Context context;
	  private LatLonPoint latlng;
	  public static CameraUpdate cameraUpdate;
	  public NearbyQuery query;  
      
      public int bitmapNum=0;       //�����û�ͷ�ĸ���
      private Timer timer;
      private TimerTask task;
      public List<String> objStrings =new ArrayList<String>();//���ع������û���obj
      public LatLng searchLatLng;   //ͬ��
      public volatile int chucuoonce=0;
  	  private boolean mFirstFix = false;
	  private OnLocationChangedListener mListener;
      private boolean zuji=true;
      public AMapLocationClient mLocationClient;
      private Marker mLoveMarker;    //��ǰMarker
      public static View  yonghuDataView;  //�����û����Ͽ�View
      private List<Marker> mFujinMarker=new ArrayList<Marker>(); //û������ͷ��ֱ�����ͷ��ĸ����û�Marker
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
				Toast.makeText(context, "��ʼ��ʧ�ܣ����˳�������һ��", Toast.LENGTH_LONG).show();
		}
		else {
			aMap.clear();
			aMap=mMapView.getMap();     // FragmentǶ�׸ߵµ�ͼ�����ٴν���Fragment��ʱ��                           //�������ֵ�����Ƕ�׵ĵ�ͼ������޷���λ������                           //���������ֵ�ԭ�����ڣ�fragment�ڱ��Ƴ�ʱ������ִ��onDestroy��������������ִ��onDestroyView����������fragment�е������Ѿ��ڵ�һ�β���ʱ����˳�ʼ���ˣ��������´����У�aMap��Ϊnull,�޷�ִ��getMap()������ʼ�������޷�����������λ���ܡ�
			if(aMap==null)
				Toast.makeText(context, "��ʼ��ʧ�ܣ����˳�������һ��", Toast.LENGTH_LONG).show();
		   }
	    aMap.setLocationSource(this);
	    aMap.setMyLocationEnabled(true); 
	    aMap.setOnCameraChangeListener(this);
	    aMap.setOnMarkerClickListener(this);
	    
	    UiSettings uiSettings;     //AMap������
	    
	    uiSettings=aMap.getUiSettings();
	    uiSettings.setAllGesturesEnabled(true);
	    if(context==null)
	        context=(Context)getActivity();
	    mSensorEventHelper = new SensorEventHelper(context);
		if (mSensorEventHelper != null) {
			mSensorEventHelper.registerSensorListener();
		}
	    
		cameraUpdate=CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng1,zoom,0,0));
        aMap.moveCamera(cameraUpdate);       //��ʼ������Ӧ��λ��
        aMap.invalidate();

        mNearbySearch=NearbySearch.getInstance(context);
        mNearbySearch.startUploadNearbyInfoAuto(new UploadInfoCallback() {
        	//�����Զ��ϴ����ݺ��ϴ��ļ��ʱ��
        	@Override
        	public UploadInfo OnUploadInfoCallback() {
        	       UploadInfo loadInfo = new UploadInfo();
        	       loadInfo.setCoordType(NearbySearch.AMAP);
        	       //λ����Ϣ
        	       loadInfo.setPoint(latlng);
        	       //�û�id��Ϣ
                   loadInfo.setUserID((String)MyUser.getObjectByKey("objectId")); 
  
        	       return loadInfo;
        	}
        	},10000);
        
        mNearbySearch.addNearbyListener(this);
      //������������
        query = new NearbyQuery();
        //�������������ĵ�
        query.setCenterPoint(new LatLonPoint(lat, lon));
        //����������������ϵ
        query.setCoordType(NearbySearch.AMAP);
        //���������뾶
        query.setRadius(500);
        //���ò�ѯ��ʱ��
        query.setTimeRange(10000);
        //�����첽��ѯ�ӿ�
        mNearbySearch.searchNearbyInfoAsyn(query);
        
        timer=new Timer();     //�������Ѳ�10s��ÿ10s�Ѳ�һ��
		task=new TimerTask(){
	 		@Override
		 		public void run() 
	 		     
		 		{   int daojishi=10;     //����ʱˢ��
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
						Toast.makeText(context, "��ǰ״̬:ȥ������λ����Ϣ��ֹͣ�ϴ�����λ����Ϣ", Toast.LENGTH_LONG).show();
                      }
				}else if(zuji==false){
					zuji=true;
					zujiBtn.setBackgroundResource(R.drawable.shoeprints);
					mNearbySearch.startUploadNearbyInfoAuto(new UploadInfoCallback() {
			            	//�����Զ��ϴ����ݺ��ϴ��ļ��ʱ��
			            	@Override
			            	public UploadInfo OnUploadInfoCallback() {
			            	       UploadInfo loadInfo = new UploadInfo();
			            	       loadInfo.setCoordType(NearbySearch.AMAP);
			            	       //λ����Ϣ
			            	       loadInfo.setPoint(latlng);
			            	       //�û�id��Ϣ
			                       loadInfo.setUserID((String)MyUser.getObjectByKey("objectId")); 
			            	       return loadInfo;
			            	}
			            	},10000);
					
					Toast.makeText(context, "��ǰ״̬:�ϴ�����λ����Ϣ", Toast.LENGTH_SHORT).show();
					
					
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
		    LatLng yuanLatLng = null;   //ԭ����LatLng,����Ѱ
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
			    		  yonghuString.setText("��ǰ�����û���:"+(nearbySearchResult.getTotalNum()-1));
			    	}else{
			    	
			    		yonghuString.setText("��ǰ�����û���:"+(nearbySearchResult.getTotalNum()));
			    	}
			    	
			    	
			    	
			        for (int i = 0; i < nearbySearchResult.getNearbyInfoList().size(); i++) 
			       {  
			            cunzai=false;
			        	if(!nearbySearchResult.getNearbyInfoList().get(i).getUserID().equals((String)MyUser.getCurrentUser().getObjectId()))  {
			                List<String> list=yongbDb.loadObjectId();  //��ѯ���ݱ������е�objectId
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
			              
			                     yonghuString.setText("��ǰ�����û���:"+(nearbySearchResult.getTotalNum()));
			                     
			                  
			              }
			}
			     else if(chucuoonce==0){
			             Toast.makeText(context,"�ܱ����������쳣���쳣��Ϊ��"+resultCode ,Toast.LENGTH_LONG).show();
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
					addCircle(latLng1, amaplocation.getAccuracy());//��Ӷ�λ����Բ
					addMarker(latLng1);//��Ӷ�λͼ��
					mSensorEventHelper.setCurrentMarker(mLocMarker);//��λͼ����ת
	
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
					 Toast.makeText(getActivity(),"�Է��Ѽ��������",Toast.LENGTH_SHORT).show();
				  }else if(i==3){
					  Toast.makeText(getActivity(),"�Է��ܾ�����",Toast.LENGTH_SHORT).show();
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
						  Toast.makeText(getActivity(),"�Է�δ���ֻ���",Toast.LENGTH_SHORT).show();
					}
				}else if(i==2){
					  Toast.makeText(getActivity(),"�Է��Ѽ��������",Toast.LENGTH_SHORT).show();
				 }else if(i==3){
					  Toast.makeText(getActivity(),"�Է��ܾ�����",Toast.LENGTH_SHORT).show();
				 }
				
			}
		  });
		  ImageView jiaImage=(ImageView)fujinData.findViewById(R.id.jiatu);
		  jiaImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int i=fragmentChat.converdb.getIsFriend((String) marker.getObject());
				if(i==0){  //ֻ�е��Է���İ���˵�ʱ��ſ������
			  BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();   //������ֵ����������ĥ���ˣ��Ҳ��˺þã����ںþã�ԭ������String��nizhenû�취����setName��
			  bmobIMUserInfo.setUserId((String) marker.getObject());
			  bmobIMUserInfo.setAvatar(yongbDb.loadUserTouxiangUrl((String)marker.getObject()));
			  if(nizhen==null||nizhen.equals("")){
				  bmobIMUserInfo.setName((String) marker.getObject());
			  }else{
			     bmobIMUserInfo.setName(nizhen);
			  }
			  sendAddFriendMessage(bmobIMUserInfo);
				}else if(i==1){
					Toast.makeText(context, "�Է��Ѿ������ĺ��ѣ��������", Toast.LENGTH_SHORT).show();
				}else if(i==2){
					Toast.makeText(context, "�Է��Ѽ��������", Toast.LENGTH_SHORT).show();
				}else if(i==3){
					Toast.makeText(context, "�Է��ܾ�����������", Toast.LENGTH_SHORT).show();
				}
			}
		});
		  fuzhiMap.addView(fujinData);
    }

    private void sendAddFriendMessage(BmobIMUserInfo bmobIMUserInfo){    //������Ӻ�������
   	 BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo, true,new ConversationListener() {
			
			@Override
			public void done(BmobIMConversation arg0, BmobException e) {
				if(e==null){
					
				}else {
					Toast.makeText(context, "ʧ�ܣ�"+e.getMessage(),Toast.LENGTH_SHORT).show();

				}
				
			}
		});
   
   		 BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
	
   	 AddFriendMessage msg=new AddFriendMessage();
        MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
        msg.setContent("�ܸ�����ʶ�㣬���ԼӸ�������");
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name", currentUser.getNick());
        map.put("avatar", currentUser.getTouXiangUrl());
        map.put("uid", currentUser.getObjectId());
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
			
			@Override
			public void done(BmobIMMessage msg, BmobException e) {
				if(e==null){
					Toast.makeText(context, "���������ͳɹ����ȴ���֤",Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(context, "����ʧ�ܣ�"+e.getMessage(),Toast.LENGTH_SHORT).show();

				}
				
			}
		});
    }
    private int benyonghucunzai(List<NearbyInfo> list){   //����1��ʱ���ʾ�б��û�
   	 for (int i = 0; i < list.size(); i++) {
			   if(list.get(i).getUserID().equals((String)MyUser.getCurrentUser().getObjectId())){
				   return 1;
			   }
			
		}
   	  return 0;     //����0��ʱ���ʾû�б��û�
   	 
    }
	private  void addSanMarker1() 
	{    
		
		List<String>  objectIdList=yongbDb.loadobjectIdWhereUrlemp();//����û��Url��objectId
	    if(objectIdList.size()>0){
	    	for (int i = 0; i < objectIdList.size(); i++) {
	    		BmobQuery<MyUser> query=new BmobQuery<MyUser>();
	    		query.getObject(objectIdList.get(i), new QueryListener<MyUser>() {
					
					@Override
					public void done(MyUser object, BmobException e) {
						if(e==null){
							yongbDb.saveUserNameandTouxiangUrl(object.getObjectId(), object.getNick(),object.getTouXiangUrl());
							yongbDb.saveData(object.getObjectId(),object.getNick(),object.getSex(), object.getAge(), object.getZhiye(),object.getUsername());
							if(object.getTouXiangUrl()!=null){      //����������Ӧ��Urlֵ
							     checkJiaZai(object.getObjectId(),object.getTouXiangUrl());
							}
							 else {
								 if(!markXianShi(object.getObjectId()))  {//�����޸�Ϊ���ж�mark�Ƿ����
							 	    addSanMarkerDir(object.getObjectId(),null);     //���touxiangUrlΪ�յĻ���ֱ�Ӽ���
								 }else{                                         //�������1�����Ǽ�����ϣ�������������µĵ���λ������                    
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
							Toast.makeText(context,"ʧ�ܣ�"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(context,"ʧ�ܣ�"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			   
			}
	    }else {             //����:��url�����ڵ�ʱ��Ҳ�޷��ж����Ƿ��Ѿ��ڵ�ͼ�ϼ�����ʾ�ˣ����ж��Ƿ������ʾ�������õ���λ��
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
						    objStrings.add(obString);   //objStrings�б��Ǽ������Լ�ͼƬ���û����б�obj
						}	
					((Activity)context).runOnUiThread(new Runnable() {  
						
						@Override
						public void run() {
								addSanMarkerRefresh(obString,getYonghuPic(obString));
						}
					});
					}
				}).start();
                //��ͷ��Url��objectId���ع��˵Ļ�������position
            
	        }else {
	        	 new Thread(new Runnable() {
     	            @Override
						public void run() {
						     
					    	 bitmap11=Utility.getTouxiangBitmap(touxiangUrl, context,yongbDb);
					
					    	 if(bitmap11!=null){
						     	download.saveYonghuPic(bitmap11,obString);
							    bitmapNum++;
							    objStrings.add(obString);   //objStrings�б��Ǽ������Լ�ͼƬ���û����б�obj
							}	
						((Activity)context).runOnUiThread(new Runnable() {  //����������bitmap�Ϳ��Է��ڵ�ͼ����
							
							@Override
							public void run() {
									addSanMarkerDir(obString,getYonghuPic(obString));
							}
						});
						}
					}).start();
                    //��ͷ��Url��objectId���ع��˵Ļ�������position
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
