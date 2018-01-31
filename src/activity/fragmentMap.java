package activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import mapAct.iwantShareAct;
import mapAct.shareObject_UserXiang;
import mapAct.shareObject_XiangXiDataAct;
import message.AddFriendMessage;
import model.Jubao;
import model.location;
import model.shareObject;
import myCustomView.CircleImageView;
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
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
import com.amap.api.services.a.am;
import com.amap.api.services.a.ca;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearch.NearbyListener;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.amap.api.services.nearby.NearbySearch.NearbyQuery;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.sharefriend.app.R;

import db.yonghuDB;
import Util.Http;
import Util.HttpCallbackListener;
import Util.SensorEventHelper;
import Util.Utility;
import Util.download;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class fragmentMap extends Fragment implements AMapLocationListener,LocationSource, OnCameraChangeListener, OnMarkerClickListener, NearbyListener,OnGeocodeSearchListener,OnPoiSearchListener {
	private volatile int zmarkNum=0;//ֱ������û�ͷ���Marker����
    public static RelativeLayout fuzhiMap;
    private ImageButton zujiBtn;
    
	public static  yonghuDB yongbDb;  //��Χ�û����ݱ�
    private Circle mCircle;
    public double lat;
    public double lon;
    
    
    public float zoom;
    private LatLng latLng1;   //���Ҳ�Ǳ�ʾ��λ
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
	  
	  private BmobGeoPoint point;
	  
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
      
      
      private View  yonghuDataView;  //�����û����Ͽ�View
      private View addressDataView;   //��λ��ַ���Ͽ�View
      private View shareObjectDataView;  //������Ʒ���Ͽ�View
      public static RelativeLayout fujinData;
      public static RelativeLayout addressData;
      public static RelativeLayout shareObjectData;  //
      
      private List<Marker> mFujinMarker=new ArrayList<Marker>(); //û������ͷ��ֱ�����ͷ��ĸ����û�Marker
      private Bitmap bitmap11;
      private boolean flag;
      private AlertDialog.Builder builder;
      private AlertDialog dialog;     //��������dialog
      private AlertDialog dialog1;    //��Ҫ�����ֻ�����֤����
      
      private EditText searchEditText;        	//��ͼ������
      private ImageView searchImage;          //����ɾ����
      private Button searchButton;            //������ť
      
      private SharedPreferences.Editor editor;
      private SharedPreferences pre;   //��Ҫ���Ű�����ɾ����Ȼ����fragmentChat.pre����
      
      private GeocodeSearch geocodeSearch;
      private GeocodeQuery geocodeQuery;
      
      private PoiSearch.Query queryPoi;
      private PoiSearch poiSearch;
      
      private List<Marker> searchMarkers;
      private List<Marker> shareMarkers;  //������Ʒmarkers
      
      
      private LinearLayout mShowLayout;
      
      private String [] mShareObject;
      
      private Button iwantShare;   //��Ҫ����ť
      
      private MyUser currentUser;   //��ǰ�û�
      
      private List<View> views;  //����horizontalviewҪ���ص�view
      private List<TextView> textViews;  //������Ӧ��textView
      
      private int chooseNum;   //ѡ��Ĺ�����Ʒ�ı��
      private String shareObjectAddress;//������Ʒ��λ��
      private TextView shareObject_data_address;
      
      
      private String shareObject_user_image;
      private String shareObject_user_nickName;  //�ǳ�
      private String shareObject_user_sex;    //�Ա�
      private String shareObject_user_age;   //����
      private Integer shareObject_credit;    //��������
      
      private int screenWidth;
      private int screenHeight;
      
  	  public fragmentMap(){
	 	 context=getContext();
	  }
	@Override

	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		View view=null;
		
		view=inflater.inflate(R.layout.map,container,false);
		
		WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outmMetrics=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outmMetrics);
		screenHeight=outmMetrics.heightPixels;
		screenWidth=outmMetrics.widthPixels;
		
		views=new ArrayList<View>();
		textViews=new ArrayList<TextView>();
		
		currentUser=MyUser.getCurrentUser(MyUser.class);
		
		ImageButton locButton;
		ImageButton renButton;
		ImageButton myuserBtn;    //�ҵ��û������ť
		
		myuserBtn=(ImageButton)view.findViewById(R.id.myuser_btn);
		
		iwantShare=(Button)view.findViewById(R.id.iwantshare);
		
		fuzhiMap=(RelativeLayout)view;
		
		mShowLayout=(LinearLayout)view.findViewById(R.id.showShareObject);
		
		initData();
		initView(inflater);
		
		searchEditText=(EditText)view.findViewById(R.id.search_et_input);
		searchImage=(ImageView)view.findViewById(R.id.search_iv_delete);
		searchButton=(Button)view.findViewById(R.id.search_btn_back);
		
		zujiBtn=(ImageButton)view.findViewById(R.id.zujibtn);
		
		
		searchMarkers=new ArrayList<Marker>();
		shareMarkers=new ArrayList<Marker>();
		
		
		if(context==null)
			context=getActivity();
		
		
		geocodeSearch=new GeocodeSearch(context);
		geocodeSearch.setOnGeocodeSearchListener(this);
		
		pre=PreferenceManager.getDefaultSharedPreferences(getActivity());
		editor=PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
		
		flag=pre.getBoolean("flag", true);    //��ʾҪ������ʾ����
		
		if(flag){
			builder=new AlertDialog.Builder(context);
			
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface d, int which) {
					 editor.putBoolean("flag",flag);
					 editor.commit();
					 
					 if(dialog.isShowing())
					    	dialog.dismiss();
				}
			});
			View view2=LayoutInflater.from(context).inflate(R.layout.mapdialog,null);
			builder.setView(view2);
			
			CheckBox checkBox =(CheckBox)view2.findViewById(R.id.checkbox);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					 flag=!isChecked;
				}
			});

	        dialog=builder.create();
	        if(!dialog.isShowing())
		    	dialog.show();
		}
		
		
		lat=pre.getFloat("lat", 39);
		lon=pre.getFloat("lon",116);
		
		if(lat>=90||lat<=-90){
			lat=39;
		}
		
		zoom=pre.getFloat("zoom", 18);
		
		latlng=new LatLonPoint(lat, lon);
		
		latLng1=new LatLng(lat, lon);
			
		point=new BmobGeoPoint(lon, lat);
		
		BmobQuery<location> bmobQuery=new BmobQuery<location>();
	 	bmobQuery.addWhereEqualTo("myUser",currentUser);
		bmobQuery.findObjects(new FindListener<location>() {

		  @Override
		  public void done(List<location> list,BmobException e) {
              if(e==null){
            	if(list.size()>0){
            	 location lo=new location();
            	 lo.setMyLocation(point);
				 lo.update(list.get(0).getObjectId(),new UpdateListener() {
														
					@Override
				public void done(BmobException e) {
						if(e==null){
							
						}else {
						 Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
							}
															
			         	}
							});
            	}else{
					location lo1=new location();
					
					lo1.setMyLocation(point);
					lo1.setMyUser(currentUser);
					lo1.save(new SaveListener<String>() {

						@Override
						public void done(String a,
								BmobException e) {
							if(e==null){

							}else {
								 Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
						  	}
							
						}
						
						
						
					});
				}
											
		}else {
			 Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
	    	}
		}});
		
		mMapView=(MapView)view.findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		locButton=(ImageButton)view.findViewById(R.id.locationButton);
		renButton=(ImageButton)view.findViewById(R.id.renren);
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
        query.setRadius(5000);
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
	   //////////////////////////////////////////////////////
	   
	
	   myuserBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(context,map_mine.class);
			startActivity(intent);
			
		}
	  });
	   
	  
	  
	   
	   iwantShare.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			  
			
			
			 if(currentUser.getMobilePhoneNumberVerified()!=null&&currentUser.getMobilePhoneNumberVerified()){   //���������֤��ֱ�ӽ��룬��Ȼ����֤
				   
				   Intent intent=new Intent(context,iwantShareAct.class);
				   startActivity(intent);
				   
			   }else {
				   
				    AlertDialog.Builder builder=new AlertDialog.Builder(context);
					
					builder.setMessage("����ǰ�˻��ֻ���δ����֤�����ݡ����簲ȫ������Ϊ�������ر��Ϲ���˫���Ϸ����棬������֤�ֻ���");
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialoginterface, int which) {
							Intent intent=new Intent(context,mobileVerifyAct.class);
							startActivity(intent);
							if(dialog1!=null){
								dialog1.dismiss();
							}
							
						}
					});
					
			        dialog1=builder.create();
			        if(!dialog1.isShowing())
				    	dialog1.show();
				   
				   
			   }
			
		}
	   });
	   
	   searchImage.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			searchEditText.setText("");
			
		}
	});
	   
	   
	   
	  
	   
	   searchButton.setOnClickListener(new OnClickListener() {    //���������ť
		
		@Override
		public void onClick(View v) {
		
			  beignSearch();
			
		}
	   });
	   
	  
	   searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(actionId==EditorInfo.IME_ACTION_SEARCH){
				beignSearch();
			}
			return false;
		}
	});
	   
	   searchEditText.addTextChangedListener(new TextWatcher() {   //������
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			   if(s.length()>0){
				   searchImage.setVisibility(View.VISIBLE);
			   }else if(s.length()==0){
				   searchImage.setVisibility(View.GONE);
			   }
			
		}
		
		
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
			
		}
	});
        
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
					
					BmobQuery<location> query=new BmobQuery<location>();   //ɾ������е�
					query.addWhereEqualTo("myUser",currentUser);
					query.findObjects(new FindListener<location>() {

						@Override
						public void done(List<location> list, BmobException e) {
							if(e==null){
								if(list!=null&&list.size()>0){
									location loc=new location();
									loc.setObjectId(list.get(0).getObjectId());
									loc.delete(new UpdateListener() {
										
										@Override
										public void done(BmobException e) {
											if(e==null){
												
											}else {
												Toast.makeText(context, "����"+e.getMessage(), Toast.LENGTH_LONG).show();
											}
											
										}
									});
								}
							}else {
								Toast.makeText(context, "����"+e.getMessage(), Toast.LENGTH_LONG).show();
							}
							
						}
					});
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
		
	    renButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,fujinlieAct.class);
				startActivity(intent);
				
			}
		});
	    
	    locButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{    lat=pre.getFloat("lat", 39);
				 lon=pre.getFloat("lon", 116);
				 
				 editor.putFloat("zoom", aMap.getCameraPosition().zoom);
				 editor.commit();
				 
				 zoom=aMap.getCameraPosition().zoom;
				 
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
	          if(AMapUtils.calculateLineDistance(yuanLatLng, searchLatLng)>100)
	          {   yuanLatLng=searchLatLng;
	        	  query.setCenterPoint(new LatLonPoint(searchLatLng.latitude, searchLatLng.longitude));
			      mNearbySearch.searchNearbyInfoAsyn(query);
	          }
	
		   
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		

		
		if(marker.getObject() instanceof PoiItem){   //�����PoiItem�Ļ�
	
			if(addressData==null){       //��ӵ�������View
			addAddressDataView(marker);
	
			}else {
				fuzhiMap.removeView(addressData);
				addAddressDataView(marker);
	
			}
			return false;
			
		}else if(marker.getObject() instanceof String ){ //�����String          //����û�����View

			if(fujinData==null){
			    addYonghuDataView(marker);
				}else {
				    fuzhiMap.removeView(fujinData);
				    addYonghuDataView(marker);
				}
				return false; 
				
		}else if(marker.getObject() instanceof shareObject){  //��ӷ�����Ʒ����¼�
          
			
			if(shareObjectData==null){
				
		
			    addShareObjectDataView(marker);
			    
			    
				}else {
		
				    fuzhiMap.removeView(shareObjectData);
				    addShareObjectDataView(marker);
				    
				}
				return false; 
				
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
			    	     if(resultCode==1802){
			               Toast.makeText(context,"�ܱ����������쳣�����ȼ������״���Ƿ�����" ,Toast.LENGTH_LONG).show();
			               chucuoonce=1;
			    	     }else {
			    	    	 Toast.makeText(context,"�ܱ����������쳣���쳣��Ϊ��"+resultCode ,Toast.LENGTH_LONG).show();
				               chucuoonce=1;
				        	}
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
				
				latlng.setLatitude(lat);
				latlng.setLongitude(lon);
				
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
		}else{

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
	}
	private void addLoveding(CameraPosition cameraPosition){
		if(mLoveMarker!=null){
			mLoveMarker.setPosition(cameraPosition.target);
		}else{
	
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
	boolean canCall;
	
	private void addYonghuDataView(final Marker marker){
	      
	    
          yonghuDataView=LayoutInflater.from(context).inflate(R.layout.fujin_yonghu_data, null);
		  fujinData=(RelativeLayout)yonghuDataView;
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
			  canCall=bundle.getBoolean("canCall");
	
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
				if(fujinData!=null){
					fuzhiMap.removeView(fujinData);
					fujinData=null;
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
				if(canCall){
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
		    }else{
		    	Toast.makeText(getActivity(),"�Է��ܾ���绰",Toast.LENGTH_SHORT).show();
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
   
	private void addAddressDataView(Marker marker){
		
          addressDataView=LayoutInflater.from(context).inflate(R.layout.fujin_address_data, null);
		  addressData=(RelativeLayout)addressDataView;
		  RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,Utility.dip2px(context, 160));
		  layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	      addressData.setLayoutParams(layoutParams);
	      
		  TextView address_name=(TextView)addressData.findViewById(R.id.address_name);
		  TextView address_des=(TextView)addressData.findViewById(R.id.address_des);
		  TextView address_dis=(TextView)addressData.findViewById(R.id.address_dis);
		  ImageView imageCha=(ImageView)addressData.findViewById(R.id.cha);
		  
		  address_name.setText(((PoiItem)marker.getObject()).getTitle());
		  address_des.setText(((PoiItem)marker.getObject()).getSnippet());
		  address_dis.setText(((PoiItem)marker.getObject()).getDistance()+"m");
		  imageCha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(addressData!=null){
					fuzhiMap.removeView(addressData);
					addressData=null;
				}
				
			}
		});
		  
		  fuzhiMap.addView(addressData);
	}
	
	//****************��ӹ�����Ʒ���Ͽ�View*******************************
	
	private void addShareObjectDataView(final Marker marker){

          shareObjectDataView=LayoutInflater.from(context).inflate(R.layout.fujin_shareobject_data, null);
		  shareObjectData=(RelativeLayout)shareObjectDataView;
		  RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	      layoutParams.addRule(RelativeLayout.ABOVE, R.id.refreshbtn);
	      layoutParams.setMarginStart(Utility.dip2px(context, 15));
	      layoutParams.setMarginEnd(Utility.dip2px(context, 15));
	      shareObjectData.setLayoutParams(layoutParams);

	      
		  final CircleImageView shareObjectDataImage=(CircleImageView)shareObjectData.findViewById(R.id.shareObject_data_image);
		  TextView shareObject_data_nick=(TextView)shareObjectData.findViewById(R.id.shareObject_data_nick);
		  ImageView right42=(ImageView)shareObjectData.findViewById(R.id.right42);
          ImageView cha=(ImageView)shareObjectData.findViewById(R.id.cha);
		  
          shareObject_data_nick.setText(((shareObject)marker.getObject()).getTitle());
          
          
		  float width=getCharacterWidth(shareObject_data_nick);  //ÿ���ַ����
          
          final TextView shareObject_myUser=(TextView)shareObjectData.findViewById(R.id.shareObject_myUser);
		  ImageView right43=(ImageView)shareObjectData.findViewById(R.id.right43);
		  
		  shareObject_data_address=(TextView)shareObjectData.findViewById(R.id.shareObject_data_address);
		  TextView useView=(TextView)shareObjectData.findViewById(R.id.use);
		  TextView sendMsgView=(TextView)shareObjectData.findViewById(R.id.sendMsg);
		  TextView callTo=(TextView)shareObjectData.findViewById(R.id.calltu);
		  
		  shareObject_data_address.setWidth(screenWidth-2*Utility.dip2px(context, 15)-Utility.dip2px(context, 100)-Utility.dip2px(context, 100));//get�õ�����px
		  
		  
		  new Thread(new Runnable() {
				
				@Override
				public void run() {
					 final Bitmap bitmap= Utility.getPicture(((shareObject)marker.getObject()).getImageUrl());
	                 ((Activity)context).runOnUiThread(new Runnable() {
				 		
						@Override
						public void run() {
							if(bitmap!=null){
						     	shareObjectDataImage.setImageBitmap(bitmap);
						     
							} 
						}
	                 }
				
		      	);
				}
		  }).start();
				
		  
		 
		  
		  BmobQuery<MyUser> query1=new BmobQuery<MyUser>();
		  query1.addWhereEqualTo("objectId", ((shareObject)marker.getObject()).getMyUser().getObjectId());
		  query1.findObjects(new FindListener<MyUser>() {

			@Override
			public void done(List<MyUser> list, BmobException e) {
				if(e==null){
					
					String nickName=list.get(0).getNick();
					shareObject_myUser.setText(nickName);
					shareObject_credit=list.get(0).getCredit();
					shareObject_user_age=list.get(0).getAge();
					shareObject_user_nickName=nickName;
					shareObject_user_sex=list.get(0).getSex();
					shareObject_user_image=list.get(0).getTouXiangUrl();
					
				}else {
					Toast.makeText(context, "������˼��"+e.getMessage(),Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		  
		  RegeocodeQuery query=new RegeocodeQuery(new LatLonPoint(((shareObject)marker.getObject()).getObjectionPoint().getLatitude(), ((shareObject)marker.getObject()).getObjectionPoint().getLongitude()), 200,GeocodeSearch.AMAP);
		  geocodeSearch.getFromLocationAsyn(query);
		  
		  fuzhiMap.addView(shareObjectData);
		  
		  cha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			   
				if(shareObjectData!=null){
					fuzhiMap.removeView(shareObjectData);
					shareObjectData=null;
				}
				
				
			}
		});
		  
		  right42.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,shareObject_XiangXiDataAct.class);
				intent.putExtra("imageUrl", ((shareObject)marker.getObject()).getImageUrl());
				intent.putExtra("title",((shareObject)marker.getObject()).getTitle());
				intent.putExtra("description", ((shareObject)marker.getObject()).getDescription());
				intent.putExtra("address",shareObject_data_address.getText().toString());
				startActivity(intent);
			}
		});
		  
		  right43.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,shareObject_UserXiang.class);
				intent.putExtra("imageUrl", shareObject_user_image);
				intent.putExtra("nick",shareObject_user_nickName);
				intent.putExtra("age", shareObject_user_age);
				intent.putExtra("sex",shareObject_user_sex);
				intent.putExtra("credit", shareObject_credit);
				intent.putExtra("objectId", ((shareObject)marker.getObject()).getMyUser().getObjectId());
				startActivity(intent);
				  
			}
		});
		  
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
							yongbDb.saveData(object.getObjectId(),object.getNick(),object.getSex(), object.getAge(), object.getZhiye(),object.getUsername(),object.isCanCall());
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
							Toast.makeText(context,"ʧ�ܣ�����������Ժ���"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(context,"ʧ�ܣ�����������Ժ���"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
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
			    	mFujinMarker.add(zmarkNum, aMap.addMarker(options));   //��һ���־���aMap���Marker����ͼ��
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
	           if(objStrings!=null)
	              objStrings.clear();
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
	
	@Override
	public void onGeocodeSearched(GeocodeResult result, int num) {
		double lat=result.getGeocodeAddressList().get(0).getLatLonPoint().getLatitude();
		double lon=result.getGeocodeAddressList().get(0).getLatLonPoint().getLongitude();
		aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(lat, lon), zoom, 0, 0)));
		aMap.invalidate();
	}
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int code) {
		
		if(code==1000){
			shareObjectAddress=result.getRegeocodeAddress().getFormatAddress();
		    shareObject_data_address.setText(shareObjectAddress);
		}
		
	}
	@Override
	public void onPoiItemSearched(PoiItem item, int code) {
		
		
	} 
	@Override
	public void onPoiSearched(PoiResult result, int code) {
		
		
		
	  if(code==1000){
		  
		  for (int i = 0; i < searchMarkers.size(); i++) {
			  searchMarkers.get(i).remove();
			  searchMarkers.get(i).destroy();
			  searchMarkers.clear();
			 
			  
		}
		  
		  aMap.clear();
		  mLocMarker=null;
		  mLoveMarker=null;
		  addMarker(latLng1);
		  addLoveding(aMap.getCameraPosition());
		
		for (int i = 0; i < result.getPois().size(); i++) {
			MarkerOptions options=null;
			switch (i) {
				case 0:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding1)));
					break;
				case 1:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding2)));
					break;
				case 2:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding3)));
					break;
				case 3:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding4)));
					break;
				case 4:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding5)));
					break;
				case 5:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding6)));
					break;
				case 6:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding7)));
					break;
				case 7:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding8)));
					break;
				case 8:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding9)));
					break;
				case 9:
					options=new MarkerOptions().position(new LatLng(result.getPois().get(i).getLatLonPoint().getLatitude(), result.getPois().get(i).getLatLonPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ding10)));
					break;
				default :
					break;
			}
            
			double lathere=result.getPois().get(i).getLatLonPoint().getLatitude();
			double lonhere=result.getPois().get(i).getLatLonPoint().getLongitude();
			float distance=AMapUtils.calculateLineDistance(latLng1, new LatLng(lathere, lonhere));
			result.getPois().get(i).setDistance((int) distance);
			
		    int j=min1(result);
		   
		   
			double lat=result.getPois().get(j).getLatLonPoint().getLatitude();
			double lon=result.getPois().get(j).getLatLonPoint().getLongitude();
			aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(lat, lon), zoom, 0, 0)));
			aMap.invalidate();
			
			Marker marker= aMap.addMarker(options);
			marker.setObject(result.getPois().get(i));
			searchMarkers.add(marker);
		}
		
	  }else {
		  Toast.makeText(context, "��ǰ���ִ������Ժ�����",Toast.LENGTH_SHORT).show();
	   }
		
		   
	}
	private int min1(PoiResult result){  
        int minValue = result.getPois().get(0).getDistance(); 
        int j=0;
        for (int i = 0; i<result.getPois().size();i++){  
            if (result.getPois().get(i).getDistance()<minValue)  {
                minValue = result.getPois().get(i).getDistance(); 
                j=i;
                }
        }  
        return j;  
  
    } 
	private void beignSearch(){
		   geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
		   
		   String content=searchEditText.getText().toString();
		   String city=pre.getString("locCity", "����");
		   queryPoi=new PoiSearch.Query(content,"", city);
		   queryPoi.setPageSize(10);
		   queryPoi.setPageNum(1);
		   
		   
		   poiSearch=new PoiSearch(context, queryPoi);
		   		   
		   poiSearch.setOnPoiSearchListener(fragmentMap.this);
		   
		   poiSearch.searchPOIAsyn();
	}
	
	private void initData(){
		mShareObject=new String []{"ȫ��","��ɡ","�·�","ʳ��","����","�鼮","�����","����","�ֻ�","��ȯ"};
	}
	
	//********************���ع�����ƷhorizontalView�Լ��������Marker�¼�*****************************//
	
	private void initView(LayoutInflater inflater){
		 for (int i = 0; i < mShareObject.length; i++) {
			    views.add(inflater.inflate(R.layout.share_view, mShowLayout, false));
			    textViews.add((TextView)views.get(i).findViewById(R.id.text));
			    textViews.get(i).setText(mShareObject[i]);
			    
			    final int k=i;
			    
                views.get(i).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(chooseNum!=k){
							
							  aMap.clear();
							  mLocMarker=null;
							  mLoveMarker=null;
							  addMarker(latLng1);
							  addLoveding(aMap.getCameraPosition());
							  
							  shareMarkers.clear();
							
						}
						 
							
		                chooseNum=k;
							
						for (int j = 0; j < textViews.size(); j++) {
							textViews.get(j).setBackgroundColor(Color.parseColor("#FFFFFF"));
						}
						
			  			textViews.get(k).setBackgroundColor(Color.parseColor("#999999"));
						
						BmobQuery<shareObject> query=new BmobQuery<shareObject>();
						query.addWhereEqualTo("isWorking", false);
						query.addWhereNotEqualTo("myUser", MyUser.getCurrentUser(MyUser.class));
						if(!textViews.get(k).getText().toString().equals("ȫ��")){
							query.addWhereEqualTo("title", textViews.get(k).getText().toString());
						}
						query.addWhereNear("objectPoint", new BmobGeoPoint(lon, lat));
						query.setLimit(10);
						query.findObjects(new FindListener<shareObject>() {

							@Override
							public void done(List<shareObject> list,
									BmobException e) {
								if(e==null){
									
									    MarkerOptions options=null;
									    if(textViews.get(k).getText().toString().equals("ȫ��")){  //����title��ʾ
									    	
									    	for (int j = 0; j < list.size(); j++) {
									    		
									    		if(list.get(j).getTitle().equals("��ɡ")){
									    		options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.san)));
										     	}else if(list.get(j).getTitle().equals("�·�")){
										     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fu)));
										     	}else if(list.get(j).getTitle().equals("ʳ��")){
											     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.food)));
											    }else if(list.get(j).getTitle().equals("����")){
											     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.car)));
											    }else if(list.get(j).getTitle().equals("�鼮")){
											     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.book)));
											    }else if(list.get(j).getTitle().equals("��Ӱ��")){
											     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.camera)));
											    }else if(list.get(j).getTitle().equals("����")){
											     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.computer)));
											    }else if(list.get(j).getTitle().equals("�ֻ�")){
											     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.mobile)));
											    }else if(list.get(j).getTitle().equals("��ȯ")){
											     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.quan)));
											    }
									    		
									    		Marker marker= aMap.addMarker(options);
												marker.setObject(list.get(j));
									    		
												shareMarkers.add(marker);
									    	}
									   	
									    
									    }else {
											
									    	for (int j = 0; j < list.size(); j++) {
												
									    		    if(list.get(j).getTitle().equals("��ɡ")){
										    		options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.san)));
											     	}else if(list.get(j).getTitle().equals("�·�")){
											     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fu)));
											     	}else if(list.get(j).getTitle().equals("ʳ��")){
												     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.food)));
												    }else if(list.get(j).getTitle().equals("����")){
												     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.car)));
												    }else if(list.get(j).getTitle().equals("�鼮")){
												     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.book)));
												    }else if(list.get(j).getTitle().equals("��Ӱ��")){
												     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.camera)));
												    }else if(list.get(j).getTitle().equals("����")){
												     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.computer)));
												    }else if(list.get(j).getTitle().equals("�ֻ�")){
												     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.mobile)));
												    }else if(list.get(j).getTitle().equals("��ȯ")){
												     	options=new MarkerOptions().position(new LatLng(list.get(j).getObjectionPoint().getLatitude(), list.get(j).getObjectionPoint().getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.quan)));
												    }
										    		
										    		Marker marker= aMap.addMarker(options);
													marker.setObject(list.get(j));
										    		
													shareMarkers.add(marker);
									    		
									    		
											}
									    	
									    	
									    	
										}
									
								}else {
									 Toast.makeText(context, "��ǰ���ִ������Ժ�����",Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					}
				});
			    
			    mShowLayout.addView(views.get(i));
			    
			    
		}
		 
		 
		 
	}
	
	//�������
	public float getCharacterWidth(TextView tv){
	if(null == tv) 
		return 0f;
	
	return getCharacterWidth(tv.getText().toString(),tv.getTextSize()) * tv.getScaleX();
	}
	
	
	//��ȡÿ���ַ��Ŀ����������
	public float getCharacterWidth(String text, float size){
	if(null == text || "".equals(text))
	return 0;
	float width = 0;
	Paint paint = new Paint();
	paint.setTextSize(size);
	float text_width = paint.measureText(text);//�õ����峤��
	width = text_width/text.length();//ÿһ���ַ��ĳ���
	return width;
	}
	
	
}
