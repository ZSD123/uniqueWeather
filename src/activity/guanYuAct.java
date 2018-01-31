package activity;

import java.util.ArrayList;
import java.util.List;

import com.sharefriend.app.R;

import activity.manageAct.ViewHolder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class guanYuAct extends baseActivity {
    private ListView listView;
    
    class ViewHolder{
    	TextView textView;
    	
    }
    ViewHolder viewHolder;
    
    
    private SharedPreferences pre;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guanyu);
		
		pre=PreferenceManager.getDefaultSharedPreferences(this);
		
		
		LinearLayout lin=(LinearLayout)findViewById(R.id.lin);
		CustomFontTextView textView=(CustomFontTextView)findViewById(R.id.text_EndRain);
		TextView v1=(TextView)findViewById(R.id.v1);
		
		final int designNum=pre.getInt("design", 0);
		if(designNum==4){
			lin.setBackgroundColor(Color.parseColor("#051C3D"));
			textView.setTextColor(Color.parseColor("#A2C0DE"));
			v1.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		listView=(ListView)findViewById(R.id.listview);
		final List<String> list=new ArrayList<String>();
		list.add("π¶ƒ‹ΩÈ…‹");
		list.add("∑¥¿°");
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
				     Intent intent=new Intent(guanYuAct.this,functionAct.class);
				     startActivity(intent);
				 }else if(position==1){
				     Intent intent=new Intent(guanYuAct.this,fankuiAct.class);
				     startActivity(intent);
				 }
				
			}
		});
	}
     
}
