package activity;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PublicKey;
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
    public String accountName;
    public SharedPreferences pre;
    public SharedPreferences.Editor editor;
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
	
	
	}		
	private void init() 
	{
	   mViewPager=(ViewPager)findViewById(R.id.id_viewpager);
	   for (int i=0;i<title.length;i++)                     //加载fragmentPart
	      {
		      fragmentPart fragP=new fragmentPart(weather_info.this);
		      Bundle bundle =new Bundle();
		      bundle.putString(fragmentPart.keyToGet,title[i]);
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

	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data)
	{   Toast.makeText(this, "tou", Toast.LENGTH_SHORT).show();
		switch(requestCode)
		{
		case 1:
			if(resultCode==RESULT_OK)
			{   Log.d("Main", "回来了");
				fragmentPart.flag=1;
				editor.putString("countyName", data.getStringExtra("countyName"));
				editor.putInt("flag", fragmentPart.flag);
				editor.putString("selectedCityName", data.getStringExtra("selectedCityName"));
				editor.commit();
				fragmentPart.chenhuonce=pre.getInt("chenhuonce",0);
				if(fragmentPart.chenhuonce==0)
				{
				final EditText editText=new EditText(this);
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setTitle("请输入对您的称呼").setNegativeButton("取消", null).setView(editText);
				builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						accountName=editText.getText().toString(); 
						editor.putString("accountName", accountName);
						editor.commit();
						if(!accountName.isEmpty())
							{
						       fragmentPart.refreshUserName(accountName);
							}
					}
				});
				builder.show();
				fragmentPart.chenhuonce=1;
				editor.putInt("chenhuonce",fragmentPart.chenhuonce);
				editor.commit();
				}
				String countyName = data.getStringExtra("countyName");
				fragmentPart.refreshCountyName(countyName);
                fragmentPart.queryWeather(weather_info.this);
				Toast.makeText(weather_info.this, "刷新中...", Toast.LENGTH_SHORT).show();
				
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
				fragmentPart.refreshUserPicture(bitmap);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			}
		default:
			Toast.makeText(this,"moren",Toast.LENGTH_SHORT).show();
			break;
		}
	}

	
	
  
  
}
