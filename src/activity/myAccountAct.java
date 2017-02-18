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
	private Spinner spinner1;    //�Ա��
	private EditText editText3;   //����EditText
    private Spinner spinner2;    //ְҵ
    private EditText editText6;  //���ڵ�EditText
    private String address="http://route.showapi.com/238-2";  //��γ��ת��Ϊ��ַ
	private AMapLocationClient mLocationClient=null;
	private SharedPreferences pre;
	private AMapLocationClientOption mLocationClientOption=null;
	private EditText editText7;   //����EditText
	private EditText editText1;   //���EditText
	private EditText editText2;   //����EditText
	private EditText editText4;   //����EditText
	private EditText editText5;   //ѧУEditText
    
	private Button button;       //�༭Button
	private Button button1;      //ȡ��Button
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
				    Toast.makeText(myAccountAct.this,"����ʧ�ܣ����Ժ����ԣ����������ӣ����������ֻ�����", Toast.LENGTH_SHORT).show();
				else {
					Toast.makeText(myAccountAct.this,"����ʧ�ܣ����Ժ����ԣ�"+arg1,Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				try {
					JSONObject jsonObject=jsonArray.getJSONObject(0);
					String sex=jsonObject.getString("sex");
					if(sex.equals("��"))
						spinner1.setSelection(0);
					else if (sex.equals("Ů")) {
						spinner1.setSelection(1);
					}
					editText2.setText(jsonObject.getString("age")+"��");
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
	    	"��",
	    	"Ů",
	    	"����"
	    };
	    final String arr1[]=new String[]{
	     "IT��ҵ:������/�����/ͨ��",
	     "���죺����/����/����",
	     "ҽ�ƣ�ҽѧ/����/��ҩ",
	     "���ڣ�����/Ͷ��/����",
	     "��ҵ����ҵ/����ҵ/���徭Ӫ",
	     "�Ļ����Ļ�/���/��ý",
	     "����:����/����/����",
	     "����:��ʦ/����",
	     "����:����/��ѵ",
	     "����:����Ա/����/��ҵ��λ",
	     "ģ��",
	     "������Ա������/�ս�",
	     "ѧ��",
	     "����"
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
				Toast.makeText(myAccountAct.this,"û��ѡ",Toast.LENGTH_SHORT).show();
				
			}
		   
	    });
	    editText3.setOnTouchListener(new OnTouchListener() {    //����EditText
			
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
					
					builder.setTitle("ѡȡ��������");
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						 	StringBuffer sb=new StringBuffer();
						    sb.append(String.format("%d��%02d��%02d��", 
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
	    editText6.setOnClickListener(new OnClickListener() {    //���ڵ�EditText
			
			@Override
			public void onClick(View view) {
			     AlertDialog.Builder builder=new AlertDialog.Builder(myAccountAct.this);
			     final String[] xuanzeweizhi={"�Զ���λ","�Լ�ѡ��"};
			     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						  if(which==0){
							  Toast.makeText(myAccountAct.this,"���Եȣ����϶�λ", Toast.LENGTH_SHORT).show();
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
	            if(button.getText().equals("��    ��"))  
	            { editText1.setEnabled(true); 	
	              editText2.setEnabled(true); 	
	              editText3.setEnabled(true); 	
	              editText4.setEnabled(true); 	
	              editText5.setEnabled(true); 	
	              editText6.setEnabled(true); 	
	              editText7.setEnabled(true); 	
				  spinner1.setClickable(true);
				  spinner2.setClickable(true);
				  button.setText("��    ��");
				  button1.setVisibility(View.VISIBLE);
	            }
	            else if(button.getText().equals("��    ��")){
	            	editText1.setEnabled(false); 
	            	editText2.setEnabled(false);
	            	editText3.setEnabled(false);
	            	editText4.setEnabled(false);
	            	editText5.setEnabled(false);
	            	editText6.setEnabled(false);
	            	editText7.setEnabled(false);
	            	spinner1.setClickable(false);
	            	spinner2.setClickable(false);
	            	button.setText("��    ��");
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
							Toast.makeText(myAccountAct.this, "����ɹ�", Toast.LENGTH_SHORT).show();
							
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							Toast.makeText(myAccountAct.this,"����ʧ�ܣ�"+arg1 ,Toast.LENGTH_SHORT).show();
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
            	button.setText("��    ��");
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
