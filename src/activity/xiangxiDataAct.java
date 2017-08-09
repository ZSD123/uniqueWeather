package activity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import myCustomView.CircleImageView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.uniqueweather.app.R;

import Util.Utility;
import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class xiangxiDataAct extends Activity {
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xiangxidata);
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

        MyUser myUser=(MyUser) getIntent().getBundleExtra("bundle").getSerializable("myUser");        
        
        String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+myUser.getObjectId()+".jpg_";
        File file=new File(path);
        
        Bitmap bitmap= Utility.getPicture(myUser.getTouXiangUrl());
        if(bitmap!=null){
        	circleImageView.setImageBitmap(bitmap);
        }else if(file.exists()){
        	try {
    			InputStream is=new FileInputStream(file);
    			
    			BitmapFactory.Options opts=new BitmapFactory.Options();
    			opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
    			
    			opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
    			opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
    			
    			opts.inSampleSize=2;
    			opts.inInputShareable=true;//设置解码位图的尺寸信息
    			
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
					opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
					
					opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
					opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
					
					opts.inSampleSize=2;
					opts.inInputShareable=true;//设置解码位图的尺寸信息
					
					Bitmap bitmap2=BitmapFactory.decodeResource(getResources(),R.drawable.userpicture, opts);
					circleImageView.setImageBitmap(bitmap2);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		  }
        
         textView.setText(myUser.getNick());
         String sex=(String)myUser.getSex();
 	     if(sex!=null)
 	       if(sex.equals("男")){
 	    	  spinner1.setSelection(0);
 	    	  
 	       }
 	      else if(sex.equals("女")){
 			  spinner1.setSelection(1);
 		   }else if(sex.equals("保密")){
 			  spinner1.setSelection(2);
 	    	}
 	     
 	     editText2.setText(myUser.getAge());
 	     editText3.setText(myUser.getShengri());
 	     editText4.setText(myUser.getConstellation());
        
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
 	     
 	        int sel=0;
 	         for (int i = 0; i < arr1.length; i++) {
 	 			if(arr1[i].equals((String)MyUser.getObjectByKey("zhiye")))
 	 				sel=i;
 	 		}
 	 	    spinner2.setSelection(sel);
            Log.d("Main", "进入后职业="+myUser.getZhiye());
 	 	    editText5.setText(myUser.getSchool());
 	 	    editText6.setText(myUser.getSuozaidi());
 	 	    editText7.setText(myUser.getGuxiang());
 	        spinner1.setEnabled(false);
 	        spinner2.setEnabled(false);
	}
       
}
