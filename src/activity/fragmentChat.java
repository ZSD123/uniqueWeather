package activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Black;
import model.Friend;
import model.UserModel;
import myCustomView.CircleImageView;
import myCustomView.DividerItemDecoration;
import myCustomView.messageSwipe;

import myCustomView.myChatPager;
import service.autoUqdateService;
import wxapi.WXEntryActivity;
import Util.Http;
import Util.HttpCallbackListener;

import Util.TimeUtil;
import Util.Utility;
import Util.download;

import adapter.OnRecyclerViewListener;
import adapter.friendAdapter;
import adapter.messageAdapter;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnScrollChangeListener;
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

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMReceiveStatus;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;

import cn.bmob.newim.db.BmobIMDBManager;
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
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import db.conversationDB;

public  class fragmentChat extends Fragment implements AMapLocationListener 
{   
    

	public  static TextView weather;

	public  static TextView temper;
	public static CircleImageView userPicture;
    private MessageListHandler messageListHandler;
   
    private static ImageView pic;
    public  static TextView userName;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pre;
    
    private static Bitmap bitmap;  //天气bitmap
    

    private AMapLocationClientOption mLocationOption=null;
    private AMapLocationClient mLocationClient;
    
    private myChatPager chatPager;

	 private View view4;     //联系人界面view
	private List<View> views;    //装载消息界面view和联系人界面view的views
	
	private static messageAdapter messageadapter;  //消息列表适配器

	private static friendAdapter friendadapter;    //朋友列表适配器
	
	
	
	public static conversationDB converdb;  //对话数据表
	private boolean once=false;
	private String username;
	
	public static ImageView newFriendImage;  //联系人的红点
	public static ImageView newFriendImage1;  //新的朋友红点
	
	private static List<Friend> friends=new ArrayList<Friend>(); 
	public static LayoutInflater layoutInflater;
	
	public static BmobIM bmobIM;
    public static  List<BmobIMConversation>  conversations;

    public static  MyHorizontalView myHorizontalView;
    private LinearLayoutManager mLinearLayoutManager;
    public static boolean canScroll=true;  //设置是否可以滑动
    
	private static Context context;
     
	public static PopupWindow popupWindow;
	
	  //这下面都是要变色的，放在一起
	private static int [] buttonResource;
	private static String buttonColor;   //点亮的按钮的颜色
	private static String buttonColor1;  //没有点亮的按钮的颜色
	
	private static Button buttonMes;
	private static Button buttonCon;
	
	private static TextView myAccount;  //我的名片TextView 
	private static TextView manage;     //账户管理
	private static  TextView design;    //设计界面
	
	
	public static RelativeLayout leftmenuBack;
	public static RelativeLayout lin1;
	public static RelativeLayout weatherLinearLayout;
	
	
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
            
	        myHorizontalView=(MyHorizontalView)view.findViewById(R.id.horiView);
	        username=(String)MyUser.getObjectByKey("username");
	        	
	        converdb=conversationDB.getInstance(getActivity());
		
	        MyUser user=MyUser.getCurrentUser(MyUser.class);
            if(user!=null&&user.getObjectId()!=null)
              BmobIM.connect(user.getObjectId(),new ConnectListener() {
				
				@Override
				public void done(String uid, BmobException e) {
					if(e==null){
						Log.d("Main","连接成功");
					}else {
						Toast.makeText(getActivity(), "连接失败，"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
       
            buttonResource=new int[2];
            
            Button button1;
            Button button2;//logout Button
            ImageView jia;  //加号按钮
            
              
            View view3;    //消息界面View
            final messageSwipe sw_refresh1;   //消息界面的refresh
            final SwipeRefreshLayout sw_refresh2;  //联系人列表的refresh
            
            
            leftmenuBack=(RelativeLayout)view.findViewById(R.id.leftmenuBack);
            lin1=(RelativeLayout)view.findViewById(R.id.lin1);
            weatherLinearLayout=(RelativeLayout)view.findViewById(R.id.weatherlinearlayout);
            
            weather=(TextView)view.findViewById(R.id.weather);
			temper=(TextView)view.findViewById(R.id.temper);
			pic=(ImageView)view.findViewById(R.id.weather_pic);
			button1=(Button)view.findViewById(R.id.button_map);
			button2=(Button)view.findViewById(R.id.buttonlogout);
			
			jia=(ImageView)view.findViewById(R.id.jia);

			myAccount=(TextView)view.findViewById(R.id.myAccount);
			manage=(TextView)view.findViewById(R.id.manage);
			design=(TextView)view.findViewById(R.id.design);
			
			
		    buttonMes=(Button)view.findViewById(R.id.chat_mes);
		    buttonCon=(Button)view.findViewById(R.id.chat_con);
			newFriendImage=(ImageView)view.findViewById(R.id.newfriend_image);
			
			chatPager=(myChatPager)view.findViewById(R.id.chatPager);
            layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view3=layoutInflater.inflate(R.layout.chatlist,null);
			view4=layoutInflater.inflate(R.layout.contactslist,null);
			

	          
            bmobIM=BmobIM.getInstance();
            conversations=new ArrayList<BmobIMConversation>();
            
            conversations=bmobIM.loadAllConversation();
			
            int num=getCount();
            
			RecyclerView messageRecyclerView=(RecyclerView)view3.findViewById(R.id.chatList);
			mLinearLayoutManager=new LinearLayoutManager(context);
			
			messageRecyclerView.setLayoutManager(new LinearLayoutManager(context));
			messageRecyclerView.setItemAnimator(new DefaultItemAnimator());
			messageRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
			messageadapter=new messageAdapter(context,num);
			
            messageadapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
				
				@Override
				public boolean onItemLongClick(final int position) {
					 AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				     final String[] xuanzeweizhi={"删除该会话"};
				     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							  if(which==0){
								    bmobIM.deleteConversation(conversations.get(position).getConversationId());
									converdb.saveNewContentById(conversations.get(position).getConversationId(),"");
									converdb.clearUnReadNumById(conversations.get(position).getConversationId());
									converdb.saveTimeById(conversations.get(position).getConversationId(), 0);
									
									if(converdb.getIsFriend(conversations.get(position).getConversationId())==0){//如果是陌生人的话就直接删除
								         converdb.deleteCoversationById(conversations.get(position).getConversationId());
							        }
									
									
									fragmentChat.refreshConversations(2,conversations.get(position).getConversationId());
							
							  }
						}
					});
				     builder.show();
					return true;
			
				}
				
				@Override
				public void onItemClick(int position) {
					   if(myHorizontalView.getScrollX()!=MyHorizontalView.mMenuWidth){
						   myHorizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth, 0);
						  
				     	}else if(popupWindow!=null&&popupWindow.isShowing()){
				    		popupWindow.dismiss();
				    		
				       }else{
				    	   final BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
							
							bmobIMUserInfo.setUserId(conversations.get(position).getConversationId());
							if(conversations.get(position).getConversationTitle()!=null&&!conversations.get(position).getConversationTitle().equals(""))
						    	bmobIMUserInfo.setName(conversations.get(position).getConversationTitle());
							else if(conversations.get(position).getConversationId()!=null){
								bmobIMUserInfo.setName(conversations.get(position).getConversationId());
							}
							bmobIMUserInfo.setAvatar(conversations.get(position).getConversationIcon());
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
			
			
            sw_refresh1=(messageSwipe)view3.findViewById(R.id.sw_refresh);
            //会话列表下拉刷新事件
            sw_refresh1.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					refreshConversations(-1, null);
					sw_refresh1.setRefreshing(false);
				
					
				}
			});
            
             //会话列表点击事件
            messageRecyclerView.setOnTouchListener(new OnTouchListener() {
				
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
				                        content="[图片]";
								else if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
									content="[视频]";
								else if(list.get(i).getMessage().getMsgType().equals(BmobIMMessageType.VOICE.getType()))
									content="[语音]";
							    
						
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
            //会话列表适配器
            //会话列表的ViewHolder
            messageRecyclerView.setAdapter(messageadapter);

  		     //联系人列表加载适配器
			friends.clear();
			friends=fragmentChat.converdb.getFriends();
		    
			final RecyclerView mRecyclerView=(RecyclerView)view4.findViewById(R.id.rc_view);
			
		    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
			friendadapter=new friendAdapter(context, friends);
			friendadapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
				
				@Override
				public boolean onItemLongClick(int position) {
				    
					return true;
				}
				
				@Override
				public void onItemClick(int position) {
				    	if(myHorizontalView.getScrollX()!=MyHorizontalView.mMenuWidth){
						   myHorizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth, 0);
						}else if(popupWindow!=null&&popupWindow.isShowing()){
				    		popupWindow.dismiss();
				    	}else{
					    
				    	if(position==0){
							Intent intent=new Intent(getActivity(),newFriendActivity.class);
							startActivity(intent);
						
						}else if(position>0){
							final BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
							bmobIMUserInfo.setUserId((friendadapter.getItem(position)).getFriendUser().getObjectId());
							bmobIMUserInfo.setName((friendadapter.getItem(position)).getFriendUser().getNick());
							bmobIMUserInfo.setAvatar((friendadapter.getItem(position)).getFriendUser().getTouXiangUrl());
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
				}
			});
			
			mRecyclerView.setAdapter(friendadapter);					
			//点击联系人列表horizontalview滑回原来的地方
			mRecyclerView.setOnTouchListener(new OnTouchListener() {
				
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
			
			sw_refresh2=(SwipeRefreshLayout)view4.findViewById(R.id.sw_refresh);
			
			//联系人刷新列表事件
			sw_refresh2.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					refreshNewFriend();
					sw_refresh2.setRefreshing(false);
				
				}
			});
		    
			
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
			
		    
			 
			
			String nick=(String)BmobUser.getObjectByKey("nick");

		    if(nick!=null){
		    	userName.setText(nick);
		    	Toast.makeText(getActivity(),nick+ ",欢迎回家", Toast.LENGTH_SHORT).show();
		    } else 
			{   BmobQuery<MyUser> query=new BmobQuery<MyUser>();
			    query.getObject(weather_info.objectId,new QueryListener<MyUser>() {

					@Override
					public void done(MyUser myUser, BmobException e) {
						if(e==null){
							weather_info.myUserdb.checkandSaveUpdateN((String)MyUser.getObjectByKey("username"),myUser.getNick());
							userName.setText(weather_info.myUserdb.loadUserName(username));
						}else {
							Toast.makeText(getActivity(), "失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					 }
			    	});
				
			}
	
			editor=PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
			pre=PreferenceManager.getDefaultSharedPreferences(getActivity());
			
			int designNum=pre.getInt("design", 0);
			
			switch (designNum) {
				case 0 :
					setButtonResource(new int []{R.drawable.danblue,R.drawable.white0});
					setButtonColor("#68B4FF","#000000");
					break;
				case 1:
					leftmenuBack.setBackgroundResource(R.drawable.sun);
					lin1.setBackgroundColor(Color.parseColor("#F2CC4D"));
					weatherLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
					
					
					setButtonResource(new int []{R.drawable.danyellow,R.drawable.white1});
		 			setButtonColor("#F2CC4D","#000000");
		 			
		 			setTextColorBlack();
					break;
				case 2:
					fragmentChat.leftmenuBack.setBackgroundResource(R.drawable.flower);
					fragmentChat.lin1.setBackgroundColor(Color.parseColor("#FCBC1D"));
					fragmentChat.weatherLinearLayout.setBackgroundColor(Color.parseColor("#F5D4D9"));
					
					fragmentChat.setButtonResource(new int []{R.drawable.danflower,R.drawable.pink});
					fragmentChat.setButtonColor("#FFFFFF","#000000");
					
					setTextColorBlack();
					break;
				case 3:
					leftmenuBack.setBackgroundResource(R.drawable.coldmount);
					lin1.setBackgroundColor(Color.parseColor("#FFFFFF"));
					weatherLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
					
					
					setButtonResource(new int []{R.drawable.danblack,R.drawable.white1});
					setButtonColor("#000000","#000000");
					
					weather.setTextColor(Color.parseColor("#000000"));
					temper.setTextColor(Color.parseColor("#000000"));
					
					setTextColorBlack();
					break;
				case 4:
					leftmenuBack.setBackgroundResource(R.drawable.night1);
					lin1.setBackgroundColor(Color.parseColor("#122950"));
					weatherLinearLayout.setBackgroundColor(Color.parseColor("#0A182D"));
					
					setButtonResource(new int []{R.drawable.dannight,R.drawable.white2});
					setButtonColor("#6A82A5","#A2C0DE");
					
					setNightMode();  //设置夜间模式的一系列程序代码打包
					break;
				default :
					break;
			}
				
			
			  
		    mLocationClient=new AMapLocationClient(getActivity());
		    mLocationOption=new AMapLocationClientOption();
		    mLocationClient.setLocationListener(this);
		    mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		    mLocationOption.setInterval(5000);
		    mLocationClient.setLocationOption(mLocationOption);
		    mLocationClient.startLocation();
		    
		     String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/"+(String)BmobUser.getObjectByKey("username")+"头像.png";
		    
             File file=new File(path);
		      if(file.exists()){
				  userPicture.setImageBitmap(BitmapFactory.decodeFile(path));  //这里不优化加载了，这里优化加载很难看
			
				   }else {
					  refreshUserPicture(null, 1);
				   }
            	
            	pic.setImageBitmap(getPicture());
				weather.setText(pre.getString("weatherInfo", ""));
				temper.setText(pre.getString("temperature", ""));
				
				//微信朋友圈分享功能初始化
			
				
		        weatherLinearLayout.setOnTouchListener(new OnTouchListener() {
					
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
						 	myHorizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth,0);
						 	
						 	canScroll=true;
						 	
						 	buttonMes.setBackgroundResource(getButtonResource()[0]);
							buttonMes.setTextColor(Color.parseColor(getButtonColor()));
							
							buttonCon.setBackgroundResource(getButtonResource()[1]);
							buttonCon.setTextColor(Color.parseColor(getButtonColor1()));
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
							
							canScroll=false;
							
							buttonCon.setBackgroundResource(getButtonResource()[0]);
							buttonCon.setTextColor(Color.parseColor(getButtonColor()));
							
							buttonMes.setBackgroundResource(getButtonResource()[1]);
							buttonMes.setTextColor(Color.parseColor(getButtonColor1()));
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
			    
			    design.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 Intent intent=new Intent(getActivity(),designAct.class);
					     startActivity(intent);
						
					}
				});
			    
			    button2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						
						AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
						final AlertDialog alertDialog=builder.create();
						builder.setTitle("退出登录提醒");
						builder.setMessage("确定退出登录吗？");
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								    MyUser.logOut();
								    loginAct.mTencent.logout(context);
									Intent intent=new Intent(getActivity(),loginAct.class);
									startActivity(intent);
									bmobIM.removeMessageListHandler(messageListHandler);
									conversations.clear();
									friends.clear();
									mLocationClient.stopLocation();
									getActivity().finish();
									
								
								  
							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
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
					    	popupWindow=new PopupWindow(customView,Utility.dip2px(context,100), Utility.dip2px(context,300));//dp转px
					    	popupWindow.setAnimationStyle(R.style.AnimationFade);
					    	
					    	Button buttonInvite=(Button)customView.findViewById(R.id.buttonInvite);
					    	Button buttonSearch=(Button)customView.findViewById(R.id.buttonSearch);
					    	Button buttonShare=(Button)customView.findViewById(R.id.buttonShare); 
					    	buttonInvite.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Intent sendIntent = new Intent();
									sendIntent.setAction(Intent.ACTION_SEND);
									sendIntent.putExtra(Intent.EXTRA_TEXT, "这里有个好玩的app，一起来玩吧。http://shouji.baidu.com/software/22466614.html");
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
					    	buttonShare.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Intent intent=new Intent(context,WXEntryActivity.class);
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
        	if(i==1){     //这是当只有联网更新的时候才调用
        		 String touxiangUrl=(String)BmobUser.getObjectByKey("touxiangUrl");
             	
             	   if(touxiangUrl!=null)       //这里防止更新图片后另外一台客户端没有更新
      			{  
      			   BmobFile bmobFile=new BmobFile((String)BmobUser.getObjectByKey("username")+"头像"+".png",null,touxiangUrl);//确定文件名字（头像.png）和网络地址
      			   download.downloadFile(bmobFile,context);//进行下载操作
      			   
      			}
        	}
		   
        }
	
	
   

	  public static void refreshNewFriend(){    //当通过或者对方通过好友的时候刷新联系人列表
		  UserModel.getInstance().queryFriends(new FindListener<Friend>() {

				@Override
				public void done(final List<Friend> list, BmobException e) {
					
					if(e==null||e.getErrorCode()==0){
						friends.clear();
						friends=list;
						
						for (int i = 0; i < list.size(); i++) {
							if(list.get(i).getFriendUser().getObjectId()!=null){
							converdb.saveId(list.get(i).getFriendUser().getObjectId(),1);  //这里存储联系人的id和昵称，然后获取会话的时候就不存储联系人的昵称了，有时候昵称错误，然后这里可以存储isFriend，就此判断是否是朋友
					    	converdb.saveNickById(list.get(i).getFriendUser().getObjectId(), list.get(i).getFriendUser().getNick());
					    	converdb.saveTouXiangById(list.get(i).getFriendUser().getObjectId(), list.get(i).getFriendUser().getTouXiangUrl());
							}
						}
						
						List<Friend> dbfriends=new ArrayList<Friend>();
						dbfriends=converdb.getFriends();

						if(friends.size()>0){
						  for (int i = 0; i <dbfriends.size(); i++) {
							int k=checkLocalAndWebFriend(friends, dbfriends.get(i));
							if(k==2){   //如果k==2表示没有这个朋友
						     if(dbfriends.get(i).getFriendUser().getObjectId()!=null)
							   if(converdb.getIsFriend(dbfriends.get(i).getFriendUser().getObjectId())!=2&&converdb.getIsFriend(dbfriends.get(i).getFriendUser().getObjectId())!=3&&!dbfriends.get(i).getFriendUser().getObjectId().equals("e5be088480"))//朋友系统和黑名单系统是分开的，互不触犯
								   converdb.updateIsFriendI(dbfriends.get(i).getFriendUser().getObjectId(),0);
						    	}
						  }
						}else {     //也要考虑0的情况
							for (int i = 0; i < dbfriends.size(); i++) {
						      if(dbfriends.get(i).getFriendUser().getObjectId()!=null)
						       if(converdb.getIsFriend(dbfriends.get(i).getFriendUser().getObjectId())!=2&&converdb.getIsFriend(dbfriends.get(i).getFriendUser().getObjectId())!=3&&!dbfriends.get(i).getFriendUser().getObjectId().equals("e5be088480"))
								converdb.updateIsFriendI(dbfriends.get(i).getFriendUser().getObjectId(),0);
							}
						 }
					  friends.clear();
					  
					  friends=converdb.getFriends();
					  
					  friendadapter.refresh(friends);
                      friendadapter.notifyDataSetChanged();
                      
					}else if(e.getErrorCode()==9015){
						Toast.makeText(context,"查询朋友有误,请稍后再尝试"+e.getMessage(),Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(context,"查询朋友有误"+e.getMessage()+e.getErrorCode(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
			
	}
	  public static void refreshConversations(int i,final String id){
		    if(i==0){     //接收消息后调用
		    	 converdb.addUnReadNumById(id);
		    }else if(i==1)   { //打开某一个聊天窗口后调用
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
		    
		    int count=getCount();
		    
		    messageadapter.refreshCount(count);
		    messageadapter.notifyDataSetChanged();
		   
	  }
     public static void refreshBlack(int i){
    	 MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
    	 if(i==0||i==2){                             //查看本人的黑名单列表
    	 BmobQuery<Black> bmobQuery=new BmobQuery<Black>();
    	 bmobQuery.addWhereEqualTo("myUser", currentUser);
    	 bmobQuery.findObjects(new FindListener<Black>() {

			@Override
			public void done(List<Black> list, BmobException e) {
				if(e==null){
					if(list.size()>0){
						for (int i = 0; i < list.size(); i++) {
							converdb.saveId(list.get(i).getBlackUser().getObjectId(), 0);
							converdb.updateIsFriendI(list.get(i).getBlackUser().getObjectId(), 2); //拉黑，无论是陌生人，还是朋友
						}
					}
				}else {
					Log.d("Main",e.getMessage());
				}
				
			}
		});
    	
     }
    	 if(i==0||i==1){                       //查看自己是否被某人拉黑
    	 BmobQuery<Black> bmobQuery2=new BmobQuery<Black>();
    	 bmobQuery2.addWhereEqualTo("blackUser", currentUser);
    	 bmobQuery2.findObjects(new FindListener<Black>() {

			@Override
			public void done(List<Black> list, BmobException e) {
				if(e==null){
					if(list.size()>0){
						for (int i = 0; i < list.size(); i++) {
							converdb.saveId(list.get(i).getMyUser().getObjectId(),0);
							converdb.updateIsFriendI(list.get(i).getMyUser().getObjectId(), 3); //表示对方把自己拉黑
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
					String address="http://route.showapi.com/238-2";  //经纬度转化为地址
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
			  if(Webfriends.get(i).getFriendUser().getObjectId()!=null&&friend.getFriendUser().getObjectId()!=null)
			    if(Webfriends.get(i).getFriendUser().getObjectId().equals(friend.getFriendUser().getObjectId())){
			    	  return 1;
			     }
	    	}
		  return 2;
	 }
    private static int getCount(){
    	
    	int count=0;     
		
        if(conversations!=null&&conversations.size()>0){
    
           	 for (int i = 0; i < conversations.size(); i++) {
                     
					 if(conversations.get(i).getMessages().size()>0){
						 if(conversations.get(i).getMessages().get(0).getFromId().equals(weather_info.objectId)||fragmentChat.conversations.get(i).getMessages().get(0).getToId().equals(weather_info.objectId))
							 if(!fragmentChat.conversations.get(i).getMessages().get(0).getFromId().equals(fragmentChat.conversations.get(i).getMessages().get(0).getToId()))
							   if(!fragmentChat.conversations.get(i).getMessages().get(0).getMsgType().equals("decline"))
								   if(fragmentChat.converdb.getIsFriend(fragmentChat.conversations.get(i).getConversationId())!=2)  {
						             
									   count++;   //1、确定真正的有消息，2、确定的是该消息属于本账户，3、是防止自己和自己的对话加入，4、是防止拒绝信息加入，5、拉近黑名单的无需显示在会话列表中,
								
						          }
						
					 }
				}
        
           	 return count;
           }else {
          
					return 0;
				}

    }
    
    
    
    
    
    public static void setButtonColor(String Color0,String Color1){ //Color0是设置点亮的时候颜色，Color1是设置点暗的时候颜色
    	buttonColor=Color0;
    	buttonColor1=Color1;
    	
    	buttonMes.setBackgroundResource(getButtonResource()[0]);
		buttonMes.setTextColor(Color.parseColor(Color0));  
		
		buttonCon.setBackgroundResource(getButtonResource()[1]);
		buttonCon.setTextColor(Color.parseColor(Color1));
    }
    
    private String getButtonColor(){

    	return buttonColor;
    }
    private String getButtonColor1(){
    	return buttonColor1;
    }
    public static void setButtonResource(int [] Resource){  //这个就是设置button的背景色
    	buttonResource[0]=Resource[0];
    	buttonResource[1]=Resource[1];
    }
    
    private static int [] getButtonResource(){
          return buttonResource;
    }
    public static void setNightMode(){
    	userName.setTextColor(Color.parseColor("#A2C0DE"));
		myAccount.setTextColor(Color.parseColor("#A2C0DE"));
		manage.setTextColor(Color.parseColor("#A2C0DE"));
		design.setTextColor(Color.parseColor("#A2C0DE"));
		
		weather.setTextColor(Color.parseColor("#A2C0DE"));
		temper.setTextColor(Color.parseColor("#A2C0DE"));
		
		messageadapter.refreshTextColor(1);
		friendadapter.refreshTextColor(1);
		
    }
    public static void setTextColorBlack(){
    	userName.setTextColor(Color.parseColor("#000000"));
		myAccount.setTextColor(Color.parseColor("#000000"));
		manage.setTextColor(Color.parseColor("#000000"));
		design.setTextColor(Color.parseColor("#000000"));
    }
    public static void setTextColorWhite(){
    	userName.setTextColor(Color.parseColor("#FFFFFF"));
		myAccount.setTextColor(Color.parseColor("#FFFFFF"));
		manage.setTextColor(Color.parseColor("#FFFFFF"));
		design.setTextColor(Color.parseColor("#FFFFFF"));
    }
    
    public static void setFromNightModeToOthers(){   //表示从夜间模式转换其他模式
    	userName.setTextColor(Color.parseColor("#FFFFFF"));
		myAccount.setTextColor(Color.parseColor("#FFFFFF"));
		manage.setTextColor(Color.parseColor("#FFFFFF"));
		design.setTextColor(Color.parseColor("#FFFFFF"));
		
		weather.setTextColor(Color.parseColor("#FFFFFF"));
		temper.setTextColor(Color.parseColor("#FFFFFF"));
		
    	messageadapter.refreshTextColor(0);
    	friendadapter.refreshTextColor(0);
    }
    
	
}
