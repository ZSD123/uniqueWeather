package activity;

import java.io.File;
import java.net.URI;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


import com.amap.api.location.AMapLocationClient;
import com.amap.api.services.a.bu;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class weather_info extends baseFragmentActivity {
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
	   
	   
	   
	   MyUser newUser=new MyUser();
	   newUser.setInstallationId(BmobInstallation.getInstallationId(weather_info.this));
	   MyUser bmobUser=BmobUser.getCurrentUser(MyUser.class);
	   newUser.update(bmobUser.getObjectId(), new UpdateListener() {
		
		@Override
		public void done(BmobException e) {
			if(e==null){

			}else if(e.getErrorCode()==206){
				Toast.makeText(weather_info.this, "为了您的账户安全，请重新登录", Toast.LENGTH_SHORT).show();
				MyUser.logOut();
				MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
				Intent intent=new Intent(getApplicationContext(),loginAct.class);
				startActivity(intent);
				finish();
			}
			
			
		}
	    });
		
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
			final String path=getPath(weather_info.this, uri);  //这里取得路径，将uri转化为路径
			ContentResolver cr=this.getContentResolver();
			fragmentPart.editor.putString("userPicture",path);
			fragmentPart.editor.commit();
			try{
				Bitmap bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri));
				fragmentPart.refreshUserPicture(bitmap);
		        
             	final BmobFile bmobFile=new BmobFile(new File(path));
				bmobFile.uploadblock(new UploadFileListener() {
					
					
					@Override
					public void onProgress(Integer value){
						
					}

					@Override
					public void done(BmobException e) {
						   if(e==null){
						String fileUrl=bmobFile.getFileUrl();
					    MyUser userInfo=BmobUser.getCurrentUser(MyUser.class);
						final String yuanUrl=userInfo.getTouXiangUrl();
						userInfo.setTouXiangUrl(fileUrl);
						
						userInfo.update(userInfo.getObjectId(),new UpdateListener() {
							
							@Override
							public void done(BmobException e) {
								  if(e==null){
							            Toast.makeText(weather_info.this, "保存成功",Toast.LENGTH_SHORT).show();
							            BmobFile file=new BmobFile();
							            file.setUrl(yuanUrl);
							            file.delete(new UpdateListener() {
											
											@Override
											public void done(BmobException e) {
												if(e==null){
													
												}else {
													Toast.makeText(weather_info.this,e.getMessage(), Toast.LENGTH_SHORT).show();
												}
												
											}
										});
							        }else{
							           Toast.makeText(weather_info.this,"更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
							        }
								
							}
					    	});
						   } else {
							   Toast.makeText(weather_info.this,"失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
							
						}
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
	public static String getPath(final Context context, final Uri uri) {  
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
	        // ExternalStorageProvider  
	        if (isExternalStorageDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
	            }  
	  
	            // TODO handle non-primary volumes  
	        }  
	        // DownloadsProvider  
	        else if (isDownloadsDocument(uri)) {  
	  
	            final String id = DocumentsContract.getDocumentId(uri);  
	            final Uri contentUri = ContentUris.withAppendedId(  
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
	  
	            return getDataColumn(context, contentUri, null, null);  
	        }  
	        // MediaProvider  
	        else if (isMediaDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            Uri contentUri = null;  
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
	            }  
	  
	            final String selection = "_id=?";  
	            final String[] selectionArgs = new String[] {  
	                    split[1]  
	            };  
	  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    }  
	    // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
	  
	        // Return the remote address  
	        if (isGooglePhotosUri(uri))  
	            return uri.getLastPathSegment();  
	  
	        return getDataColumn(context, uri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	  
	    return null;  
	}  
	  
	/** 
	 * Get the value of the data column for this Uri. This is useful for 
	 * MediaStore Uris, and other file-based ContentProviders. 
	 * 
	 * @param context The context. 
	 * @param uri The Uri to query. 
	 * @param selection (Optional) Filter used in the query. 
	 * @param selectionArgs (Optional) Selection arguments used in the query. 
	 * @return The value of the _data column, which is typically a file path. 
	 */  
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = "_data";  
	    final String[] projection = {  
	            column  
	    };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}  
	
}
