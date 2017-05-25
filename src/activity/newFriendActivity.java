package activity;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;

import message.AddFriendMessage;
import message.AgreeAddFriendMessage;
import message.Config;
import message.declineFriendMessage;
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
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.amap.api.services.a.v;
import com.uniqueweather.app.R;

import Util.Utility;
import adapter.newFriendAdapter;
import android.R.integer;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
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
   public static newFriendAdapter bAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_friend_list);
		
		fragmentPart.newFriendImage.setVisibility(View.GONE);//这是为了进入这个页面之后相应的红点会消失
		fragmentPart.newFriendImage1.setVisibility(View.GONE);
		
		bAdapter=new newFriendAdapter(newFriendActivity.this,NewFriendManager.getInstance(newFriendActivity.this));
		
		ListView listView=(ListView)findViewById(R.id.add_friend_list);
	 
		listView.setAdapter(bAdapter);
	}
	

}
