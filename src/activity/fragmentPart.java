package activity;


import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import service.autoUqdateService;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.a.bo;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearch.NearbyListener;
import com.amap.api.services.nearby.NearbySearch.NearbyQuery;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.uniqueweather.app.R;


import Util.Http;
import Util.HttpCallbackListener;
import Util.Utility;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.LoginFilter.UsernameFilterGeneric;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view .ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public  class fragmentPart extends Fragment implements  AMapLocationListener, LocationSource, NearbyListener
{   public static String keyToGet="begin";
    private String theKey;
	private static TextView time;
	private static TextView realtime;
	private static RelativeLayout weather_layout;
	private static TextView weather;
	private static TextView temper;
	private static ImageView userPicture;
	private TextView myCity;
	private Button button_switch;
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
    public static int flag;
    private static Bitmap bitmap;
    public static int chenhuonce=0;
    public Context context;
    public Button button1;
    public MyHorizontalView horizontalView;
    
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
    public fragmentPart(Context context)
    {
    	this.context=context;
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
	        userPicture=(ImageView)view.findViewById(R.id.userPicture);
			realtime=(TextView)view.findViewById(R.id.realTime);
			weather_layout=(RelativeLayout)view.findViewById(R.id.weather_info);
			weather=(TextView)view.findViewById(R.id.weather);
			temper=(TextView)view.findViewById(R.id.temper);
			myCity=(TextView)view.findViewById(R.id.myCity);
			button_switch=(Button)view.findViewById(R.id.switch_city);
			button_refresh=(Button)view.findViewById(R.id.refresh);
			countyname=(TextView)view.findViewById(R.id.countyName);
			pic=(ImageView)view.findViewById(R.id.weather_pic);
			button1=(Button)view.findViewById(R.id.button_map);
			horizontalView=(MyHorizontalView)view.findViewById(R.id.horiView);
			
			editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
			pre=PreferenceManager.getDefaultSharedPreferences(context);
		    flag=pre.getInt("flag", 0);
			accountName=pre.getString("accountName","");
			if(!accountName.isEmpty())
			{Toast.makeText(context,accountName + ",欢迎您", Toast.LENGTH_SHORT).show();
			 userName.setText(accountName);
			}
			else 
			{
				userName.setText("未命名");
			}
			 uriUserPicture = pre.getString("userPicture",null);
			 if(uriUserPicture!=null)
				{  
				    Uri uri=Uri.parse(uriUserPicture);
				    try{ContentResolver cResolver=getActivity().getContentResolver();
					Bitmap bitmap=BitmapFactory.decodeStream(cResolver.openInputStream(uri));
					userPicture.setImageBitmap(bitmap);
				    }catch(Exception e)
				    {
				    	e.printStackTrace();
				    	Toast.makeText(context, "未找到相应图片", Toast.LENGTH_SHORT).show();
				    }
				}
				weather_layout.setVisibility(View.INVISIBLE);
			if(flag==0)
				{   Toast.makeText(context, "请选择城市", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent();
					intent.setClass(context,chooseAreaActivity.class);
					((Activity)context).startActivityForResult(intent,1);
					
				}
			else{
					countyName=pre.getString("countyName","");
				    pic.setImageBitmap(getPicture());
			        countyname.setText(countyName);
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
					queryWeather(context);
				}
				button_switch.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{   int[] location=new int[2];
					    v.getLocationOnScreen(location);
					    int x=location[0];
					    if(x<100)
					  { Intent intent=new Intent();
						intent.setClass(context,chooseAreaActivity.class);
						((Activity)context).startActivityForResult(intent,1);
					  }
					    else 
					    {
							horizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth, 0);
						}
					}
				});
				button_refresh.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{   queryWeather(context);
					    Toast.makeText(getActivity(), "刷新中...", Toast.LENGTH_SHORT).show();
					}
				});
				userPicture.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View V) {
						Intent intent = new Intent(Intent.ACTION_PICK, null);
			            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			                    "image/*");
			            ((Activity)context).startActivityForResult(intent, 2);
					}
				});
				userName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final EditText editText=new EditText(getActivity());
						AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
						builder.setTitle("请输入对您的尊称").setNegativeButton("取消", null).setView(editText);
						builder.setPositiveButton("确定",new DialogInterface.OnClickListener() 
						{ @Override
							public void onClick(DialogInterface dialogInterface, int which) 
						    {
								
							    String accountNameString=editText.getText().toString();
								editor.putString("accountName",accountNameString );
								editor.commit();
								userName.setText(accountNameString);
							}
						});
						builder.show();
					}
				});
				myCity.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(context,myCityAction.class);
						intent.putExtra("selectedCityName",pre.getString("selectedCityName", "") );
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
		}
		else if(theKey.equals("map"))
		{   
			view=inflater.inflate(R. layout.map,container,false);
			lat=pre.getFloat("lat", 39);
			lon=pre.getFloat("lon",116);
			zoom=pre.getFloat("zoom", 18);
			latLng1=new LatLng(lat, lon);
			mMapView=(MapView)view.findViewById(R.id.map);
			mMapView.onCreate(savedInstanceState);
			locButton=(ImageButton)view.findViewById(R.id.locationButton);
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
		
		    aMap.setMyLocationEnabled(true); 
			mLocationClient=new AMapLocationClient(context);
			mLocationClientOption=new AMapLocationClientOption();
			mLocationClient.setLocationListener(this);
			
			mLocationClientOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			mLocationClientOption.setInterval(2000);
			mLocationClientOption.setMockEnable(true);
			mLocationClientOption.setKillProcess(true);
			mLocationClient.setLocationOption(mLocationClientOption);
			mLocationClient.startLocation();
		    cameraUpdate=CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng1,zoom,0,0));
            aMap.moveCamera(cameraUpdate);
            aMap.invalidate();
			NearbySearch mNearbySearch=NearbySearch.getInstance(context);
			mNearbySearch.startUploadNearbyInfoAuto(new UploadInfoCallback() {
				
				@Override
				public UploadInfo OnUploadInfoCallback() {
					UploadInfo loadiInfo=new UploadInfo();
					loadiInfo.setCoordType(NearbySearch.AMAP);
					loadiInfo.setPoint(latlng);
					loadiInfo.setUserID(accountName);
					return loadiInfo;
				}
			}, 10000);
			mNearbySearch.addNearbyListener(this);
			locButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{    lat=pre.getFloat("lat", 39);
					 lon=pre.getFloat("lon", 116);
					 zoom=pre.getFloat("zoom",18);
					 editor.putFloat("zoom", aMap.getCameraPosition().zoom);
					 editor.commit();
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
			  Http.sendWeatherRequest(countyName,weather_info.address3, new HttpCallbackListener()
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
		{
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
					amaplocation.getLocationType();
				    lat=amaplocation.getLatitude();
				    lon=amaplocation.getLongitude();	
					editor.putFloat("lat",(float) lat);
					editor.putFloat("lon", (float)lon);
					editor.putFloat("zoom", aMap.getCameraPosition().zoom);
					editor.commit();
					latlng=new LatLonPoint(lat, lon);
					latLng1=new LatLng(lat, lon);
	                aMap.clear();
	                aMap.addMarker(new MarkerOptions().position(latLng1).icon(BitmapDescriptorFactory.fromResource(R.drawable.melocate)));
				   }
				else if(chucuoonce==0)
			     	{
				  	Log.e("error", "errorcode:"+amaplocation.getErrorCode()+",errorInfo:"+amaplocation.getErrorInfo());
					Toast.makeText(context, "errorcode:"+amaplocation.getErrorCode()+",errorInfo:"+amaplocation.getErrorInfo(), Toast.LENGTH_LONG).show();
					chucuoonce=1;
			     	}
			  }
			
			
		}
		@Override
		public void activate(OnLocationChangedListener arg0) 
		{
			
			
		}
		@Override
		public void deactivate() {
			
			
		}
		@Override
		public void onNearbyInfoSearched(NearbySearchResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
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
		public void onPause() {
			super.onPause();
			if(mMapView!=null)
			   mMapView.onPause();
		}
		@Override
		public void onDestroy()
		{
			super.onDestroy();
			if (mMapView!=null) 
			  mMapView.onDestroy();
		}
		@Override
		public void onResume() {
			super.onResume();
			if(mMapView!=null)
			  mMapView.onResume();
		}
		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			if(mMapView!=null)
			  mMapView.onSaveInstanceState(outState);
		}
	
	

}
