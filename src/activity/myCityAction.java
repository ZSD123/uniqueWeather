package activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import model.jingdian;
import model.jingdiancity;
import model.myCity;

import com.uniqueweather.app.R;

import db.jingdianDB;
import db.mycityDB;

import Util.Http;
import Util.HttpCallbackListener;
import Util.Utility;
import adapter.myCityAdapter;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class myCityAction extends Activity {
	 public List<myCity> dataList=new ArrayList<myCity>();
	 public String citynsString;
	 public String temp;
	 public RelativeLayout mycitylayout;
	 public jingdianDB dJingdianDB;
	 public mycityDB mycitydb;
	 public String c="";
	 public Bitmap bitmap1;
	 public boolean jingdiancityflag;
	 public boolean jingdianflag;
	 public SharedPreferences pre;
	 public SharedPreferences.Editor editor;
	 public Button buttonadd;
	 public Button buttonjian;
	 public static int i=0;
	 public myCity mycity1;
	 public static String k="1";
	 public static String album1=Environment.getExternalStorageDirectory()+"/download/"+"jingdian"+".png";
	 public static String album2=Environment.getExternalStorageDirectory()+"/download/"+"jingdian"+k+".png";
     @Override
     public void onCreate(Bundle savedInstancestate)
     {   
    	 super.onCreate(savedInstancestate);
    	 requestWindowFeature(Window.FEATURE_NO_TITLE);
    	 setContentView(R.layout.mycity);
    	 ListView listView=(ListView)findViewById(R.id.myCityList);
    	 myCityAdapter adapter=new myCityAdapter(myCityAction.this,R.layout.mycitylist, dataList);
    	 listView.setAdapter(adapter);
    	 buttonadd=(Button)findViewById(R.id.add);
    	 buttonjian=(Button)findViewById(R.id.jian);
    	 citynsString=getIntent().getStringExtra("selectedCityName");
    	 temp=getIntent().getStringExtra("temp");
    	 char b[]=citynsString.toCharArray();
    	 for(int i=0;i<b.length-1;i++)
    	 {       		  
    	     c=""+c+b[i];
    	 }
    	 init(c);
    	 buttonadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			     Intent intent=new Intent(myCityAction.this,chooseAreaActivity.class);
			     startActivityForResult(intent, 1);
				 
			}
		});
    	
     }
     public void init(final String c)
     {   
    	 dJingdianDB=jingdianDB.getInstance(this);
    	 mycitydb=mycityDB.getInstance(this);
    	 i=mycitydb.loadmycityId();
         pre=PreferenceManager.getDefaultSharedPreferences(myCityAction.this);
         editor=PreferenceManager.getDefaultSharedPreferences(myCityAction.this).edit();
         jingdiancityflag=pre.getBoolean("jingdiancityflag", false);
    	 myCity mycity=new myCity(citynsString,weather_info.ALBUM_PATH , temp,album1);
    	 dataList.add(mycity);
    	 for(int j=0;j<i;j++)
    	 {
    		
    	 }
    	 for(int j=0;j<i;j++)
    	 {   
    		 myCity mycity1=new myCity(mycityName[i], weather, c)
    	 }
    	 if(!jingdiancityflag)
    	 {
    	 Http.sendjingdiancityRequest(new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if(!response.isEmpty())
				{editor.putBoolean("jingdiancityflag", true);
				 editor.commit();
				Utility.handlejingdiancity(response,myCityAction.this);
				final jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
				 Http.sendjingdianRequest(myJingdiancity.getjingdiancityid(), new HttpCallbackListener() {
						
						@Override
						public void onFinish(String response) {
							Utility.handlejingdian(response,myCityAction.this);
							List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
					    	 jingdian jingdian2=jingdians.get(0);
					    	 Bitmap bitmap=Utility.getPicture(jingdian2.getImageUrl());
					    	 savePicture(album1, bitmap);
							 
					    	 
						}
					});}}
		});
    	 }
    	 else { 
    		 
    		 final jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
    		 List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
    		 if(jingdians.size()>0)
    		 {  
			     final jingdian jingdian2=jingdians.get(0);
				bitmap1=Utility.getPicture(jingdian2.getImageUrl());
				if(bitmap1!=null)
		         savePicture(album1, bitmap1);


	 }	
    		 
    		 else {
    			 Http.sendjingdianRequest(c, new HttpCallbackListener() {
					
					@Override
					public void onFinish(String response) 
					{
						Utility.handlejingdian(response, myCityAction.this);
						List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
						 if(jingdians.size()>0)
			    		 {  
			    			 jingdian selectedJingdian=jingdians.get(0);
			    			 Bitmap bitmap=Utility.getPicture(selectedJingdian.getImageUrl());
			    			 savePicture(album1, bitmap);
			    		 }	
					}
				});
			}
		}
    	 
    	 
     }
     public void savePicture(String address,Bitmap bitmap)
     {
    	 File file=new File(address);
    	 try {
    		Log.d("Main","save");
			FileOutputStream outputStream=new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		    outputStream.flush();
		    outputStream.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
    	
    	 
     }
     @Override
     public void onActivityResult(int resquestCode,int resultCode,Intent data)
     {
    	 if(resultCode==RESULT_OK)
    	 {
    		 switch (resquestCode) {
			case 1:
				final String name= data.getStringExtra("selectedCityName");
				 Http.sendWeatherRequest(name,weather_info.address3, new HttpCallbackListener()
		 			{
		 				@Override
		 				public void onFinish(String response)
		 				{   
		 					handleWeatherByTable(response,name);
		 					Bitmap bitmap=null;
		 					bitmap=Utility.getPicture(mycity1.getMyCityWeather());
		 					if(bitmap!=null)
		 					{ savePicture(weather_info.ALBUM_PATH1,bitmap);
		 					  String lcun=pre.getString("l","1");
		 					  
		 					}
		 					bitmap=Utility.getPicture(mycity1.getMyCityPic());
		 					if(bitmap!=null)
		 						savePicture(album2, bitmap);
		 				    runOnUiThread(new Runnable(){
		 				    @Override
		 				    public void run(){
		 				    	showWeather();
		 				    }});
		 						
		 				}
		 			});
                break;
			default:
				break;
			}
    			 
    	 }
     }
     public void handleWeatherByTable(String response,String name)
     {
    	 try {
    			JSONObject jsonobject=new JSONObject(response);
    			JSONObject obj1=jsonobject.getJSONObject("showapi_res_body");
    			JSONObject obj2=obj1.getJSONObject("now");
    			 mycity1=new myCity(name, obj2.getString("weather"),obj2.getString("temperature"),obj2.getString("weather_pic"));
    			mycitydb.saveMyCity(mycity1);
		} catch (Exception e) {
			e.printStackTrace();
		}
     }
}
