package adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.amap.api.mapcore2d.p;
import com.amap.api.services.a.m;
import com.sharefriend.app.R;

import message.AgreeAddFriendMessage;
import message.Config;
import message.declineFriendMessage;
import model.Friend;
import model.NewFriend;
import model.NewFriendManager;
import model.UserModel;
import myCustomView.CircleImageView;
import Util.Utility;
import activity.MyUser;
import activity.fragmentChat;
import activity.newFriendActivity;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class newFriendAdapter extends BaseAdapter {
	
	private NewFriendManager manager;
	private Context mContext;
	private List<NewFriend> newFriends=new ArrayList<NewFriend>();
    public  int state=8;
   private  List<Integer> intlist=new ArrayList<Integer>();
   
   private SharedPreferences pre;
   
	public newFriendAdapter(Context context,NewFriendManager manager){
		mContext=context;
		newFriends=manager.getAllNewFriend();
		this.manager=manager;
		pre=PreferenceManager.getDefaultSharedPreferences(mContext);
		
	}
    class ViewHolder{
    	TextView newFriendBeizhu;
    	Button button;
    	Button button2;
    	CheckBox checkBox;
    	CircleImageView image;
    	TextView  textView;
    }
	 
	@Override
	public void notifyDataSetChanged() {
		newFriends=manager.getAllNewFriend();
		super.notifyDataSetChanged();
	}

    public List<NewFriend> getNewFriendToDelete(){
    	 List<NewFriend> list=new ArrayList<NewFriend>();
    	 for (int i = 0; i <intlist.size(); i++) {
			  list.add((NewFriend) getItem(i));
		 }
    	 intlist.clear();
    	 return list;
    }
    public List<Integer> getIntList(){
  
    	return intlist;
    }
   
    
	@Override
	public View getView(  final int position, View convertView, ViewGroup parent) {
		
		int designNum=pre.getInt("design", 0);

		
        final ViewHolder viewHolder;
		final int mPosition=position;
		if(convertView==null){
			LayoutInflater layoutInflater=((Activity)mContext).getLayoutInflater();
			convertView=layoutInflater.inflate(R.layout.agreefriend, null);
			viewHolder=new ViewHolder();
			viewHolder.newFriendBeizhu=(TextView)convertView.findViewById(R.id.agreefriendbeizhu);
			viewHolder.button=(Button)convertView.findViewById(R.id.btn_add);
			viewHolder.button2=(Button)convertView.findViewById(R.id.btn_decline);
			viewHolder.checkBox=(CheckBox)convertView.findViewById(R.id.agreefriendcheck);
			viewHolder.textView=(TextView)convertView.findViewById(R.id.agreefriend_msg);
			viewHolder.image=(CircleImageView)convertView.findViewById(R.id.agreefriendimage);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder)convertView.getTag();
		}
	   
		if(designNum==4){
			viewHolder.newFriendBeizhu.setTextColor(Color.parseColor("#A2C0DE"));
			viewHolder.button.setTextColor(Color.parseColor("#A2C0DE"));
			viewHolder.button2.setTextColor(Color.parseColor("#A2C0DE"));
			viewHolder.textView.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		if(state==8){
			viewHolder.checkBox.setVisibility(View.GONE);
		}else if(state==0){
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		}
	    viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				  if(isChecked&&!intlist.contains(position)){
					    intlist.add(position);
				  }else if(!isChecked&&intlist.contains(position)){
					  intlist.remove(position);
				  }
				
			}
		});
		
		if(newFriends.get(position).getStatus()==Config.STATUS_VERIFY_IREFUSE){
			 
			 viewHolder.button.setVisibility(View.GONE);
			 viewHolder. button2.setVisibility(View.GONE);
			 viewHolder.textView.setVisibility(View.VISIBLE);
			 viewHolder. textView.setText("您已拒绝对方的好友请求");
			 viewHolder.newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
			 
		}else if(newFriends.get(position).getStatus()==Config.STATUS_VERIFY_REFUSE){
			viewHolder.button.setVisibility(View.GONE);
			viewHolder.button2.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.VISIBLE);
			viewHolder.textView.setText("对方已拒绝您的好友请求");
			viewHolder.newFriendBeizhu.setVisibility(View.VISIBLE);
			viewHolder.newFriendBeizhu.setText("\n"+newFriends.get(position).getName());
			 
		}else if(newFriends.get(position).getStatus()==Config.STATUS_VERIFIED){
			viewHolder.button.setVisibility(View.GONE);
			viewHolder.button2.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.VISIBLE);
			viewHolder.textView.setText("您已添加对方");
			viewHolder.newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
		}else{
			viewHolder.newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
		}
	   
	
	    File file=new File(Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+newFriends.get(position).getUid()+".jpg_");
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
				if(bitmap2!=null)
			    	viewHolder.image.setImageBitmap(bitmap2);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}else {
		  if(newFriends.get(mPosition).getAvatar()!=null){
		    new Thread(new Runnable() {
				
				@Override
				public void run() {
					final Bitmap bitmap=Utility.getPicture(newFriends.get(mPosition).getAvatar());
					
					((Activity)mContext).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(bitmap!=null)
									viewHolder.image.setImageBitmap(bitmap);
								else {
									 loadimage(viewHolder.image);
								}
							}
						});
					
				}
			}).start();
		  }else {
			 loadimage(viewHolder.image);
	    	}
		}
		
	
		
		
	
		viewHolder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendAgreeAddFriendMessage(newFriends.get(mPosition), null);
		        MyUser user = new MyUser();
		        user.setObjectId(newFriends.get(mPosition).getUid());
				UserModel.getInstance().agreeAddFriend(user, new SaveListener<String>() {

					@Override
					public void done(String arg0, BmobException e) {
						  if (e == null) {
	                            fragmentChat.refreshNewFriend();
	                            fragmentChat.converdb.saveisFriend(newFriends.get(mPosition).getUid(), 1);
	                            notifyDataSetChanged();
	                            
	                        } else {
	                        	Toast.makeText(mContext, "失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
	                        }
						
					}
				});
		     }
		});

		viewHolder.button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
				bmobIMUserInfo.setUserId(newFriends.get(mPosition).getUid());
				bmobIMUserInfo.setAvatar(newFriends.get(mPosition).getAvatar());
				bmobIMUserInfo.setName(newFriends.get(mPosition).getName());
				
				sendDeclineMessage(bmobIMUserInfo,newFriends.get(mPosition));
				
			}
		});
		return convertView;
	}
	
	@Override
	public long getItemId(int position) {
		return newFriends.get(position).getId();
	}
	
	@Override
	public Object getItem(int position) {
	
		// TODO Auto-generated method stub
		return newFriends.get(position);
	}
	
	@Override
	public int getCount() {
		for (int i = 0; i < newFriends.size(); i++) {
			
		}
	
		return newFriends.size();
	}
	private void loadimage(CircleImageView imageView){
		  BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
			
			opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
			opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
			
			opts.inSampleSize=2;
			opts.inInputShareable=true;//设置解码位图的尺寸信息
			
			Bitmap bitmap2=BitmapFactory.decodeResource(mContext.getResources(),R.drawable.userpicture, opts);
			imageView.setImageBitmap(bitmap2);

	}
	
	private void sendAgreeAddFriendMessage(final NewFriend add,final SaveListener listener){
    	BmobIMUserInfo info=new BmobIMUserInfo(add.getUid(), add.getName(),add.getAvatar());
    	BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(info, true, null);  //这里后面的info是对方的用户信息和资料
    	BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
    	AgreeAddFriendMessage msg=new AgreeAddFriendMessage();
    	MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
    	msg.setContent("我通过了你的好友验证请求，我们可以开始聊天了!");
    	Map<String ,Object> map=new HashMap<String, Object>();
    	   map.put("msg",currentUser.getUsername()+"同意添加你为好友");//显示在通知栏上面的内容
    	    map.put("uid",add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
    	    map.put("time", add.getTime());//添加好友的请求时间
    	    msg.setExtraMap(map);
    	 conversation.sendMessage(msg,new MessageSendListener() {
			
			@Override
			public void done(BmobIMMessage msg, BmobException e) {
				
				if(e==null){
					NewFriendManager.getInstance(mContext).updateNewFriend(add,Config.STATUS_VERIFIED);
					Toast.makeText(mContext,"发送成功", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(mContext,"发送失败，"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
     }
	 private void sendDeclineMessage(final BmobIMUserInfo bmobIMUserInfo,final NewFriend newFriend1){    //发送添加好友拒绝消息
	   	 BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo, true,new ConversationListener() {
				
				@Override
				public void done(BmobIMConversation arg0, BmobException e) {
					if(e==null){
						
					}else {
						Toast.makeText(mContext, "失败，"+e.getMessage(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	   	 BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
	    	 final declineFriendMessage msg1=new declineFriendMessage();
	        MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
	        msg1.setContent("拒绝您的好友请求");
	        Map<String,Object> map=new HashMap<String,Object>();
	        map.put("name", currentUser.getNick());
	        map.put("avatar", currentUser.getTouXiangUrl());
	        map.put("uid", currentUser.getObjectId());
	        msg1.setExtraMap(map);
	        conversation.sendMessage(msg1, new MessageSendListener() {
				
				@Override
				public void done(BmobIMMessage msg, BmobException e) {
					if(e==null){
						Toast.makeText(mContext, "拒绝成功",Toast.LENGTH_SHORT).show();
						NewFriend newFriend=declineFriendMessage.Iconvert(bmobIMUserInfo,newFriend1);
						newFriend.setTime(newFriend1.getTime()+1);
					    manager.insertOrUpdateNewFriend(newFriend);
					    manager.deleteNewFriend(newFriend1);
						newFriendActivity.bAdapter.notifyDataSetChanged();
					}else {
						Toast.makeText(mContext, "发送失败，"+e.getMessage(),Toast.LENGTH_SHORT).show();
       
					}
					
				}
			});
	    }
	    
}


