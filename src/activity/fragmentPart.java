package activity;


import com.uniqueweather.app.R;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragmentPart extends Fragment 
{   public static String keyToGet="begin";
    private String theKey;
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
		}
		else if(theKey.equals("map"))
		{
			view=inflater.inflate(R.layout.map,container,false);
		}
	
		return view;
	}
     


}
