package activity;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;


import com.uniqueweather.app.R;


import android.R.integer;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
		setContentView(R.layout.main);
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
	{  mViewPager=(ViewPager)findViewById(R.id.id_viewpager);
	   for (int i=0;i<title.length;i++)                     //加载fragmentPart
	      {   
		      fragmentPart fragP=fragmentPart.getInstance(this);
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
			{final Uri uri=data.getData();
			ContentResolver cr=this.getContentResolver();
			fragmentPart.editor.putString("userPicture", uri.toString());
			fragmentPart.editor.commit();
			try{
				Bitmap bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri));
				fragmentPart.refreshUserPicture(bitmap);
		        
             	BmobFile bmobFile=new BmobFile(new File(uri.toString()));
				bmobFile.uploadblock(weather_info.this,new UploadFileListener() {
					
					@Override
					public void onSuccess() {
						  Toast.makeText(weather_info.this,"成功", Toast.LENGTH_SHORT).show();
						
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
					   Toast.makeText(weather_info.this,"失败，"+arg1+uri.toString(), Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onProgress(Integer value){
						
					}
				});
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
