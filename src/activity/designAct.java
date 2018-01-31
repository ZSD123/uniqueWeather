package activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.api.maps2d.model.BitmapDescriptor;
import com.sharefriend.app.R;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;  
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class designAct extends baseActivity {
    private GridView gridView;
    private int [] icon={R.drawable.back,R.drawable.sun,R.drawable.flower,R.drawable.white_design,R.drawable.night1};
    private String [] iconName={"深蓝星空","温暖阳光","樱花飘零","白色简约","夜间模式"};
    private List<Map<String, Object>> data_list;
    private designAdapter sim_adapter;
    private int designNum;
    private int yuanNum;   //原来的Num
    
    private SharedPreferences pre;
    private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.design);
		
		final RelativeLayout rLayout=(RelativeLayout)findViewById(R.id.relative);
		final CustomFontTextView textView1=(CustomFontTextView)findViewById(R.id.account);
		
	
		
		gridView=(GridView)findViewById(R.id.gridview);
      
		pre=PreferenceManager.getDefaultSharedPreferences(this);
		editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
		
		
		designNum=pre.getInt("design", 0);
		
		if(designNum==4){
			rLayout.setBackgroundColor(Color.parseColor("#051C3D"));
		    textView1.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		yuanNum=designNum;
		
		data_list = new ArrayList<Map<String, Object>>();
		getData();
		
		String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new designAdapter(this, data_list, R.layout.item_design, from, to);
        //配置适配器
   
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				LinearLayout lin=view.findViewById(R.id.lin);
				lin.setBackgroundResource(R.drawable.bian);
				
				TextView textView=view.findViewById(R.id.text);
				if(textView.getText().length()==4) 
			       textView.setText(textView.getText()+"(已选择)");
				
				yuanNum=designNum;
				designNum=position;
				sim_adapter.notifyDataSetChanged();
				
				editor.putInt("design", position);
				editor.commit();
				
				if(position!=yuanNum){
			    	Toast.makeText(designAct.this, "正在加载请稍后",Toast.LENGTH_SHORT).show();
			    	if(yuanNum==4){
			              fragmentChat.setFromNightModeToOthers();
			              rLayout.setBackgroundColor(Color.parseColor("#E4EDF5"));
			  		      textView1.setTextColor(Color.parseColor("#000000"));
			  		      
			  		      sim_adapter.notifyDataSetChanged();
			    	}
			    	
			    	
			    	switch (position) {
						case 0:
						   
						    fragmentChat.leftmenuBack.setBackgroundResource(R.drawable.back);
							fragmentChat.lin1.setBackgroundColor(Color.parseColor("#2B5EA4"));
							fragmentChat.weatherLinearLayout.setBackgroundColor(Color.parseColor("#E4EDF5"));
							
							fragmentChat.setButtonResource(new int []{R.drawable.danblue,R.drawable.white0});
							fragmentChat.setButtonColor("#68B4FF","#000000");
							
							fragmentChat.setTextColorWhite();
				    	  break;
						case 1:
							fragmentChat.leftmenuBack.setBackgroundResource(R.drawable.sun);
							fragmentChat.lin1.setBackgroundColor(Color.parseColor("#F2CC4D"));
							fragmentChat.weatherLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
							
							  
							fragmentChat.setButtonResource(new int []{R.drawable.danyellow,R.drawable.white1});
							fragmentChat.setButtonColor("#F2CC4D","#000000");
							
							fragmentChat.setTextColorBlack();
							break;
						case 2: 
							fragmentChat.leftmenuBack.setBackgroundResource(R.drawable.flower);
							fragmentChat.lin1.setBackgroundColor(Color.parseColor("#FCBC1D"));
							fragmentChat.weatherLinearLayout.setBackgroundColor(Color.parseColor("#F5D4D9"));
							
							fragmentChat.setButtonResource(new int []{R.drawable.danflower,R.drawable.pink});
							fragmentChat.setButtonColor("#FFFFFF","#000000");
							
							fragmentChat.setTextColorBlack();   //设置名片text和nickName为黑色，这样好些
							break;  
						case 3:
							fragmentChat.leftmenuBack.setBackgroundResource(R.drawable.coldmount);
							fragmentChat.lin1.setBackgroundColor(Color.parseColor("#FFFFFF"));
							fragmentChat.weatherLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
							
							
							fragmentChat.setButtonResource(new int []{R.drawable.danblack,R.drawable.white1});
							fragmentChat.setButtonColor("#000000","#000000");
							
							fragmentChat.weather.setTextColor(Color.parseColor("#000000"));
							fragmentChat.temper.setTextColor(Color.parseColor("#000000"));
							
							fragmentChat.setTextColorBlack();
							break;  
						case 4:
			 				fragmentChat.leftmenuBack.setBackgroundResource(R.drawable.night1);
							fragmentChat.lin1.setBackgroundColor(Color.parseColor("#122950"));
							fragmentChat.weatherLinearLayout.setBackgroundColor(Color.parseColor("#0A182D"));
							
							fragmentChat.setButtonResource(new int []{R.drawable.dannight,R.drawable.white2});
							fragmentChat.setButtonColor("#6A82A5","#A2C0DE");
							
							fragmentChat.setNightMode();
							
							rLayout.setBackgroundColor(Color.parseColor("#051C3D"));
						    textView1.setTextColor(Color.parseColor("#A2C0DE"));
							break;
						default :
							break;
					}
			    	Toast.makeText(designAct.this, "更新完毕",Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(designAct.this, "当前已选择",Toast.LENGTH_SHORT).show();
				}
			}
		});
        gridView.setAdapter(sim_adapter);
        
	}
	public List<Map<String, Object>> getData(){        
        //icon和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
            
        return data_list;
    }
	class designAdapter extends SimpleAdapter{
		
		
  
		public designAdapter(Context context,List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
            if(convertView==null){
            	viewHolder=new ViewHolder();
            	convertView=LayoutInflater.from(designAct.this).inflate(R.layout.item_design,null);
            	viewHolder.lin=(LinearLayout)convertView.findViewById(R.id.lin);
            	viewHolder.image=(ImageView)convertView.findViewById(R.id.image);
            	viewHolder.text=(TextView)convertView.findViewById(R.id.text);
            	viewHolder.text1=(TextView)convertView.findViewById(R.id.text1);
            	convertView.setTag(viewHolder);
            }else {
				viewHolder=(ViewHolder)convertView.getTag();
			}
            
            
            viewHolder.lin.setBackgroundResource(0);
            viewHolder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(),icon[position]));
            if(designNum==4){
            	viewHolder.text.setTextColor(Color.parseColor("#A2C0DE"));
            }else {
            	viewHolder.text.setTextColor(Color.parseColor("#000000"));
			}
            
            if(position==2||position==3){ 
            	viewHolder.text1.setVisibility(View.VISIBLE);
            	 if(designNum==4){
                  	viewHolder.text1.setTextColor(Color.parseColor("#A2C0DE"));
                  }else {
                  	viewHolder.text1.setTextColor(Color.parseColor("#000000"));
      			}
            	viewHolder.text1.setText("作者：吴迪");
            	
            }
            
            viewHolder.text.setText(iconName[position]);
            
            if(position==designNum){
            	viewHolder.lin.setBackgroundResource(R.drawable.bian);
            	if(viewHolder.text.getText().length()==4)
				   viewHolder.text.setText(viewHolder.text.getText()+"(已选择)");
            }
            
            return convertView;
		}
		
		
	}
	class ViewHolder{
		LinearLayout lin;
		ImageView image;
		TextView text;
		TextView text1;
	}

}
