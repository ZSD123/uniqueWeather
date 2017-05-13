package activity;


import java.util.HashMap;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;

import message.AddFriendMessage;
import message.AgreeAddFriendMessage;
import model.NewFriend;
import model.RefreshEvent;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.uniqueweather.app.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

public class newFriendActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_friend_list);
		
		ListView listView=(ListView)findViewById(R.id.add_friend_list);
		final LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		BaseAdapter baseAdapter=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view;
				if(convertView==null){
					view=layoutInflater.inflate(R.layout.agreefriend, null);
				}else {
					view=convertView;
				}
				Button button=(Button)view.findViewById(R.id.btn_add);
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						
					}
				});
				return view;
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		listView.setAdapter(baseAdapter);
	}
	private void processCustomMessage(BmobIMMessage msg,BmobIMUserInfo info){
		String type=msg.getMsgType();
		EventBus.getDefault().post(new RefreshEvent());
		if(type.equals("add")){
			NewFriend friend=AddFriendMessage.convert(msg);
			//long id=
		}
		
		
	}
	   private void sendAgreeAddFriendMessage(NewFriend add,SaveListener listener){
	    	BmobIMUserInfo info=new BmobIMUserInfo(add.getUid(), add.getName(),add.getAvatar());
	    	BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(info, true, null);
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
						
					}
				}
			});
	     }
       
}
