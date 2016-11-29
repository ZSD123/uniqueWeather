package adapter;

import java.util.List;

import com.uniqueweather.app.R;




import activity.myCityAction;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class myCityAdapter extends android.widget.ArrayAdapter<model.myCity> {
    private int resourceId;
	public myCityAdapter(Context context, int resource, List<model.myCity> objects) {
		super(context, resource, objects);
		resourceId=resource;
	}
	@Override
	public View getView(int position,View convertView,ViewGroup parent)
	{
		model.myCity mycity=getItem(position);
		View view;
		ViewHolder viewHolder = null;
		if(convertView==null)
		{   viewHolder=new ViewHolder();
			view=android.view.LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder.mycityRelativeLayout=(RelativeLayout)view.findViewById(R.id.mycitylistrelative);
			viewHolder.myCityNameTextView=(TextView)view.findViewById(R.id.myCityName);
			viewHolder.myCityWeatherImageView=(ImageView)view.findViewById(R.id.myCityWeather);
			viewHolder.myCityTemp=(TextView)view.findViewById(R.id.myCityTemp);
			view.setTag(viewHolder);
		}
		else {
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		viewHolder.myCityNameTextView.setText(mycity.getMyCityName());
		Uri uri=Uri.parse(mycity.getMyCityWeatherLocal());
        viewHolder.myCityWeatherImageView.setImageURI(uri);
		viewHolder.myCityTemp.setText(mycity.getMyCityTemp());
		BitmapDrawable drawable=new BitmapDrawable(mycity.getMyCityPicLocal());
		if(drawable!=null)
		{ 
		  viewHolder.mycityRelativeLayout.setBackgroundDrawable(drawable);
		  viewHolder.mycityRelativeLayout.getBackground().setAlpha(120);
		}
		return view;
		
	}
	class ViewHolder{
		RelativeLayout mycityRelativeLayout;
		TextView myCityNameTextView;
		ImageView myCityWeatherImageView;
		TextView myCityTemp;

	}

}
