package activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.R.bool;
import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class myCityAction extends Activity {
	 public List<myCity> dataList=new ArrayList<myCity>();
	 public String citynsString;
	 public String temp;  //主城市温度
	 public String temp1;//其他添加城市温度
	 public RelativeLayout mycitylayout;
	 public jingdianDB dJingdianDB;
	 public mycityDB mycitydb;
	 public String c="";
	 public boolean myjingdianflag=false;//之前是否已经联网查询并且已经加载相应城市的景点
	 public boolean myjingdiancityflag=false;//之前是否已经联网查询并且存储城市和城市id
	 public Bitmap bitmap1;
	 public boolean mycitycunzai=false;//选择的城市是否已经表中存在
	 public SharedPreferences pre;
	 public SharedPreferences.Editor editor;
	 public Button buttonadd;
	 public Button buttonjian;
	 public static int i=0;
	 public myCity mycity1;
	 public String weatherPicPath;
	 public String cityPicPath;
	 public  myCityAdapter adapter;
	 public ProgressDialog progressDialog;
	 String mycityPic;//我的城市背景图URL
	 public String weather_pic;
	 public View viewitem;
	 public boolean existCityPic=false;
	 public boolean changeLocalCity=false;                                 //存在背景图片
	 public String lastLocalCity;
	 public List<myCity> myCityDeleted=new ArrayList<myCity>();
	 public ListView listView; 
	 public List<myCity> myCities=new ArrayList<myCity>(); //加载我的城市列表
	 public boolean localcityflag=false;   //当点击本地城市的时候按下返回就会取消锁定
	 public List<Integer> deletedInt=new ArrayList<Integer>();//选定的城市的id
	 public static String album1=Environment.getExternalStorageDirectory()+"/download/"+"jingdian"+".png";
	 public Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg)
			{   
				switch (msg.what) {
				case 0: 
					 adapter.remove(adapter.getItem(0));
					 adapter.insert(mycity1, 0);
				     adapter.notifyDataSetChanged();
			    	 break;
			    case 1:
			    	adapter.notifyDataSetChanged();
			    	dismissProgressDialog();

				default:
					break;
				}
			}
		 };
     @Override
     public void onCreate(Bundle savedInstancestate)
     {   Log.d("Main", "116行");
    	 super.onCreate(savedInstancestate);
    	 requestWindowFeature(Window.FEATURE_NO_TITLE);
    	 setContentView(R.layout.mycity);
    	 listView=(ListView)findViewById(R.id.myCityList);
    	 adapter=new myCityAdapter(myCityAction.this,R.layout.mycitylist, dataList);
    	 listView.setAdapter(adapter);
         pre=PreferenceManager.getDefaultSharedPreferences(myCityAction.this);
         editor=PreferenceManager.getDefaultSharedPreferences(myCityAction.this).edit();
    	 buttonadd=(Button)findViewById(R.id.add);
    	 buttonjian=(Button)findViewById(R.id.jian);
    	 citynsString=getIntent().getStringExtra("selectedCityName");
    	 temp=getIntent().getStringExtra("temp");
    	 lastLocalCity=pre.getString("lastLocalCity", "");
    	 if(!lastLocalCity.equals(citynsString))
    	 {
    		 changeLocalCity=true;
    		 editor.putString("lastLocalCity",citynsString);
    		 editor.commit();
    	 }
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
    	 buttonjian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{  if(myCityDeleted.size()==0)
				Toast.makeText(myCityAction.this,"请长按您要删除的城市",Toast.LENGTH_SHORT).show();
			  else 
			     {
				   mycitydb.deleteMyCity(myCityDeleted);
				   for(int i=1;i<adapter.getCount();i++)
				   {     for (int j = 0; j < myCityDeleted.size(); j++) 
				       {
						    if(adapter.getItem(i).getMyCityName()==myCityDeleted.get(j).getMyCityName())
						    { 
						    	adapter.remove(adapter.getItem(i));
						    	i--;
						    }
					   
				       }
		
			       }
				   myCityDeleted.clear();
				   for(int i=0;i<deletedInt.size();i++)
				      listView.getChildAt(deletedInt.get(i)).setBackgroundColor(0);
			    }
		}});
    	 listView.setOnItemLongClickListener(new OnItemLongClickListener() 
    	 {  @Override
    		public boolean onItemLongClick(AdapterView<?> arg0,View arg1,int arg2,long arg3)
    		{   
    		    viewitem=listView.getChildAt(arg2);
    		    viewitem.setBackgroundColor(Color.parseColor("#00FFFF"));//点击的项目变颜色
    		    if(arg2!=0)
    		       myCityDeleted.add(dataList.get(arg2));//加入要删除的城市列表
    		    if(arg2==0)
    		    	localcityflag=true;
                Integer integer=new Integer(arg2);
                deletedInt.add(integer);
    		    return true;
    		    
    		  
    		}
		});
		
    	
     }
     public void init(final String c)
     {  
    	 dJingdianDB=jingdianDB.getInstance(this);
    	 mycitydb=mycityDB.getInstance(this);
         myjingdiancityflag=pre.getBoolean("myjingdiancityflag",false);
         try{
             jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);     //判断数据库里是否相应城市的景点数据
    	     List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
    	     if(jingdians.size()>0)
     		 {   editor.putBoolean("myjingdianflag", true);
     		     editor.commit();
     		 }
    	else {
            	 editor.putBoolean("myjingdianflag", false);
     			 editor.commit();
			  }
         }catch(Exception e)
         {
        	 e.printStackTrace();

         }
         myjingdianflag=pre.getBoolean("myjingdianflag",false);
         firstShowLocalCity();                            //先简单地加载本地城市，防止等待时间过长                 
         if(!existCityPic||changeLocalCity) 
      {   if(!myjingdiancityflag)                                                   
         {   
             Log.d("Main","这里4");
        	 Http.sendjingdiancityRequest(new HttpCallbackListener() {   //获得景点城市的情况，目的是获取相应的id
			@Override
			public void onFinish(String response) {                        //通常不等到onFinish的数据返回就转向下面的程序                          
				if(!response.isEmpty())
				{editor.putBoolean("myjingdiancityflag",true);
				 editor.commit();
				  Utility.handlejingdiancity(response,myCityAction.this);
                  Http.sendjingdianRequest(dJingdianDB.loadjingdianCity(c).getjingdiancityid(), new HttpCallbackListener() {
							
							@Override
							public void onFinish(String response) 
							{   
								Utility.handlejingdian(response, myCityAction.this);
								jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
								if(myJingdiancity.getjingdiancityName()!=null)
								{List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
								 if(jingdians.size()>0)
					    		 {   editor.putBoolean("myjingdianflag", true);
					    		     editor.commit();
					    			 final jingdian selectedJingdian=jingdians.get(0);
					    			 new Thread(new Runnable() {
					 					@Override
					 					public void run() {
					 						bitmap1=Utility.getPicture(selectedJingdian.getImageUrl());
					 		  				 if(bitmap1!=null)
					 		  		           savePicture(album1, bitmap1);
					 		  			  	 mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,album1);//第二个参数应该一致，都是本地地址
					 		  		    	 Message message=new Message();
					 		  		    	 message.what=0;
					 		  		    	 handler.sendMessage(message);
					 		  		
					 		  		    	 }
					 				}).start();
					    		  	
					    		 }	
								}
							     else {
						        	     mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,null);//第二个参数应该一致，都是本地地址
								    	 Message message=new Message();
								    	 message.what=0;
								    	 handler.sendMessage(message);
							
								      }
							}
						});
		         }
		         
				}
				                                                    
			});
         }
      
       else if(!myjingdianflag)               //之前是否加载了相应的城市的景点
      	{ 
      	   Http.sendjingdianRequest(dJingdianDB.loadjingdianCity(c).getjingdiancityid(), new HttpCallbackListener() {
					
					@Override
					public void onFinish(String response) 
					{ 
						Utility.handlejingdian(response, myCityAction.this);
						jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
						if(myJingdiancity.getjingdiancityName()!=null)
						{List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
						 if(jingdians.size()>0)
			    		 {  
							 editor.putBoolean("myjingdianflag", true);
			    		     editor.commit();
			    			 final jingdian selectedJingdian=jingdians.get(0);
			    			 new Thread(new Runnable() {
			 					@Override
			 					public void run() {
			 						bitmap1=Utility.getPicture(selectedJingdian.getImageUrl());
			 		  				 if(bitmap1!=null)
			 		  		           savePicture(album1, bitmap1);
			 		  			  	 mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,album1);//第二个参数应该一致，都是本地地址
			 		  		    	 Message message=new Message();
			 		  		    	 message.what=0;
			 		  		    	 handler.sendMessage(message);
			 		
			 		  		    	 }
			 				}).start();
			    		 }
						}
						   else {
				        	     mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,null);//第二个参数应该一致，都是本地地址
						    	 Message message=new Message();
						    	 message.what=0;
						    	 handler.sendMessage(message);
						
						      }
					}
				});
       }
       else {
    	
      	 jingdiancity myJingdiancity1=dJingdianDB.loadjingdianCity(c);
         if(myJingdiancity1.getjingdiancityName()!=null)
         {   List<jingdian> jingdians1=dJingdianDB.loadjingdian(myJingdiancity1);//载入相应的城市的景点
      		 if(jingdians1.size()>0)//如果已经有相应城市的景点列表
      		 {  Log.d("Main","这里2");
  			    final jingdian jingdian2=jingdians1.get(0);
  			    new Thread(new Runnable() {
					@Override
					public void run() {
						bitmap1=Utility.getPicture(jingdian2.getImageUrl());
		  				 if(bitmap1!=null)
		  		           savePicture(album1, bitmap1);
		  			  	 mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,album1);//第二个参数应该一致，都是本地地址
		  		    	 Message message=new Message();
		  		    	 message.what=0;
		  		    	 handler.sendMessage(message);
		  	
		  		    	 }
				}).start();
  			   }
		    }
         else {
        	     mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,null);//第二个参数应该一致，都是本地地
		    	 Log.d("Main","加载本地城市");
		    	 Message message=new Message();
		    	 message.what=0;
		    	 handler.sendMessage(message);
	
		      }
       }
      }
     showAllMyCity();//进入后显示数据表中所有的城市
   }
    	 
    	 
     
     private void firstShowLocalCity()            //先简单加载
     {   File file=new File(album1);
         if(file.exists())
	        {mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH,temp,null,album1);
	         existCityPic=true;
	        }
         else 
         {
        	 mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH,temp,null,null);
        	 existCityPic=false;
		 }
		 dataList.add(mycity1);
		 adapter.notifyDataSetChanged();
	 }
     
	public void savePicture(String address,Bitmap bitmap)
     {
    	 File file=new File(address);
    	 try {
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
				List<myCity> cityList=mycitydb.loadMyCity();
				for (int i = 0; i < cityList.size(); i++)    //如果数据列表中有相应的城市
				{
					if(name.equals(cityList.get(i).getMyCityName()))
						mycitycunzai=true;
			    }
				if(!mycitycunzai)
				 {  showProgressDialog();
					Http.sendWeatherRequest(name,weather_info.address3, new HttpCallbackListener()
		 			{
		 				@Override
		 				public void onFinish(String response)
		 				{   Log.d("Main","handleWeatherByTable");
		 					handleWeatherByTable(response,name);
		 			    }
		 			});
				 }
                break;
			default:
				break;
			}
    			 
    	 }
     }
     public void handleWeatherByTable(String response,String name)//赋值我的城市的一些参数
     {
    	 try {   Log.d("Main", "3");
    			JSONObject jsonobject=new JSONObject(response);
    			JSONObject obj1=jsonobject.getJSONObject("showapi_res_body");
    			JSONObject obj2=obj1.getJSONObject("now");
			    temp1=obj2.getString("temperature")+"℃";
			    weather_pic= obj2.getString("weather_pic");
				char b[]=name.toCharArray();
				c="";
   	    	    for(int i=0;i<b.length-1;i++)
   	    	    {       		  
   	    	     c=""+c+b[i];
   	    	    }
   	    	    Log.d("Main","2");
   	    	    jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
   	    	if(myJingdiancity.getjingdiancityName()!=null)
   	    	    {
   				List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);//载入该城市的所有景点
   				Log.d("Main", String.valueOf(jingdians.size())); 
   		          if(jingdians.size()>0)                        //如果已经有相应的城市的景点数据
   				     {   jingdian jingdian3=jingdians.get(0);
   			    		 mycityPic=jingdian3.getImageUrl();
   			    		 Log.d("Main","1");
   			    	 }
   		          else {
   		        	      getMyCityPic(name);
				       }
   	    	    }
   			else {
   			     Log.d("Main","getMyCityPci");
			     getMyCityPic(name);
			     }
   	    	    
		      } catch (Exception e) {
			    e.printStackTrace();
		     }
     }
     public String getWeatherPicPath()//获得城市天气小图标动态路径
     {     String l=pre.getString("l","1");
    	   weatherPicPath=Environment.getExternalStorageDirectory()+"/download/"+"weather"+l+".png";
    	   int lcun=Integer.parseInt(l)+1;
    	   editor.putString("l",String.valueOf(lcun));
    	   editor.commit();
    	   return weatherPicPath;    //城市天气小图标本地动态路径
     }
     public String getCityPicPath()//获得城市背景图动态路径
     {   String k=pre.getString("k", "1");
    	 cityPicPath=Environment.getExternalStorageDirectory()+"/download/"+"jingdian"+k+".png";
    	 int kcun=Integer.parseInt(k)+1;
    	 editor.putString("k", String.valueOf(kcun));
    	 editor.commit();
    	 return cityPicPath;     //城市背景景点本地动态路径
     }
     public void getMyCityPic(final String name)//获得城市背景图片URL
     {   Log.d("Main","getMyCityPic");
    	 char b[]=name.toCharArray();
         c="";
    	 for(int i=0;i<b.length-1;i++)
    	 {       		  
    	     c=""+c+b[i];
    	 }
    	 Log.d("Main","time");
    	 final Timer timer=new Timer();            //计时，防止用户等太久
    	 TimerTask myTask=new TimerTask() {
			
			@Override
			public void run() 
			{     
				new Thread(new Runnable() {
					
					@Override
					public void run() {
					
				         Bitmap bitmap=null;
					     bitmap=Utility.getPicture(weather_pic);
					      if(bitmap!=null)
					          savePicture(getWeatherPicPath(),bitmap);
					  	  mycity1=new myCity(name,weather_pic,weatherPicPath,temp1,null,null);
					      mycitydb.saveMyCity(mycity1);
                          dataList.add(mycity1);
						  Message message=new Message();
						  message.what=1;
						  handler.sendMessage(message);
						  timer.cancel();
					}
				}).start();
			
			  
			}
		};
     	 timer.schedule(myTask, 5000);
    	  Http.sendjingdianRequest(dJingdianDB.loadjingdianCity(c).getjingdiancityid(), new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) 
				{   Log.d("Main",response);
					Utility.handlejingdian(response, myCityAction.this);
                    jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
					List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
					 if(jingdians.size()>0)
					 {   
						 jingdian jingdian3=jingdians.get(0);
		    		     mycityPic=jingdian3.getImageUrl();
		    		     mycity1=new myCity(name,weather_pic,null,temp1,mycityPic,null);
		    		     Bitmap bitmap=null;
		 					bitmap=Utility.getPicture(mycity1.getMyCityWeatherWeb());
		 					if(bitmap!=null)
		 			            savePicture(getWeatherPicPath(),bitmap);
		 					bitmap=Utility.getPicture(mycity1.getMyCityPicWeb());   //这里getMyCityPic()获得相应城市的景点的URL，这里getPicture()通过URL获得图片
		 					if(bitmap!=null)                               
		 						savePicture(getCityPicPath(), bitmap);
		 					runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									   mycity1=new myCity(name,mycity1.getMyCityWeatherWeb(), weatherPicPath,temp1,mycity1.getMyCityPicWeb(),cityPicPath);    //weatherPicPath和后面的是本地路径
					 				   mycitydb.saveMyCity(mycity1);
			                           dataList.add(mycity1);                             
					 				   adapter.notifyDataSetChanged();
					 				   dismissProgressDialog();
								}
							});
		 				  
		 			
					 }
					 
				}
     });
    	 
     }
     public void showAllMyCity()              //加载所有我的城市
     {
    	 
    	 myCities=mycitydb.loadMyCity();
    	 for(int m=0;m<myCities.size();m++)
    	 {   
    		 myCity city=myCities.get(m);
    		 dataList.add(city); 
    	 }
         Log.d("Main", "其他城市");
    	 
     }
     public void showProgressDialog()  
     {
    	 if(progressDialog==null)
    	 {
    		 progressDialog=new ProgressDialog(this);
    		 progressDialog.setMessage("加载中...");
    		 progressDialog.setCanceledOnTouchOutside(false);
    	 }
    	 progressDialog.show();
     }
     public void dismissProgressDialog()
     {
    	 if(progressDialog!=null)
    		 progressDialog.dismiss();
     }
     @Override
     public void onBackPressed()
     {
    	 if(myCityDeleted.size()!=0||localcityflag)
    	 {
    		 myCityDeleted.clear();
    		  for(int i=0;i<deletedInt.size();i++)
			      listView.getChildAt(deletedInt.get(i)).setBackgroundColor(0);
    		 localcityflag=false;
    	 }
         else 
         {
			super.onBackPressed();
		 }
     }
   
}
