package activity;

import com.sharefriend.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class fankuiAct extends baseActivity {
	
	 private SharedPreferences pre;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fankui);
		RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.relative);
		TextView textView1=(TextView)findViewById(R.id.text1);
		TextView textView2=(TextView)findViewById(R.id.text2);
		TextView textView3=(TextView)findViewById(R.id.text3);
		TextView textView4=(TextView)findViewById(R.id.text4);
		TextView textView5=(TextView)findViewById(R.id.text5);
		TextView textView6=(TextView)findViewById(R.id.text6);
		TextView textView7=(TextView)findViewById(R.id.text7);
		
		pre=PreferenceManager.getDefaultSharedPreferences(this);
		
		int designNum=pre.getInt("design", 0);
		if(designNum==4){
			relativeLayout.setBackgroundColor(Color.parseColor("#051C3D"));
			textView1.setTextColor(Color.parseColor("#A2C0DE"));
			textView2.setTextColor(Color.parseColor("#A2C0DE"));
			textView3.setTextColor(Color.parseColor("#A2C0DE"));
			textView4.setTextColor(Color.parseColor("#A2C0DE"));
			textView5.setTextColor(Color.parseColor("#A2C0DE"));
			textView6.setTextColor(Color.parseColor("#A2C0DE"));
			textView7.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		
		textView1.setText("��ӭ��Ҽ�Ⱥ�Ļ��߷����估ʱ������лл��ҵ�֧��");
		
		textView2.setText("QQȺ�ţ�");
		
		textView3.setText(Html.fromHtml("<u>"+"594653155"+"</u>"));
		
		textView4.setText("����ţ�");
		
		textView5.setText(Html.fromHtml("<u>"+"1833751104@qq.com"+"</u>"));
		textView3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(joinQQGroup("AM-jba03TOdMs1WGgkzW0YBucCkmkoJL")){
					Toast.makeText(fankuiAct.this, "���������ֻ�QQ��Ⱥ", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(fankuiAct.this, "δ��װ��QQ��װ�İ汾��֧��", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		textView6.setText("app����:");
	
		textView7.setText(Html.fromHtml("<u>"+"http://sharefriend.bmob.site/"+"</u>"));
		textView7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();        
				intent.setAction("android.intent.action.VIEW");    
				Uri content_url = Uri.parse("http://sharefriend.bmob.site/");   
				intent.setData(content_url);  
				startActivity(intent);
				
			}  
		});
		
    }
    /****************
    *
    * �������Ⱥ���̡�Ⱥ�ţ������ѽ���Ⱥ(594653155) �� key Ϊ�� AM-jba03TOdMs1WGgkzW0YBucCkmkoJL
    * ���� joinQQGroup(AM-jba03TOdMs1WGgkzW0YBucCkmkoJL) ���ɷ�����Q�ͻ��������Ⱥ �����ѽ���Ⱥ(594653155)
    *
    * @param key �ɹ������ɵ�key
    * @return ����true��ʾ������Q�ɹ�������fals��ʾ����ʧ��
    ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
       // ��Flag�ɸ��ݾ����Ʒ��Ҫ�Զ��壬�����ã����ڼ�Ⱥ���水���أ�������Q�����棬�����ã������ػ᷵�ص������Ʒ����    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // δ��װ��Q��װ�İ汾��֧��
            return false;
        }
    }

	
}
