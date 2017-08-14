package activity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import myCustomView.CircleImageView;
import myCustomView.TouchImageView;

import cn.bmob.v3.datatype.BmobFile;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.uniqueweather.app.R;

import Util.Utility;
import Util.download;
import adapter.ImageLoaderFactory;
import android.R.integer;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class xiangxiDataAct extends baseActivity {
	private Spinner spinner1;    //�Ա��
	private EditText editText3;   //����EditText
    private Spinner spinner2;    //ְҵ
    private EditText editText6;  //���ڵ�EditText
	private AMapLocationClient mLocationClient=null;
	private AMapLocationClientOption mLocationClientOption=null;
	private EditText editText7;   //����EditText
	private EditText editText2;   //����EditText
	private EditText editText4;   //����EditText
	private EditText editText5;   //ѧУEditText
    private CircleImageView circleImageView;
	private TextView textView;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayAdapter<String> arrayAdapter2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view=getLayoutInflater().inflate(R.layout.xiangxidata, null);
		setContentView(view);
		circleImageView=(CircleImageView)findViewById(R.id.userPicture);
		textView=(TextView)findViewById(R.id.userName);
		spinner1=(Spinner)findViewById(R.id.spinner1);
		spinner2=(Spinner)findViewById(R.id.spinner2);
		editText2=(EditText)findViewById(R.id.nianling);
		editText3=(EditText)findViewById(R.id.shengri);
		editText4=(EditText)findViewById(R.id.xingzuo);
		editText5=(EditText)findViewById(R.id.xuexiao);
		editText6=(EditText)findViewById(R.id.suozaidi);
		editText7=(EditText)findViewById(R.id.guxiang);

        final MyUser myUser=(MyUser) getIntent().getBundleExtra("bundle").getSerializable("myUser");        
        
        final String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+myUser.getObjectId()+".jpg_";
        final File file=new File(path);
        if(myUser.getTouXiangUrl()!=null)
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				 final Bitmap bitmap= Utility.getPicture(myUser.getTouXiangUrl());
                 runOnUiThread(new Runnable() {
			 		
					@Override
					public void run() {
						if(bitmap!=null){
					     	circleImageView.setImageBitmap(bitmap);
					     	download.saveYonghuPic(bitmap, myUser.getObjectId());
						} else if(file.exists()){
				        	try {
				    			InputStream is=new FileInputStream(file);
				    			
				    			BitmapFactory.Options opts=new BitmapFactory.Options();
				    			opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
				    			
				    			opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
				    			opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
				    			
				    			opts.inSampleSize=2;
				    			opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
				    			
				    			Bitmap bitmap2=BitmapFactory.decodeStream(is, null, opts);
				    			circleImageView.setImageBitmap(bitmap2);
				    		} catch (FileNotFoundException e1) {
				    			// TODO Auto-generated catch block
				    			e1.printStackTrace();
				    		}

						}else {
							 try {
									InputStream is=new FileInputStream(file);
									
									BitmapFactory.Options opts=new BitmapFactory.Options();
									opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
									
									opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
									opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
									
									opts.inSampleSize=2;
									opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
									
									Bitmap bitmap2=BitmapFactory.decodeResource(getResources(),R.drawable.userpicture, opts);
									circleImageView.setImageBitmap(bitmap2);
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						  }
						
						
					}
				});
			
			}
		}).start();
       
        final String arr[]=new String[]{
    	    	"��",
    	    	"Ů",
    	    	"����"
    	    };
    	    final String arr1[]=new String[]{
    	     "IT��ҵ:������/�����/ͨ��",
    	     "���죺����/����/����",
    	     "ҽ�ƣ�ҽѧ/����/��ҩ",
    	     "���ڣ�����/Ͷ��/����",
    	     "��ҵ����ҵ/����ҵ/���徭Ӫ",
    	     "�Ļ����Ļ�/���/��ý",
    	     "����:����/����/����",
    	     "����:��ʦ/����",
    	     "����:����/��ѵ",
    	     "����:����Ա/����/��ҵ��λ",
    	     "ģ��",
    	     "������Ա������/�ս�",
    	     "ѧ��",
    	     "����"
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
 	       if(sex.equals("��")){
 	    	  spinner1.setSelection(0);
 	    	  
 	       }
 	      else if(sex.equals("Ů")){
 			  spinner1.setSelection(1);
 		   }else if(sex.equals("����")){
 			  spinner1.setSelection(2);
 	    	}
 	     if(myUser.getAge()!=null)
 	       editText2.setText(myUser.getAge());
 	     if(myUser.getShengri()!=null)
 	       editText3.setText(myUser.getShengri());
 	     if(myUser.getConstellation()!=null)
 	        editText4.setText(myUser.getConstellation());
        
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
			 				Toast.makeText(xiangxiDataAct.this, "����ʧ��,"+reason.toString(), Toast.LENGTH_SHORT).show();
			 				
			 				
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
							
							  BmobFile bmobFile=new BmobFile(str+".png",null,path);//ȷ���ļ����֣�ͷ��.png���������ַ
							  download.downloadFile1(bmobFile,xiangxiDataAct.this);//�������ز���
						    }
			 			}
			 		});
			          popupWindow.showAtLocation(view,Gravity.CENTER,0 , 0);
					
				}
			});
	}
       
}
