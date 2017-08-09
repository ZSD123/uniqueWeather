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
        
         textView.setText(myUser.getNick());
         String sex=(String)myUser.getSex();
 	     if(sex!=null)
 	       if(sex.equals("��")){
 	    	  spinner1.setSelection(0);
 	    	  
 	       }
 	      else if(sex.equals("Ů")){
 			  spinner1.setSelection(1);
 		   }else if(sex.equals("����")){
 			  spinner1.setSelection(2);
 	    	}
 	     
 	     editText2.setText(myUser.getAge());
 	     editText3.setText(myUser.getShengri());
 	     editText4.setText(myUser.getConstellation());
        
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
 	     
 	        int sel=0;
 	         for (int i = 0; i < arr1.length; i++) {
 	 			if(arr1[i].equals((String)MyUser.getObjectByKey("zhiye")))
 	 				sel=i;
 	 		}
 	 	    spinner2.setSelection(sel);
            Log.d("Main", "�����ְҵ="+myUser.getZhiye());
 	 	    editText5.setText(myUser.getSchool());
 	 	    editText6.setText(myUser.getSuozaidi());
 	 	    editText7.setText(myUser.getGuxiang());
 	        spinner1.setEnabled(false);
 	        spinner2.setEnabled(false);
	}
       
}
