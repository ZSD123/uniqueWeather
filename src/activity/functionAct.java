package activity;

import com.sharefriend.app.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class functionAct extends baseActivity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.function);
		LinearLayout lin=(LinearLayout)findViewById(R.id.lin);
		TextView text1=(TextView)findViewById(R.id.text1);
		TextView text2=(TextView)findViewById(R.id.text2);
		TextView text3=(TextView)findViewById(R.id.text3);
		TextView text4=(TextView)findViewById(R.id.text4);
		TextView text5=(TextView)findViewById(R.id.text5);
		
		int designNum=fragmentChat.pre.getInt("design", 0);
		if(designNum==4){
			lin.setBackgroundColor(Color.parseColor("#051C3D"));
			text1.setTextColor(Color.parseColor("#A2C0DE"));
			text2.setTextColor(Color.parseColor("#A2C0DE"));
			text3.setTextColor(Color.parseColor("#A2C0DE"));
			text4.setTextColor(Color.parseColor("#A2C0DE"));
			text5.setTextColor(Color.parseColor("#A2C0DE"));
		}
	}
       
}
