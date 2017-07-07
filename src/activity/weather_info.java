package activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import message.myMessageHandler;
import model.BmobIMApplication;
import model.UniversalImageLoader;

import cn.bmob.newim.BmobIM;
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

import db.myUserDB;
import db.myUserdbHelper;


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
	 public static myUserdbHelper dbHelper;
	 public static myUserDB myUserdb;

	 private FragmentPagerAdapter mAdapter;
	 private String username;
	 public myMessageHandler handler;
	@Override
	public void onCreate(Bundle savedInstance)
	{   
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
	    init();	
	    mViewPager.setAdapter(mAdapter);
	    UniversalImageLoader.initImageLoader(this);
	    
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
	   
	   myUserdb=myUserDB.getInstance(this);
	   username=(String)MyUser.getObjectByKey("username");
	  
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
			ContentResolver cr=this.getContentResolver();
			try{
				Bitmap bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri));
				fragmentPart.refreshUserPicture(bitmap);
		        
				bitmap=compressImage(bitmap);
				File file=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/"+"头像.png");
				BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
				bitmap.compress(Bitmap.CompressFormat.PNG, 100,bos);
				bos.flush();
				bos.close();
				
             	final BmobFile bmobFile=new BmobFile(file);
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
	private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>1000) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩        
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
	
}
