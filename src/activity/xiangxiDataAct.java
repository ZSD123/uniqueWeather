package activity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import mapAct.shareObject_UserXiang;
import myCustomView.CircleImageView;
import myCustomView.TouchImageView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sharefriend.app.R;

import Util.Utility;
import Util.download;
import adapter.ImageLoaderFactory;
import android.R.integer;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class xiangxiDataAct extends baseActivity {
	private Spinner spinner1;    //性别的
	private EditText editText3;   //生日EditText
    private Spinner spinner2;    //职业
    private EditText editText6;  //所在地EditText
	private AMapLocationClient mLocationClient=null;
	private AMapLocationClientOption mLocationClientOption=null;
	private EditText editText7;   //故乡EditText
	private EditText editText2;   //年龄EditText
	private EditText editText4;   //星座EditText
	private EditText editText5;   //学校EditText
    private CircleImageView circleImageView;
	private TextView textView;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayAdapter<String> arrayAdapter2;
	
	private SharedPreferences pre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xiangxidata);
		
		 pre=PreferenceManager.getDefaultSharedPreferences(this);
		
		ScrollView scrollView=(ScrollView)findViewById(R.id.scrollview);
		
		
		circleImageView=(CircleImageView)findViewById(R.id.userPicture);
		textView=(TextView)findViewById(R.id.userName);
		 TextView textView1=(TextView)findViewById(R.id.text1);
		 TextView textView2=(TextView)findViewById(R.id.text2);
		 TextView textView3=(TextView)findViewById(R.id.text3);
		 TextView textView4=(TextView)findViewById(R.id.text4);
		 TextView textView5=(TextView)findViewById(R.id.text5);
		 TextView textView6=(TextView)findViewById(R.id.text6);
		 TextView textView7=(TextView)findViewById(R.id.text7);
		 TextView textView8=(TextView)findViewById(R.id.text8);
		
		spinner1=(Spinner)findViewById(R.id.spinner1);
		spinner2=(Spinner)findViewById(R.id.spinner2);
		editText2=(EditText)findViewById(R.id.nianling);
		editText3=(EditText)findViewById(R.id.shengri);
		editText4=(EditText)findViewById(R.id.xingzuo);
		editText5=(EditText)findViewById(R.id.xuexiao);
		editText6=(EditText)findViewById(R.id.suozaidi);
		editText7=(EditText)findViewById(R.id.guxiang);
		
		int designNum=pre.getInt("design", 0);
		if(designNum==4){
			scrollView.setBackgroundColor(Color.parseColor("#051C3D"));
			editText2.setTextColor(Color.parseColor("#A2C0DE"));
  			editText3.setTextColor(Color.parseColor("#A2C0DE"));
  			editText4.setTextColor(Color.parseColor("#A2C0DE"));
  			editText5.setTextColor(Color.parseColor("#A2C0DE"));
  			editText6.setTextColor(Color.parseColor("#A2C0DE"));
  			editText7.setTextColor(Color.parseColor("#A2C0DE"));
  			
  			editText2.setBackgroundColor(Color.parseColor("#051C3D"));
  			editText3.setBackgroundColor(Color.parseColor("#051C3D"));
  			editText4.setBackgroundColor(Color.parseColor("#051C3D"));
  			editText5.setBackgroundColor(Color.parseColor("#051C3D"));
  			editText6.setBackgroundColor(Color.parseColor("#051C3D"));
  			editText7.setBackgroundColor(Color.parseColor("#051C3D"));
  			
  			textView.setTextColor(Color.parseColor("#A2C0DE"));
  			textView1.setTextColor(Color.parseColor("#A2C0DE"));
  			textView2.setTextColor(Color.parseColor("#A2C0DE"));
  			textView3.setTextColor(Color.parseColor("#A2C0DE"));
  			textView4.setTextColor(Color.parseColor("#A2C0DE"));
  			textView5.setTextColor(Color.parseColor("#A2C0DE"));
  			textView6.setTextColor(Color.parseColor("#A2C0DE"));
  			textView7.setTextColor(Color.parseColor("#A2C0DE"));
  			textView8.setTextColor(Color.parseColor("#A2C0DE"));
  			spinner1.setBackgroundColor(Color.parseColor("#278CCE"));
  			spinner2.setBackgroundColor(Color.parseColor("#278CCE"));
		}

        final MyUser myUser=(MyUser) getIntent().getBundleExtra("bundle").getSerializable("myUser");        
        
        final String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/";
        
    
       
        final String arr[]=new String[]{
    	    	"男",
    	    	"女",
    	    	"保密"
    	    };
    	    final String arr1[]=new String[]{
    	     "IT行业:互联网/计算机/通信",
    	     "制造：生产/工艺/制造",
    	     "医疗：医学/护理/制药",
    	     "金融：银行/投资/保险",
    	     "商业：商业/服务业/个体经营",
    	     "文化：文化/广告/传媒",
    	     "艺术:娱乐/艺术/表演",
    	     "法律:律师/法务",
    	     "教育:教育/培训",
    	     "行政:公务员/行政/事业单位",
    	     "模特",
    	     "乘务人员：机长/空姐",
    	     "学生",
    	     "其他"
    	    };
    	    arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr);
            arrayAdapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr1);
    	    spinner1.setAdapter(arrayAdapter);
    	    spinner2.setAdapter(arrayAdapter2);
    	    
    	    spinner1.setEnabled(false);
  	        spinner2.setEnabled(false);
    	 if(myUser.getNick()!=null)
           textView.setText(myUser.getNick());
    	
            String sex=myUser.getSex();
 	     if(sex!=null)
 	       if(sex.equals("男")){
 	    	  spinner1.setSelection(0);
 	    	  
 	       }
 	      else if(sex.equals("女")){
 			  spinner1.setSelection(1);
 		   }else if(sex.equals("保密")){
 			  spinner1.setSelection(2);
 	    	}
 	     if(myUser.getAge()!=null)
 	       editText2.setText(myUser.getAge());
 	     if(myUser.getShengri()!=null)
 	       editText3.setText(myUser.getShengri());
 	     
 	     if(myUser.getConstellation()!=null){
 	    	
 	        editText4.setText(myUser.getConstellation());
 	     }
 	       int sel=0;
 	         for (int i = 0; i < arr1.length; i++) {
 	 			if(arr1[i].equals(myUser.getZhiye()))
 	 				sel=i;
 	 		}
 	         
 	 	    spinner2.setSelection(sel);
 	 	  if(myUser.getSchool()!=null)
 	 	    editText5.setText(myUser.getSchool());
 	 	  if(myUser.getSuozaidi()!=null)
 	 	    editText6.setText(myUser.getSuozaidi());
 	 	  if(myUser.getGuxiang()!=null)
 	 	    editText7.setText(myUser.getGuxiang());
 	     
         new Thread(new Runnable() {
			
			@Override
			public void run() {
				 final Bitmap bitmap= Utility.getPicture(myUser.getTouXiangUrl());
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
 	 	  
 	 	  
 	 	    circleImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final PopupWindow popupWindow=new PopupWindow(xiangxiDataAct.this);
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
			         
			          ImageLoaderFactory.getLoader().load(imageView,TextUtils.isEmpty(myUser.getTouXiangUrl()) ? path:myUser.getTouXiangUrl(), R.drawable.no404, new ImageLoadingListener() {
			 			
			 			@Override
			 			public void onLoadingStarted(String arg0, View arg1) {
			 		     
			 				
			 			}
			 			
			 			@Override
			 			public void onLoadingFailed(String arg0, View arg1, FailReason reason) {
			 				Toast.makeText(xiangxiDataAct.this, "加载失败,"+reason.toString(), Toast.LENGTH_SHORT).show();
			 				
			 				
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
							
							  BmobFile bmobFile=new BmobFile(myUser.getObjectId()+".jpg",null,myUser.getTouXiangUrl());//确定文件名字（头像.png）和网络地址
							  File saveFile=new File(path,bmobFile.getFilename());
							  bmobFile.download(saveFile,new DownloadFileListener() {
								
								@Override
								public void onProgress(Integer arg0, long arg1) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void done(String savePath, BmobException e) {
									 if(e==null){
										 Toast.makeText(xiangxiDataAct.this,"下载成功，保存路径："+savePath, Toast.LENGTH_SHORT).show();
									 }else {
										 Toast.makeText(xiangxiDataAct.this,"失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
									}
									
									
								}
							});
						    }
			 			}
			 		});
			          popupWindow.showAtLocation(view,Gravity.CENTER,0 , 0);
					
				}
			});
	}
       
}
