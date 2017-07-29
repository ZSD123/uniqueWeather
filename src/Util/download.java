package Util;

import java.io.File;

import activity.fragmentChat;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

public class download {
     public static void downloadFile(final BmobFile file,final Context context){//刷新头像

    	   final File saveFile=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/",file.getFilename());//文件路径
    	   final String filename=file.getFilename();
    	 
    	 file.download(saveFile, new DownloadFileListener() {
			
			@Override
			public void onProgress(Integer arg0, long arg1) {
				
				
			}
			
			@Override
			public void done(String savePath, BmobException e) {
	
			      if(e==null){
			    	     BitmapFactory.Options opts=new BitmapFactory.Options();
						opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
						
						opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
						opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
						
						opts.inSampleSize=2;
						opts.inInputShareable=true;//设置解码位图的尺寸信息
						
	                    Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/"+filename,opts);
			    	    fragmentChat.userPicture.setImageBitmap(bitmap);
			    	  
			    	  
			      }else if(e.getErrorCode()==9016){
					Toast.makeText(context,"无网络连接，请检查您的手机网络", Toast.LENGTH_SHORT).show();
					setBitmapWithoutWeb(filename);
					}else {
						Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
						setBitmapWithoutWeb(filename);
				     }
				
			}
		});
     }
     public static void downloadFile1(final BmobFile file,final Context context){//下载图片

  	   final File saveFile=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/downloadPicture/",file.getFilename());//文件路径
  	   final String filename=file.getFilename();
  	 
  	 file.download(saveFile, new DownloadFileListener() {
			
			@Override
			public void onProgress(Integer arg0, long arg1) {
				
				
			}
			
			@Override
			public void done(String savePath, BmobException e) {
	
			      if(e==null){
						Toast.makeText(context,"图片下载成功，保存在"+savePath, Toast.LENGTH_SHORT).show();
			    	  }else if(e.getErrorCode()==9016){
				     	Toast.makeText(context,"无网络连接，请检查您的手机网络", Toast.LENGTH_SHORT).show();
					    setBitmapWithoutWeb(filename);
					}else {
						Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
						setBitmapWithoutWeb(filename);
				     }
				
			}
		});
   }
     public static void setBitmapWithoutWeb(String filename){
    	 File file =new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/"+filename);
			if(file.exists()){
				
				   BitmapFactory.Options opts=new BitmapFactory.Options();
					opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
					
					opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
					opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
					
					opts.inSampleSize=2;
					opts.inInputShareable=true;//设置解码位图的尺寸信息
					
                   Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/"+filename,opts);
		           fragmentChat.userPicture.setImageBitmap(bitmap);
			}
     }
     
     
}
