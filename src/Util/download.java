package Util;

import java.io.File;

import activity.fragmentPart;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
     public static void downloadFile(final BmobFile file,final Context context){
    	 final File saveFile=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/",file.getFilename());//文件路径
    	 final String filename=file.getFilename();
    	 final SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
    	 file.download(saveFile, new DownloadFileListener() {
			
			@Override
			public void onProgress(Integer arg0, long arg1) {
				
				
			}
			
			@Override
			public void done(String savePath, BmobException e) {
				  editor.putString("userPicture", savePath);
				  editor.commit();
			      if(e==null){
	                  Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/"+filename);
			    	  fragmentPart.userPicture.setImageBitmap(bitmap);
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
				 Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/"+filename);
		    	 fragmentPart.userPicture.setImageBitmap(bitmap);
			}
     }
}
