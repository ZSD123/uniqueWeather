package activity;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;

import message.AddFriendMessage;
import message.AgreeAddFriendMessage;
import message.Config;
import model.DemoMessageHandler;
import model.NewFriend;
import model.NewFriendManager;
import model.RefreshEvent;
import model.UserModel;
import myCustomView.CircleImageView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.a.b;
import cn.bmob.newim.core.d.b.h;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.amap.api.services.a.v;
import com.uniqueweather.app.R;

import Util.Utility;
import android.R.integer;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class newFriendActivity extends Activity {
   public static TextView newFriendBeizhu;
   private List<NewFriend> newFriends;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_friend_list);
		
		fragmentPart.newFriendImage.setVisibility(View.GONE);//这是为了进入这个页面之后相应的红点会消失
		fragmentPart.newFriendImage1.setVisibility(View.GONE);
		
		newFriends=NewFriendManager.getInstance(this).getAllNewFriend();
		ListView listView=(ListView)findViewById(R.id.add_friend_list);
		BaseAdapter baseAdapter=new BaseAdapter() {
			
			@Override
			public View getView(  int position, View convertView, ViewGroup parent) {
				
				View view;
				final int mPosition=position;
				if(convertView==null){
					LayoutInflater layoutInflater=getLayoutInflater();
					view=layoutInflater.inflate(R.layout.agreefriend, null);
				}else {
					view=convertView;
				}
				newFriendBeizhu=(TextView)view.findViewById(R.id.agreefriendbeizhu);
				newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
				
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
				
				
				Button button=(Button)view.findViewById(R.id.btn_add);
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
			                        } else {
			                            Log.e("Main",e.getMessage());
			                        }
								
							}
						});
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
				// TODO Auto-generated method stub
				return newFriends.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return newFriends.size();
			}
		};
		listView.setAdapter(baseAdapter);
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
						NewFriendManager.getInstance(newFriendActivity.this).updateNewFriend(add,Config.STATUS_VERIFIED);
						Toast.makeText(newFriendActivity.this,"发送成功", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(newFriendActivity.this,"发送失败，"+e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
	     }
       
}
