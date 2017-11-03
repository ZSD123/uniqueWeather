package adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import model.Friend;
import myCustomView.CircleImageView;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessageType;

import com.sharefriend.app.R;

import Util.TimeUtil;
import Util.Utility;
import Util.download;
import activity.MyUser;
import activity.fragmentChat;
import activity.weather_info;
import adapter.friendAdapter.friendViewHolder;
import adapter.friendAdapter.zeroViewHolder;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class messageAdapter extends Adapter<RecyclerView.ViewHolder> {
	    private Context mContext;
	    private int mCount;
	    private int colorNum=0;
		public messageAdapter(Context mContext,int count) {
			this.mContext=mContext;
			this.mCount=count;
			
		}
		@Override
		public int getItemCount() {
			
			return mCount;
		
		}
		
		private OnRecyclerViewListener onRecyclerViewListener;

	    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
	        this.onRecyclerViewListener = onRecyclerViewListener;
	    }

	    public BmobIMConversation getItem(int position){
	        return fragmentChat.conversations == null?null:fragmentChat.conversations.get(position);
	    }
	    
	    
		public void removeData(int position){
			fragmentChat.conversations.remove(position);
			notifyItemRemoved(position);
		}
		
	   public void refreshCount(int count){
		   this.mCount=count;
	   }


		
		@Override
		public void onBindViewHolder(final ViewHolder holder, final int position) {
			
			if(fragmentChat.conversations.size()==0){
			     	int count=getItemCount();   //获得不为0的数目
			     	
			        int count1=0;
					for (int i = 0; count1 < count&&i<fragmentChat.conversations.size();i++ ) {
					   if(fragmentChat.conversations.get(i).getMessages().size()>0){
						if(fragmentChat.conversations.get(i).getMessages().get(0).getFromId().equals(weather_info.objectId)||fragmentChat.conversations.get(i).getMessages().get(0).getToId().equals(weather_info.objectId)){
					     if(!fragmentChat.conversations.get(i).getMessages().get(0).getFromId().equals(fragmentChat.conversations.get(i).getMessages().get(0).getToId())){
					      if(!fragmentChat.conversations.get(i).getMessages().get(0).getMsgType().equals("decline")){
						count1++;   
						Log.d("Main", "count1="+count1);
						String id=fragmentChat.conversations.get(i).getConversationId();

					fragmentChat.converdb.saveId(id,0);
					String content="";
		
					if(fragmentChat.conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.TEXT.getType()))
						content=fragmentChat.conversations.get(i).getMessages().get(0).getContent();
						else if(fragmentChat.conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.IMAGE.getType()))
		                        content="[图片]";
						else if(fragmentChat.conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
							content="[视频]";
						else if(fragmentChat.conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.VOICE.getType()))
							content="[语音]";
						else if(fragmentChat.conversations.get(i).getMessages().get(0).getMsgType().equals("agree"))
							content="我已经通过了你的好友请求，一起来聊天吧";
                    
					fragmentChat.converdb.saveNewContentById(id, content);
					fragmentChat.converdb.saveTimeById(id,fragmentChat.conversations.get(i).getMessages().get(0).getCreateTime());
					fragmentChat.converdb.saveTouXiangById(id, fragmentChat.conversations.get(i).getConversationIcon());
						}
					   }
				}
				   }	
				}
					fragmentChat.conversations=fragmentChat.converdb.getConverByTime();
				}else {
					fragmentChat.conversations.clear();
					fragmentChat.conversations=fragmentChat.converdb.getConverByTime();  //如果不为0就按照时间排序获取会话
			}
			   

				if(fragmentChat.conversations.size()>0&&position<fragmentChat.conversations.size()&&!fragmentChat.conversations.get(position).getConversationId().equals(weather_info.objectId)){   //禁止和自己发生对话
					if(fragmentChat.converdb.getNewContentById(fragmentChat.conversations.get(position).getConversationId())!=null){  //禁止读取信息为空的消息
						if(fragmentChat.conversations.get(position).getConversationTitle()!=null){  //禁止username为空
			   
				((conversationViewHolder)holder).itemView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(onRecyclerViewListener!=null)
					    	onRecyclerViewListener.onItemClick(position);
					
					}
				});
             ((conversationViewHolder)holder).itemView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					if(onRecyclerViewListener!=null){
						onRecyclerViewListener.onItemLongClick(position);
					}
					return false;
				}
			});
    
				String nickName=fragmentChat.conversations.get(position).getConversationTitle();
				if(nickName!=null){
                    if(colorNum==1){
                    	((conversationViewHolder)holder).tv_name.setTextColor(Color.parseColor("#A2C0DE"));
                    }else if(colorNum==0){
                    	((conversationViewHolder)holder).tv_name.setTextColor(Color.parseColor("#000000"));
                    }
                    
					((conversationViewHolder)holder).tv_name.setText(nickName);
				}
				
				String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+fragmentChat.conversations.get(position).getConversationId()+".jpg_";
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
						((conversationViewHolder)holder).imageView.setImageBitmap(bitmap2);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						path=null;
			    }else {
			   
                      if(fragmentChat.conversations.get(position).getConversationIcon()!=null&&!fragmentChat.conversations.get(position).getConversationIcon().equals("")){
                    	 
								  new Thread(new Runnable() {
										
										@Override
										public void run() {
										    final Bitmap bitmap=Utility.getPicture(fragmentChat.conversations.get(position).getConversationIcon());
											if(bitmap!=null){
											
												download.saveYonghuPic(bitmap,fragmentChat.conversations.get(position).getConversationId());
												((Activity)mContext).runOnUiThread(new Runnable() {
												
												@Override
												public void run() {
													((conversationViewHolder)holder).imageView.setImageBitmap(bitmap);
													
												}
											});

											}
										}
									}).start();
		                 }else {
             
		                	 ((conversationViewHolder)holder).imageView.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.userpicture));
					   }
					}
		            if(colorNum==1){
		            	((conversationViewHolder)holder).tv_message.setTextColor(Color.parseColor("#A2C0DE"));
		            }else if(colorNum==0){
		            	((conversationViewHolder)holder).tv_message.setTextColor(Color.parseColor("#000000"));
		            }
					((conversationViewHolder)holder).tv_message.setText(fragmentChat.converdb.getNewContentById(fragmentChat.conversations.get(position).getConversationId()));
		
				   long time=fragmentChat.conversations.get(position).getUpdateTime();
				   if(colorNum==1){
		            	((conversationViewHolder)holder).tv_time.setTextColor(Color.parseColor("#A2C0DE"));
		            }else if(colorNum==0){
		            	((conversationViewHolder)holder).tv_time.setTextColor(Color.parseColor("#000000"));
		            }
					((conversationViewHolder)holder).tv_time.setText(""+TimeUtil.getChatTime(false,time));
	              
				  if(fragmentChat.converdb.getUnReadNumById(fragmentChat.conversations.get(position).getConversationId())!=0){
					  ((conversationViewHolder)holder).tv_unread.setVisibility(View.VISIBLE);
					  if(colorNum==1){
			            	((conversationViewHolder)holder).tv_unread.setTextColor(Color.parseColor("#A2C0DE"));
			            }else if(colorNum==0){
			            	((conversationViewHolder)holder).tv_unread.setTextColor(Color.parseColor("#000000"));
			            }
					  ((conversationViewHolder)holder).tv_unread.setText(""+fragmentChat.converdb.getUnReadNumById(fragmentChat.conversations.get(position).getConversationId()));
			     	} else {
					  ((conversationViewHolder)holder).tv_unread.setVisibility(View.INVISIBLE);
		    	    }
			
					}
				}
					
			}
		}
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		    
		    	return new conversationViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_conversation, parent,false));
		  
		}
		class conversationViewHolder extends ViewHolder implements View.OnClickListener,View.OnLongClickListener{
	        CircleImageView imageView;
	        TextView tv_name;
	        TextView tv_message;
	        TextView tv_time;
	        TextView tv_unread;
			public conversationViewHolder(View view) {
				super(view);
				itemView.setOnClickListener(this);
				itemView.setOnLongClickListener(this);
				tv_name=(TextView)view.findViewById(R.id.tv_recent_name);
				imageView=(CircleImageView)view.findViewById(R.id.iv_recent_avatar);
				tv_message=(TextView)view.findViewById(R.id.tv_recent_msg);
				tv_time=(TextView)view.findViewById(R.id.tv_recent_time);
				tv_unread=(TextView)view.findViewById(R.id.tv_recent_unread);
				
			}
			@Override
			public boolean onLongClick(View v) {
				
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
