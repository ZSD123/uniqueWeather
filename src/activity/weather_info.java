package activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import service.autoUqdateService;

import com.uniqueweather.app.R;

import Util.Http;
import Util.HttpCallbackListener;
import Util.Utility;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class weather_info extends FragmentActivity {
	private int chenhuonce=0;

	private int flag=0;
	private TextView countyname;
	private Button button_switch;
    private Button button_refresh;
    public String countyName;
    private ImageView pic;
    private Bitmap bitmap;
    private String uriUserPicture;
    public String accountName;
    public SharedPreferences pre;
    public SharedPreferences.Editor editor;
    public static String l="1";
    public static String ALBUM_PATH=Environment.getExternalStorageDirectory()+"/download/"+"weather"+".png";
	public static String address3="http://route.showapi.com/9-2";
	
	 private ViewPager mViewPager;
	 private List<fragmentPart> fragList=new ArrayList<fragmentPart>();
	 private String [] title=new String[]{
	    		"weather","map"
	         };
	 private Button button1;
	 private Button button2;
	 private FragmentPagerAdapter mAdapter;
	@Override
	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		button1=(Button)findViewById(R.id.button_weather);
		button2=(Button)findViewById(R.id.button_map);
	    init();	
	    mViewPager.setAdapter(mAdapter);
	
		
	    editor=PreferenceManager.getDefaultSharedPreferences(weather_info.this).edit();
		pre=PreferenceManager.getDefaultSharedPreferences(weather_info.this);
		flag=pre.getInt("flag", 0);
		accountName=pre.getString("accountName","");
		if(!accountName.isEmpty())
		{Toast.makeText(this,accountName + ",��ӭ��", Toast.LENGTH_SHORT).show();
		 userName.setText(accountName);
		}
		else {
			userName.setText("δ����");
		}
		uriUserPicture=pre.getString("userPicture",null);
		if(uriUserPicture!=null)
		{  
		    Uri uri=Uri.parse(uriUserPicture);
		    try{ContentResolver cResolver=this.getContentResolver();
			Bitmap bitmap=BitmapFactory.decodeStream(cResolver.openInputStream(uri));
			userPicture.setImageBitmap(bitmap);
		    }catch(Exception e)
		    {
		    	e.printStackTrace();
		    	Toast.makeText(weather_info.this, "δ�ҵ���ӦͼƬ", Toast.LENGTH_SHORT).show();
		    }
		}
		weather_layout.setVisibility(View.INVISIBLE);
		if(flag==0)
		{   Toast.makeText(this, "��ѡ�����", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent();
			intent.setClass(weather_info.this,chooseAreaActivity.class);
			startActivityForResult(intent,1);
			
		}
		else{
			countyName=pre.getString("countyName","");
		    pic.setImageBitmap(getPicture());
	        countyname.setText(countyName);
		    weather_layout.setVisibility(View.VISIBLE);
		    weather.setText(pre.getString("weatherInfo", ""));
		    temper.setText(pre.getString("temperature", ""));
		    realtime.setText(pre.getString("realtime", ""));
		    String publishTime=pre.getString("time", "");
		    char[]b=publishTime.toCharArray();
		    if(b.length>0)
		    { String c=""+b[0]+b[1]+b[2]+b[3]+"��"+b[4]+b[5]+"��"+b[6]+b[7]+"��"+" "+b[8]+b[9]+":"+b[10]+b[11]+":"+b[12]+b[13]+"����";
		    time.setText(c);
		    }
			queryWeather();
		}
		button_switch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent();
				intent.setClass(weather_info.this,chooseAreaActivity.class);
				startActivityForResult(intent,1);
			}
		});
		button_refresh.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{   queryWeather();
			    Toast.makeText(weather_info.this, "ˢ����...", Toast.LENGTH_SHORT).show();
			}
		});
		userPicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View V) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
	            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	                    "image/*");
				startActivityForResult(intent, 2);
			}
		});
		userName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final EditText editText=new EditText(weather_info.this);
				AlertDialog.Builder builder=new AlertDialog.Builder(weather_info.this);
				builder.setTitle("��������������").setNegativeButton("ȡ��", null).setView(editText);
				builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() 
				{ @Override
					public void onClick(DialogInterface dialogInterface, int which) 
				    {
						
					    String accountNameString=editText.getText().toString();
						editor.putString("accountName",accountNameString );
						editor.commit();
						userName.setText(accountNameString);
					}
				});
				builder.show();
			}
		});
		myCity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(weather_info.this,myCityAction.class);
				intent.putExtra("selectedCityName",pre.getString("selectedCityName", "") );
				intent.putExtra("temp", pre.getString("temperature",""));
				startActivity(intent);
				
			}
		});
		}
	private void init() 
	{
	   mViewPager=(ViewPager)findViewById(R.id.id_viewpager);
	   for (int i=0;i<title.length;i++)                     //����fragmentPart
	      {
		      fragmentPart fragP=new fragmentPart();
		      Bundle bundle =new Bundle();
		      bundle.putString(fragmentPart.keyToGet,title[i]);
		      bundle.putString("", value);
		      fragP.setArguments(bundle);
		      fragList.add(fragP);
	      }
	   mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
		
		@Override
		public int getCount() 
		{
			
			return fragList.size();
		}
		
		@Override
		public android.support.v4.app.Fragment getItem(int position) 
		{
			return fragList.get(position);
			
		}
	   };
		
	}
	public void showWeather()
	{
	     SharedPreferences pre=PreferenceManager.getDefaultSharedPreferences(this);
	     weather_layout.setVisibility(View.VISIBLE);
	     weather.setText(pre.getString("weatherInfo", ""));
	     temper.setText(pre.getString("temperature", ""));
	     realtime.setText(pre.getString("realtime", ""));
	     String publishTime=pre.getString("time", "");
	     char[]b=publishTime.toCharArray();
	     if(b.length>0)
	     { String c=""+b[0]+b[1]+b[2]+b[3]+"��"+b[4]+b[5]+"��"+b[6]+b[7]+"��"+" "+b[8]+b[9]+":"+b[10]+b[11]+":"+b[12]+b[13]+"����";
	     time.setText(c);
	     }
	     if(bitmap!=null)
	         pic.setImageBitmap(bitmap);
	     Intent intent=new Intent(this,autoUqdateService.class);
	     startService(intent);
	}
	
	public void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		switch(requestCode)
		{
		case 1:
			if(resultCode==RESULT_OK)
			{
				flag=1;
				editor.putString("countyName", data.getStringExtra("countyName"));
				editor.putInt("flag", flag);
				editor.putString("selectedCityName", data.getStringExtra("selectedCityName"));
				editor.commit();
				chenhuonce=pre.getInt("chenhuonce",0);
				if(chenhuonce==0)
				{
				final EditText editText=new EditText(this);
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setTitle("����������ĳƺ�").setNegativeButton("ȡ��", null).setView(editText);
				builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						accountName=editText.getText().toString(); 
						editor.putString("accountName", accountName);
						editor.commit();
						if(!accountName.isEmpty())
							userName.setText(accountName);
					}
				});
				builder.show();
				chenhuonce=1;
				editor.putInt("chenhuonce",chenhuonce);
				editor.commit();
				}
				countyName=data.getStringExtra("countyName");
				countyname.setText(countyName);
                queryWeather();
				Toast.makeText(weather_info.this, "ˢ����...", Toast.LENGTH_SHORT).show();
				
			} 
			break;
		case 2:
			if(resultCode==RESULT_OK)
			{Uri uri=data.getData();
			ContentResolver cr=this.getContentResolver();
			editor.putString("userPicture", uri.toString());
			editor.commit();
			try{
				Bitmap bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri));
				userPicture.setImageBitmap(bitmap);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;}
		default:
			break;
		}
	}
	public void queryWeather()
	{
		  Http.sendWeatherRequest(countyName,address3, new HttpCallbackListener()
			{
				@Override
				public void onFinish(String response)
				{   
					Utility.handleWeather(response,weather_info.this);
					bitmap=Utility.getPicture(pre.getString("weather_pic", ""));
					if(bitmap!=null)
					    savePicture(bitmap,ALBUM_PATH);
				    runOnUiThread(new Runnable(){
				    @Override
				    public void run(){
				    	showWeather();
				    }});
						
				}
			});
	}
	public void savePicture(Bitmap bitmap,String path)
	{
		File file=new File(path);
		try{
		FileOutputStream out=new FileOutputStream(file);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		out.flush();
		out.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
    public Bitmap getPicture()
    {
    	Bitmap bitmap=null;
    	bitmap=BitmapFactory.decodeFile(ALBUM_PATH);
    	return bitmap;
    }
    
}
