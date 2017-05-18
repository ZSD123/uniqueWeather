package activity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;

import message.AddFriendMessage;
import message.AgreeAddFriendMessage;
import model.DemoMessageHandler;
import model.NewFriend;
import model.NewFriendManager;
import model.RefreshEvent;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.a.b;
import cn.bmob.newim.core.d.b.h;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.amap.api.services.a.v;
import com.uniqueweather.app.R;

import android.app.Activity;
import android.app.DownloadManager.Query;
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
import android.widget.TextView;

public class newFriendActivity extends Activity {
   public static TextView newFriendBeizhu;
   private List<NewFriend> newFriends;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_friend_list);
		
		newFriends=NewFriendManager.getInstance(this).getAllNewFriend();
		ListView listView=(ListView)findViewById(R.id.add_friend_list);
		BaseAdapter baseAdapter=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view;
				if(convertView==null){
					LayoutInflater layoutInflater=getLayoutInflater();
					view=layoutInflater.inflate(R.layout.agreefriend, null);
				}else {
					view=convertView;
				}
				newFriendBeizhu=(TextView)view.findViewById(R.id.agreefriendbeizhu);
				newFriendBeizhu.setText(newFriends.get(position).getName()+newFriends.get(position).getMsg());
				
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
	

	private void sendAgreeAddFriendMessage(NewFriend add,SaveListener listener){
	    	BmobIMUserInfo info=new BmobIMUserInfo(add.getUid(), add.getName(),add.getAvatar());
	    	BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(info, true, null);
	    	BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
	    	AgreeAddFriendMessage msg=new AgreeAddFriendMessage();
	    	MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
	    	msg.setContent("��ͨ������ĺ�����֤�������ǿ��Կ�ʼ������!");
	    	Map<String ,Object> map=new HashMap<String, Object>();
	    	   map.put("msg",currentUser.getUsername()+"ͬ��������Ϊ����");//��ʾ��֪ͨ�����������
	    	    map.put("uid",add.getUid());//�����ߵ�uid-�����������ӵķ��ͷ��ҵ��������Ӻ��ѵ�����
	    	    map.put("time", add.getTime());//���Ӻ��ѵ�����ʱ��
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