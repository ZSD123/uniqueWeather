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
    public static String ALBUM_PATH=Environment.getExternalStorageDirectory()+"/download/"+"weather"+".png";
	public static String address3="http://route.showapi.com/9-2";
	
	 public static ViewPager mViewPager;
	 private List<fragmentPart> fragList=new ArrayList<fragmentPart>();
	 private String [] title=new String[]{
	    		"weather","map"
	         };
	 private FragmentPagerAdapter mAdapter;
	@Override
	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
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
	{  Log.d("Main", "tou");
	   Log.d("Main",String.valueOf(requestCode));
		switch(requestCode)
		{
		case 1:
			if(resultCode==RESULT_OK)
			{   Log.d("Main", "回来了");
				fragmentPart.flag=1;
				fragmentPart.editor.putString("countyName", data.getExtras().getString("countyName"));
				fragmentPart.editor.putInt("flag", fragmentPart.flag);
				fragmentPart.editor.putString("selectedCityName", data.getExtras().getString("selectedCityName"));
				fragmentPart.editor.commit();
				Log.d("Main", String.valueOf(fragmentPart.pre.getInt("flag",144)));
				fragmentPart.chenhuonce=fragmentPart.pre.getInt("chenhuonce",0);
				if(fragmentPart.chenhuonce==0)
				{
				final EditText editText=new EditText(this);
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setTitle("请输入对您的称呼").setNegativeButton("取消", null).setView(editText);
				builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						accountName=editText.getText().toString(); 
						fragmentPart.editor.putString("accountName", accountName);
						fragmentPart.editor.commit();
						if(!accountName.isEmpty())
							{
						       fragmentPart.refreshUserName(accountName);
							}
					}
				});
				builder.show();
				fragmentPart.chenhuonce=1;
				fragmentPart.editor.putInt("chenhuonce",fragmentPart.chenhuonce);
				fragmentPart.editor.commit();
				}
				fragmentPart.countyName = data.getStringExtra("countyName");
				fragmentPart.refreshCountyName(fragmentPart.countyName);
                fragmentPart.queryWeather(weather_info.this);
				Toast.makeText(weather_info.this, "刷新中...", Toast.LENGTH_SHORT).show();
				
			} 
			break;
		case 2:
			if(resultCode==RESULT_OK)
			{Uri uri=data.getData();
			ContentResolver cr=this.getContentResolver();
			fragmentPart.editor.putString("userPicture", uri.toString());
			fragmentPart.editor.commit();
			try{
				Bitmap bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri));
				fragmentPart.refreshUserPicture(bitmap);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
			}
		default:
			break;
		}
	}

	
	
  
  
}
