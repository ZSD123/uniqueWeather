package Util;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.jingdian;
import model.jingdiancity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uniqueweather.app.R;

import db.yonghuDB;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

public class Utility {
	public static void handleWeather(String weather,Context context)
	{  String time;
	   String weatherInfo;
	   String temperature;
	   String weather_pic;
		try{
		JSONObject jsonobject=new JSONObject(weather);
		JSONObject obj1=jsonobject.getJSONObject("showapi_res_body");
		time=obj1.getString("time");
		JSONObject obj2=obj1.getJSONObject("now");
		weatherInfo=obj2.getString("weather");
		weather_pic=obj2.getString("weather_pic");
		temperature=obj2.getString("temperature");
		saveWeatherInfo(time,weatherInfo,temperature,weather_pic,context);
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	}
	public static void saveWeatherInfo(String time,String weatherInfo,String temperature,String weather_pic,Context context)
	{
		
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString("weatherInfo", weatherInfo);
		editor.putString("temperature", temperature+"��");
		editor.putString("weather_pic", weather_pic);
		editor.commit();
	}
	public static Bitmap getPicture(String urlstring)
	{
		Bitmap bitmap=null;
		HttpURLConnection connection=null;
        try{ 
             URL url=new URL(urlstring);
             connection=(HttpURLConnection)url.openConnection();
             connection.setConnectTimeout(8000);
             connection.setRequestMethod("GET");
             connection.setDoInput(true);
             connection.setUseCaches(true);
             if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
             {  InputStream in=connection.getInputStream();
             
         	   BitmapFactory.Options opts=new BitmapFactory.Options();
			   opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
			
			   opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
		     	opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
			
		     	opts.inSampleSize=2;
			    opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
			
			    bitmap=BitmapFactory.decodeStream(in, null, opts);
             
             }
             connection.disconnect();
        }catch(Exception e)
        {
        	e.printStackTrace();
        }

        return bitmap;
	}

	public static Bitmap getTouxiangBitmap(String urlstring,Context context,yonghuDB yonghudB)
	{
		Bitmap bitmap=null;
		HttpURLConnection connection=null;
        try{ 
             URL url=new URL(urlstring);
             connection=(HttpURLConnection)url.openConnection();
             connection.setConnectTimeout(8000);
             connection.setRequestMethod("GET");
             connection.setDoInput(true);
             connection.setUseCaches(true);
             if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
             {  InputStream in=connection.getInputStream();
                 bitmap=BitmapFactory.decodeStream(in);
             }else {
				bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.userpicture);//��������ͼƬʧ�ܵĻ��ͼ��ر���ͼƬ
				yonghudB.updateJiaZai0(urlstring);
             }
        }catch(Exception e)
        {
        	e.printStackTrace();
        	    
        }

        return bitmap;
	}
 
   
     public static void handleAreaByXY(String response,Context context)
     {   SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
    	 try{
    		 JSONObject jsonObject=new JSONObject(response);
    		 JSONObject jsonObject2=jsonObject.getJSONObject("showapi_res_body");
    		 JSONObject jsonObject3=jsonObject2.getJSONObject("addressComponent");
    		 String locProvince=jsonObject3.getString("province");
    		 String locCity=jsonObject3.getString("city");
    		 String locDistrict=jsonObject3.getString("district");
    		 editor.putString("locProvice", locProvince);
    		 editor.putString("locCity", locCity);
    		 editor.putString("locDistrict",locDistrict);
    		 editor.commit();
    	 }catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
     }
     
}
