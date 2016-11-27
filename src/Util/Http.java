package Util;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import android.app.DownloadManager.Request;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.renderscript.Sampler.Value;

import com.show.api.ShowApiRequest;

public class Http {
	public static void sendHttpRequest(final String areaName,final String address,final String level,final HttpCallbackListener listener)
	{   
		new Thread(new Runnable(){
				@Override
				public void run(){
					
                        String appid="26715";
                        String secret="bbd0cdd1a7b94310a4e66baf37c9ce0d";
                        final String res=new ShowApiRequest(address,appid,secret)
                        .addTextPara("level", level)
                        .addTextPara("areaName",areaName)
                        .post();
                        System.out.print(res);
                        if(listener!=null)
                        	listener.onFinish(res);
					}	
				}).start();
		
	}
	public static void sendWeatherRequest(final String area,final String address,final HttpCallbackListener listener )
	{
		new Thread(new Runnable()
		{
			@Override
			public void run(){
				 String appid="26715";
                 String secret="bbd0cdd1a7b94310a4e66baf37c9ce0d";
                 final String res=new ShowApiRequest(address,appid,secret)
                                    .addTextPara("area", area)
                                    .post();
                 System.out.print(res);
                 if(listener!=null)
                	 listener.onFinish(res);
			}
		}).start();
		
	}
    public static void sendjingdiancityRequest(final HttpCallbackListener listener)
    {
    	new Thread(new  Runnable() {
    			@Override
				public void run() {
    				try{
    				HttpClient httpClient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost("http://apis.haoservice.com/lifeservice/travel/cityList");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("key", "1b2b0212fabe4b33bcbd1e21e46e9b75"));
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"utf-8");
						httpPost.setEntity(entity);
						HttpResponse httpResponse=httpClient.execute(httpPost); 
						if(httpResponse.getStatusLine().getStatusCode()==200)
						{
							HttpEntity entity1=httpResponse.getEntity();
							String response=EntityUtils.toString(entity1,"utf-8");
							if(listener!=null)
								listener.onFinish(response);
						}
					} catch (UnsupportedEncodingException e) {
					
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
					
						e.printStackTrace();
					}
					
				}
			}).start();
    	
    }
    public static void sendjingdianRequest(final String cid,final HttpCallbackListener listener)
    {
    	new Thread(new  Runnable() {
    			@Override
				public void run() {
    				try{
    				HttpClient httpClient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost("http://apis.haoservice.com/lifeservice/travel/scenery");
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("key", "1b2b0212fabe4b33bcbd1e21e46e9b75"));
					params.add(new BasicNameValuePair("cid",cid));
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"utf-8");
						httpPost.setEntity(entity);
						HttpResponse httpResponse=httpClient.execute(httpPost); 
						if(httpResponse.getStatusLine().getStatusCode()==200)
						{
							HttpEntity entity1=httpResponse.getEntity();
							String response=EntityUtils.toString(entity1,"utf-8");
							if(listener!=null)
								listener.onFinish(response);
						}
					} catch(Exception e)
					{
						e.printStackTrace();
					}
					
				}
			}).start();
    	
    }

}
