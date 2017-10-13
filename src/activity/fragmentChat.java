package activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.Black;
import model.Friend;
import model.UserModel;
import myCustomView.CircleImageView;

import myCustomView.myChatPager;
import service.autoUqdateService;
import Util.Http;
import Util.HttpCallbackListener;

import Util.TimeUtil;
import Util.Utility;
import Util.download;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.Bundle;
import android.os.Environment;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;

import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.sharefriend.app.R;

import db.conversationDB;

public  class fragmentChat extends Fragment implements AMapLocationListener 
{   
    

	private static TextView weather;

	private static TextView temper;
	public static CircleImageView userPicture;
    private MessageListHandler messageListHandler;
   
  private static ImageView pic;
    public  static TextView userName;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pre;
    
    private static Bitmap bitmap;  //����bitmap
    
    public static MyHorizontalView horizontalView;

    private AMapLocationClientOption mLocationOption=null;
    private AMapLocationClient mLocationClient;
    
    private myChatPager chatPager;

	 private View view4;     //��ϵ�˽���view
	private List<View> views;    //װ����Ϣ����view����ϵ�˽���view��views
	
	private static BaseAdapter baseAdapter1;
	private static BaseAdapter baseAdapter2;//�����б�
	

	public static conversationDB converdb;  //�Ի����ݱ�
	private boolean once=false;
	private String username;
	
	public static ImageView newFriendImage;  //��ϵ�˵ĺ��
	public static ImageView newFriendImage1;  //�µ����Ѻ��
	
	private static List<Friend> friends=new ArrayList<Friend>(); 
	public static LayoutInflater layoutInflater;
	
	public static BmobIM bmobIM;
    public static  List<BmobIMConversation>  conversations;
	private List<BmobIMConversation> converList=new ArrayList<BmobIMConversation>();//������Ϊ�˻�Ӧ����¼����м���MyUser����Ϣ����
    private MyHorizontalView myHorizontalView;
	private static Context context;
     
	public static PopupWindow popupWindow;
	
	public fragmentChat(){
	}
	
  
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			 Bundle savedInstanceState) 
    { 
            View view = null;;
		    context=getActivity();
		    
			view=inflater.inflate(R.layout.connection,container,false);
		    userName=(TextView)view.findViewById(R.id.userName);
	        userPicture=(CircleImageView)view.findViewById(R.id.userPicture);
            
	        RelativeLayout weatherLayout=(RelativeLayout)view.findViewById(R.id.weatherlinearlayout);     
	        myHorizontalView=(MyHorizontalView)view.findViewById(R.id.horiView);
	        username=(String)MyUser.getObjectByKey("username");
	        	
	        converdb=conversationDB.getInstance(getActivity());
		
	        MyUser user=MyUser.getCurrentUser(MyUser.class);
            if(user!=null&&user.getObjectId()!=null)
              BmobIM.connect(user.getObjectId(),new ConnectListener() {
				
				@Override
				public void done(String uid, BmobException e) {
					if(e==null){
						Log.d("Main","���ӳɹ�");
					}else {
						Toast.makeText(getActivity(), "����ʧ�ܣ�"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
       
            
            Button button1;
            Button button2;//logout Button
            ImageView jia;  //�ӺŰ�ť
            
            TextView myAccount;     //�ҵ���ƬTextView 
            TextView manage;     //�˻�����
            View view3;    //��Ϣ����View
            final SwipeRefreshLayout sw_refresh1;   //��Ϣ�����refresh
            final SwipeRefreshLayout sw_refresh2;  //��ϵ���б��refresh
            
			weather=(TextView)view.findViewById(R.id.weather);
			temper=(TextView)view.findViewById(R.id.temper);
			pic=(ImageView)view.findViewById(R.id.weather_pic);
			button1=(Button)view.findViewById(R.id.button_map);
			button2=(Button)view.findViewById(R.id.button1);
			jia=(ImageView)view.findViewById(R.id.jia);
			horizontalView=(MyHorizontalView)view.findViewById(R.id.horiView);
			myAccount=(TextView)view.findViewById(R.id.myAccount);
			manage=(TextView)view.findViewById(R.id.manage);
		    final Button  buttonMes=(Button)view.findViewById(R.id.chat_mes);
		    final Button buttonCon=(Button)view.findViewById(R.id.chat_con);
			newFriendImage=(ImageView)view.findViewById(R.id.newfriend_image);
			
			chatPager=(myChatPager)view.findViewById(R.id.chatPager);
            layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view3=layoutInflater.inflate(R.layout.chatlist,null);
			view4=layoutInflater.inflate(R.layout.contactslist,null);
			
			
            ListView conversationList=(ListView)view3.findViewById(R.id.chatList);
            sw_refresh1=(SwipeRefreshLayout)view3.findViewById(R.id.sw_refresh);
            //�Ự�б�����ˢ���¼�
            sw_refresh1.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					conversations=bmobIM.loadAllConversation();
					baseAdapter1.notifyDataSetChanged();
					sw_refresh1.setRefreshing(false);
		
				}
			});
            
        
         
          //�Ự�б����¼�
            conversationList.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
			
					if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_UP){
				    	if(myHorizontalView.getScrollX()!=MyHorizontalView.mMenuWidth){
						   myHorizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth, 0);
						   return true;
				     	}
				    	if(popupWindow!=null&&popupWindow.isShowing()){
				    		popupWindow.dismiss();
				    		return true;
				    	 }
					}
					return false;
				}
			});
            conversationList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
						final BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
											
						bmobIMUserInfo.setUserId(converList.get(position).getConversationId());
						if(converList.get(position).getConversationTitle()!=null&&!converList.get(position).getConversationTitle().equals(""))
					    	bmobIMUserInfo.setName(converList.get(position).getConversationTitle());
						else if(converList.get(position).getConversationId()!=null){
							bmobIMUserInfo.setName(converList.get(position).getConversationId());
						}
						bmobIMUserInfo.setAvatar(converList.get(position).getConversationIcon());
				     	BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo ,new ConversationListener() {
							
							@Override
							public void done(BmobIMConversation c, BmobException e) {
								if(e==null){
									Bundle bundle=new Bundle();
									bundle.putSerializable("c",c);
									bundle.putSerializable("userInfo",bmobIMUserInfo);
									Intent intent=new Intent(getActivity(),ChatActivity.class);
									intent.putExtra("bundle", bundle);
									startActivity(intent);
									
								}else {
	                                   Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					
				
					
					
				}
			});
            conversationList.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, final int position, long id) {
					 AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				     final String[] xuanzeweizhi={"ɾ���ûỰ"};
				     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							  if(which==0){
								   bmobIM.deleteConversation(converList.get(position).getConversationId());
									converdb.saveNewContentById(converList.get(position).getConversationId(),"");
									converdb.clearUnReadNumById(converList.get(position).getConversationId());
									converdb.saveTimeById(converList.get(position).getConversationId(), 0);
									
									if(converdb.getIsFriend(converList.get(position).getConversationId())==0){//�����İ���˵Ļ���ֱ��ɾ��
								         converdb.deleteCoversationById(converList.get(position).getConversationId());
							        }
									
									
									fragmentChat.refreshConversations(2,converList.get(position).getConversationId());
							
							  }
						}
					});
				     builder.show();
					return true;
				}
			});
            bmobIM=BmobIM.getInstance();
            conversations=bmobIM.loadAllConversation();
            
            
            messageListHandler=new MessageListHandler() {
				
				@Override
				public void onMessageReceive(List<MessageEvent> list) {
					 for (int i = 0; i <list.size(); i++) {
					      if(!(list.get(i).getMessage().getFromId()).equals(list.get(i).getMessage().getToId())){
					    	  if(!list.get(i).getMessage().getMsgType().equals("agree")&&!list.get(i).getMessage().getMsgType().equals("add")&&!list.get(i).getMessage().getMsgType().equals("decline")){
					    		  if(converdb.getIsFriend(list.get(i).getConversation().getConversationId())!=2){
						         String content="";
		  		                 String id=list.get(i).getConversation().getConversationId();
							    if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.TEXT.getType()))
								content=list.get(i).getMessage().getContent();
								else if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.IMAGE.getType()))
				                        content="[ͼƬ]";
								else if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
									content="[��Ƶ]";
								else if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.VOICE.getType()))
									content="[����]";
							    
						
							    converdb.saveId(list.get(i).getConversation().getConversationId(), 0);
							    converdb.saveNickById(list.get(i).getConversation().getConversationId(), list.get(i).getFromUserInfo().getName()) ;   
								converdb.saveNewContentById(id, content);
								converdb.saveTimeById(id, list.get(i).getMessage().getCreateTime());
						    	refreshConversations(0,list.get(i).getConversation().getConversationId());
					         }
						   }
					      }
				    }
					 
					
				}
			};
            bmobIM.addMessageListHandler(messageListHandler);
            //�Ự�б�������
            //�Ự�б��ViewHolder
            class ViewHolder1{
            	CircleImageView imageView;
            	TextView nameText;
            	TextView contentText;
            	TextView timeText;
            	TextView unReadText;
            	
            }
          
  			 baseAdapter1=new BaseAdapter() {
  				
  				@Override
  				public View getView(final int position, View convertView, ViewGroup parent) {
  					
  				  	if(converList.size()==0){
  				     	int count=getCount();   //��ò�Ϊ0����Ŀ
  				     	
  				        int count1=0;
  						for (int i = 0; count1 < count&&i<conversations.size();i++ ) {
  						   if(conversations.get(i).getMessages().size()>0){
  							if(conversations.get(i).getMessages().get(0).getFromId().equals(weather_info.objectId)||conversations.get(i).getMessages().get(0).getToId().equals(weather_info.objectId)){
  						     if(!conversations.get(i).getMessages().get(0).getFromId().equals(conversations.get(i).getMessages().get(0).getToId())){
  						      if(!conversations.get(i).getMessages().get(0).getMsgType().equals("decline")){
  							count1++;   
  							String id=conversations.get(i).getConversationId();
       
							converdb.saveId(id,0);
							String content="";
				
							if(conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.TEXT.getType()))
								content=conversations.get(i).getMessages().get(0).getContent();
	  						else if(conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.IMAGE.getType()))
	  		                        content="[ͼƬ]";
	  						else if(conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
	  							content="[��Ƶ]";
	  						else if(conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.VOICE.getType()))
	  							content="[����]";
	  						else if(conversations.get(i).getMessages().get(0).getMsgType().equals("agree"))
	  							content="���Ѿ�ͨ������ĺ�������һ���������";

							converdb.saveNewContentById(id, content);
							converdb.saveTimeById(id,conversations.get(i).getMessages().get(0).getCreateTime());
							converdb.saveTouXiangById(id, conversations.get(i).getConversationIcon());
  							}
  						   }
						}
  					   }	
  					}
  						converList=converdb.getConverByTime();
  					}else {
  						converList.clear();
  						converList=converdb.getConverByTime();  //�����Ϊ0�Ͱ���ʱ�������ȡ�Ự
					}
  				   
  				  
  				  	
  				  	final ViewHolder1 viewHolder1;

  					if(convertView==null){
  						convertView=layoutInflater.inflate(R.layout.item_conversation, null);
  						viewHolder1=new ViewHolder1();
  						viewHolder1.imageView=(CircleImageView)convertView.findViewById(R.id.iv_recent_avatar);
  						viewHolder1.nameText=(TextView)convertView.findViewById(R.id.tv_recent_name);
  						viewHolder1.contentText=(TextView)convertView.findViewById(R.id.tv_recent_msg);
  						viewHolder1.unReadText=(TextView)convertView.findViewById(R.id.tv_recent_unread);
  						viewHolder1.timeText=(TextView)convertView.findViewById(R.id.tv_recent_time);
  						convertView.setTag(viewHolder1);
  						}else {
  							viewHolder1=(ViewHolder1)convertView.getTag();
  						}
  	                 
  					if(converList.size()>0&&position<converList.size()&&!converList.get(position).getConversationId().equals(weather_info.objectId)){   //��ֹ���Լ������Ի�
  						if(converdb.getNewContentById(converList.get(position).getConversationId())!=null){  //��ֹ��ȡ��ϢΪ�յ���Ϣ
  							if(converList.get(position).getConversationTitle()!=null){  //��ֹusernameΪ��
  				   
  					String nickName=converList.get(position).getConversationTitle();
  					if(nickName!=null){
 
  						viewHolder1.nameText.setText(nickName);
  					}
  					
  					String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+converList.get(position).getConversationId()+".jpg_";
  					File file=new File(path);
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
  							viewHolder1.imageView.setImageBitmap(bitmap2);
  						} catch (FileNotFoundException e1) {
  							// TODO Auto-generated catch block
  							e1.printStackTrace();
  						}
  							path=null;
  				    }else {
  				   
                              if(converList.get(position).getConversationIcon()!=null&&!converList.get(position).getConversationIcon().equals("")){
                            	 
      								  new Thread(new Runnable() {
      										
      										@Override
      										public void run() {
      										    final Bitmap bitmap=Utility.getPicture(converList.get(position).getConversationIcon());
      											if(bitmap!=null){
      											
      												download.saveYonghuPic(bitmap,converList.get(position).getConversationId());
      												getActivity().runOnUiThread(new Runnable() {
														
														@Override
														public void run() {
		      												viewHolder1.imageView.setImageBitmap(bitmap);
															
														}
													});

      											}
      										}
      									}).start();
  			                 }else {
                     
								viewHolder1.imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.userpicture));
							   }
  						}
  			                     

				    viewHolder1.contentText.setText(converdb.getNewContentById(converList.get(position).getConversationId()));
  					
  					long time=converList.get(position).getUpdateTime();
  					
  					viewHolder1.timeText.setText(""+TimeUtil.getChatTime(false,time));
  					
  					  if(converdb.getUnReadNumById(converList.get(position).getConversationId())!=0){
  				    	viewHolder1.unReadText.setVisibility(View.VISIBLE);
  				    	viewHolder1.unReadText.setText(""+converdb.getUnReadNumById(converList.get(position).getConversationId()));
  				     	}
  					  else {
						viewHolder1.unReadText.setVisibility(View.INVISIBLE);
				    	}
  						}
  					}
  				}
  					return convertView;
  				}
  				
  				@Override
  				public long getItemId(int position) {
  					return position;
  				}
  				
  				@Override
  				public Object getItem(int position) {
  					 return converList.get(position);
  				}
  				
  				@Override
  				public int getCount() {
  					int count=0;
                 if(conversations!=null&&conversations.size()>0){
                    	 for (int i = 0; i < conversations.size(); i++) {
                                
							 if(conversations.get(i).getMessages().size()>0){
								 if(conversations.get(i).getMessages().get(0).getFromId().equals(weather_info.objectId)||conversations.get(i).getMessages().get(0).getToId().equals(weather_info.objectId))
									 if(!conversations.get(i).getMessages().get(0).getFromId().equals(conversations.get(i).getMessages().get(0).getToId()))
									   if(!conversations.get(i).getMessages().get(0).getMsgType().equals("decline"))
										   if(converdb.getIsFriend(conversations.get(i).getConversationId())!=2)  {
								    
											   count++;   //1��ȷ������������Ϣ��2��ȷ�����Ǹ���Ϣ���ڱ��˻���3���Ƿ�ֹ�Լ����Լ��ĶԻ����룬4���Ƿ�ֹ�ܾ���Ϣ���룬5��������������������ʾ�ڻỰ�б���,
								          }
								
							 }
						}
                
                    	 return count;
                    }else {
                   
  						return 0;
  					}

  				}
  			};
  			
  			
  			
  			conversationList.setAdapter(baseAdapter1);
  			
  			
		  
			final ListView contactsList=(ListView)view4.findViewById(R.id.contactslist);
			sw_refresh2=(SwipeRefreshLayout)view4.findViewById(R.id.sw_refresh);
			
			//��ϵ��ˢ���б��¼�
			sw_refresh2.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					refreshNewFriend();
					sw_refresh2.setRefreshing(false);
				
				}
			});
		
			//�����ϵ���б�horizontalview����ԭ���ĵط�
			contactsList.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_UP){
				    	if(myHorizontalView.getScrollX()!=MyHorizontalView.mMenuWidth){
						   myHorizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth, 0);
						   return true;
				     	}
				    	if(popupWindow!=null&&popupWindow.isShowing()){
				    		popupWindow.dismiss();
				    		return true;
				    	 }
					}
					return false;
					
				}
			});
			
			 //��ϵ���б����¼�
		    contactsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				    
					if(position==0){
						Intent intent=new Intent(getActivity(),newFriendActivity.class);
						startActivity(intent);
					
					}else if(position>0){
						final BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
						bmobIMUserInfo.setUserId(((Friend)baseAdapter2.getItem(position)).getFriendUser().getObjectId());
						bmobIMUserInfo.setName(((Friend)baseAdapter2.getItem(position)).getFriendUser().getNick());
						bmobIMUserInfo.setAvatar(((Friend)baseAdapter2.getItem(position)).getFriendUser().getTouXiangUrl());
				     	BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo ,new ConversationListener() {
							
							@Override
							public void done(BmobIMConversation c, BmobException e) {
								if(e==null){
									Bundle bundle=new Bundle();
									bundle.putSerializable("c",c);
									bundle.putSerializable("userInfo",bmobIMUserInfo);
									Intent intent=new Intent(getActivity(),ChatActivity.class);
									intent.putExtra("bundle", bundle);
									startActivity(intent);
									
								}else {
	                                   Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					}
					
				}
			});
		    //��ϵ���б����������
			 friends.clear();
			 friends=fragmentChat.converdb.getFriends();
			 
			
			 
		    baseAdapter2=new BaseAdapter() {
				
				@Override
				public View getView(final int position, View convertView, ViewGroup parent) {
					 View view6 = null;
				    if(position==0){
					if(convertView==null){
						view6=layoutInflater.inflate(R.layout.new_friend, null);
						}else {
							view6=convertView;
					       
						}
				    
					newFriendImage1=(ImageView)view6.findViewById(R.id.newfriend_image);
					return view6;
				    }
				   else if(position>0) {
					    
				    	if(convertView==null||position==1){
				    	 	view6=layoutInflater.inflate(R.layout.item_user_friend, null);
				    	}else if(convertView!=null&&position!=1){
				    		view6=convertView;
				    	} 

                          
				    	    final CircleImageView circleImageView=(CircleImageView)view6.findViewById(R.id.user_friend_image);
					    	TextView textView=(TextView)view6.findViewById(R.id.user_friend_name);
					    	
					    	String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+friends.get(position-1).getFriendUser().getObjectId()+".jpg_";
					       
					    	File file=new File(path);
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
			  							circleImageView.setImageBitmap(bitmap2);
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
                                             getActivity().runOnUiThread(new Runnable() {
										 		
												@Override
												public void run() {
													if(bitmap!=null){
												     	circleImageView.setImageBitmap(bitmap);
												     	download.saveYonghuPic(bitmap, friends.get(position-1).getFriendUser().getObjectId());
												
													}
													
												}
											});
										
										}
									}).start();
									}else {
										circleImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.userpicture));
									}
							}
							
							textView.setText(friends.get(position-1).getFriendUser().getNick());
                     
							return view6;
				    	}
				    
				 return view6;
				}
				
				@Override
				public long getItemId(int position) {
					return position;
				}
				
				@Override
				public Object getItem(int position) {
					return friends.get(position-1);
				}
				
				@Override
				public int getCount() {
					if(friends==null){
						return 1;
					}else {
						return (friends.size()+1);
					
					}
				
				}
			};
			contactsList.setAdapter(baseAdapter2);
			
			views=new ArrayList<View>();
			views.add(view3);
			views.add(view4);
			
            PagerAdapter pagerAdapter=new PagerAdapter() {
				
				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {

					return arg0==arg1;
				}
				
				@Override
				public int getCount() {
					
					return views.size();
				}

				@Override
				public void destroyItem(ViewGroup container, int position,
						Object object) {
					container.removeView(views.get(position));
				}

				@Override
				public Object instantiateItem(final ViewGroup container, int position) {  //�������������position����ͼ�����ӵ�container�У��Լ����ص�ǰposition��view��Ϊ��ͼ��key
				    container.addView(views.get(position));
					return views.get(position);
				}
			};
			
			chatPager.setAdapter(pagerAdapter);
			
		
			 
			
			String nick=(String)BmobUser.getObjectByKey("nick");

		    if(nick!=null){
		    	userName.setText(nick);
		    	Toast.makeText(getActivity(),nick+ ",��ӭ�ؼ�", Toast.LENGTH_SHORT).show();
		    }	
		       else 
			{   BmobQuery<MyUser> query=new BmobQuery<MyUser>();
			    query.getObject((String)MyUser.getObjectByKey("objectId"),new QueryListener<MyUser>() {

					@Override
					public void done(MyUser myUser, BmobException e) {
						if(e==null){
							weather_info.myUserdb.checkandSaveUpdateN((String)MyUser.getObjectByKey("username"),myUser.getNick());
							userName.setText(weather_info.myUserdb.loadUserName(username));
						}else {
							Toast.makeText(getActivity(), "ʧ�ܣ�"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					 }
			    	});
				
			}
	
			editor=PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
			pre=PreferenceManager.getDefaultSharedPreferences(getActivity());
			
		    mLocationClient=new AMapLocationClient(getActivity());
		    mLocationOption=new AMapLocationClientOption();
		    mLocationClient.setLocationListener(this);
		    mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		    mLocationOption.setInterval(5000);
		    mLocationClient.setLocationOption(mLocationOption);
		    mLocationClient.startLocation();
		    
		    
		    Bitmap bitmap1;
		    
		    String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/"+(String)BmobUser.getObjectByKey("username")+"ͷ��.png";
		    
             File file=new File(path);
		      if(file.exists()){
				   try {
					InputStream is=new FileInputStream(file);
					
					BitmapFactory.Options opts=new BitmapFactory.Options();
					opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
					
					opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
					opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
					
					opts.inSampleSize=2;
					opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
					
					bitmap1=BitmapFactory.decodeStream(is, null, opts);
					userPicture.setImageBitmap(bitmap1);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				   }else {
					  refreshUserPicture(null, 1);
				   }
            	
            	pic.setImageBitmap(getPicture());
				weather.setText(pre.getString("weatherInfo", ""));
				temper.setText(pre.getString("temperature", ""));
				
		        weatherLayout.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_UP){
					    	if(myHorizontalView.getScrollX()!=MyHorizontalView.mMenuWidth){
							   myHorizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth, 0);
							   return true;
					     	}
					    	if(popupWindow!=null&&popupWindow.isShowing()){
					    		popupWindow.dismiss();
					    		return true;
					    	 }
						}
						return false;
						
					
					}
				});
		        
				buttonMes.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
					 	
					 	if(popupWindow!=null&&popupWindow.isShowing())
				    		popupWindow.dismiss();
					 	else {
					 		chatPager.setCurrentItem(0);
						 	horizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth,0);
						 	
						 	buttonMes.setBackgroundResource(R.drawable.danblue);
							buttonMes.setTextColor(Color.parseColor("#FF68B4FF"));
							
							buttonCon.setBackgroundResource(R.drawable.white);
							buttonCon.setTextColor(Color.parseColor("black"));
						}
					 	
						
						
					}
				});
				
				buttonCon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						
						if(popupWindow!=null&&popupWindow.isShowing())
				    		popupWindow.dismiss();
						else {
							chatPager.setCurrentItem(1);
							buttonCon.setBackgroundResource(R.drawable.danblue);
							buttonCon.setTextColor(Color.parseColor("#FF68B4FF"));
							
							buttonMes.setBackgroundResource(R.drawable.white);
							buttonMes.setTextColor(Color.parseColor("black"));
						}
						
						
						
					}
				});
				
		    	userPicture.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View V) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
			            intent.setType("image/jpeg");
			            getActivity().startActivityForResult(intent, 2);
					}
				});
				
			
			    button1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					     Intent intent=new Intent(getActivity(),mapActivity.class);
					     startActivity(intent);
					     
						
					}
				});
			    myAccount.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						Intent intent=new Intent(getActivity(),myAccountAct.class);
						Bundle data=new Bundle();
						data.putString("name",userName.getText().toString());
						intent.putExtras(data);
						startActivity(intent);
					}
				});
			    manage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					    Intent intent=new Intent(getActivity(),manageAct.class);
						startActivity(intent);
					}
				});
			    
			    button2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						
						AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
						final AlertDialog alertDialog=builder.create();
						builder.setTitle("�˳���¼����");
						builder.setMessage("ȷ���˳���¼��");
						builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								    MyUser.logOut();
									BmobUser currentUser=BmobUser.getCurrentUser();
									Intent intent=new Intent(getActivity(),loginAct.class);
									startActivity(intent);
									bmobIM.removeMessageListHandler(messageListHandler);
									converList.clear();
									friends.clear();
									conversations.clear();
									getActivity().finish();
								
								  
							}
						});
						builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(alertDialog.isShowing()){
									alertDialog.dismiss();
								}
							}
						});
						
						builder.show();
						
					  
					}
				});
			    
			    jia.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(popupWindow!=null&&popupWindow.isShowing()){
							popupWindow.dismiss();
							
						}else {
					    	View customView=inflater.inflate(R.layout.popview_item1, null,false);
					    	popupWindow=new PopupWindow(customView,Utility.dip2px(context,100), Utility.dip2px(context,300));//dpתpx
					    	popupWindow.setAnimationStyle(R.style.AnimationFade);
					    	
					    	Button buttonInvite=(Button)customView.findViewById(R.id.buttonInvite);
					    	Button buttonSearch=(Button)customView.findViewById(R.id.buttonSearch);
					    	buttonInvite.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Intent sendIntent = new Intent();
									sendIntent.setAction(Intent.ACTION_SEND);
									sendIntent.putExtra(Intent.EXTRA_TEXT, "�����и������app��һ������ɡ�http://shouji.baidu.com/software/22466614.html");
									sendIntent.setType("text/plain");
									startActivity(sendIntent);
									
								}
							});
					    	buttonSearch.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Intent intent=new Intent(context,searchAct.class);
									startActivity(intent);
									
								}
							});
					    	
							popupWindow.showAsDropDown(v,0,5);
						}
						
						
					}
				});
			    
			    userName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(getActivity(),myAccountAct.class);
						Bundle data=new Bundle();
						data.putString("name",userName.getText().toString());
						intent.putExtras(data);
						startActivity(intent);
						
					}
				});
			
		return view;
	}
	

	
	  public Bitmap getPicture()
	    {   String ALBUM_PATH=Environment.getExternalStorageDirectory()+"/download/"+"weather"+".png";
	    	Bitmap bitmap=null;
	    	bitmap=BitmapFactory.decodeFile(ALBUM_PATH);
	    	return bitmap;
	    }
	  public static void queryWeather(final Context context)
		{     String address3="http://route.showapi.com/9-2";
		      final String ALBUM_PATH=Environment.getExternalStorageDirectory()+"/download/"+"weather"+".png";
			  Http.sendWeatherRequest(pre.getString("locDistrict",""),address3, new HttpCallbackListener()
				{   
					@Override
					public void onFinish(String response)
					{   
						Utility.handleWeather(response,context);
						bitmap=Utility.getPicture(pre.getString("weather_pic", ""));
						if(bitmap!=null){
							File file=new File(ALBUM_PATH);
							try{
							FileOutputStream out=new FileOutputStream(file);
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
							out.flush();
							out.close();
							}catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					    ((Activity) context).runOnUiThread(new Runnable(){
					    @Override
					    public void run(){
					    	showWeather(context);
					    }});
							
					}
				});
		}
     
	//   private void savePicture(Bitmap bitmap,String path)
	//	{   
		//	File file=new File(path);
		//	try{
		//	FileOutputStream out=new FileOutputStream(file);
			//bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		//	out.flush();
		//	out.close();
			//}catch(Exception e)
		///	{
		//		e.printStackTrace();
			//}
	//	}
	
    public static void showWeather(Context context)
		{
		    
             weather.setText(pre.getString("weatherInfo", ""));
		     temper.setText(pre.getString("temperature", ""));

		     if(bitmap!=null)
		         pic.setImageBitmap(bitmap);
		     Intent intent=new Intent(context,autoUqdateService.class);
			 context.startService(intent);
		}
        public static void refreshUserName(String response)
        {   
        	userName.setText(response);
        }
  
        public static void refreshUserPicture(Bitmap bitmap,int i)
          
        {  
        	
        	if(bitmap!=null){
        	userPicture.setImageBitmap(bitmap);
        	
        	}
        	if(i==1){     //���ǵ�ֻ���������µ�ʱ��ŵ���
        		 String touxiangUrl=(String)BmobUser.getObjectByKey("touxiangUrl");
             	
             	   if(touxiangUrl!=null)       //�����ֹ����ͼƬ������һ̨�ͻ���û�и���
      			{  
      			   BmobFile bmobFile=new BmobFile((String)BmobUser.getObjectByKey("username")+"ͷ��"+".png",null,touxiangUrl);//ȷ���ļ����֣�ͷ��.png���������ַ
      			   download.downloadFile(bmobFile,context);//�������ز���
      			   
      			}
        	}
		   
        }
	
	
   

	  public static void refreshNewFriend(){    //��ͨ�����߶Է�ͨ�����ѵ�ʱ��ˢ����ϵ���б�
		  UserModel.getInstance().queryFriends(new FindListener<Friend>() {

				@Override
				public void done(final List<Friend> list, BmobException e) {
					
					if(e==null||e.getErrorCode()==0){
						friends.clear();
						friends=list;
						
						for (int i = 0; i < list.size(); i++) {
							if(list.get(i).getFriendUser().getObjectId()!=null){
							converdb.saveId(list.get(i).getFriendUser().getObjectId(),1);  //����洢��ϵ�˵�id���ǳƣ�Ȼ���ȡ�Ự��ʱ��Ͳ��洢��ϵ�˵��ǳ��ˣ���ʱ���ǳƴ���Ȼ��������Դ洢isFriend���ʹ��ж��Ƿ�������
					    	converdb.saveNickById(list.get(i).getFriendUser().getObjectId(), list.get(i).getFriendUser().getNick());
					    	converdb.saveTouXiangById(list.get(i).getFriendUser().getObjectId(), list.get(i).getFriendUser().getTouXiangUrl());
							}
						}
						
						List<Friend> dbfriends=new ArrayList<Friend>();
						dbfriends=converdb.getFriends();

						if(friends.size()>0){
						  for (int i = 0; i <dbfriends.size(); i++) {
							int k=checkLocalAndWebFriend(friends, dbfriends.get(i));
							if(k==2){   //���k==2��ʾû���������
							   if(converdb.getIsFriend(dbfriends.get(i).getFriendUser().getObjectId())!=2&&converdb.getIsFriend(dbfriends.get(i).getFriendUser().getObjectId())!=3&&!dbfriends.get(i).getFriendUser().getObjectId().equals("e5be088480"))//����ϵͳ�ͺ�����ϵͳ�Ƿֿ��ģ���������
								   converdb.updateIsFriendI(dbfriends.get(i).getFriendUser().getObjectId(),0);
						    	}
						  }
						}else {     //ҲҪ����0�����
							for (int i = 0; i < dbfriends.size(); i++) {
						       if(converdb.getIsFriend(dbfriends.get(i).getFriendUser().getObjectId())!=2&&converdb.getIsFriend(dbfriends.get(i).getFriendUser().getObjectId())!=3&&!dbfriends.get(i).getFriendUser().getObjectId().equals("e5be088480"))
								converdb.updateIsFriendI(dbfriends.get(i).getFriendUser().getObjectId(),0);
							}
						 }
					  friends.clear();
					  
					  friends=converdb.getFriends();
					  
                      baseAdapter2.notifyDataSetChanged();
                      
					}else if(e.getErrorCode()==9015){
						Toast.makeText(context,"��ѯ��������,���Ժ��ٳ���"+e.getMessage(),Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(context,"��ѯ��������"+e.getMessage()+e.getErrorCode(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
			
	}
	  public static void refreshConversations(int i,final String id){
		    if(i==0){     //������Ϣ�����
		    	 converdb.addUnReadNumById(id);
		    }else if(i==1)   { //��ĳһ�����촰�ں����
		    	 converdb.clearUnReadNumById(id);
		    }
		
		    if(id!=null&&converdb.getNickById(id).equals(id)){

	        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
	        query.getObject(id, new QueryListener<MyUser>() {
	    		
	    		@Override
	    		public void done(MyUser user, BmobException e ) {
	    		      if(e==null){
	    		    	 converdb.saveNickById(id, user.getNick());

	    		      }else{
	    		    	  Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
	    		      }
	    			
	    		}
	    	 });
		    }
		    conversations=bmobIM.loadAllConversation();
		    
		    baseAdapter1.notifyDataSetChanged();
		   
	  }
     public static void refreshBlack(int i){
    	 MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
    	 if(i==0||i==2){                             //�鿴���˵ĺ������б�
    	 BmobQuery<Black> bmobQuery=new BmobQuery<Black>();
    	 bmobQuery.addWhereEqualTo("myUser", currentUser);
    	 bmobQuery.findObjects(new FindListener<Black>() {

			@Override
			public void done(List<Black> list, BmobException e) {
				if(e==null){
					if(list.size()>0){
						for (int i = 0; i < list.size(); i++) {
							converdb.saveId(list.get(i).getBlackUser().getObjectId(), 0);
							converdb.updateIsFriendI(list.get(i).getBlackUser().getObjectId(), 2); //���ڣ�������İ���ˣ���������
						}
					}
				}else {
					Log.d("Main",e.getMessage());
				}
				
			}
		});
    	
     }
    	 if(i==0||i==1){                       //�鿴�Լ��Ƿ�ĳ������
    	 BmobQuery<Black> bmobQuery2=new BmobQuery<Black>();
    	 bmobQuery2.addWhereEqualTo("blackUser", currentUser);
    	 bmobQuery2.findObjects(new FindListener<Black>() {

			@Override
			public void done(List<Black> list, BmobException e) {
				if(e==null){
					if(list.size()>0){
						for (int i = 0; i < list.size(); i++) {
							converdb.saveId(list.get(i).getMyUser().getObjectId(),0);
							converdb.updateIsFriendI(list.get(i).getMyUser().getObjectId(), 3); //��ʾ�Է����Լ�����
						}
					}
				}else {
					Log.d("Main",e.getMessage());
				}
				
				
			}
		});
    
    	 }
    	
     }


	@Override
	public void onLocationChanged(AMapLocation amaplocation) {
		if(amaplocation!=null)
		  {  
			
			if(amaplocation.getErrorCode()==0)
			   {
				   double lat=amaplocation.getLatitude();
				   double lon=amaplocation.getLongitude();
					String address="http://route.showapi.com/238-2";  //��γ��ת��Ϊ��ַ
					if(!once)
					Http.queryAreaByXY(lat, lon, address, new HttpCallbackListener() {
						@Override
						public void onFinish(String response) {
							
						    Utility.handleAreaByXY(response, getActivity());
							queryWeather(getActivity());
							once=true;
							
						}
					});
			   }
			}
		
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if(null != mLocationClient)
		{   mLocationClient.stopLocation();
			mLocationClient.onDestroy();
		}
	}
	private static int checkLocalAndWebFriend(List<Friend> Webfriends,Friend friend){   
		  for (int i = 0; i < Webfriends.size(); i++) {
			    if(Webfriends.get(i).getFriendUser().getObjectId().equals(friend.getFriendUser().getObjectId())){
			    	  return 1;
			     }
	    	}
		  return 2;
	 }

	
}
