package activity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.b.in;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.sharefriend.app.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class searchAct extends Activity {
	 private ProgressBar progressBar;
	 private RelativeLayout relativeLayout; 
	 private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		
		RelativeLayout relative=(RelativeLayout)findViewById(R.id.relative);
		CustomFontTextView textView1=(CustomFontTextView)findViewById(R.id.account);
		final EditText editText=(EditText)findViewById(R.id.edittext);
		final Button button=(Button)findViewById(R.id.button);
		
		int designNum=fragmentChat.pre.getInt("design", 0);
		if(designNum==4){
			relative.setBackgroundColor(Color.parseColor("#051C3D"));
			textView1.setTextColor(Color.parseColor("#A2C0DE"));
			editText.setTextColor(Color.parseColor("#A2C0DE"));
			button.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		
		
	    progressBar=(ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		relativeLayout=(RelativeLayout)findViewById(R.id.relativelayout1);
		relativeLayout.setVisibility(View.GONE);
		
		
		textView=(TextView)findViewById(R.id.noresult);
		
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()!=0){
					button.setVisibility(View.VISIBLE);
				}else {
					button.setVisibility(View.GONE);
				}
				
			}
		});
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  relativeLayout.setVisibility(View.VISIBLE);
				  progressBar.setVisibility(View.VISIBLE);
				  
				  BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                  query.addWhereEqualTo("username", editText.getText().toString());
                  query.findObjects(new FindListener<MyUser>() {
					
					@Override
					public void done(List<MyUser> list, BmobException e) {
						if(e==null){
							relativeLayout.setVisibility(View.GONE);
							progressBar.setVisibility(View.GONE);
							
							if(list.size()==0){
								textView.setVisibility(View.VISIBLE);
							}else {
								Intent intent=new Intent(searchAct.this,searchResultAct.class);
								intent.putExtra("nick",list.get(0).getNick());
								intent.putExtra("touxiang", list.get(0).getTouXiangUrl());
								intent.putExtra("objectId", list.get(0).getObjectId());
								startActivity(intent);
							 }
						}else {
							Toast.makeText(searchAct.this, "Ê§°Ü,"+e.getMessage(),Toast.LENGTH_SHORT).show();
						}
						
					}
				});
				
			}
		});
		
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				  if (actionId == EditorInfo.IME_ACTION_SEND  
	                        || actionId == EditorInfo.IME_ACTION_DONE  
	                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {  
	                  
					  relativeLayout.setVisibility(View.VISIBLE);
					  progressBar.setVisibility(View.VISIBLE);
					  
					  BmobQuery<MyUser> query=new BmobQuery<MyUser>();
	                  query.addWhereEqualTo("username", editText.getText().toString());
	                  query.findObjects(new FindListener<MyUser>() {
						
						@Override
						public void done(List<MyUser> list, BmobException e) {
							if(e==null){
								relativeLayout.setVisibility(View.GONE);
								progressBar.setVisibility(View.GONE);
								
								if(list.size()==0){
									textView.setVisibility(View.VISIBLE);
								}else {
									Intent intent=new Intent(searchAct.this,searchResultAct.class);
									intent.putExtra("nick",list.get(0).getNick());
									intent.putExtra("touxiang", list.get(0).getTouXiangUrl());
									intent.putExtra("objectId", list.get(0).getObjectId());
									startActivity(intent);
								 }
							}else {
								Toast.makeText(searchAct.this, "Ê§°Ü,"+e.getMessage(),Toast.LENGTH_SHORT).show();
							}
							
						}
					});
	                }  
				return false;
			}
		});
	}

}
