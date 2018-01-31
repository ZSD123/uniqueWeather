package adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.sharefriend.app.R;

import model.location;
import myCustomView.CircleImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import Util.Utility;
import Util.download;
import activity.MyUser;
import activity.fragmentChat;
import activity.fujinlieAct;
import adapter.friendAdapter.friendViewHolder;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class fujinAdapter extends Adapter<RecyclerView.ViewHolder> {

    private int count;
    private List<MyUser> list;
    private Context mContext;
    private SharedPreferences pre;
    public fujinAdapter(int count,Context mContext,List<MyUser> list){
  
    	this.count=count;
    	this.mContext=mContext;
    	this.list=list;
    	pre=PreferenceManager.getDefaultSharedPreferences(mContext);
    }
	@Override
	public int getItemCount() {
		return this.count;
	}
	public MyUser getItem(int position){
		return list.get(position);
	}
    
	private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    
    
  
	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		((fujinrenViewHolder)holder).itemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onRecyclerViewListener!=null)
			    	onRecyclerViewListener.onItemClick(position);
				
				
			}
		});
		

		
		String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+list.get(position).getObjectId()+".jpg_";

    	File file=new File(path);
  
		if(file.exists()){

			  try {
					    InputStream is=new FileInputStream(file);

						BitmapFactory.Options opts=new BitmapFactory.Options();
		
						opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存

						opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式

						opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
	
						opts.inSampleSize=2;
	
						opts.inInputShareable=true;//设置解码位图的尺寸信息
		
						Bitmap bitmap2=BitmapFactory.decodeStream(is, null, opts);
		
						((fujinrenViewHolder)holder).circleImageView.setImageBitmap(bitmap2);
			
			     } catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();

					}
		
		}else {

			if(list.get(position).getTouXiangUrl()!=null){
			    new Thread(new Runnable() {
					
					@Override
					public void run() {
		
						final Bitmap bitmap=Utility.getPicture(list.get(position).getTouXiangUrl());
                         ((Activity) mContext).runOnUiThread(new Runnable() {
					 		
							@Override
							public void run() {
								if(bitmap!=null){
									((fujinrenViewHolder)holder).circleImageView.setImageBitmap(bitmap);
							     	download.saveYonghuPic(bitmap,list.get(position).getObjectId());
	
								}
								
							}
						});
					
					}
				}).start();
				}else {

					((fujinrenViewHolder)holder).circleImageView.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.userpicture));
				}
		}

		int designNum=pre.getInt("design", 0);
		if(designNum==4){
			((fujinrenViewHolder)holder).tv_name.setTextColor(Color.parseColor("#A2C0DE"));
			((fujinrenViewHolder)holder).tv_distance.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		
		if(list.get(position).getNick()!=null){
		  ((fujinrenViewHolder)holder).tv_name.setText(list.get(position).getNick());
		}
		if(list.get(position).getSex()!=null&&!list.get(position).getSex().equals("")){
			if(list.get(position).getSex().equals("男")){
				((fujinrenViewHolder)holder).sexImage.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.man));
				
			}else if(list.get(position).getSex().equals("女")){
				((fujinrenViewHolder)holder).sexImage.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.woman));
			}
		}
		
	
		int distanceofAbs=fujinlieAct.fujinliedb.getDistanceById(list.get(position).getObjectId());
		
		if(distanceofAbs>1000000){
			((fujinrenViewHolder)holder).tv_distance.setText(">1000km");
		}else if(distanceofAbs<100){
			((fujinrenViewHolder)holder).tv_distance.setText("<100m");
		}else {
			((fujinrenViewHolder)holder).tv_distance.setText(distanceofAbs+"m");
		}
	
		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new fujinrenViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_fujin_yonghu_lie, parent,false));
	}
	class fujinrenViewHolder extends ViewHolder{
        CircleImageView circleImageView;
        TextView tv_name;
        ImageView sexImage;
        TextView tv_distance;
        TextView tv_city;
        
		public fujinrenViewHolder(View view) {
			super(view);
		    circleImageView=(CircleImageView)view.findViewById(R.id.circleimage);
		    tv_name=(TextView)view.findViewById(R.id.tv_name);
		    sexImage=(ImageView)view.findViewById(R.id.sexImage);
		    tv_distance=(TextView)view.findViewById(R.id.distance);
		    tv_city=(TextView)view.findViewById(R.id.city);
		}
		
	}

}
