package adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.uniqueweather.app.R;

import message.AgreeAddFriendMessage;
import message.Config;
import message.declineFriendMessage;
import model.NewFriend;
import model.NewFriendManager;
import model.UserModel;
import myCustomView.CircleImageView;
import Util.Utility;
import activity.MyUser;
import activity.fragmentPart;
import activity.newFriendActivity;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class newFriendAdapter extends BaseAdapter {
	
	private NewFriendManager manager;
	private Context mContext;
	private List<NewFriend> newFriends=new ArrayList<NewFriend>();
    public  int state=8;
   private  List<Integer> intlist=new ArrayList<Integer>();
	public newFriendAdapter(Context context,NewFriendManager manager){
		mContext=context;
		newFriends=manager.getAllNewFriend();
		this.manager=manager;
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
    	for (int i = 0; i < intlist.size(); i++) {
			Log.d("Main", "int="+intlist.get(i));
		}
    	return intlist;
    }

	@Override
	public View getView(  final int position, View convertView, ViewGroup parent) {

		
		
		View view;
		final int mPosition=position;
		if(convertView==null){
			LayoutInflater layoutInflater=((Activity)mContext).getLayoutInflater();
			view=layoutInflater.inflate(R.layout.agreefriend, null);
		}else {
			view=convertView;
		}
		TextView newFriendBeizhu=(TextView)view.findViewById(R.id.agreefriendbeizhu);
		Button button=(Button)view.findViewById(R.id.btn_add);
		Button button2=(Button)view.findViewById(R.id.btn_decline);
	    CheckBox checkBox=(CheckBox)view.findViewById(R.id.agreefriendcheck);
		if(state==8){
			checkBox.setVisibility(View.GONE);
		}else if(state==0){
			checkBox.setVisibility(View.VISIBLE);
		}
	    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				  if(isChecked&&!intlist.contains(position)){
					    intlist.add(position);
				  }else if(!isChecked&&intlist.contains(position)){
					  intlist.remove(position);
				  }
				
			}
		});
		
		TextView textView=(TextView)view.findViewById(R.id.agreefriend_msg);
		if(newFriends.get(position).getStatus()==Config.STATUS_VERIFY_IREFUSE){
			 
			 button.setVisibility(View.GONE);
			 button2.setVisibility(View.GONE);
			 textView.setVisibility(View.VISIBLE);
			 textView.setText("您已拒绝对方的好友请求");
			 newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
			 
		}else if(newFriends.get(position).getStatus()==Config.STATUS_VERIFY_REFUSE){
			 button.setVisibility(View.GONE);
			 button2.setVisibility(View.GONE);
			 textView.setVisibility(View.VISIBLE);
			 textView.setText("对方已拒绝您的好友请求");
			 newFriendBeizhu.setVisibility(View.VISIBLE);
			 newFriendBeizhu.setText("\n"+newFriends.get(position).getName());
			 
		}else {
			newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
		}
	   
	
	
		
		final CircleImageView image=(CircleImageView)view.findViewById(R.id.agreefriendimage);
	    File file=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+newFriends.get(position).getUid()+".jpg_");
		if(file.exists()){
			image.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+newFriends.get(position).getUid()+".jpg_"));
		}
		
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				Bitmap bitmap=Utility.getPicture(newFriends.get(mPosition).getAvatar());
				
				if(bitmap!=null)
					image.setImageBitmap(bitmap);
			}
		});
		
		
	
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendAgreeAddFriendMessage(newFriends.get(mPosition), null);
		        MyUser user = new MyUser();
		        user.setObjectId(newFriends.get(mPosition).getUid());
				UserModel.getInstance().agreeAddFriend(user, new SaveListener<String>() {

					@Override
					public void done(String arg0, BmobException e) {
						  if (e == null) {
	                            Log.e("Main","success");
	                            fragmentPart.refreshNewFriend();
	                        } else {
	                            Log.e("Main",e.getMessage());
	                        }
						
					}
				});
		     }
		});

		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
				bmobIMUserInfo.setUserId(newFriends.get(mPosition).getUid());
				bmobIMUserInfo.setAvatar(newFriends.get(mPosition).getAvatar());
				bmobIMUserInfo.setName(newFriends.get(mPosition).getName());
				
				sendDeclineMessage(bmobIMUserInfo,newFriends.get(mPosition));
				
			}
		});
		return view;
	}
	
	@Override
	public long getItemId(int position) {
		return newFriends.get(position).getId();
	}
	
	@Override
	public Object getItem(int position) {
		for (int i = 0; i < newFriends.size(); i++) {
			Log.d("Main","getItemstatus="+newFriends.get(position).getStatus());
			
		}
	
		// TODO Auto-generated method stub
		return newFriends.get(position);
	}
	
	@Override
	public int getCount() {
		Log.d("Main","count="+newFriends.size());
		return newFriends.size();
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
						Log.d("Main","1步失败");
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
                        Log.d("Main","2步失败");
					}
					
				}
			});
	    }
	    
}


