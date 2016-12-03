package activity;


import com.uniqueweather.app.R;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view .ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class fragmentPart extends Fragment 
{   public static String keyToGet="begin";
    private String theKey;
	private TextView time;
	private TextView realtime;
	private static RelativeLayout weather_layout;
	private TextView weather;
	private TextView temper;
	private ImageView userPicture;
	private TextView myCity;
	private Button button_switch;
    private Button button_refresh;
    public String countyName;
    private ImageView pic;
    private TextView userName;
    private TextView countyname;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
    {   
		if(getArguments()!=null)
		{
			theKey=getArguments().getString(keyToGet);
			
		}
		if(theKey.equals("weather"))
		{   
			view=inflater.inflate(R.layout.connection,container,false);
		    userName=(TextView)view.findViewById(R.id.userName);
			time=(TextView)view.findViewById(R.id.time);
	        userPicture=(ImageView)view.findViewById(R.id.userPicture);
			realtime=(TextView)view.findViewById(R.id.realTime);
			weather_layout=(RelativeLayout)view.findViewById(R.id.weather_info);
			weather=(TextView)view.findViewById(R.id.weather);
			temper=(TextView)view.findViewById(R.id.temper);
			myCity=(TextView)view.findViewById(R.id.myCity);
			button_switch=(Button)view.findViewById(R.id.switch_city);
			button_refresh=(Button)view.findViewById(R.id.refresh);
			countyname=(TextView)view.findViewById(R.id.countyName);
			pic=(ImageView)view.findViewById(R.id.weather_pic);
			
		
		}
		else if(theKey.equals("map"))
		{
			view=inflater.inflate(R.layout.map,container,false);
		}
	
		return view;
	}
     


}
