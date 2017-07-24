package activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.content.Context;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

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

import com.uniqueweather.app.R;

import db.conversationDB;

public  class fragmentChat extends Fragment 
{   
    

	private static TextView weather;

	private static TextView temper;
	public static CircleImageView userPicture;

		
	
	//private ListView chatList;      //聊天列表
  private static ImageView pic;
    public  static TextView userName;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pre;
    
    private static Bitmap bitmap;
    
    private static Context context;

    public MyHorizontalView horizontalView;

    
    private myChatPager chatPager;

	 private View view4;     //联系人界面view
	private List<View> views;    //装载消息界面view和联系人界面view的views
	
	private static BaseAdapter baseAdapter1;
	private static BaseAdapter baseAdapter2;//朋友列表
	

	public static conversationDB converdb;  //对话数据表
	
	
	private String username;

	private int onceConversation=0;  //这里防止多次刷新图片内存溢出，当它为0的时候表示要刷新，当它为1的时候停止刷新
	
	private int onceContact=0;    //联系人列表
	
	public static ImageView newFriendImage;
	public static ImageView newFriendImage1;
	
	private static List<Friend> friends=new ArrayList<Friend>(); 
	public static LayoutInflater layoutInflater;
	
	public static BmobIM bmobIM=BmobIM.getInstance();
    public static  List<BmobIMConversation>  conversations;
	private List<BmobIMConversation> converList=new ArrayList<BmobIMConversation>();//这里是为了回应点击事件进行加载MyUser的信息资料
    
	public fragmentChat(){
		context=getActivity();
	}
	
  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			 Bundle savedInstanceState) 
    { 
            View view = null;;
		 
			view=inflater.inflate(R.layout.connection,container,false);
		    userName=(TextView)view.findViewById(R.id.userName);
	        userPicture=(CircleImageView)view.findViewById(R.id.userPicture);
            
	        username=(String)MyUser.getObjectByKey("username");
	        if(context==null)
	        	context=getActivity();
	        converdb=conversationDB.getInstance(context);
	        
			
             MyUser user=MyUser.getCurrentUser(MyUser.class);
            if(user.getObjectId()!=null)
              BmobIM.connect(user.getObjectId(),new ConnectListener() {
				
				@Override
				public void done(String uid, BmobException e) {
					if(e==null){
						Log.d("Main","连接成功");
						
					}else {
						Toast.makeText(context, "连接失败，"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
            
            TextView myCity;
            Button button1;
            Button button2;//logout Button
            TextView myAccount;     //我的账户TextView 
            View view3;    //消息界面View
            
			weather=(TextView)view.findViewById(R.id.weather);
			temper=(TextView)view.findViewById(R.id.temper);
			myCity=(TextView)view.findViewById(R.id.myCity);
			pic=(ImageView)view.findViewById(R.id.weather_pic);
			button1=(Button)view.findViewById(R.id.button_map);
			button2=(Button)view.findViewById(R.id.button1);
			horizontalView=(MyHorizontalView)view.findViewById(R.id.horiView);
			myAccount=(TextView)view.findViewById(R.id.myAccount);
		    final Button  buttonMes=(Button)view.findViewById(R.id.chat_mes);
		    final Button buttonCon=(Button)view.findViewById(R.id.chat_con);
			newFriendImage=(ImageView)view.findViewById(R.id.newfriend_image);
			
			chatPager=(myChatPager)view.findViewById(R.id.chatPager);
            layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view3=layoutInflater.inflate(R.layout.chatlist,null);
			view4=layoutInflater.inflate(R.layout.contactslist,null);
			
			//会话列表点击事件
			
            ListView conversationList=(ListView)view3;
            conversationList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					try {
						final BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
						bmobIMUserInfo.setUserId(((BmobIMConversation)baseAdapter1.getItem(position)).getConversationId());
						bmobIMUserInfo.setName(((BmobIMConversation)baseAdapter1.getItem(position)).getConversationTitle());
						bmobIMUserInfo.setAvatar(((BmobIMConversation)baseAdapter1.getItem(position)).getConversationIcon());
				     	BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo ,new ConversationListener() {
							
							@Override
							public void done(BmobIMConversation c, BmobException e) {
								if(e==null){
									Bundle bundle=new Bundle();
									bundle.putSerializable("c",c);
									bundle.putSerializable("userInfo",bmobIMUserInfo);
									Intent intent=new Intent(context,ChatActivity.class);
									intent.putExtra("bundle", bundle);
									startActivity(intent);
									
								}else {
	                                   Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
			});
			
            
            conversations=bmobIM.loadAllConversation();
            bmobIM.addMessageListHandler(new MessageListHandler() {
				
				@Override
				public void onMessageReceive(List<MessageEvent> list) {
					 for (int i = 0; i <list.size(); i++) {
						 String content="";
		  		         String id=list.get(i).getConversation().getConversationId();
							if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.TEXT.getType()))
								content=list.get(i).getMessage().getContent();
								else if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.IMAGE.getType()))
				                        content="[图片]";
								else if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
									content="[视频]";
								else if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.VOICE.getType()))
									content="[语音]";
							
							converdb.saveNewContentById(id, content);
							converdb.saveTimeById(id, list.get(i).getMessage().getCreateTime());
							
							refreshConversations(0,list.get(i).getConversation().getConversationId());
							
						}
					 
					}
					
				}
			);
            //会话列表适配器
            //会话列表的ViewHolder
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
  				     	int count=getCount();
  						for (int i = 0; i < count; i++) {
  							String id=conversations.get(i).getConversationId();
							converdb.saveId(id);
							
							String content="";
							if(conversations.get(i).getMessages().size()>0){
								
							if(conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.TEXT.getType()))
								content=conversations.get(i).getMessages().get(0).getContent();
	  						else if(conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.IMAGE.getType()))
	  		                        content="[图片]";
	  						else if(conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
	  							content="[视频]";
	  						else if(conversations.get(i).getMessages().get(0).getMsgType().equals(BmobIMMessageType.VOICE.getType()))
	  							content="[语音]";
							}
							
							Log.d("Main","content="+content);
							converdb.saveNewContentById(id, content);
							
							if(conversations.get(i).getMessages().size()>0)
						    	converdb.saveTimeById(id,conversations.get(i).getMessages().get(0).getCreateTime());
							converdb.saveTouXiangById(id, conversations.get(i).getConversationIcon());
						}
  						converList=converdb.getConverByTime();
  					}else {
  						converList.clear();
  						converList=converdb.getConverByTime();  //如果不为0就按照时间排序获取会话
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
  	
  					
  					String nickName=converList.get(position).getConversationTitle();
  					if(nickName!=null){
  						viewHolder1.nameText.setText(nickName);
  					}
  					String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+conversations.get(position).getConversationId()+".jpg_";
  					File file=new File(path);
  						if(file.exists()){
                            BitmapFactory.Options options=new BitmapFactory.Options();
                            options.inSampleSize=2;
  							viewHolder1.imageView.setImageBitmap(BitmapFactory.decodeFile(path,options));
  							path=null;
  						}else {
                              if(converList.get(position).getConversationIcon()!=null){
                            	  if(onceConversation==0)
      								  new Thread(new Runnable() {
      										
      										@Override
      										public void run() {
      										    final Bitmap bitmap=Utility.getPicture(converList.get(position).getConversationIcon());
      											if(bitmap!=null){
      												onceConversation=1;
      												savePicture(bitmap, Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+converList.get(position).getConversationId()+".jpg_");
      												((Activity)context).runOnUiThread(new Runnable() {
														
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
                    if(bmobIM.loadAllConversation()!=null){
                    	 for (int i = 0; i < bmobIM.loadAllConversation().size(); i++) {
							 if(conversations.get(i).getMessages().size()>0){
								 count++;
							 }
						}
                    	 return count;
                    }
                    else {
  						return 0;
  					}

  				}
  			};
  			((ListView)view3).setAdapter(baseAdapter1);
		   //联系人列表点击事件
			ListView contactsList=(ListView)view4;
		    contactsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				
					if(position==0){
						Intent intent=new Intent(context,newFriendActivity.class);
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
									Intent intent=new Intent(context,ChatActivity.class);
									intent.putExtra("bundle", bundle);
									startActivity(intent);
									
								}else {
	                                   Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					}
					
				}
			});
		    //联系人列表加载适配器
			UserModel.getInstance().queryFriends(new FindListener<Friend>() {

				@Override
				public void done(final List<Friend> list, BmobException e) {
					
					if(e==null||e.getErrorCode()==0){
						friends=list;
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

							    	  converdb.saveId(friends.get(position-1).getFriendUser().getObjectId());  //这里存储联系人的id和昵称，然后获取会话的时候就不存储联系人的昵称了，有时候昵称错误
							    	  converdb.saveNickById(friends.get(position-1).getFriendUser().getObjectId(), friends.get(position-1).getFriendUser().getNick());
							    	  converdb.saveTouXiangById(friends.get(position-1).getFriendUser().getObjectId(), friends.get(position-1).getFriendUser().getTouXiangUrl());
							    	  
							    	   final CircleImageView circleImageView=(CircleImageView)view6.findViewById(R.id.user_friend_image);
								    	TextView textView=(TextView)view6.findViewById(R.id.user_friend_name);
								    	
								    	String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+friends.get(position-1).getFriendUser().getObjectId()+".jpg_";
								       
								    	File file=new File(path);
										if(file.exists()){
											BitmapFactory.Options options=new BitmapFactory.Options();
											options.inSampleSize=2;
											circleImageView.setImageBitmap(BitmapFactory.decodeFile(path,options));
											path=null;
										}else {
											if(onceContact==0)
											    new Thread(new Runnable() {
													
													@Override
													public void run() {
														final Bitmap bitmap=Utility.getPicture(friends.get(position-1).getFriendUser().getTouXiangUrl());
			                                             ((Activity)context).runOnUiThread(new Runnable() {
													 		
															@Override
															public void run() {
																if(bitmap!=null){
															     	circleImageView.setImageBitmap(bitmap);
															     	onceContact=1;
																}
																
															}
														});
													
													}
												}).start();
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
								return list.get(position-1);
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
						((ListView)view4).setAdapter(baseAdapter2);
					}else{
						Log.d("Main","查询朋友有误,"+e.getMessage()+e.getErrorCode());
					}
					
				}
			});
			((ListView)view4).setAdapter(baseAdapter2);
			
		    refreshNewFriend();
		    
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
				public Object instantiateItem(final ViewGroup container, int position) {  //将参数里给定的position的视图，增加到container中，以及返回当前position的view作为此图的key
				    container.addView(views.get(position));
					return views.get(position);
				}
			};
			
			chatPager.setAdapter(pagerAdapter);
			
			if(context==null)
			    context=(Context)getActivity();
			
			String nick=(String)BmobUser.getObjectByKey("nick");

		    if(nick!=null){
		    	userName.setText(nick);
		    	Toast.makeText(context,nick+ ",欢迎您", Toast.LENGTH_SHORT).show();
		    }	else 
			{   BmobQuery<MyUser> query=new BmobQuery<MyUser>();
			    query.getObject((String)MyUser.getObjectByKey("objectId"),new QueryListener<MyUser>() {

					@Override
					public void done(MyUser myUser, BmobException e) {
						if(e==null){
							weather_info.myUserdb.checkandSaveUpdateN((String)MyUser.getObjectByKey("username"),myUser.getNick());
							userName.setText(weather_info.myUserdb.loadUserName(username));
						}else {
							Toast.makeText(context, "失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					 }
			    	});
				
			}
	
			editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
			pre=PreferenceManager.getDefaultSharedPreferences(context);
			
		
		    Bitmap bitmap1;
		    
		    String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/"+"头像.png";
		    
            File file=new File(path);
		      if(file.exists()){
					  bitmap1=BitmapFactory.decodeFile(path);
	                  userPicture.setImageBitmap(bitmap1);
	                  path=null;
				   }else {
					  refreshUserPicture(null, 1);
				}
            	
            	pic.setImageBitmap(getPicture());
				weather.setText(pre.getString("weatherInfo", ""));
				temper.setText(pre.getString("temperature", ""));
		       
				buttonMes.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						chatPager.setCurrentItem(0);
					 	horizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth,0);
					 	
						buttonMes.setBackgroundResource(R.drawable.danblue);
						buttonMes.setTextColor(Color.parseColor("#FF68B4FF"));
						
						buttonCon.setBackgroundResource(R.drawable.white);
						buttonCon.setTextColor(Color.parseColor("black"));
					}
				});
				
				buttonCon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						chatPager.setCurrentItem(1);
						
						buttonCon.setBackgroundResource(R.drawable.danblue);
						buttonCon.setTextColor(Color.parseColor("#FF68B4FF"));
						
						buttonMes.setBackgroundResource(R.drawable.white);
						buttonMes.setTextColor(Color.parseColor("black"));
						
					}
				});
				
		    	userPicture.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View V) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
			            intent.setType("image/jpeg");
			            ((Activity)context).startActivityForResult(intent, 2);
					}
				});
				
				myCity.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(context,myCityAction.class);
						intent.putExtra("selectedCityName",pre.getString("locCity", "") );
						intent.putExtra("temp", pre.getString("temperature",""));
						startActivity(intent);
						
					}
				});
			    button1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					     Intent intent=new Intent(context,mapActivity.class);
					     startActivity(intent);
					     
						
					}
				});
			    myAccount.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						Intent intent=new Intent(context,myAccountAct.class);
						Bundle data=new Bundle();
						data.putString("name",userName.getText().toString());
						intent.putExtras(data);
						startActivity(intent);
					}
				});
			    button2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
					    BmobUser.logOut();
						BmobUser currentUser=BmobUser.getCurrentUser();
						Intent intent=new Intent(context,loginAct.class);
						startActivity(intent);
						Activity activity=(Activity)context;
						activity.finish();
					}
				});
			    userName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(context,myAccountAct.class);
						Bundle data=new Bundle();
						data.putString("name",userName.getText().toString());
						intent.putExtras(data);
						startActivity(intent);
						
					}
				});
		
		
	
		return view;
	}
	
	  public Bitmap getPicture()
	    {
	    	Bitmap bitmap=null;
	    	bitmap=BitmapFactory.decodeFile(weather_info.ALBUM_PATH);
	    	return bitmap;
	    }
	  public static void queryWeather(final Context context)
		{     
			  Http.sendWeatherRequest(pre.getString("locDistrict",""),weather_info.address3, new HttpCallbackListener()
				{   
					@Override
					public void onFinish(String response)
					{   
						Utility.handleWeather(response,context);
						bitmap=Utility.getPicture(pre.getString("weather_pic", ""));
						if(bitmap!=null)
						    savePicture(bitmap,weather_info.ALBUM_PATH);
					    ((Activity) context).runOnUiThread(new Runnable(){
					    @Override
					    public void run(){
					    	showWeather(context);
					    }});
							
					}
				});
		}

	public static void savePicture(Bitmap bitmap,String path)
		{ 
			File file=new File(path);
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
        {  if(bitmap!=null)
        	userPicture.setImageBitmap(bitmap);
        	
        	if(i==1){     //这是当只有联网更新的时候才调用
        		 String touxiangUrl=(String)BmobUser.getObjectByKey("touxiangUrl");
             	
             	   if(touxiangUrl!=null)       //这里防止更新图片后另外一台客户端没有更新
      			{  
      			   BmobFile bmobFile=new BmobFile("头像"+".png",null,touxiangUrl);//确定文件名字（头像.png）和网络地址
      			   download.downloadFile(bmobFile,context);//进行下载操作
      			   
      			}
        	}
		   
        }
	
	
   

	  public static void refreshNewFriend(){    //当通过或者对方通过好友的时候刷新联系人列表
		  UserModel.getInstance().queryFriends(new FindListener<Friend>() {

			@Override
			public void done(List<Friend> list, BmobException e) {
				if(e==null){
				friends=list;
				baseAdapter2.notifyDataSetChanged();
				}else {
					Toast.makeText(context,"更新朋友有误，"+e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	  public static void refreshConversations(int i,String id){
		    if(i==0){     //接收消息后调用
		    	 converdb.addUnReadNumById(id);
		    }else if(i==1)   { //打开某一个聊天窗口后调用
		    	 converdb.clearUnReadNumById(id);
		    }
		    conversations=bmobIM.loadAllConversation();
		    
		    baseAdapter1.notifyDataSetChanged();
		   
	  }
}
