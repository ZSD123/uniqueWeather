package Util;


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

import db.jingdianDB;
import db.yonghuDB;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

public class Utility {
	public static jingdiancity jingdiancity1;
	public static jingdianDB jingdiandb;
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
		editor.putString("temperature", temperature+"℃");
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
                 bitmap=BitmapFactory.decodeStream(in);
             }
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
				bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.userpicture);//加载网络图片失败的话就加载本地图片
				yonghudB.updateJiaZai0(urlstring);
             }
        }catch(Exception e)
        {
        	e.printStackTrace();
        	    
        }

        return bitmap;
	}
     public static void handlejingdiancity(String response,Context context)
     {   jingdiandb=jingdianDB.getInstance(context);
    	 try{
    	 JSONObject jsonObject=new JSONObject(response);
    	 JSONArray jArray=jsonObject.getJSONArray("result");
    	 for(int i=0;i<jArray.length();i++)
    	 {jingdiancity1=new jingdiancity();
    	  JSONObject jsonObject2=jArray.getJSONObject(i);
    	  jingdiancity1.setjingdiancityid(jsonObject2.getString("cityId"));
    	  jingdiancity1.setjingdiancityName(jsonObject2.getString("cityName"));
    	  jingdiandb.savejingdianCity(jingdiancity1);
    	 }
    	 
     }catch(Exception e)
     {
    	 e.printStackTrace();
     }
     }
     public static void handlejingdian(String response,Context context)
     {   jingdiandb=jingdianDB.getInstance(context);
    	 try{
    		 JSONObject jsonObject=new JSONObject(response);
    		 JSONArray jsonArray=jsonObject.getJSONArray("result");
    		 for(int i=0;i<jsonArray.length();i++)
    		 {
    			 jingdian jingdian1=new jingdian();
    			 jingdian1.setCityId(jsonArray.getJSONObject(i).getString("cityId"));
    			 jingdian1.setGrade(jsonArray.getJSONObject(i).getString("grade"));
    			 jingdian1.setImageUrl(jsonArray.getJSONObject(i).getString("imgurl"));
    			 jingdian1.setjingdiantitle(jsonArray.getJSONObject(i).getString("title"));
    			 jingdian1.setPrice_min(jsonArray.getJSONObject(i).getString("price_min"));
    			 jingdian1.seturlString(jsonArray.getJSONObject(0).getString("url"));
    			 jingdiandb.savejingdian(jingdian1);
    		 }
    	 }catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
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
