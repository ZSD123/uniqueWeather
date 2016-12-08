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
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
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

public  class fragmentPart extends Fragment implements  AMapLocationListener, LocationSource
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
    
    public static MapView mMapView;
    public AMap aMap;
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationClientOption;
    public CameraUpdate cameraUpdate;
    public int dingweionce=0;
    private ImageButton locButton;
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
			mMapView=(MapView)view.findViewById(R.id.map);
			locButton=(ImageButton)view.findViewById(R.id.locationButton);
			mMapView.onCreate(savedInstanceState);
			if(aMap==null)
			{
				aMap=mMapView.getMap();
			}
		    aMap.setMyLocationEnabled(true);
			mLocationClient=new AMapLocationClient(context);
			mLocationClientOption=new AMapLocationClientOption();
			mLocationClient.setLocationListener( this);
			mLocationClientOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			mLocationClientOption.setInterval(2000);
			mLocationClient.setLocationOption(mLocationClientOption);
			mLocationClient.startLocation();
			locButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					dingweionce=0;
					
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
				if(dingweionce==0)
				{if(amaplocation.getErrorCode()==0)
				   {
					amaplocation.getLocationType();
					double lat=amaplocation.getLatitude();
					double lon=amaplocation.getLongitude();
					LatLng latlng=new LatLng(lat, lon);
					cameraUpdate=CameraUpdateFactory.newCameraPosition(new CameraPosition(latlng,20 ,0, 0));
	                aMap.moveCamera(cameraUpdate);
	                aMap.invalidate();
	                aMap.clear();
	                aMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
	                dingweionce=1;
	                }
				else 
			     	{
				  	Log.e("error", "errorcode:"+amaplocation.getErrorCode()+",errorInfo:"+amaplocation.getErrorInfo());
					Toast.makeText(context, "errorcode:"+amaplocation.getErrorCode()+",errorInfo:"+amaplocation.getErrorInfo(), Toast.LENGTH_LONG).show();
					dingweionce=1;
			     	}
				}
			}
			
		}
		@Override
		public void activate(OnLocationChangedListener arg0) {
			
			
		}
		@Override
		public void deactivate() {
			// TODO Auto-generated method stub
			
		}


}
