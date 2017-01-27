package activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobConfig.Builder;

import com.uniqueweather.app.R;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
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
	 public static String provider;
	 public String yuanLocation;
	 public String xianLocation;
	 public  int chenhuonce=0;
	 public SharedPreferences.Editor editor1;
	 public  SharedPreferences pref;
	 private FragmentPagerAdapter mAdapter;
	@Override
	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
	    init();	
	    mViewPager.setAdapter(mAdapter);
	    editor1=PreferenceManager.getDefaultSharedPreferences(this).edit();
	    pref=PreferenceManager.getDefaultSharedPreferences(this);
	    chenhuonce=pref.getInt("chenhuonce",0);
		if(chenhuonce==0)
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
		chenhuonce=1;
	    editor1.putInt("chenhuonce",chenhuonce);
		editor1.commit();
		}
	    
	 }		
	private void init() 
	{  Bmob.initialize(this,"f3065817051f7c298d2e49d9329a2a6b");
	   BmobConfig config=new BmobConfig.Builder(this)
	                     .setApplicationId("f3065817051f7c298d2e49d9329a2a6b")
	                     .setConnectTimeout(30)
	                     .setUploadBlockSize(1024*1024)
	                     .setFileExpiration(2500)
	                     .build();
	   Bmob.initialize(config);
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
	{ 
		switch(requestCode)
		{
		case 1:
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
