package activity;


import com.uniqueweather.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class register extends Activity {
    private ImageButton imageButton; //����ImageButton
	private Button button;    //ע��Button
	private ImageView imageView;//�۾�ImageView
    private boolean eye=false;     //�۾�����
    private EditText editText1;   //�ʺ�EditText
    private EditText editText2;   //����EditText
    private EditText editText3;   //��֤��EditText
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		button=(Button)findViewById(R.id.zhuce);
		imageView=(ImageView)findViewById(R.id.kekanmima);
		editText1=(EditText)findViewById(R.id.editText_account);
		editText2=(EditText)findViewById(R.id.editText_secret);
		editText3=(EditText)findViewById(R.id.yanzhengma);
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				finish();
				
			}
		});
		 imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
				     if(eye){
				    	 editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
				    	 imageView.setImageResource(R.drawable.eyeclose);
				    	 eye=false;
				    	 
				     }else {
						editText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						imageView.setImageResource(R.drawable.eyeopen);
						eye=true;
					}
					
				}
			});
	}
    


}
