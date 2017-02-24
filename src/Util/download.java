package Util;

import java.io.File;

import activity.fragmentPart;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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
    	 file.download(saveFile, new DownloadFileListener() {
			
			@Override
			public void onProgress(Integer arg0, long arg1) {
				
				
			}
			
			@Override
			public void done(String savePath, BmobException e) {
			      if(e==null){
	                  Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)BmobUser.getObjectByKey("username")+"/"+filename);
			    	  fragmentPart.userPicture.setImageBitmap(bitmap);
			      }else {
					Toast.makeText(context,"连接失败，"+e.getMessage()+e.getErrorCode(),Toast.LENGTH_SHORT).show();
					Log.d("Main",e.getMessage()+e.getErrorCode());
				}
				
			}
		});
     }
}
