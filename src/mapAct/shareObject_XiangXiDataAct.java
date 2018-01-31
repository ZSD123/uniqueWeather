package mapAct;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.shareObject;
import myCustomView.TouchImageView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sharefriend.app.R;

import Util.Utility;
import Util.download;
import activity.MyUser;
import activity.baseActivity;
import adapter.ImageLoaderFactory;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class shareObject_XiangXiDataAct extends baseActivity {
    
	private String imageUrl;
	private String nick;
	private String description;
	private String address;
	
	private ImageButton imageButton;
	private ImageView objectImage;
	private EditText objecteEdit;
	private EditText descriptionEdit;
	private EditText addressEdit;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shareobject_xiangxi);
		
		description=getIntent().getExtras().getString("description");
		
		final String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/shareObject/";
		
		
		imageUrl=getIntent().getExtras().getString("imageUrl");
		nick=getIntent().getExtras().getString("title");

		address=getIntent().getExtras().getString("address");
		
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		objectImage=(ImageView)findViewById(R.id.objectImage);
		objecteEdit=(EditText)findViewById(R.id.objectEdit);
		descriptionEdit=(EditText)findViewById(R.id.descriptionEdit);
		addressEdit=(EditText)findViewById(R.id.addressEdit);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				 final Bitmap bitmap= Utility.getPicture(imageUrl);
                 runOnUiThread(new Runnable() {
			 		
					@Override
					public void run() {
						if(bitmap!=null){
					     	objectImage.setImageBitmap(bitmap);
					     
						} 
					}
                 }
			
	      	);
			}
	  }).start();
			
		objectImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final PopupWindow popupWindow=new PopupWindow(shareObject_XiangXiDataAct.this);
		          View view=getLayoutInflater().inflate(R.layout.pictureview, null);
		          
		          popupWindow.setContentView(view);
		          popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		          popupWindow.setHeight(LayoutParams.MATCH_PARENT);
		          popupWindow.setFocusable(true);
		          view.setOnKeyListener(new OnKeyListener() {
		 			
		 			@Override
		 			public boolean onKey(View v, int keyCode, KeyEvent event) {
		 				  if (keyCode == KeyEvent.KEYCODE_BACK) { 
		 					  if(popupWindow.isShowing()){
		 			            popupWindow.dismiss(); 
		 			            return true;  
		 					  }
		 			        }  
		 				return false;
		 			}
		 		});
		          
		          final TouchImageView imageView=(TouchImageView) view.findViewById(R.id.image);
		          RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.picturerela);
		          relativeLayout.setOnClickListener(new OnClickListener() {
		 			
		 			@Override
		 			public void onClick(View v) {
		 				if(popupWindow.isShowing())
		 					popupWindow.dismiss();
		 				
		 			}
		 		});
		         
		          ImageLoaderFactory.getLoader().load(imageView,imageUrl, R.drawable.no404, new ImageLoadingListener() {
		 			
		 			@Override
		 			public void onLoadingStarted(String arg0, View arg1) {
		 		     
		 				
		 			}
		 			
		 			@Override
		 			public void onLoadingFailed(String arg0, View arg1, FailReason reason) {
		 				Toast.makeText(shareObject_XiangXiDataAct.this, "加载失败,"+reason.toString(), Toast.LENGTH_SHORT).show();
		 				
		 				
		 			}
		 			
		 			@Override
		 			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		 				
		 				
		 			}
		 			
		 			@Override
		 			public void onLoadingCancelled(String arg0, View arg1) {
		 				// TODO Auto-generated method stub
		 				
		 			}
		 		});
		          imageView.setOnClickListener(new OnClickListener() {
		 			
		 			@Override
		 			public void onClick(View v) {
		 				if(popupWindow.isShowing())
		 					popupWindow.dismiss();
		 				
		 			}
		 		});
		     
		          ImageButton button=(ImageButton)view.findViewById(R.id.download);
		          button.setOnClickListener(new OnClickListener() {
		 			
		 			@Override
		 			public void onClick(View v) {
		 			
		 					
					    if(path!=null){
		 				  SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
				    	  Date curDate=new Date(System.currentTimeMillis());
				    	  String str=format.format(curDate);  
						
						  BmobFile bmobFile=new BmobFile(str+".png",null,imageUrl);//确定文件名字（头像.png）和网络地址
						  
						  File saveFile=new File(path, nick+"_"+description+".png");
						  bmobFile.download(saveFile, new DownloadFileListener() {
							
							@Override
							public void onProgress(Integer arg0, long arg1) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void done(String savePath, BmobException e) {
								 if(e==null){
									 Toast.makeText(shareObject_XiangXiDataAct.this,"下载成功，保存路径："+savePath, Toast.LENGTH_SHORT).show();
								 }else {
									 Toast.makeText(shareObject_XiangXiDataAct.this,"失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
								}
								
								
							}
						});
					    }
		 			}
		 		});
		          popupWindow.showAtLocation(view,Gravity.CENTER,0 , 0);
				
				
				
			}
		});
		
		objecteEdit.setText(nick);
		descriptionEdit.setText(description);
		addressEdit.setText(address);
		
		
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		
	}

	

}
