package activity;


import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import receiver.uqdateReceive;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.services.a.bu;
import com.sharefriend.app.R;

import Util.Http;
import Util.HttpCallbackListener;
import Util.Utility;
import android.R.anim;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class myAccountAct extends baseActivity implements AMapLocationListener,OnTouchListener {
	private Spinner spinner1;    //性别的
	private EditText editText3;   //生日EditText
    private Spinner spinner2;    //职业
    private EditText editText6;  //所在地EditText
    private String address="http://route.showapi.com/238-2";  //经纬度转化为地址
	private AMapLocationClient mLocationClient=null;
	private AMapLocationClientOption mLocationClientOption=null;
	private EditText editText7;   //故乡EditText
	private EditText editText1;   //尊称EditText
	private EditText editText2;   //年龄EditText
	private EditText editText4;   //星座EditText
	private EditText editText5;   //学校EditText
    private EditText editText8;   //手机号EditText
    private EditText editText9;    //邮箱EditText
	
	private Button button;       //编辑Button
	private Button button1;      //取消Button
	
	private ArrayAdapter<String> arrayAdapter;
	private ArrayAdapter<String> arrayAdapter2;
	private int year;
	private int month;
	private int day;
	@Override
     public void onCreate(Bundle savedInstanceState)
	 { 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mydata);  
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		int designNum=fragmentChat.pre.getInt("design", 0);
		
		ScrollView scrollView=(ScrollView)findViewById(R.id.scrollview);
	
		
	    spinner1=(Spinner)findViewById(R.id.spinner1);
	    spinner2=(Spinner)findViewById(R.id.spinner2);
	    editText1=(EditText)findViewById(R.id.zunchen);
	    editText2=(EditText)findViewById(R.id.nianling);
	    editText3=(EditText)findViewById(R.id.shengri);
	    editText4=(EditText)findViewById(R.id.xingzuo);
	    editText5=(EditText)findViewById(R.id.xuexiao);
	    editText6=(EditText)findViewById(R.id.suozaidi);
	    editText7=(EditText)findViewById(R.id.guxiang);
	    editText8=(EditText)findViewById(R.id.shoujihao);
	    editText9=(EditText)findViewById(R.id.youxiang);
	    
	    TextView textView1=(TextView)findViewById(R.id.zunchenText);
	    TextView textView2=(TextView)findViewById(R.id.sexText);
	    TextView textView3=(TextView)findViewById(R.id.ageText);
	    TextView textView4=(TextView)findViewById(R.id.birthText);
	    TextView textView5=(TextView)findViewById(R.id.xingzuoText);
	    TextView textView6=(TextView)findViewById(R.id.zhiyeText);
	    TextView textView7=(TextView)findViewById(R.id.mobileText);
	    TextView textView8=(TextView)findViewById(R.id.emailText);
	    TextView textView9=(TextView)findViewById(R.id.schoolText);
	    TextView textView10=(TextView)findViewById(R.id.suozaidiText);
	    TextView textView11=(TextView)findViewById(R.id.guxiangText);
	  
	    
	    button=(Button)findViewById(R.id.button);
	    button1=(Button)findViewById(R.id.button1);
	    
	    
	  		if(designNum==4){
	  			scrollView.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText1.setTextColor(Color.parseColor("#A2C0DE"));
	  			editText2.setTextColor(Color.parseColor("#A2C0DE"));
	  			editText3.setTextColor(Color.parseColor("#A2C0DE"));
	  			editText4.setTextColor(Color.parseColor("#A2C0DE"));
	  			editText5.setTextColor(Color.parseColor("#A2C0DE"));
	  			editText6.setTextColor(Color.parseColor("#A2C0DE"));
	  			editText7.setTextColor(Color.parseColor("#A2C0DE"));
	  			editText8.setTextColor(Color.parseColor("#A2C0DE"));
	  			editText9.setTextColor(Color.parseColor("#A2C0DE"));
 			
	  			editText1.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText2.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText3.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText4.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText5.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText6.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText7.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText8.setBackgroundColor(Color.parseColor("#051C3D"));
	  			editText9.setBackgroundColor(Color.parseColor("#051C3D"));
	  			
	  			textView1.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView2.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView3.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView4.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView5.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView6.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView7.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView8.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView9.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView10.setTextColor(Color.parseColor("#A2C0DE"));
	  			textView11.setTextColor(Color.parseColor("#A2C0DE"));
	  			
	  			button.setTextColor(Color.parseColor("#A2C0DE"));
	  			button1.setTextColor(Color.parseColor("#A2C0DE"));
	  			
	  			spinner1.setBackgroundColor(Color.parseColor("#278CCE"));
	  			spinner2.setBackgroundColor(Color.parseColor("#278CCE"));
	  		}
	    
	    mLocationClient=new AMapLocationClient(myAccountAct.this);
	    mLocationClient.setLocationListener(this);
	    mLocationClientOption=new AMapLocationClientOption();
	    mLocationClientOption.setOnceLocation(true);
	    mLocationClientOption.setMockEnable(true);
		mLocationClientOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
	    mLocationClient.setLocationOption(mLocationClientOption);
		
	    editText1.setText(getIntent().getExtras().getString("name"));
	    
		final MyUser userInfo=BmobUser.getCurrentUser(MyUser.class);
	    
	   
	    editText2.setText((String)MyUser.getObjectByKey("age"));
	    editText3.setText((String)MyUser.getObjectByKey("shengri"));
	    editText4.setText((String)MyUser.getObjectByKey("constellation"));

	    editText5.setText((String)MyUser.getObjectByKey("school"));
	    editText6.setText((String)MyUser.getObjectByKey("suozaidi"));
	    editText7.setText((String)MyUser.getObjectByKey("guxiang"));
	    if(loginAct.isMobileNO(BmobUser.getCurrentUser(MyUser.class).getUsername())){
	    	editText8.setText(BmobUser.getCurrentUser(MyUser.class).getUsername());
	    }else if(loginAct.isEmail(BmobUser.getCurrentUser(MyUser.class).getUsername())){
			editText9.setText(BmobUser.getCurrentUser(MyUser.class).getUsername());
		}
	    
	    final String arr[]=new String[]{
	    	"男",
	    	"女",
	    	"保密"
	    };
	    final String arr1[]=new String[]{
	     "IT行业:互联网/计算机/通信",
	     "制造：生产/工艺/制造",
	     "医疗：医学/护理/制药",
	     "金融：银行/投资/保险",
	     "商业：商业/服务业/个体经营",
	     "文化：文化/广告/传媒",
	     "艺术:娱乐/艺术/表演",
	     "法律:律师/法务",
	     "教育:教育/培训",
	     "行政:公务员/行政/事业单位",
	     "模特",
	     "乘务人员：机长/空姐",
	     "学生",
	     "其他"
	    };
	    arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr);
        arrayAdapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr1);
	    spinner1.setAdapter(arrayAdapter);
	    spinner2.setAdapter(arrayAdapter2);
	    
	    spinner1.setEnabled(false);
	    spinner2.setEnabled(false);
	    
	    String sex=(String)MyUser.getObjectByKey("sex");
	    if(sex!=null)
	       if(sex.equals("男")){
	    	  spinner1.setSelection(0);
	    	  
	       }
	      else if(sex.equals("女")){
			  spinner1.setSelection(1);
		   }else if(sex.equals("保密")){
			  spinner1.setSelection(2);
	    	}
	    int sel=0;
	   for (int i = 0; i < arr1.length; i++) {
			if(arr1[i].equals((String)MyUser.getObjectByKey("zhiye")))
				sel=i;
		}
	    spinner2.setSelection(sel);
	    
	    editText2.setOnTouchListener(this);
	    editText3.setOnTouchListener(this);
	    editText4.setOnTouchListener(this);
	    editText6.setOnClickListener(new OnClickListener() {    //所在地EditText
			
			@Override
			public void onClick(View view) {
			     AlertDialog.Builder builder=new AlertDialog.Builder(myAccountAct.this);
			     final String[] xuanzeweizhi={"自动定位","自己选择"};
			     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						  if(which==0){
							  Toast.makeText(myAccountAct.this,"请稍等，马上定位", Toast.LENGTH_SHORT).show();
						      mLocationClient.startLocation();
						  }else if(which==1){
							  Intent intent=new Intent(myAccountAct.this,chooseAreaActivity.class);
							  startActivityForResult(intent, 0);
						  }
					}
				});
			     builder.show();
			}
		});
	    editText7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(myAccountAct.this,chooseAreaActivity.class);
				startActivityForResult(intent, 1);
				
			}
		});
	    
	    button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
	            if(button.getText().equals("编    辑"))  
	            { editText1.setEnabled(true); 	
	              editText2.setEnabled(true); 	
	              editText3.setEnabled(true); 	
	              editText4.setEnabled(true); 	
	              editText5.setEnabled(true); 	
	              editText6.setEnabled(true); 	
	              editText7.setEnabled(true); 	
				  spinner1.setEnabled(true);
				  spinner2.setEnabled(true);
				  button.setText("保    存");
				  button1.setVisibility(View.VISIBLE);
	            }
	            else if(button.getText().equals("保    存")&&!editText1.getText().equals("")){
	            	editText1.setEnabled(false); 
	            	editText2.setEnabled(false);
	            	editText3.setEnabled(false);
	            	editText4.setEnabled(false);
	            	editText5.setEnabled(false);
	            	editText6.setEnabled(false);
	            	editText7.setEnabled(false);
	            	spinner1.setEnabled(false);
	            	spinner2.setEnabled(false);
	            	button.setText("编    辑");
	            	button1.setVisibility(View.GONE);
	            	
	            	weather_info.myUserdb.checkandSaveUpdateN((String)MyUser.getObjectByKey("username"), editText1.getText().toString());
	            	fragmentChat.userName.setText(editText1.getText().toString());
	            	
	            	MyUser newUser=new MyUser();
	            	newUser.setNick(editText1.getText().toString());
	            	newUser.setSex(spinner1.getSelectedItem().toString());
	            	newUser.setAge(editText2.getText().toString());
	            	newUser.setShengri(editText3.getText().toString());
	            	newUser.setConstellation(editText4.getText().toString());
	            	newUser.setZhiye(arr1[spinner2.getSelectedItemPosition()]);
	            	newUser.setSchool(editText5.getText().toString());
	            	newUser.setSuozaidi(editText6.getText().toString());
	            	newUser.setGuxiang(editText7.getText().toString());
	            	newUser.update( userInfo.getObjectId(),new UpdateListener() {
						
					
						@Override
						public void done(BmobException e) {
							if(e==null){
								Toast.makeText(myAccountAct.this,"更新信息成功", Toast.LENGTH_SHORT).show();
							}else {
								Toast.makeText(myAccountAct.this,"失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
							}
							
						}
					});
				}else if(editText1.getText().equals("")){
					Toast.makeText(myAccountAct.this,"尊称不能为空", Toast.LENGTH_SHORT).show();
				}
			}
		});
	    button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				editText1.setEnabled(false); 
            	editText2.setEnabled(false);
            	editText3.setEnabled(false);
            	editText4.setEnabled(false);
            	editText5.setEnabled(false);
            	editText6.setEnabled(false);
            	editText7.setEnabled(false);
            	spinner1.setEnabled(false);
            	spinner2.setEnabled(false);
            	button.setText("编    辑");
            	button1.setVisibility(View.GONE);
				
			}
		});
	 }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if(resultCode==RESULT_OK){
				String city=data.getExtras().getString("selectedCityName");
				String county=data.getExtras().getString("countyName");
				editText6.setText(city+county);
			}
			break;
		case 1:
			if(resultCode==RESULT_OK){
				String city=data.getExtras().getString("selectedCityName");
				String county=data.getExtras().getString("countyName");
				editText7.setText(city+county);
			}
		default:
			break;
		}
		
	}
	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation != null) {
		    if (aMapLocation.getErrorCode() == 0) {
		    	    editText6.setText(aMapLocation.getCity()+aMapLocation.getDistrict());
				 }
		else {
		Toast.makeText(myAccountAct.this, "errorcode:"+aMapLocation.getErrorCode()+",errorInfo:"+aMapLocation.getErrorInfo(), Toast.LENGTH_LONG).show();
		
  	}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.stopLocation();
			mLocationClient.onDestroy();
		}
		mLocationClient = null;
	}
	private void getAstro(int month, int day) {
        String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
                "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座","摩羯座"};
        int[] arr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};// 两个星座分割日
        if(day<arr[month-1]){
        	editText4.setText(astro[month-1]);
        }else {
			editText4.setText(astro[month]);
		}
        
    }
	
	@Override
	public boolean onTouch(View view, MotionEvent arg1) {
		if(arg1.getAction()==MotionEvent.ACTION_DOWN)
		switch (view.getId()) {
		case R.id.nianling:
		case R.id.xingzuo:
		case R.id.shengri:
			AlertDialog.Builder builder=new AlertDialog.Builder(myAccountAct.this);
			View view1=View.inflate(myAccountAct.this,R.layout.date_time_dialog,null);
			final DatePicker datePicker=(DatePicker)view1.findViewById(R.id.date_picker);
			builder.setView(view1);
			
			final Calendar cal=Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			String shengri=editText3.getText().toString();
		    char[]b=shengri.toCharArray();
		    if(b.length>0)
		    {	
		        datePicker.init(Integer.parseInt(""+b[0]+b[1]+b[2]+b[3]),Integer.parseInt(""+b[5]+b[6])-1, Integer.parseInt(""+b[8]+b[9]), null);
		    }
		    else {
		    	datePicker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),null);
			}
			builder.setTitle("选取您的生日,系统将自动转化为年龄和星座");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				 	StringBuffer sb=new StringBuffer();
				    sb.append(String.format("%d年%02d月%02d日", 
				    		datePicker.getYear(),
				    		datePicker.getMonth()+1,
				    		datePicker.getDayOfMonth()));
				    sb.append("  ");
				    editText3.setText(sb);
				    
				    int nowyear=cal.get(Calendar.YEAR);
				    int birth=nowyear-datePicker.getYear();
				    int nowmonth=cal.get(Calendar.MONTH)+1;
				    int nowday=cal.get(Calendar.DAY_OF_MONTH);
				    if(nowmonth>datePicker.getMonth()+1){

				    }else if(datePicker.getMonth()+1==nowmonth){
				    	if(nowday>=datePicker.getDayOfMonth()){
				    	}else{
				    		birth--;
				    
				    	}
				    }else{
				    	birth--;

				    }
				    editText2.setText(birth+"岁");
				    getAstro(datePicker.getMonth()+1,datePicker.getDayOfMonth());
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			});
			Dialog dialog=builder.create();
			dialog.show();
			
			break;
        default:
			break;
		}
		return false;
	}
	
       
}
