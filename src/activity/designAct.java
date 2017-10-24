package activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.api.maps2d.model.BitmapDescriptor;
import com.sharefriend.app.R;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;  
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class designAct extends baseActivity {
    private GridView gridView;
    private int [] icon={R.drawable.back,R.drawable.sun};
    private String [] iconName={"深蓝星空","金色阳光"};
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.design);
		gridView=(GridView)findViewById(R.id.gridview);

		data_list = new ArrayList<Map<String, Object>>();
		getData();
		
		String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item_design, from, to);
        //配置适配器
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				LinearLayout lin=view.findViewById(R.id.lin);
				lin.setBackgroundResource(R.drawable.bian);
				TextView textView=view.findViewById(R.id.text);
				textView.setText(textView.getText()+"(已选择)");
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
	

}
