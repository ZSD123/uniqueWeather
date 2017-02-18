package activity;


import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import receiver.uqdateReceive;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.UpdateListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.services.a.bu;
import com.uniqueweather.app.R;

import Util.Http;
import Util.HttpCallbackListener;
import Util.Utility;
import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class myAccountAct extends Activity implements AMapLocationListener {
	private Spinner spinner1;    //性别的
	private EditText editText3;   //生日EditText
    private Spinner spinner2;    //职业
    private EditText editText6;  //所在地EditText
    private String address="http://route.showapi.com/238-2";  //经纬度转化为地址
	private AMapLocationClient mLocationClient=null;
	private SharedPreferences pre;
	private AMapLocationClientOption mLocationClientOption=null;
	private EditText editText7;   //故乡EditText
	private EditText editText1;   //尊称EditText
	private EditText editText2;   //年龄EditText
	private EditText editText4;   //星座EditText
	private EditText editText5;   //学校EditText
    
	private Button button;       //编辑Button
	private Button button1;      //取消Button
	@Override
     public void onCreate(Bundle savedInstanceState)
	 {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mydata);
	    spinner1=(Spinner)findViewById(R.id.spinner1);
	    spinner2=(Spinner)findViewById(R.id.spinner2);
	    editText1=(EditText)findViewById(R.id.zunchen);
	    editText2=(EditText)findViewById(R.id.nianling);
	    editText3=(EditText)findViewById(R.id.shengri);
	    editText4=(EditText)findViewById(R.id.xingzuo);
	    editText5=(EditText)findViewById(R.id.xuexiao);
	    editText6=(EditText)findViewById(R.id.suozaidi);
	    editText7=(EditText)findViewById(R.id.guxiang);
	    
	    
	    button=(Button)findViewById(R.id.button);
	    button1=(Button)findViewById(R.id.button1);
	    
	    mLocationClient=new AMapLocationClient(myAccountAct.this);
	    mLocationClient.setLocationListener(this);
	    mLocationClientOption=new AMapLocationClientOption();
	    mLocationClientOption.setOnceLocation(true);
	    mLocationClientOption.setMockEnable(true);
		mLocationClientOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
	    mLocationClient.setLocationOption(mLocationClientOption);
		
	    editText1.setText(getIntent().getExtras().getString("name"));
	    
	    final MyUser userInfo=MyUser.getCurrentUser(myAccountAct.this,MyUser.class);
	    
	    BmobQuery<MyUser> query=new BmobQuery<MyUser>("_User");
	    query.addWhereEqualTo("username",userInfo.getUsername());
	    query.findObjects(myAccountAct.this,new FindCallback() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				if(arg0==9016)
				    Toast.makeText(myAccountAct.this,"连接失败，请稍后再试，无网络连接，请检查您的手机网络", Toast.LENGTH_SHORT).show();
				else {
					Toast.makeText(myAccountAct.this,"连接失败，请稍后再试，"+arg1,Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				try {
					JSONObject jsonObject=jsonArray.getJSONObject(0);
					String sex=jsonObject.getString("sex");
					if(sex.equals("男"))
						spinner1.setSelection(0);
					else if (sex.equals("女")) {
						spinner1.setSelection(1);
					}
					editText2.setText(jsonObject.getString("age")+"岁");
					editText3.setText(jsonObject.getString("shengri"));
					editText4.setText(jsonObject.getString("constellation"));
					spinner2.setSelection(jsonObject.getInt("zhiye"));
					editText5.setText(jsonObject.getString("school"));
					editText6.setText(jsonObject.getString("suozaidi"));
					editText7.setText(jsonObject.getString("guxiang"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	    
	    pre=PreferenceManager.getDefaultSharedPreferences(this);
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
	    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr);
	    ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr1);
	    spinner1.setAdapter(arrayAdapter);
	    spinner2.setAdapter(arrayAdapter2);
	    spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner=(Spinner)parent;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(myAccountAct.this,"没有选",Toast.LENGTH_SHORT).show();
				
			}
		   
	    });
	    editText3.setOnTouchListener(new OnTouchListener() {    //生日EditText
			
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
					AlertDialog.Builder builder=new AlertDialog.Builder(myAccountAct.this);
					View view1=View.inflate(myAccountAct.this,R.layout.date_time_dialog,null);
					final DatePicker datePicker=(DatePicker)view1.findViewById(R.id.date_picker);
					builder.setView(view1);
					
					Calendar cal=Calendar.getInstance();
					cal.setTimeInMillis(System.currentTimeMillis());
					datePicker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),null);
					
					builder.setTitle("选取您的生日");
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
						    dialog.cancel();
						}
					});
					Dialog dialog=builder.create();
					dialog.show();
				}
				return false;
			}
		});
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
				  spinner1.setClickable(true);
				  spinner2.setClickable(true);
				  button.setText("保    存");
				  button1.setVisibility(View.VISIBLE);
	            }
	            else if(button.getText().equals("保    存")){
	            	editText1.setEnabled(false); 
	            	editText2.setEnabled(false);
	            	editText3.setEnabled(false);
	            	editText4.setEnabled(false);
	            	editText5.setEnabled(false);
	            	editText6.setEnabled(false);
	            	editText7.setEnabled(false);
	            	spinner1.setClickable(false);
	            	spinner2.setClickable(false);
	            	button.setText("编    辑");
	            	button1.setVisibility(View.GONE);
	            	MyUser newUser=new MyUser();
	            	newUser.setNick(editText1.getText().toString());
	            	newUser.setSex(spinner1.getSelectedItem().toString());
	            	newUser.setAge(editText2.getText().toString());
	            	newUser.setShengri(editText3.getText().toString());
	            	newUser.setConstellation(editText4.getText().toString());
	            	newUser.setZhiye(spinner2.getSelectedItemPosition());
	            	newUser.setSchool(editText5.getText().toString());
	            	newUser.setSuozaidi(editText6.getText().toString());
	            	newUser.setGuxiang(editText7.getText().toString());
	            	newUser.update(myAccountAct.this, userInfo.getObjectId(),new UpdateListener() {
						
						@Override
						public void onSuccess() {
							Toast.makeText(myAccountAct.this, "保存成功", Toast.LENGTH_SHORT).show();
							
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							Toast.makeText(myAccountAct.this,"保存失败，"+arg1 ,Toast.LENGTH_SHORT).show();
						}
					});
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
            	spinner1.setClickable(false);
            	spinner2.setClickable(false);
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
		    	    Log.d("Main","1");
		    	    Log.d("Main", aMapLocation.getCity());
		    	    editText2.setText(aMapLocation.getCity()+aMapLocation.getDistrict());
				 }
		else {
		Toast.makeText(myAccountAct.this, "errorcode:"+aMapLocation.getErrorCode()+",errorInfo:"+aMapLocation.getErrorInfo(), Toast.LENGTH_LONG).show();
		Log.d("Main","errorcode:"+aMapLocation.getErrorCode()+",errorInfo:"+aMapLocation.getErrorInfo());
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
	
       
}
