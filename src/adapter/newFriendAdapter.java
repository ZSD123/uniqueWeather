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
import com.uniqueweather.app.R;

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
	public newFriendAdapter(Context context,NewFriendManager manager){
		mContext=context;
		newFriends=manager.getAllNewFriend();
		this.manager=manager;
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
			 viewHolder. textView.setText("���Ѿܾ��Է��ĺ�������");
			 viewHolder.newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
			 
		}else if(newFriends.get(position).getStatus()==Config.STATUS_VERIFY_REFUSE){
			viewHolder.button.setVisibility(View.GONE);
			viewHolder.button2.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.VISIBLE);
			viewHolder.textView.setText("�Է��Ѿܾ����ĺ�������");
			viewHolder.newFriendBeizhu.setVisibility(View.VISIBLE);
			viewHolder.newFriendBeizhu.setText("\n"+newFriends.get(position).getName());
			 
		}else if(newFriends.get(position).getStatus()==Config.STATUS_VERIFIED){
			viewHolder.button.setVisibility(View.GONE);
			viewHolder.button2.setVisibility(View.GONE);
			viewHolder.textView.setVisibility(View.VISIBLE);
			viewHolder.textView.setText("������ӶԷ�");
			viewHolder.newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
		}else{
			viewHolder.newFriendBeizhu.setText(newFriends.get(position).getName()+"\n"+newFriends.get(position).getMsg());
		}
	   
	
	    File file=new File(Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+newFriends.get(position).getUid()+".jpg_");
		if(file.exists()){
			
             try {
				InputStream is=new FileInputStream(file);
				
				BitmapFactory.Options opts=new BitmapFactory.Options();
				opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
				
				opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
				opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
				
				opts.inSampleSize=2;
				opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
				
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
	                        	Toast.makeText(mContext, "ʧ�ܣ�"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
			opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
			
			opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
			opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
			
			opts.inSampleSize=2;
			opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
			
			Bitmap bitmap2=BitmapFactory.decodeResource(mContext.getResources(),R.drawable.userpicture, opts);
			imageView.setImageBitmap(bitmap2);

	}
	
	private void sendAgreeAddFriendMessage(final NewFriend add,final SaveListener listener){
    	BmobIMUserInfo info=new BmobIMUserInfo(add.getUid(), add.getName(),add.getAvatar());
    	BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(info, true, null);  //��������info�ǶԷ����û���Ϣ������
    	BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
    	AgreeAddFriendMessage msg=new AgreeAddFriendMessage();
    	MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
    	msg.setContent("��ͨ������ĺ�����֤�������ǿ��Կ�ʼ������!");
    	Map<String ,Object> map=new HashMap<String, Object>();
    	   map.put("msg",currentUser.getUsername()+"ͬ�������Ϊ����");//��ʾ��֪ͨ�����������
    	    map.put("uid",add.getUid());//�����ߵ�uid-����������ӵķ��ͷ��ҵ�������Ӻ��ѵ�����
    	    map.put("time", add.getTime());//��Ӻ��ѵ�����ʱ��
    	    msg.setExtraMap(map);
    	 conversation.sendMessage(msg,new MessageSendListener() {
			
			@Override
			public void done(BmobIMMessage msg, BmobException e) {
				
				if(e==null){
					NewFriendManager.getInstance(mContext).updateNewFriend(add,Config.STATUS_VERIFIED);
					Toast.makeText(mContext,"���ͳɹ�", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(mContext,"����ʧ�ܣ�"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
     }
	 private void sendDeclineMessage(final BmobIMUserInfo bmobIMUserInfo,final NewFriend newFriend1){    //������Ӻ��Ѿܾ���Ϣ
	   	 BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo, true,new ConversationListener() {
				
				@Override
				public void done(BmobIMConversation arg0, BmobException e) {
					if(e==null){
						
					}else {
						Toast.makeText(mContext, "ʧ�ܣ�"+e.getMessage(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	   	 BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
	    	 final declineFriendMessage msg1=new declineFriendMessage();
	        MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
	        msg1.setContent("�ܾ����ĺ�������");
	        Map<String,Object> map=new HashMap<String,Object>();
	        map.put("name", currentUser.getNick());
	        map.put("avatar", currentUser.getTouXiangUrl());
	        map.put("uid", currentUser.getObjectId());
	        msg1.setExtraMap(map);
	        conversation.sendMessage(msg1, new MessageSendListener() {
				
				@Override
				public void done(BmobIMMessage msg, BmobException e) {
					if(e==null){
						Toast.makeText(mContext, "�ܾ��ɹ�",Toast.LENGTH_SHORT).show();
						NewFriend newFriend=declineFriendMessage.Iconvert(bmobIMUserInfo,newFriend1);
						newFriend.setTime(newFriend1.getTime()+1);
					    manager.insertOrUpdateNewFriend(newFriend);
					    manager.deleteNewFriend(newFriend1);
						newFriendActivity.bAdapter.notifyDataSetChanged();
					}else {
						Toast.makeText(mContext, "����ʧ�ܣ�"+e.getMessage(),Toast.LENGTH_SHORT).show();
       
					}
					
				}
			});
	    }
	    
}


