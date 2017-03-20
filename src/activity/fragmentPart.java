package activity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import myCustomView.CircleImageView;
import myCustomView.MapCircleImageView;
import service.autoUqdateService;
import Util.Http;
import Util.HttpCallbackListener;
import Util.SensorEventHelper;
import Util.Utility;
import Util.download;
import android.R.anim;
import android.app.Activity;
import android.app.Notification.MessagingStyle.Message;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.mapcore2d.db;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
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
import com.amap.api.services.nearby.NearbySearch.NearbyQuery;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.uniqueweather.app.R;

import db.yonghuDB;
public  class fragmentPart extends Fragment implements  AMapLocationListener, LocationSource, NearbyListener,OnCameraChangeListener
{   public static String keyToGet="begin";
    private String theKey;
	private static TextView time;
	private static TextView realtime;
	private static RelativeLayout weather_layout;
	private static TextView weather;
	private static TextView temper;
	public static CircleImageView userPicture;
	public static MapCircleImageView userPicture1;  //地图上userPicture
	
	private TextView myCity;
    private Button button_refresh;
    public static String countyName;
    private static ImageView pic;
    private static TextView userName;
    private static TextView countyname;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pre;
    private String accountName;
    private View view;
    private String uriUserPicture;
    private static Bitmap bitmap;
    private static Bitmap bitmap1;
    public Context context;
    public Button button1;
    public MyHorizontalView horizontalView;
    public Button button2;     //退出登录Button
    
    public String  yonghuUrl;   //其他用户url    
    public int yonghuNum=0;      //加载头像Url组的
    public Bitmap bitmaptou[]=new Bitmap[1000];    //其他用户头
    public int bitmapNum=0;       //其他用户头的个数
    
    public Handler handler;     
    
    public UiSettings uiSettings;     //AMap的设置
    public  static LocationManager locationManager;
    public static MapView mMapView;
    public static AMap aMap;
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationClientOption;
    public static CameraUpdate cameraUpdate;
    private ImageButton locButton;
    private LatLonPoint latlng;
    private LatLng latLng1;
    public float zoom;
    public int chucuoonce=0;
    public double lat;
    public double lon;
    public LatLng yuanLatLng;   //原来的LatLng,即搜寻
    public LatLng searchLatLng;   //同上
    public NearbyQuery query;  
    public NearbySearch mNearbySearch;
      
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
	private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private SensorEventHelper mSensorEventHelper;
	private Circle mCircle;
	private Marker mLocMarker;    //定位Marker
	private Marker [] mFujinMarker=new Marker[1000];;   //附近用户Marker
	private  List<String> urlList=new ArrayList<String>();;
	
	private int markNum=0;//Marker个数
	private Marker mLoveMarker;    //当前Marker
	private boolean mFirstFix = false;
	private String address="http://route.showapi.com/238-2";  //经纬度转化为地址
	private TextView myAccount;     //我的账户TextView 	
	private OnLocationChangedListener mListener;
	private String yuanLocation;
	
	private yonghuDB yongbDb;
	public fragmentPart(){
		
	}
	public static fragmentPart getInstance(Context context){
		fragmentPart fragmentPart=new fragmentPart();
		fragmentPart.context=context;
		return fragmentPart;
	}
  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
    {   
		if(getArguments()!=null)
		{
			theKey=getArguments().getString(keyToGet);
			
		}
		if(theKey.equals("weather"))
		{   
			view=inflater.inflate(R.layout.connection,container,false);
		    userName=(TextView)view.findViewById(R.id.userName);
			time=(TextView)view.findViewById(R.id.time);
	        userPicture=(CircleImageView)view.findViewById(R.id.userPicture);

	        realtime=(TextView)view.findViewById(R.id.realTime);
			weather_layout=(RelativeLayout)view.findViewById(R.id.weather_info);
			weather=(TextView)view.findViewById(R.id.weather);
			temper=(TextView)view.findViewById(R.id.temper);
			myCity=(TextView)view.findViewById(R.id.myCity);
			button_refresh=(Button)view.findViewById(R.id.refresh);
			countyname=(TextView)view.findViewById(R.id.countyName);
			pic=(ImageView)view.findViewById(R.id.weather_pic);
			button1=(Button)view.findViewById(R.id.button_map);
			button2=(Button)view.findViewById(R.id.button1);
			horizontalView=(MyHorizontalView)view.findViewById(R.id.horiView);
			myAccount=(TextView)view.findViewById(R.id.myAccount);
			
			if(context==null)
				context=(Context)getActivity();
			
			String nick=(String)BmobUser.getObjectByKey("nick");
		    String touxiangUrl=(String)BmobUser.getObjectByKey("touxiangUrl");
		    if(nick!=null){
		    	userName.setText(nick);
		    }else {
				Toast.makeText(context, "连接失败，请稍后再试", Toast.LENGTH_SHORT).show();
			}
			
			editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
			pre=PreferenceManager.getDefaultSharedPreferences(context);
			accountName=pre.getString("accountName","");
			if(!accountName.isEmpty())
			{Toast.makeText(context,accountName + ",欢迎您", Toast.LENGTH_SHORT).show();
			 userName.setText(accountName);
			}
			else 
			{
				userName.setText("未命名");
			}
			String userPictString=pre.getString("userPicture", "");
            if(!userPictString.equals("")){       //如果有保存头像路径，并且头像路径的file存在，就可以快速取材，并且设置图片
            	File file=new File(userPictString);
            	if(file.exists()){
            	    bitmap1=BitmapFactory.decodeFile(userPictString);
            		userPicture.setImageBitmap(bitmap1);
      
            	}
            }
			 if(touxiangUrl!=null)       //这里防止更新图片后另外一台客户端没有更新
				{  
				   BmobFile bmobFile=new BmobFile("头像"+".png",null,touxiangUrl);//确定文件名字（头像.png）和网络地址
				   download.downloadFile(bmobFile,context);//进行下载操作
				   
				}
		        countyName=pre.getString("locDistrict","");
				pic.setImageBitmap(getPicture());
			    countyname.setText(pre.getString("locCity","")+countyName);
				weather.setText(pre.getString("weatherInfo", ""));
				temper.setText(pre.getString("temperature", ""));
				realtime.setText(pre.getString("realtime", ""));
				String publishTime=pre.getString("time", "");
				char[]b=publishTime.toCharArray();
				    if(b.length>0)
				    { String c=""+b[0]+b[1]+b[2]+b[3]+"年"+b[4]+b[5]+"月"+b[6]+b[7]+"日"+" "+b[8]+b[9]+":"+b[10]+b[11]+":"+b[12]+b[13]+"发布";
				    time.setText(c);
				    }
				
		   
				button_refresh.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{   
					    Http.queryAreaByXY(pre.getFloat("lat", 0),pre.getFloat("lon",0), address, new HttpCallbackListener() {
						   @Override
						    public void onFinish(String response) {
						    Utility.handleAreaByXY(response, context);
						    queryWeather(context);
							   ((Activity) context).runOnUiThread(new Runnable(){
							    @Override
							    public void run(){
							    	countyname.setText(pre.getString("locCity","")+pre.getString("locDistrict",""));
							    	yuanLocation=pre.getString("locCity","")+pre.getString("locDistrict","");
				            }});
					       
						}
					  });
					    
					    Toast.makeText(getActivity(), "刷新中...", Toast.LENGTH_SHORT).show();
					}
				});
				userPicture.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View V) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
			            intent.setType("image/jpeg");
			            ((Activity)context).startActivityForResult(intent, 2);
					}
				});
				
				myCity.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(context,myCityAction.class);
						intent.putExtra("selectedCityName",pre.getString("locCity", "") );
						intent.putExtra("temp", pre.getString("temperature",""));
						startActivity(intent);
						
					}
				});
			    button1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					    	weather_info.mViewPager.setCurrentItem(1);
						
					}
				});
			    myAccount.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						Intent intent=new Intent(context,myAccountAct.class);
						Bundle data=new Bundle();
						data.putString("name",userName.getText().toString());
						intent.putExtras(data);
						startActivity(intent);
					}
				});
			    button2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
					    BmobUser.logOut();
						BmobUser currentUser=BmobUser.getCurrentUser();
						Intent intent=new Intent(context,loginAct.class);
						startActivity(intent);
						Activity activity=(Activity)context;
						activity.finish();
					}
				});
			    
		}
		else if(theKey.equals("map"))
		{   
			view=inflater.inflate(R.layout.map,container,false);
			lat=pre.getFloat("lat", 39);
			lon=pre.getFloat("lon",116);
			zoom=pre.getFloat("zoom", 18);
			latLng1=new LatLng(lat, lon);
			mMapView=(MapView)view.findViewById(R.id.map);
			mMapView.onCreate(savedInstanceState);
			locButton=(ImageButton)view.findViewById(R.id.locationButton);
			View mapView=LayoutInflater.from(context).inflate(R.layout.mapcircleimageview, null);
			userPicture1=(MapCircleImageView)mapView.findViewById(R.id.mapCircle);
			
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
            query.setRadius(1000);
            //设置查询的时间
            query.setTimeRange(10000);
            //调用异步查询接口
            mNearbySearch.searchNearbyInfoAsyn(query);
            
            
            
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
		   
			
		}
	
		return view;
	}
	
	  public Bitmap getPicture()
	    {
	    	Bitmap bitmap=null;
	    	bitmap=BitmapFactory.decodeFile(weather_info.ALBUM_PATH);
	    	return bitmap;
	    }
	  public static void queryWeather(final Context context)
		{     
			  Http.sendWeatherRequest(pre.getString("locDistrict",""),weather_info.address3, new HttpCallbackListener()
				{   
					@Override
					public void onFinish(String response)
					{   
						Utility.handleWeather(response,context);
						bitmap=Utility.getPicture(pre.getString("weather_pic", ""));
						if(bitmap!=null)
						    savePicture(bitmap,weather_info.ALBUM_PATH);
					    ((Activity) context).runOnUiThread(new Runnable(){
					    @Override
					    public void run(){
					    	showWeather(context);
					    }});
							
					}
				});
		}

	public static void savePicture(Bitmap bitmap,String path)
		{   File file1=new File(Environment.getExternalStorageDirectory()+"/download");
		    if(!file1.exists())
		    	file1.mkdirs();
			File file=new File(path);
			try{
			FileOutputStream out=new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	  public static void showWeather(Context context)
		{
		    
		     weather_layout.setVisibility(View.VISIBLE);
		     weather.setText(pre.getString("weatherInfo", ""));
		     temper.setText(pre.getString("temperature", ""));
		     realtime.setText(pre.getString("realtime", ""));
		     String publishTime=pre.getString("time", "");
		     char[]b=publishTime.toCharArray();
		     if(b.length>0)
		     { String c=""+b[0]+b[1]+b[2]+b[3]+"年"+b[4]+b[5]+"月"+b[6]+b[7]+"日"+" "+b[8]+b[9]+":"+b[10]+b[11]+":"+b[12]+b[13]+"发布";
		     time.setText(c);
		     }
		     if(bitmap!=null)
		         pic.setImageBitmap(bitmap);
		     Intent intent=new Intent(context,autoUqdateService.class);
			 context.startService(intent);
		}
        public static void refreshUserName(String response)
        {   
        	userName.setText(response);
        }
        public static void refreshCountyName(String response)
        {
        	countyname.setText(response);
        }
        public static void refreshUserPicture(Bitmap bitmap)
        {
        	userPicture.setImageBitmap(bitmap);
        }
	
		@Override
		public void onLocationChanged(AMapLocation amaplocation) 
		{   
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
						Http.queryAreaByXY(lat, lon, address, new HttpCallbackListener() {
							@Override
							public void onFinish(String response) {
							    Utility.handleAreaByXY(response, context);
								queryWeather(context);
								if(!pre.getString("locCity","").equals("")&&!pre.getString("locDistrict","").equals(""))
								{    
									
									((Activity) context).runOnUiThread(new Runnable(){
								    @Override
								    public void run(){
								    	countyname.setText(pre.getString("locCity","")+pre.getString("locDistrict",""));
								    	
								   }});
								}
							}
						});
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
		@Override
		public void activate(OnLocationChangedListener listener) 
		{
			mListener = listener;
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
			markNum=0;
		}
		@Override
		public void onDestroy()
		{
			super.onDestroy();
			if (mMapView!=null) 
			  mMapView.onDestroy();
			mMapView=null;
			if(null != mLocationClient)
			{   mLocationClient.stopLocation();
				mLocationClient.onDestroy();
			}
            markNum=0;
            Log.d("Main","onDestroy");
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
		private void addCircle(LatLng latlng, double radius) {
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
		private boolean ketongguo=true;
		private boolean cunzai=false;
		private boolean fangzhiduoci=false;    //防止多次调用返回用户函数
		@Override
		public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) 
		{  
			 if(resultCode == 1000){
				  
				    if (nearbySearchResult != null
				        && nearbySearchResult.getNearbyInfoList() != null
				        && nearbySearchResult.getNearbyInfoList().size() > 0) 
				    {   
				        for (int i = 0; i < nearbySearchResult.getNearbyInfoList().size(); i++) 
				       {    cunzai=false;
				        	if(!nearbySearchResult.getNearbyInfoList().get(i).getUserID().equals((String)MyUser.getObjectByKey("objectId")))  {
				                List<String> list=yongbDb.loadObjectId();
				                if(list.size()==0){
				                	yongbDb.saveObjectIdandlatlon(nearbySearchResult.getNearbyInfoList().get(i).getUserID(), nearbySearchResult.getNearbyInfoList().get(i).getPoint().getLatitude()+"",  nearbySearchResult.getNearbyInfoList().get(i).getPoint().getLongitude()+"");
				                  
				                }else {
				                	for (int j = 0; j < list.size(); j++) {
										   if(nearbySearchResult.getNearbyInfoList().get(i).getUserID().equals(list.get(j))){
											   cunzai=true;
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
				        else {
				                     Toast.makeText(context, "周边为空",Toast.LENGTH_LONG).show();
				              }
				}
				     else{
				             Toast.makeText(context,"周边搜索出现异常，异常码为："+resultCode ,Toast.LENGTH_LONG).show();
				         }

			
		}
		@Override
		public void onNearbyInfoUploaded(int resultCode) 
		{
		   
		}
		@Override
		public void onUserInfoCleared(int resultCode) {
			
		}
		private  void addSanMarker1() 
		{   List<String>  objectIdList=yongbDb.loadobjectIdWhereUrlemp();
		    if(objectIdList.size()>0){
		    	for (int i = 0; i < objectIdList.size(); i++) {
		    		BmobQuery<MyUser> query=new BmobQuery<MyUser>();
				    query.addWhereEqualTo("objectId",yongbDb.loadUserTouxiangUrl(objectIdList.get(i)));
				    query.findObjects(new FindListener<MyUser>() {

						@Override
						public void done(List<MyUser> object, BmobException e) {
							if(e==null){
								Log.d("Main","查询返回");
								yongbDb.saveUserNameandTouxiangUrl(object.get(0).getObjectId(), object.get(0).getNick(),object.get(0).getTouXiangUrl());
								if(!object.get(0).getTouXiangUrl().equals(""))
								     checkJiaZai();
								 else {
								 	  addSanMarker2();     //如果touxiangUrl为空的话，直接加载
								     }
							}else {
								Toast.makeText(context,"失败，"+e.getMessage(),Toast.LENGTH_SHORT);
							 }
						}
					});
				}
		    }
		
		   
			
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
		@Override
		public void onCameraChange(CameraPosition cameraPosition) {
			addLoveding(cameraPosition);
	        
		}
		@Override
		public void onCameraChangeFinish(CameraPosition cameraPosition) {
		          if(yuanLatLng==null){
		        	  yuanLatLng=cameraPosition.target;
		          }
		          searchLatLng=cameraPosition.target;
		          if(AMapUtils.calculateLineDistance(yuanLatLng, searchLatLng)>500)
		          {   yuanLatLng=searchLatLng;
		        	  query.setCenterPoint(new LatLonPoint(searchLatLng.latitude, searchLatLng.longitude));
				      mNearbySearch.searchNearbyInfoAsyn(query);
				      ketongguo=true;
		          }
		
			   
		}
		
	   private int getPixelsFromDp(int size){
		   DisplayMetrics metrics=new DisplayMetrics();
		   ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		   return (size*metrics.densityDpi/DisplayMetrics.DENSITY_DEFAULT);
	   }
	   private void checkJiaZai(){
		   urlList=yongbDb.loadJiaZai0Url();
		   if(urlList.size()>0){
			  new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int i = 0; i < urlList.size(); i++) {
						bitmaptou[bitmapNum]=Utility.getTouxiangBitmap(urlList.get(i), context,yongbDb);
						bitmapNum++;
					}
					
				}
			}).run();
		   }
	   }
	   private void addSanMarker2(){
		   
	   }
}
