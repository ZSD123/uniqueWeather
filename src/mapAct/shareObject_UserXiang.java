package mapAct;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import myCustomView.CircleImageView;
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
import activity.xiangxiDataAct;
import adapter.ImageLoaderFactory;
import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class shareObject_UserXiang extends baseActivity {
    
	private CircleImageView circleImageView;
	private EditText nickEdit;
	private EditText ageEdit;
	private EditText sexEdit;
	private EditText crediEdit;
	
	private String imageUrl;
	private String nick;
	private String age;
	private String sex;
	private Integer credit;
	
    private ImageButton imageButton;
    
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shareobject_userxiang);
		
		final String objectId=getIntent().getExtras().getString("objectId");
		
		final String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/";
		
		
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		
		circleImageView=(CircleImageView)findViewById(R.id.userImage);
		nickEdit=(EditText)findViewById(R.id.nickEdit);
		ageEdit=(EditText)findViewById(R.id.ageEdit);
		sexEdit=(EditText)findViewById(R.id.sexEdit);
		crediEdit=(EditText)findViewById(R.id.creditEdit);
		
		imageUrl=getIntent().getExtras().getString("imageUrl");
		nick=getIntent().getExtras().getString("nick");
		age=getIntent().getExtras().getString("age");
		sex=getIntent().getExtras().getString("sex");
		credit=getIntent().getExtras().getInt("credit");
		
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				 final Bitmap bitmap= Utility.getPicture(imageUrl);
                 runOnUiThread(new Runnable() {
			 		
					@Override
					public void run() {
						if(bitmap!=null){
					     	circleImageView.setImageBitmap(bitmap);
					     
						} 
					}
                 }
			
	      	); 
			}
	     }).start();
	    
	    nickEdit.setText(nick);
	    ageEdit.setText(age);
	    sexEdit.setText(sex);
	    crediEdit.setText(credit.toString());
	    
	    circleImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 final PopupWindow popupWindow=new PopupWindow(shareObject_UserXiang.this);
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
		 				Toast.makeText(shareObject_UserXiang.this, "加载失败,"+reason.toString(), Toast.LENGTH_SHORT).show();
		 				
		 				
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
						
						  final BmobFile bmobFile=new BmobFile(objectId+".jpg", null, imageUrl);
						  File saveFile=new File(path, bmobFile.getFilename());
						  bmobFile.download(saveFile, new DownloadFileListener() {
							
							@Override
							public void onProgress(Integer arg0, long arg1) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void done(String savePath, BmobException e) {
								 if(e==null){
									 Toast.makeText(shareObject_UserXiang.this,"下载成功，保存路径："+savePath, Toast.LENGTH_SHORT).show();
								 }else {
									 Toast.makeText(shareObject_UserXiang.this,"失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
								}
								
							}
						});
						  
					    }
		 			}
		 		});
		          popupWindow.showAtLocation(view,Gravity.CENTER,0 , 0);
				
				
			}
		});
	    
	    imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

 
}
