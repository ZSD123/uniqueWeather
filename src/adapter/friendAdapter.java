package adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;

import com.sharefriend.app.R;

import model.Friend;
import myCustomView.CircleImageView;

import Util.Utility;
import Util.download;
import activity.MyUser;
import activity.fragmentChat;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class friendAdapter extends Adapter<RecyclerView.ViewHolder> {
    private List<Friend> friends;
    private Context mContext;
    private final int TYPE_0=0;
    private final int TYPE_FRIEND=1;
    private int colorNum=0;
    
	public friendAdapter(Context mContext,List<Friend> mList) {
		this.mContext=mContext;
		this.friends=mList;
	}
	@Override
	public int getItemCount() {
		
		if(friends==null){
			return 1;
		}else {
			return (friends.size()+1);
		
		}
	
	}
	private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public Friend getItem(int position){
        return this.friends == null?null:this.friends.get(position-1);
    }
    
    public void refresh(List<Friend> list){
    	this.friends=list;
    }
	public void removeData(int position){
		friends.remove(position);
		notifyItemRemoved(position);
	}
	
	
	@Override
	public int getItemViewType(int position) {
		if(position==0){
			return TYPE_0;
		}else if(position>0){
			return TYPE_FRIEND;
		}
		return super.getItemViewType(position);
	}


	
	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		if(position==0){
			if(colorNum==1){
				((zeroViewHolder)holder).textView.setTextColor(Color.parseColor("#A2C0DE"));
			}else if(colorNum==0){
				((zeroViewHolder)holder).textView.setTextColor(Color.parseColor("#000000"));
			}
			((zeroViewHolder)holder).itemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onRecyclerViewListener!=null)
				    	onRecyclerViewListener.onItemClick(position);
					
				}
			});
		}else if(position>0){
	   	
            ((friendViewHolder)holder).itemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onRecyclerViewListener!=null)
				    	onRecyclerViewListener.onItemClick(position);
					
				}
			});
			
    	String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+friends.get(position-1).getFriendUser().getObjectId()+".jpg_";
       
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
						((friendViewHolder)holder).imageView.setImageBitmap(bitmap2);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		
		}else {

				if(friends.get(position-1).getFriendUser().getTouXiangUrl()!=null){
			    new Thread(new Runnable() {
					
					@Override
					public void run() {
						final Bitmap bitmap=Utility.getPicture(friends.get(position-1).getFriendUser().getTouXiangUrl());
                         ((Activity) mContext).runOnUiThread(new Runnable() {
					 		
							@Override
							public void run() {
								if(bitmap!=null){
									((friendViewHolder)holder).imageView.setImageBitmap(bitmap);
							     	download.saveYonghuPic(bitmap, friends.get(position-1).getFriendUser().getObjectId());
							
								}
								
							}
						});
					
					}
				}).start();
				}else {
					((friendViewHolder)holder).imageView.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.userpicture));
				}
		}
		if(colorNum==1){
			((friendViewHolder)holder).tv.setTextColor(Color.parseColor("#A2C0DE"));
		}else if(colorNum==0){
			((friendViewHolder)holder).tv.setTextColor(Color.parseColor("#000000"));
		}
		((friendViewHolder)holder).tv.setText(friends.get(position-1).getFriendUser().getNick());
 
	   }
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	    if(viewType==TYPE_0){
	    	return new zeroViewHolder(LayoutInflater.from(mContext).inflate(R.layout.new_friend, parent,false));
	    }else if(viewType==TYPE_FRIEND){
			return new friendViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user_friend, parent,false));
		}else {
			return null;
		}
		
	}
	class friendViewHolder extends ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        CircleImageView imageView;
        TextView tv;
		public friendViewHolder(View view) {
			super(view);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
			tv=(TextView)view.findViewById(R.id.user_friend_name);
			imageView=(CircleImageView)view.findViewById(R.id.user_friend_image);
		}
		@Override
		public boolean onLongClick(View v) {
			
			return true;
		}
		@Override
		public void onClick(View v) {
			
			
		}
	
		
	}
	class zeroViewHolder extends ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView textView;
		public zeroViewHolder(View view) {
			super(view);
			fragmentChat.newFriendImage1=(ImageView)view.findViewById(R.id.newfriend_image);
			textView=(TextView)view.findViewById(R.id.newfriend);
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onClick(View v) {
			
			
		}
		
	}
	public void refreshTextColor(int i){
		if(i==0)   {   //当它为0的时候为黑色
			colorNum=0;
		}else if(i==1){
			colorNum=1;
		}
		notifyDataSetChanged();
	}

}
