package activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Util.Http;
import Util.HttpCallbackListener;


import model.City;
import model.County;
import model.Province;

import com.uniqueweather.app.R;

import db.WeatherDB;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class chooseAreaActivity extends Activity {
	 static Province province=new Province();
	 static City city=new City();
	 static County county=new County();
	 private List<String> datalist=new ArrayList<String>();
	 private ListView listview;
	 private TextView titleView;
	 private int currentlevel;
	 private int provinceLevel=0;
	 private int cityLevel=1;
	 private int countyLevel=2;
	 private ArrayAdapter adapter;
	 private WeatherDB weatherDB;
	 private List<Province> provinceList;
	 private List<City> cityList;
	 private List<County> countyList;
	 public static  Province selectedProvince;
	 public static  City selectedCity;
	 public static County selectedCounty;
	 static String address1="http://route.showapi.com/101-39";
	 static String address2="http://route.showapi.com/101-111";
	 public ProgressDialog progressDialog;
	 @Override
	 public void onCreate(Bundle savedInstanceState)
	 {
		 super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.choose_area);
		 titleView=(TextView)findViewById(R.id.title_view);
		 listview=(ListView)findViewById(R.id.list_view);
		 adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,datalist);
         listview.setAdapter(adapter);
         weatherDB=WeatherDB.getInstance(this);
         queryProvince();
         listview.setOnItemClickListener(new OnItemClickListener(){
         @Override
         public void onItemClick(AdapterView<?> arg0,View view,int index,long arg3)
         
         {
        	 if(currentlevel==provinceLevel)
        	 {
        		 selectedProvince=provinceList.get(index);
        		queryCity(selectedProvince.getProvinceName());
        	 }
        	 else if(currentlevel==cityLevel)
        	 {
        		 selectedCity=cityList.get(index);
        		 queryCounty(selectedCity.getCityName());
        	 }
        	 else if(currentlevel==countyLevel)
        	 {   selectedCounty=countyList.get(index);
        		 Intent intent=new Intent();
        		 intent.putExtra("countyName",selectedCounty.getCountyName());
        		 intent.putExtra("selectedCityName", selectedCity.getCityName());
        		 setResult(RESULT_OK,intent);
        		 finish();
        	 }
         }
        		 
         } );
	 }
	 public void queryProvince()
	 {
		 provinceList=weatherDB.loadProvince();
		 if(provinceList.size()>0)
		 {   datalist.clear();
			 for(Province province:provinceList)
			 {
				 datalist.add(province.getProvinceName());
			 }
			 adapter.notifyDataSetChanged();
			 listview.setSelection(0);
			 currentlevel=provinceLevel;
			 titleView.setText("中国"); 
		 }
		 else
		 {   showProgressDialog();
			 Http.sendHttpRequest("",address1,"1",new HttpCallbackListener(){
		            boolean flag=false;
		            String flag_error;
	                 @Override
		            	public void onFinish(String response){
		    	 if(!TextUtils.isEmpty(response))
		    	 {  try{
		    		 JSONObject jsonobject=new JSONObject(response);
		    		 JSONObject jsonobject1=jsonobject.getJSONObject("showapi_res_body");
		    		 JSONArray jsonarray=jsonobject1.getJSONArray("data");
		    		 flag=jsonobject1.getBoolean("flag");
		    		 if(jsonarray.length()>0)
		    		 {  for(int i=0;i<jsonarray.length();i++)
		    			{province.setProvinceName(jsonarray.getJSONObject(i).getString("areaName"));
		    			province.setProvinceCode(jsonarray.getJSONObject(i).getInt("id"));
		    		    weatherDB.saveProvince(province);}
		    		 }
		    	 }catch(Exception e)
		    	 {
		    		 e.printStackTrace();
		    	 }
		    	 
		    	 } 
		    	 if(flag)
		    	 {
		    	 runOnUiThread(new Runnable()
		    	 {
		    		 @Override
		    		 public void run()
		    		 {   
		    			 provinceList=weatherDB.loadProvince();
						  datalist.clear();
							 for(Province province:provinceList)
							 {
								 datalist.add(province.getProvinceName());
							 }
							 adapter.notifyDataSetChanged();
							 listview.setSelection(0);
							 currentlevel=provinceLevel;
							 titleView.setText("中国"); 
							 closeDialog();
		    		 }
		    	 } );}
		    	 else
		    	 {   closeDialog();
		    		 runOnUiThread(new Runnable ()
	    				{
	    					@Override
	    					public void run(){
	    						Toast.makeText(chooseAreaActivity.this,"失败,未找到", Toast.LENGTH_SHORT).show();
	    					}
	    				}
	    						
	    						
	    						);
		    	 }
		    	 
	                 
	                 }
		    });
			

		 }
	 }
	 public void queryCity(final String provinceName)
	 {   
		 cityList=weatherDB.loadCity(provinceName);
		 if(cityList.size()>0)
		 {   datalist.clear();
			 for(City city:cityList)
			 {
				 datalist.add(city.getCityName());
			 }
			 adapter.notifyDataSetChanged();
			 listview.setSelection(0);
			 currentlevel=cityLevel;
			 titleView.setText(selectedProvince.getProvinceName());
			 
		 }
		 else
		 {   showProgressDialog();
			 Http.sendHttpRequest(provinceName,address1,"2",new HttpCallbackListener(){
	        	   boolean flag;
	    			@Override
	    			public void onFinish(String response)
	    			{  try{
	    				JSONObject jsonobject=new JSONObject(response);
	    				JSONObject jsonobject1=jsonobject.getJSONObject("showapi_res_body");
	    				 JSONArray jsonarray=jsonobject1.getJSONArray("data");
	    				 flag=jsonobject1.getBoolean("flag");
	    				 if(jsonarray.length()>0)
	    				 {
	    					 for(int i=0;i<jsonarray.length();i++)
	    					 {
	    						 city.setCityCode(jsonarray.getJSONObject(i).getString("areaCode"));
	    						 city.setCityName(jsonarray.getJSONObject(i).getString("areaName"));
	    						 city.setProvinceName(jsonarray.getJSONObject(i).getString("provinceName"));
	    						 weatherDB.saveCity(city);
	    					 }
	    				 }
	    			}catch(Exception e)
	    			{
	    				e.printStackTrace();
	    			}
	    			if(flag)
	    			{
	    			runOnUiThread(new Runnable()
				      {   
				    	  @Override
				    	  public void run()
				    	  {
				    			 cityList=weatherDB.loadCity(provinceName);
			                     datalist.clear();
								 for(City city:cityList)
								 {
									 datalist.add(city.getCityName());
								 }
								 adapter.notifyDataSetChanged();
								 listview.setSelection(0);
								 currentlevel=cityLevel;
								 titleView.setText(selectedProvince.getProvinceName()); 
						         closeDialog();
				    	  }
				      }
				    		  
				    		  
				    		  );}
	    			else{
	    				closeDialog();
	    				runOnUiThread(new Runnable()
	    				{
	    					@Override
	    					public void run(){
	    						Toast.makeText(chooseAreaActivity.this, "失败,未找到",Toast.LENGTH_SHORT).show();
	    					}
	    				}
	    						
	    						
	    						);
	    				
	    			}
	    		   
	    			}
	    		
	    		});
		}
	 }
	 public void queryCounty( final String cityName)
	 {
		 countyList=weatherDB.loadCounty(cityName);
		 if(countyList.size()>0)
		 {   datalist.clear();
			 for(County county:countyList)
			 {
				 datalist.add(county.getCountyName());
			 }
			 adapter.notifyDataSetChanged();
			 listview.setSelection(0);
			 currentlevel=countyLevel;
			 titleView.setText(cityName);
			 
		 }
		 else
		 {     showProgressDialog();
			    Http.sendHttpRequest(cityName,address2,"2",new HttpCallbackListener(){
			    	boolean flag;
	    			@Override
	    			public void onFinish(String response)
	    			{  try{
	    				JSONObject jsonobject=new JSONObject(response);
	    				JSONObject jsonobject1=jsonobject.getJSONObject("showapi_res_body");
	    				 JSONArray jsonarray=jsonobject1.getJSONArray("data");
	    				 flag=jsonobject1.getBoolean("flag");
	    				 JSONObject jsonobject2=jsonarray.getJSONObject(0);
	    				 JSONArray jsonarray1=jsonobject2.getJSONArray("children");
	    				 if(jsonarray1.length()>0)
	    				 {
	    					 for(int i=0;i<jsonarray1.length();i++)
	    					 {   
	    						 county.setCountyName(jsonarray1.getJSONObject(i).getString("areaName"));
	    						 county.setCountyCode(jsonarray1.getJSONObject(i).getString("areaCode"));
	    						 county.setCityName(jsonobject2.getString("areaName"));
	    						 weatherDB.saveCounty(county);
	    					 }
	    				 } 
	    				
	    						 
	    			}catch(Exception e)
	    			{
	    				e.printStackTrace();
	    			}
	    			if(flag)
	    			{
	    			runOnUiThread(new Runnable(){
	    			@Override
	    			public void run(){
	    				
	    				 countyList=weatherDB.loadCounty(cityName);
						 datalist.clear();
							 for(County county:countyList)
							 {
								 datalist.add(county.getCountyName());
							 }
							 adapter.notifyDataSetChanged();
							 listview.setSelection(0);
							 currentlevel=countyLevel;
				             titleView.setText(cityName);
				             closeDialog();
	    			}});}
	    			else{
	    				closeDialog();
	    				runOnUiThread(new Runnable ()
	    				{
	    					@Override
	    					public void run(){
	    						Toast.makeText(chooseAreaActivity.this,"失败,未找到", Toast.LENGTH_SHORT).show();
	    					}
	    				}
	    						
	    						
	    						);
	    				
	    				
	    			}
	    		
	    			}
			    });
                    

		 }
	 }
    public void showProgressDialog()
    {
    	if(progressDialog==null)
    	{
    		progressDialog=new ProgressDialog(this);
    		progressDialog.setMessage("加载中.....");
    		progressDialog.setCanceledOnTouchOutside(false);
    	}
    	progressDialog.show();
    }
    public void closeDialog()
    {
    	if(progressDialog!=null)
    		progressDialog.dismiss();
    }
	 @Override
	 public void onBackPressed()
	 {
		 if(currentlevel==countyLevel)
		 {
			 queryCity(selectedProvince.getProvinceName());
		 }
		 else if(currentlevel==cityLevel)
		 {
			 queryProvince();
		 }
		 else
		 {
			 finish();
		 }
	 }
	 
	 

	
}
