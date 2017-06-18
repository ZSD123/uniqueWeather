package activity;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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

import com.amap.api.services.a.bu;
import com.amap.api.services.a.v;
import com.uniqueweather.app.R;

import Util.Utility;
import adapter.newFriendAdapter;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class newFriendActivity extends Activity {
   public static TextView newFriendBeizhu;
   private Button buttonGuan;
   private ListView listView;
   private NewFriendManager newFriendManager;
   private List<NewFriend> newFriends=new ArrayList<NewFriend>();  //Ҫɾ�������������Ϣ
   public static newFriendAdapter bAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_friend_list);
		
		
		newFriendManager=NewFriendManager.getInstance(newFriendActivity.this);
		fragmentPart.newFriendImage.setVisibility(View.GONE);//����Ϊ�˽������ҳ��֮����Ӧ�ĺ�����ʧ
		fragmentPart.newFriendImage1.setVisibility(View.GONE);
		
		bAdapter=new newFriendAdapter(newFriendActivity.this,newFriendManager);
	
	    listView=(ListView)findViewById(R.id.new_friend_list);
	    buttonGuan=(Button)findViewById(R.id.btnGuan);
		listView.setAdapter(bAdapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(buttonGuan.getText().equals("���"))
				    buttonGuan.setText("ɾ��");
			    bAdapter.state=0; //Ϊ0��ʱ�����ֳ���
				bAdapter.notifyDataSetChanged();	 
				 return false;
			}
		});
		buttonGuan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    if(buttonGuan.getText().equals("���")){
			    	AlertDialog.Builder builder=new AlertDialog.Builder(newFriendActivity.this);
			    	builder.setTitle("���");
			    	builder.setMessage("�Ƿ�ȫ�����?");
			    	builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						     
						      List<NewFriend> list=newFriendManager.getAllNewFriend();
						      for (int i = 0; i < list.size(); i++) {
								  newFriendManager.deleteNewFriend(list.get(i));
							}
							bAdapter.notifyDataSetChanged();
						}
					});
			      builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					      
						
					}
				});
			      builder.show();
			    }else if(buttonGuan.getText().equals("ɾ��")){
			        List<Integer> intList=new ArrayList<Integer>();
					intList=bAdapter.getIntList();
					for (int i = 0; i < intList.size(); i++) {
					
						listView.removeViews(intList.get(i), 1);//��Ȼˢ�����ݺ������ЩView��û������ɾ����ʵ�������ݿ�����Ӧ����ɾ���˵ģ���ô���Ǿ�ֱ�ӵ���listview��remove����ɾ����Ӧ����ͼ
					}
				
					List<NewFriend> newFriends=new ArrayList<NewFriend>();
						newFriends=	bAdapter.getNewFriendToDelete();
	              	for (int i = 0; i < newFriends.size(); i++) {
							 newFriendManager.deleteNewFriend(newFriends.get(i));
						}
			
					bAdapter.notifyDataSetChanged();
				
			    }
			}
		});
	}
	@Override
	public void onBackPressed() {
		if(buttonGuan.getText().equals("ɾ��")){
			buttonGuan.setText("���");
			for (int i = 0; i < bAdapter.getCount(); i++) {
				listView.getChildAt(i).setBackgroundColor(0);
			}
			newFriends.clear();
			bAdapter.state=8;
			bAdapter.notifyDataSetChanged();
		}else {
			super.onBackPressed();
		}
	}
	

}
