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
import android.R.color;
import android.R.integer;
import android.app.Activity;
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
	 public String temp;  //�������¶�
	 public String temp1;//������ӳ����¶�
	 public RelativeLayout mycitylayout;
	 public jingdianDB dJingdianDB;
	 public mycityDB mycitydb;
	 public String c="";
	 public boolean myjingdianflag=false;//֮ǰ�Ƿ��Ѿ�������ѯ�����Ѿ�������Ӧ���еľ���
	 public boolean myjingdiancityflag=false;//֮ǰ�Ƿ��Ѿ�������ѯ���Ҵ洢���кͳ���id
	 public Bitmap bitmap1;
	 public boolean mycitycunzai=false;//ѡ��ĳ����Ƿ��Ѿ����д���
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
	 String mycityPic;//�ҵĳ��б���ͼURL
	 public String weather_pic;
	 public View viewitem;
	 public List<myCity> myCityDeleted=new ArrayList<myCity>();
	 public ListView listView; 
	 public static String album1=Environment.getExternalStorageDirectory()+"/download/"+"jingdian"+".png";
	
     @Override
     public void onCreate(Bundle savedInstancestate)
     {   
    	 super.onCreate(savedInstancestate);
    	 requestWindowFeature(Window.FEATURE_NO_TITLE);
    	 setContentView(R.layout.mycity);
    	 listView=(ListView)findViewById(R.id.myCityList);
    	 adapter=new myCityAdapter(myCityAction.this,R.layout.mycitylist, dataList);
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
    	 buttonjian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{  if(myCityDeleted.size()==0)
				Toast.makeText(myCityAction.this,"�볤����Ҫɾ���ĳ���",Toast.LENGTH_SHORT).show();
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
				   listView.setCacheColorHint(0);
			    }
		}});
    	 listView.setOnItemLongClickListener(new OnItemLongClickListener() 
    	 {  @Override
    		public boolean onItemLongClick(AdapterView<?> arg0,View arg1,int arg2,long arg3)
    		{   Log.d("Main", String.valueOf(arg2));
    		    viewitem=listView.getChildAt(arg2);
    		    viewitem.setBackgroundColor(Color.parseColor("#00FFFF"));//�������Ŀ����ɫ
    		    myCityDeleted.add(dataList.get(arg2));//����Ҫɾ���ĳ����б�
    		    return true;
    		  
    		}
		});
		
    	
     }
     public void init(final String c)
     {  
    	 dJingdianDB=jingdianDB.getInstance(this);
    	 mycitydb=mycityDB.getInstance(this);
    	 i=mycitydb.loadmycityId();   //������һ�����е�id
         pre=PreferenceManager.getDefaultSharedPreferences(myCityAction.this);
         editor=PreferenceManager.getDefaultSharedPreferences(myCityAction.this).edit();
         myjingdiancityflag=pre.getBoolean("myjingdiancityflag",false);
         
         try{
             jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);     //�ж����ݿ����Ƿ���Ӧ���еľ�������
    	     List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
    	     if(jingdians.size()>0)
     		 {   editor.putBoolean("myjingdianflag", true);
     		     editor.commit();
     		 }
         }catch(Exception e)
         {
        	 e.printStackTrace();
        	 editor.putBoolean("myjingdianflag", false);
 			 editor.commit();
         }
         
		 myjingdianflag=pre.getBoolean("myjingdianflag",false);
		                                                         //��Щ���Ǽ��ر��س��е�
 		   if(!myjingdiancityflag)                                                   
         {   showProgressDialog();
        	 Http.sendjingdiancityRequest(new HttpCallbackListener() {   //��þ�����е������Ŀ���ǻ�ȡ��Ӧ��id
			@Override
			public void onFinish(String response) {                        //ͨ�����ȵ�onFinish�����ݷ��ؾ�ת������ĳ���                          
				if(!response.isEmpty())
				{editor.putBoolean("myjingdiancityflag",true);
				 editor.commit();
				  Utility.handlejingdiancity(response,myCityAction.this);
                   if(!myjingdianflag)
		        	{
		        	   Http.sendjingdianRequest(dJingdianDB.loadjingdianCity(c).getjingdiancityid(), new HttpCallbackListener() {
							
							@Override
							public void onFinish(String response) 
							{   
								Utility.handlejingdian(response, myCityAction.this);
								jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
								List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
								 if(jingdians.size()>0)
					    		 {   editor.putBoolean("myjingdianflag", true);
					    		     editor.commit();
					    			 jingdian selectedJingdian=jingdians.get(0);
					    			 Bitmap bitmap=Utility.getPicture(selectedJingdian.getImageUrl());
					    			 savePicture(album1, bitmap);
					    			    runOnUiThread(new Runnable(){
						 				    @Override
						 				    public void run(){
						 				    	mycity1=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,album1);
				                                dataList.add(mycity1);
						 				    	adapter.notifyDataSetChanged();
						 				    	dismissProgressDialog();
						 				    }});
					    		  	
					    		 }	
							}
						});
		         }
		         else {
		        	 jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
		        	 List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);//������Ӧ�ĳ��еľ���
		        		 if(jingdians.size()>0)//����Ѿ�����Ӧ���еľ����б�
		        		 {  
		    			    final jingdian jingdian2=jingdians.get(0);
		    				bitmap1=Utility.getPicture(jingdian2.getImageUrl());
		    				 if(bitmap1!=null)
		    		           savePicture(album1, bitmap1);
		    			  	 myCity mycity=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,album1);//�ڶ�������Ӧ��һ�£����Ǳ��ص�ַ
		    		    	 dataList.add(mycity);
		    		    	 adapter.notifyDataSetChanged();
		                  }	
				      }
				}
				                                                    
			}
		    });
         }

       else if(!myjingdianflag)
      	{  showProgressDialog();
      	   Http.sendjingdianRequest(dJingdianDB.loadjingdianCity(c).getjingdiancityid(), new HttpCallbackListener() {
					
					@Override
					public void onFinish(String response) 
					{ 
						Utility.handlejingdian(response, myCityAction.this);
						jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
						List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
						 if(jingdians.size()>0)
			    		 {   editor.putBoolean("myjingdianflag", true);
			    		     editor.commit();
			    			 jingdian selectedJingdian=jingdians.get(0);
			    			 Bitmap bitmap=Utility.getPicture(selectedJingdian.getImageUrl());
			    			 savePicture(album1, bitmap);
			    		  	 myCity mycity=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,album1);
			    	    	 dataList.add(mycity);
                             adapter.notifyDataSetChanged();
                             dismissProgressDialog();
			    		 }	
					}
				});
       }
       else {
    	   Log.d("Main", dJingdianDB.loadjingdianCity(c).getjingdiancityid());
    	   Log.d("Main",  dJingdianDB.loadjingdianCity(c).getjingdiancityid());
      	 jingdiancity myJingdiancity1=dJingdianDB.loadjingdianCity(c);
      	 List<jingdian> jingdians1=dJingdianDB.loadjingdian(myJingdiancity1);//������Ӧ�ĳ��еľ���
      		 if(jingdians1.size()>0)//����Ѿ�����Ӧ���еľ����б�
      		 {  
  			    final jingdian jingdian2=jingdians1.get(0);
  				bitmap1=Utility.getPicture(jingdian2.getImageUrl());
  				 if(bitmap1!=null)
  		           savePicture(album1, bitmap1);
  			  	 myCity mycity=new myCity(citynsString,null,weather_info.ALBUM_PATH , temp,null,album1);//�ڶ�������Ӧ��һ�£����Ǳ��ص�ַ
  		    	 dataList.add(mycity);
  		    	 adapter.notifyDataSetChanged();
              }	
		    }
         showAllMyCity();//�������ʾ���ݱ������еĳ���
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
				for (int i = 0; i < cityList.size(); i++)    //��������б�������Ӧ�ĳ���
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
		 				{   
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
     public void handleWeatherByTable(String response,String name)//��ֵ�ҵĳ��е�һЩ����
     {
    	 try {
    			JSONObject jsonobject=new JSONObject(response);
    			JSONObject obj1=jsonobject.getJSONObject("showapi_res_body");
    			JSONObject obj2=obj1.getJSONObject("now");
			    temp1=obj2.getString("temperature")+"��";
			    weather_pic= obj2.getString("weather_pic");
				char b[]=name.toCharArray();
				c="";
   	    	    for(int i=0;i<b.length-1;i++)
   	    	    {       		  
   	    	     c=""+c+b[i];
   	    	    }
   	    	    jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
   				List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);//����ó��е����о���
   				 if(jingdians.size()>0)                        //����Ѿ�����Ӧ�ĳ��еľ�������
   				     {   jingdian jingdian3=jingdians.get(0);
   			    		 mycityPic=jingdian3.getImageUrl();
   			    	 }
   				 else {
					getMyCityPic(name);
				      }
   	    	    
		      } catch (Exception e) {
			    e.printStackTrace();
		     }
     }
     public String getWeatherPicPath()//��ó�������Сͼ�궯̬·��
     {     String l=pre.getString("l","1");
    	   weatherPicPath=Environment.getExternalStorageDirectory()+"/download/"+"weather"+l+".png";
    	   int lcun=Integer.parseInt(l)+1;
    	   editor.putString("l",String.valueOf(lcun));
    	   editor.commit();
    	   return weatherPicPath;    //��������Сͼ�걾�ض�̬·��
     }
     public String getCityPicPath()//��ó��б���ͼ��̬·��
     {   String k=pre.getString("k", "1");
    	 cityPicPath=Environment.getExternalStorageDirectory()+"/download/"+"jingdian"+k+".png";
    	 int kcun=Integer.parseInt(k)+1;
    	 editor.putString("k", String.valueOf(kcun));
    	 editor.commit();
    	 return cityPicPath;     //���б������㱾�ض�̬·��
     }
     public void getMyCityPic(final String name)//��ó��б���ͼƬURL
     {   Log.d("Main","getMyCityPic");
    	 char b[]=name.toCharArray();
         c="";
    	 for(int i=0;i<b.length-1;i++)
    	 {       		  
    	     c=""+c+b[i];
    	 }
    	  Http.sendjingdianRequest(dJingdianDB.loadjingdianCity(c).getjingdiancityid(), new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) 
				{   Log.d("Main",response);
					Utility.handlejingdian(response, myCityAction.this);
                    jingdiancity myJingdiancity=dJingdianDB.loadjingdianCity(c);
					List<jingdian> jingdians=dJingdianDB.loadjingdian(myJingdiancity);
					 if(jingdians.size()>0)
					 {   Log.d("Main","jingdian>0");
						 jingdian jingdian3=jingdians.get(0);
		    		     mycityPic=jingdian3.getImageUrl();
		    		     mycity1=new myCity(name,weather_pic,null,temp1,mycityPic,null);
		    		     Bitmap bitmap=null;
		 					bitmap=Utility.getPicture(mycity1.getMyCityWeatherWeb());
		 					if(bitmap!=null)
		 			            savePicture(getWeatherPicPath(),bitmap);
		 					bitmap=Utility.getPicture(mycity1.getMyCityPicWeb());   //����getMyCityPic()�����Ӧ���еľ����URL������getPicture()ͨ��URL���ͼƬ
		 					if(bitmap!=null)                               
		 						savePicture(getCityPicPath(), bitmap);
		 					runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									   mycity1=new myCity(name,mycity1.getMyCityWeatherWeb(), weatherPicPath,temp1,mycity1.getMyCityPicWeb(),cityPicPath);    //weatherPicPath�ͺ�����Ǳ���·��
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
     public void showAllMyCity()              //���������ҵĳ���
     {
    	 List<myCity> myCities=new ArrayList<myCity>();
    	 myCities=mycitydb.loadMyCity();
    	 for(int m=0;m<myCities.size();m++)
    	 {   
    		 myCity city=myCities.get(m);
    		 dataList.add(city); 
    		 adapter.notifyDataSetChanged();
    	 }
    	 
     }
     public void showProgressDialog()  
     {
    	 if(progressDialog==null)
    	 {
    		 progressDialog=new ProgressDialog(this);
    		 progressDialog.setMessage("������...");
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
    	 if(myCityDeleted.size()!=0)
    	 {
    		 myCityDeleted.clear();
    		 viewitem.setBackgroundColor(0);
    	 }
      else {
			super.onBackPressed();
		   }
     }
   
}
