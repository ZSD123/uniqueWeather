package activity;


import java.util.ArrayList;
import java.util.List;

import myCustomView.CircleImageView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

import com.sharefriend.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.Pools.Pool;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class manageAct extends baseActivity {

	
	//这是账户管理修改密码什么的
    class ViewHolder{
    	TextView textView;
    	
    }
    ViewHolder viewHolder;
    private MyUser currentUser;
    
    private SharedPreferences pre;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manage);
		
		pre=PreferenceManager.getDefaultSharedPreferences(this);
		
		CustomFontTextView textView=(CustomFontTextView)findViewById(R.id.account);
		TextView tView=(TextView)findViewById(R.id.text);
		
		RelativeLayout relative=(RelativeLayout)findViewById(R.id.relative);
		
		final int designNum=pre.getInt("design", 0);
		if(designNum==4){
			relative.setBackgroundColor(Color.parseColor("#051C3D"));
			textView.setTextColor(Color.parseColor("#A2C0DE"));
			tView.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		currentUser=BmobUser.getCurrentUser(MyUser.class);
		RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.relativeGuanyu);
		ListView listView=(ListView)findViewById(R.id.listview);
		final List<String> list=new ArrayList<String>();
		list.add("修改密码");
		list.add("黑名单");
		list.add("设置附近人是否可以打电话");
		 if(loginAct.isEmail(currentUser.getUsername())&&!currentUser.getEmailVerified()){
			  list.add("邮箱认证");
		 }
		BaseAdapter baseAdapter=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				   if(convertView==null){
						convertView=getLayoutInflater().inflate(R.layout.item_account, null);
						viewHolder=new ViewHolder();
		                viewHolder.textView=(TextView)convertView.findViewById(R.id.text);
						convertView.setTag(viewHolder);
						}else {
							viewHolder=(ViewHolder)convertView.getTag();
						}
				      if(designNum==4){
				    	  viewHolder.textView.setTextColor(Color.parseColor("#A2C0DE"));
				    	  
				      }
	                  viewHolder.textView.setText(list.get(position));
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return list.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		listView.setAdapter(baseAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 if(position==0){
					 Intent intent=new Intent(manageAct.this,modifypasswordAct.class);
					 startActivity(intent);
				 }else if(position==1){
					 Intent intent=new Intent(manageAct.this,blackAct.class);
					 startActivity(intent);
				 }else if(position==2){
					 Intent intent=new Intent(manageAct.this,setMoblieCall.class);
					 startActivity(intent);
				 }else if(position==3){
					 Intent intent=new Intent(manageAct.this,emailVerifyAct.class);
					 startActivity(intent);
				 }
				
			}
		});
		
		relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		      Intent intent=new Intent(manageAct.this,guanYuAct.class);
			  startActivity(intent);
			}
		});
	}
    
}
