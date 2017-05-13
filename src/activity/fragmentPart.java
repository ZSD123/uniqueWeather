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
import message.AgreeAddFriendMessage;
import message.myMessageHandler;
import model.Friend;
import model.NewFriend;
import myCustomView.CircleImageView;
import myCustomView.MapCircleImageView;
import myCustomView.myChatPager;
import service.autoUqdateService;
import Util.Http;
import Util.HttpCallbackListener;
import Util.SensorEventHelper;
import Util.Utility;
import Util.download;
import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.db.dao.UserDao;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.mapcore2d.db;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
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

import db.messageDB;
import db.messagedbHelper;
import db.yonghuDB;
public  class fragmentPart extends Fragment implements  AMapLocationListener, LocationSource, NearbyListener,OnCameraChangeListener,OnMarkerClickListener
{   public static String keyToGet="begin";
    private String theKey;
    
    private RelativeLayout fuzhiMap;
	private static TextView weather;
	private static TextView temper;
	public static CircleImageView userPicture;
	public static MapCircleImageView userPicture1;  //地图上userPicture
	
	private RelativeLayout fujinData;    //附近用户的资料名片
	
	public static messageDB meDb;
	
	private ListView chatList;      //聊天列表
	private TextView yonghuString;
	private Bitmap bitmap11;
	private TextView myCity;
    private static ImageView pic;
    public  static TextView userName;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pre;
    private String accountName;
    private View view;
    private static Bitmap bitmap;
    private String uriUserPicture;
    private static Bitmap bitmap1;
    public Context context;
    public Button button1;
    public MyHorizontalView horizontalView;
    public Button button2;     //退出登录Button
    
    private myChatPager chatPager;
    
    public String  yonghuUrl;   //其他用户url    
    public int yonghuNum=0;      //加载头像Url组的
    public int bitmapNum=0;       //其他用户头的个数
    public List<String> objStrings =new ArrayList<String>();//加载过其他用户的obj
    
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
    public volatile int chucuoonce=0;
    public double lat;
    public double lon;
    public LatLng yuanLatLng;   //原来的LatLng,即搜寻
    public LatLng searchLatLng;   //同上
    public NearbyQuery query;  
    public NearbySearch mNearbySearch;
    
    
    private View view6;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
	private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private SensorEventHelper mSensorEventHelper;
	private Circle mCircle;
	private Marker mLocMarker;    //定位Marker
	private List<Marker> mFujinMarker=new ArrayList<Marker>(); //没有设置头像直接添加头像的附近用户Marker
	private  List<String> urlList=new ArrayList<String>();;
	
	private volatile int zmarkNum=0;//直接添加用户头像的Marker个数
	private Marker mLoveMarker;    //当前Marker
	private boolean mFirstFix = false;
	private String address="http://route.showapi.com/238-2";  //经纬度转化为地址
	private TextView myAccount;     //我的账户TextView 	
	private OnLocationChangedListener mListener;
	private String yuanLocation;
	private ImageButton refreshBtn;  //地图刷新按钮
    private Timer timer;
	private TimerTask task;
	private int daojishi=10;     //倒计时刷新
	
	private View view3;    //消息界面View
	private View view4;     //联系人界面view
	private List<View> views;    //装载消息界面view和联系人界面view的views
	
	
	private View  yonghuDataView;  //附近用户资料卡View
	
	public static  yonghuDB yongbDb;
	private String username;
	private boolean markCunzai=false;  //加载图片的时候为了加快速度检测是否存在相应的mark
	private Button buttonMes;    //消息按钮
	private Button buttonCon;    //联系人按钮
	
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
	        userPicture=(CircleImageView)view.findViewById(R.id.userPicture);
            
	        username=(String)MyUser.getObjectByKey("username");
	        
	        
            meDb=messageDB.getInstance(context);
			
              
            
            MyUser user=MyUser.getCurrentUser(MyUser.class);
            if(user.getObjectId()!=null)
              BmobIM.connect(user.getObjectId(),new ConnectListener() {
				
				@Override
				public void done(String uid, BmobException e) {
					if(e==null){
						Log.d("Main","连接成功");
						
					}else {
						Toast.makeText(context, "连接失败，"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
            
			weather=(TextView)view.findViewById(R.id.weather);
			temper=(TextView)view.findViewById(R.id.temper);
			myCity=(TextView)view.findViewById(R.id.myCity);
			pic=(ImageView)view.findViewById(R.id.weather_pic);
			button1=(Button)view.findViewById(R.id.button_map);
			button2=(Button)view.findViewById(R.id.button1);
			horizontalView=(MyHorizontalView)view.findViewById(R.id.horiView);
			myAccount=(TextView)view.findViewById(R.id.myAccount);
		    buttonMes=(Button)view.findViewById(R.id.chat_mes);
		    buttonCon=(Button)view.findViewById(R.id.chat_con);
			
			chatPager=(myChatPager)view.findViewById(R.id.chatPager);
            final LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view3=layoutInflater.inflate(R.layout.chatlist,null);
			view4=layoutInflater.inflate(R.layout.contactslist,null);
			
			List<BmobIMConversation> conversations=BmobIM.getInstance().loadAllConversation();
			
			final BmobIM bmobIM=BmobIM.getInstance();
			
			BaseAdapter baseAdapter=new BaseAdapter() {
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view5;
					if(convertView==null){
						view5=layoutInflater.inflate(R.layout.item_conversation, null);
						}else {
							view5=convertView;
						}
					CircleImageView circle=(CircleImageView)view5.findViewById(R.id.iv_recent_avatar);
					TextView nameText=(TextView)view5.findViewById(R.id.tv_recent_name);
					TextView messageText=(TextView)view5.findViewById(R.id.tv_recent_msg);
					TextView unReadText=(TextView)view5.findViewById(R.id.tv_recent_unread);
					circle.setImageBitmap(Utility.getPicture(bmobIM.loadAllConversation().get(position).getConversationIcon()));
					nameText.setText(bmobIM.loadAllConversation().get(position).getConversationTitle());
					if(bmobIM.loadAllConversation().get(position).getMessages().size()>0)
				     	messageText.setText(bmobIM.loadAllConversation().get(position).getMessages().get(bmobIM.loadAllConversation().get(position).getMessages().size()-1).toString());
					unReadText.setText(""+bmobIM.loadAllConversation().get(position).getUnreadCount());
					return view5;
				}
				
				@Override
				public long getItemId(int position) {
					return position;
				}
				
				@Override
				public Object getItem(int position) {
					return position;
				}
				
				@Override
				public int getCount() {
					return bmobIM.loadAllConversation().size();
				}
			};
			((ListView)view3).setAdapter(baseAdapter);
			BaseAdapter baseAdapter2=new BaseAdapter() {
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
				
					if(convertView==null){
						view6=layoutInflater.inflate(R.layout.new_friend, null);
						}else {
							view6=convertView;
						}
					return view6;
				}
				
				@Override
				public long getItemId(int position) {
					return position;
				}
				
				@Override
				public Object getItem(int position) {
					return position;
				}
				
				@Override
				public int getCount() {
					return 1;
				}
			};
			((ListView)view4).setAdapter(baseAdapter2);
			ListView contactsList=(ListView)view4;
		    contactsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(view==view6){
						Intent intent=new Intent(context,newFriendActivity.class);
						startActivity(intent);
					}
					
				}
			});
			
			views=new ArrayList<View>();
			views.add(view3);
			views.add(view4);
			
            PagerAdapter pagerAdapter=new PagerAdapter() {
				
				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {

					return arg0==arg1;
				}
				
				@Override
				public int getCount() {
					
					return views.size();
				}

				@Override
				public void destroyItem(ViewGroup container, int position,
						Object object) {
					container.removeView(views.get(position));
				}

				@Override
				public Object instantiateItem(final ViewGroup container, int position) {  //将参数里给定的position的视图，增加到container中，以及返回当前position的view作为此图的key
				    container.addView(views.get(position));
					return views.get(position);
				}
			};
			
			chatPager.setAdapter(pagerAdapter);
			
			if(context==null)
			    context=(Context)getActivity();
			
			String nick=(String)BmobUser.getObjectByKey("nick");
		    String touxiangUrl=(String)BmobUser.getObjectByKey("touxiangUrl");
		    String accountString=(String)BmobUser.getObjectByKey("username");//就是账户名，比如159...或者邮箱
		    if(nick!=null){
		    	userName.setText(nick);
		    }
			
			editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
			pre=PreferenceManager.getDefaultSharedPreferences(context);
			accountName=weather_info.myUserdb.loadUserName(username); 
			if(!accountName.isEmpty())
			{Toast.makeText(context,accountName + ",欢迎您", Toast.LENGTH_SHORT).show();
			 userName.setText(accountName);
			}
			else 
			{   BmobQuery<MyUser> query=new BmobQuery<MyUser>();
			    query.getObject((String)MyUser.getObjectByKey("objectId"),new QueryListener<MyUser>() {

					@Override
					public void done(MyUser myUser, BmobException e) {
						if(e==null){
							weather_info.myUserdb.checkandSaveUpdateN((String)MyUser.getObjectByKey("username"),myUser.getNick());
							userName.setText(weather_info.myUserdb.loadUserName(username));
						}else {
							Toast.makeText(context, "失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					 }
			    	});
				
			}
		    touxiangUrl=(String)MyUser.getObjectByKey("touxiangUrl");
		
            File file=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/"+"头像.png");
		      if(file.exists()){
					  bitmap1=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/"+"头像.png");
	                  userPicture.setImageBitmap(bitmap1);
				   }
            	
            	
		      
			 if(touxiangUrl!=null)       //这里防止更新图片后另外一台客户端没有更新
				{  
				   BmobFile bmobFile=new BmobFile("头像"+".png",null,touxiangUrl);//确定文件名字（头像.png）和网络地址
				   download.downloadFile(bmobFile,context);//进行下载操作
				   
				}
				pic.setImageBitmap(getPicture());
				weather.setText(pre.getString("weatherInfo", ""));
				temper.setText(pre.getString("temperature", ""));
		       
				buttonMes.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						chatPager.setCurrentItem(0);
					 	horizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth,0);
					}
				});
				
				buttonCon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						chatPager.setCurrentItem(1);
						
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
			    userName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(context,myAccountAct.class);
						Bundle data=new Bundle();
						data.putString("name",userName.getText().toString());
						intent.putExtras(data);
						startActivity(intent);
						
					}
				});
			    
		}
		else if(theKey.equals("map"))
		{   
			view=inflater.inflate(R.layout.map,container,false);
			fuzhiMap=(RelativeLayout)view;
			
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
			LayoutParams layoutParams=new LayoutParams(getPixelsFromDp(100),getPixelsFromDp(100));
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
            
        	timer=new Timer();
			task=new TimerTask(){
			 		@Override
			 		public void run() 
			 		{   daojishi--;
			 		    if(daojishi==0)
			 			{   
							query.setCenterPoint(new LatLonPoint(searchLatLng.latitude, searchLatLng.longitude));
						    mNearbySearch.searchNearbyInfoAsyn(query);
			 			    daojishi=10;
	                        chucuoonce=0;
	                        Log.d("Main","更新");
			 			}
			 		}
			 	};
			timer.schedule(task, 1000,1000);
            
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
	  public  void saveYonghuPic(Bitmap bitmap,String obj){
		  File file1=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head");
  
		  if(!file1.exists())
		    	file1.mkdirs();
           File file=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+obj+".jpg_");

			try{
			FileOutputStream out=new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
	  }
	  public Bitmap getYonghuPic(String obj){
		  Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+obj+".jpg_");
		  return bitmap;
	  }
	
	  public static void showWeather(Context context)
		{
		    

		     weather.setText(pre.getString("weatherInfo", ""));
		     temper.setText(pre.getString("temperature", ""));

		     if(bitmap!=null)
		         pic.setImageBitmap(bitmap);
		     Intent intent=new Intent(context,autoUqdateService.class);
			 context.startService(intent);
		}
        public static void refreshUserName(String response)
        {   
        	userName.setText(response);
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
           zmarkNum=0;
           NearbySearch.destroy();
           if(timer!=null)
              timer.cancel();
           if(task!=null)
              task.cancel();
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
		private boolean cunzai=false;
		@Override
		public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) 
		{  
			 if(resultCode == 1000){
				    
				    if (nearbySearchResult != null
				        && nearbySearchResult.getNearbyInfoList() != null
				        && nearbySearchResult.getNearbyInfoList().size() > 1)
				    	
				    {   
				    	yonghuString.setText("当前附近用户有:"+(nearbySearchResult.getNearbyInfoList().size()-1));
				        for (int i = 0; i < nearbySearchResult.getNearbyInfoList().size(); i++) 
				       {  
				            cunzai=false;
				        	if(!nearbySearchResult.getNearbyInfoList().get(i).getUserID().equals((String)MyUser.getObjectByKey("objectId")))  {
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
				        else if(chucuoonce==0){
				              
				                     yonghuString.setText("当前附近用户有:"+(nearbySearchResult.getNearbyInfoList().size()-1));
				                     chucuoonce=1;
				                  
				              }
				}
				     else if(chucuoonce==0){
				             Toast.makeText(context,"周边搜索出现异常，异常码为："+resultCode ,Toast.LENGTH_LONG).show();
				             chucuoonce=1;
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
								yongbDb.saveData(object.getObjectId(),object.getNick(),object.getSex(), object.getAge(), object.getZhiye());
								
								if(!object.getTouXiangUrl().equals("")){      //当存在有相应的Url值
								     checkJiaZai(object.getObjectId(),object.getTouXiangUrl());
								}
								 else {
									 if(!markXianShi(object.getObjectId()))  //这里修改为了判断mark是否加载
								 	    addSanMarkerDir(object.getObjectId(),null);     //如果touxiangUrl为空的话，直接加载
									 else{                                         //如果等于1，就是加载完毕，这个设置它最新的地理位置坐标                    
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
				                Log.d("Main", "失败，"+e.getErrorCode()+e.getMessage());
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
		          if(AMapUtils.calculateLineDistance(yuanLatLng, searchLatLng)>1000)
		          {   yuanLatLng=searchLatLng;
		        	  query.setCenterPoint(new LatLonPoint(searchLatLng.latitude, searchLatLng.longitude));
				      mNearbySearch.searchNearbyInfoAsyn(query);
		          }
		
			   
		}
		
	   private int getPixelsFromDp(int size){
		   DisplayMetrics metrics=new DisplayMetrics();
		   ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		   return (size*metrics.densityDpi/DisplayMetrics.DENSITY_DEFAULT);
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
	   private void JiaZai(final String obString,final String touxiangUrl){ 
	        File file=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+obString+".jpg_");
	        if(file.exists()){
	        Log.d("Main", "文件存在");
            bitmap11=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+obString+".jpg_");
            addSanMarkerDir(obString,bitmap11);     
            new Thread(new Runnable() {
 	            @Override
					public void run() {
					     
				    	 bitmap11=Utility.getTouxiangBitmap(touxiangUrl, context,yongbDb);
				
				    	 if(bitmap11!=null){
					     	saveYonghuPic(bitmap11,obString);
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
						     	saveYonghuPic(bitmap11,obString);
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
	   private void addSanMarkerDir(String objectId,Bitmap bitmap1){
		   Bitmap bitmap2;
		   Log.d("Main", "直接加载marker");
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
	@Override
	public boolean onMarkerClick(Marker marker) {
		if(yonghuDataView==null){
	    addYonghuDataView(marker);
	    Log.d("Main", "点击事件");
		}else {
		    fuzhiMap.removeView(yonghuDataView);
		    addYonghuDataView(marker);
		}
		return false; 
	}
	private void addYonghuDataView(Marker marker){
	          yonghuDataView=LayoutInflater.from(context).inflate(R.layout.fujin_yonghu_data, null);
			  fujinData=(RelativeLayout)yonghuDataView.findViewById(R.id.fujin_relative);
			  RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		      layoutParams.addRule(RelativeLayout.ABOVE, R.id.refreshbtn);
		      layoutParams.setMarginStart(getPixelsFromDp(15));
		      layoutParams.setMarginEnd(getPixelsFromDp(15));
		      fujinData.setLayoutParams(layoutParams);
			  ImageView imageView=(ImageView)fujinData.findViewById(R.id.yonghu_data_image);
			  BitmapDescriptor descriptor=marker.getIcons().get(0);
			  imageView.setImageBitmap(descriptor.getBitmap());
			  Bundle bundle=new Bundle(yongbDb.loadData((String) marker.getObject()));
			  if(bundle!=null){
				  String nizhen=bundle.getString("nickName");
				  String sex=bundle.getString("sex");
				  String age=bundle.getString("age");
				  String zhiye=bundle.getString("zhiye");
				  TextView nizhenText=(TextView)fujinData.findViewById(R.id.yonghu_data_nick);
				  TextView sexText=(TextView)fujinData.findViewById(R.id.yonghu_data_sex);
				  TextView ageText=(TextView)fujinData.findViewById(R.id.yonghu_data_age);
				  TextView zhiyeText=(TextView)fujinData.findViewById(R.id.yonghu_data_zhiye);
                  nizhenText.setText(nizhen);
                  sexText.setText(sex);
                  ageText.setText(age);
                  zhiyeText.setText(zhiye);
			  }
			  ImageView jiaImage=(ImageView)fujinData.findViewById(R.id.jiatu);
			  jiaImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	                 
					
					
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
  
     private void queryFriends(final FindListener<Friend> listener){
    	 BmobQuery<Friend> query=new BmobQuery<Friend>();
    	 MyUser myUser=MyUser.getCurrentUser(MyUser.class);
    	 query.addWhereEqualTo("myUser",myUser ); // 查询当前用户的所有好友
    	 query.order("-updatedAt");
    	 query.include("friendUser");//希望在查询的同时也把该用户的
    	 query.findObjects(new FindListener<Friend>() {

			@Override
			public void done(List<Friend> list, BmobException e) {
				if(e==null){
					if(list!=null&&list.size()>0){
						Log.d("Main", list.get(0).getFriendUser().getNick());
					}else {
						Log.d("Main", "没有好友");
					}
				}else {
					Log.d("Main","出错，"+e.getMessage());
				}
				
			}
		});
     }
	
}
