package activity;

import Util.CommonUtils;
import Util.FaceTextUtils;
import Util.Utility;
import adapter.ChatAdapter;
import adapter.EmoViewPagerAdapter;
import adapter.EmoteAdapter;
import adapter.NewRecordPlayClickListener;
import adapter.OnRecyclerViewListener;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import butterknife.OnClick;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Black;
import model.Friend;
import model.Jubao;
import model.UserModel;
import myCustomView.EmoticonsEditText;


import com.amap.api.mapcore2d.di;
import com.amap.api.services.a.br;
import com.koushikdutta.async.Util;
import com.sharefriend.app.R;


import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMFileMessage;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.core.a.b;
import cn.bmob.newim.db.BmobIMDBManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.push.config.Constant;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import db.FaceText;

/**聊天界面
 * @author :smile
 * @project:ChatActivity
 * @date :2016-01-25-18:23
 */
public class ChatActivity extends baseActivity implements ObseverListener,MessageListHandler{

	LinearLayout ll_chat;

    SwipeRefreshLayout sw_refresh;

    RecyclerView rc_view;
    EmoticonsEditText edit_msg;

    Button btn_chat_add;
    Button btn_chat_emo;

    Button btn_speak;

    Button btn_chat_voice;

    Button btn_chat_keyboard;
    
    Button btn_chat_keyboard1;

    Button btn_chat_send;


    LinearLayout layout_more;
     
    String path;
    
    LinearLayout layout_add;
    LinearLayout layout_emo;

    // 语音有关

    RelativeLayout layout_record;
 
    TextView tv_voice_tips;

    ImageView iv_record;
    TextView tv_picture;
    TextView tv_camera;
    TextView tv_video;
    
    ViewPager emoPager;
    TextView talkpartername;

   
    private Drawable[] drawable_Anims;// 话筒动画
    BmobRecordManager recordManager;
    
    ChatAdapter adapter;
    protected LinearLayoutManager layoutManager;
    BmobIMConversation c;
    
    ImageView threeCircle;
    ImageView user89;   
    
    PopupWindow popupWindow;
    BmobIMUserInfo userInfo;
    
    boolean isKeFu=false;
    boolean once1=true;   //对应着回复“您好”
    boolean once2=true;   //对应着回复“后期大力推广”
    
    public static MyUser myUser;
    class VoiceTouchListener implements View.OnTouchListener {
        @Override   
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.checkSdCard()) {
                        Toast.makeText(ChatActivity.this, "发送语音需要sdcard支持！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(c.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                            	  if(fragmentChat.converdb.getIsFriend(c.getConversationId())!=3)
                            		  sendVoiceMessage(recordManager.getRecordFilePath(c.getConversationId()),recordTime);
                    			  else {
                    				 Toast.makeText(ChatActivity.this,"对方拒绝接收", Toast.LENGTH_SHORT).show();
                    			    }
                    			
                             } else {// 录音时间过短，则提示录音过短的提示
                                layout_record.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }


 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        edit_msg=(EmoticonsEditText)findViewById(R.id.edit_user_comment);
        ll_chat=(LinearLayout)findViewById(R.id.ll_chat);
        sw_refresh=(SwipeRefreshLayout)findViewById(R.id.sw_refresh);
        rc_view=(RecyclerView)findViewById(R.id.rc_view);
        btn_chat_add=(Button)findViewById(R.id.btn_chat_add);
        btn_chat_emo=(Button)findViewById(R.id.btn_chat_emo);
        btn_speak=(Button)findViewById(R.id.btn_speak);
        btn_chat_voice=(Button)findViewById(R.id.btn_chat_voice);
        btn_chat_keyboard=(Button)findViewById(R.id.btn_chat_keyboard);
        btn_chat_keyboard1=(Button)findViewById(R.id.btn_chat_keyboard1);
        btn_chat_send=(Button)findViewById(R.id.btn_chat_send);
        layout_more=(LinearLayout)findViewById(R.id.layout_more);
        layout_add=(LinearLayout)findViewById(R.id.layout_add);
        
        layout_emo=(LinearLayout)findViewById(R.id.layout_emo);
        layout_record=(RelativeLayout)findViewById(R.id.layout_record);
        tv_voice_tips=(TextView)findViewById(R.id.tv_voice_tips);
        iv_record=(ImageView)findViewById(R.id.iv_record);
        tv_picture=(TextView)findViewById(R.id.tv_picture);
        tv_camera=(TextView)findViewById(R.id.tv_camera);
        emoPager=(ViewPager)findViewById(R.id.pager_emo);
        talkpartername=(TextView)findViewById(R.id.talkpartername);
        
        user89=(ImageView)findViewById(R.id.user89);  //对方用户头标
        threeCircle=(ImageView)findViewById(R.id.threeCircle);
        
        int designNum=fragmentChat.pre.getInt("design", 0);
		
		if(designNum==4){
			ll_chat.setBackgroundColor(Color.parseColor("#051C3D"));
		    talkpartername.setTextColor(Color.parseColor("#A2C0DE"));
		}
        
        
        c= BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getBundleExtra("bundle").getSerializable("c"));
        
        userInfo=(BmobIMUserInfo)getIntent().getBundleExtra("bundle").getSerializable("userInfo");   
        if(fragmentChat.converdb.getIsFriend(userInfo.getUserId())==0)
             talkpartername.setText(userInfo.getName()+"  (陌生人)");
        else {
        	talkpartername.setText(userInfo.getName());
		}
        
        tv_video=(TextView)findViewById(R.id.tv_video);
        
        myUser=new MyUser();
        myUser.setNick(userInfo.getName());
        myUser.setTouXiangUrl(userInfo.getAvatar());
        myUser.setObjectId(userInfo.getUserId());
        
        if(myUser.getObjectId().equals("e5be088480")){
            edit_msg.setHint("您有什么问题或需要，尽情写在这里吧");
            tv_video.setVisibility(View.GONE);
            threeCircle.setVisibility(View.INVISIBLE);
            isKeFu=true;
         }
            
        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
        query.getObject(userInfo.getUserId(), new QueryListener<MyUser>() {
    		
    		@Override
    		public void done(MyUser user, BmobException e ) {
    		      if(e==null){
    		    	  myUser.setNick(user.getNick());
    		    	  myUser.setTouXiangUrl(user.getTouXiangUrl());
    		    	  myUser.setSex(user.getSex());
    		    	  myUser.setAge(user.getAge());
    		    	  myUser.setShengri(user.getShengri());
    		    	  myUser.setZhiye(user.getZhiye());
    		    	  myUser.setSchool(user.getSchool());
    		    	  myUser.setSuozaidi(user.getSuozaidi());
    		    	  myUser.setGuxiang(user.getGuxiang());
    		    	  myUser.setConstellation(user.getConstellation());

    		      }else{
    		    	  Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    		      }
    			
    		}
    	});
        fragmentChat.refreshBlack(1);  //只查询对方是否把自己加入黑名单
        
      // initNaviView();
        initEmoView();
        initSwipeLayout();
        initVoiceView();   
        initBottomView();
        
    
        edit_msg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 if (layout_more.getVisibility() == View.VISIBLE) {
			            layout_add.setVisibility(View.GONE);
			            layout_emo.setVisibility(View.GONE);
			            layout_more.setVisibility(View.GONE);
			        }
				
			}
		});
        
        btn_chat_emo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn_chat_keyboard1.setVisibility(View.GONE);
				 if (layout_more.getVisibility() == View.GONE) {
			            showEditState(true);
			        } else {
			            if (layout_add.getVisibility() == View.VISIBLE) {
			                layout_add.setVisibility(View.GONE);
			                layout_emo.setVisibility(View.VISIBLE);
			            } else {
			                layout_more.setVisibility(View.GONE);
			            }
			        }
			}
		});
        
        btn_chat_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edit_msg.setVisibility(View.VISIBLE);
				btn_speak.setVisibility(View.GONE);
				  if (layout_more.getVisibility() == View.GONE) {
			            layout_more.setVisibility(View.VISIBLE);
			            layout_add.setVisibility(View.VISIBLE);
			            layout_emo.setVisibility(View.GONE);
			            hideSoftInputView();
			        } else {
			            if (layout_emo.getVisibility() == View.VISIBLE) {
			                layout_emo.setVisibility(View.GONE);
			                layout_add.setVisibility(View.VISIBLE);
			            } else {
			                layout_more.setVisibility(View.GONE);
			            }
			        }
			}
		});
        btn_chat_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				    edit_msg.setVisibility(View.GONE);
			        layout_more.setVisibility(View.GONE);
			        btn_chat_voice.setVisibility(View.GONE);
			        btn_chat_keyboard1.setVisibility(View.VISIBLE);
			        btn_speak.setVisibility(View.VISIBLE);
			        hideSoftInputView();
			}
		});
        btn_chat_keyboard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 showEditState(false);
				 btn_chat_emo.setVisibility(View.VISIBLE);
			}
		});
        btn_chat_keyboard1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 showEditState(false);
				 btn_chat_keyboard1.setVisibility(View.GONE);
				 
			}
		});
        
        btn_chat_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			  if(fragmentChat.converdb.getIsFriend(c.getConversationId())!=3){
				 sendMessage();
				
			  }
			  else {
				 Toast.makeText(ChatActivity.this,"对方拒绝接收", Toast.LENGTH_SHORT).show();
			    }
				
			}
		});
        tv_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 if(fragmentChat.converdb.getIsFriend(c.getConversationId())!=3)
					 sendLocalImageMessage();
				  else {
					 Toast.makeText(ChatActivity.this,"对方拒绝接收", Toast.LENGTH_SHORT).show();
				    }
				
			}
		});
        tv_camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 if(fragmentChat.converdb.getIsFriend(c.getConversationId())!=3)
					 openTakePhoto();
				  else {
					 Toast.makeText(ChatActivity.this,"对方拒绝接收", Toast.LENGTH_SHORT).show();
				    }
				
				
			}
		});
        tv_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 if(fragmentChat.converdb.getIsFriend(c.getConversationId())!=3){
					  AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
			     final String[] xuanzeweizhi={"打开摄像头","本地视频"};
			     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						  if(which==0){
							  Toast.makeText(ChatActivity.this,"请稍等", Toast.LENGTH_SHORT).show();
						      Intent intent=new Intent();
						      intent.setAction("android.media.action.VIDEO_CAPTURE");
						      intent.addCategory("android.intent.category.DEFAULT");
						   
                              startActivityForResult(intent, 4);
					    	 
						  }else if(which==1){
								Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						        intent.setType("video/*");
						        startActivityForResult(intent,4);
						        
						   }
					}  
				});
			     builder.show();
				 }  
				 else {
					 Toast.makeText(ChatActivity.this,"对方拒绝接收", Toast.LENGTH_SHORT).show();
				    }
				
				
				
			}
		});
        
        user89.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   Bundle bundle=new Bundle();

		    	   bundle.putSerializable("myUser", myUser);
		    	   Intent intent=new Intent(ChatActivity.this,xiangxiDataAct.class);
		    	   intent.putExtra("bundle", bundle);
		    	   startActivity(intent);
				
			}
		});
        
       threeCircle.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(popupWindow!=null&&popupWindow.isShowing()){
				popupWindow.dismiss();
				
			}else {
				initmPopupWindowView();
				popupWindow.showAsDropDown(v,0,5);
			}
			
		}
	});
        
    }
    List<FaceText> emos;

    private void initmPopupWindowView(){
    	View customView=getLayoutInflater().inflate(R.layout.popview_item, null,false);
    	popupWindow=new PopupWindow(customView,Utility.dip2px(ChatActivity.this,100), Utility.dip2px(ChatActivity.this,300));//dp转px
    	popupWindow.setAnimationStyle(R.style.AnimationFade);
    	
    	Button buttonQingLiao=(Button)customView.findViewById(R.id.buttonQingLiao);
    	Button buttonJuBao=(Button)customView.findViewById(R.id.buttonJuBao);
    	Button buttonDelete=(Button)customView.findViewById(R.id.buttonDelete);
    	 if(fragmentChat.converdb.getIsFriend(userInfo.getUserId())==0)
              buttonDelete.setVisibility(View.GONE);
      
    	Button buttonLaHei=(Button)customView.findViewById(R.id.buttonLaHei);
    	buttonQingLiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
				final AlertDialog alertdialog=builder.create();
				builder.setTitle("删除提醒");
				builder.setMessage("确定要删除该对话全部聊天记录吗?");
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(alertdialog.isShowing()){
							alertdialog.dismiss();
						}
						
					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						for (int i = 0; i <adapter.getMessages().size(); i++) {
							c.deleteMessage(adapter.getMessages().get(i));
						}
						
						BmobIM.getInstance().deleteConversation(c.getConversationId());
						
						adapter.getMessages().clear();
						adapter.notifyDataSetChanged();
						
						fragmentChat.converdb.saveNewContentById(c.getConversationId(),"");
						fragmentChat.converdb.clearUnReadNumById(c.getConversationId());
						fragmentChat.converdb.saveTimeById(c.getConversationId(), 0);
						
						if(fragmentChat.converdb.getIsFriend(c.getConversationId())==0){//如果是陌生人的话就直接删除
					         fragmentChat.converdb.deleteCoversationById(c.getConversationId());
				        }
						
						
						fragmentChat.refreshConversations(2,c.getConversationId());
						
					}

				
				});
				builder.show();
				
				
			}
		});
    	buttonDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
				final AlertDialog alertdialog=builder.create();
				builder.setTitle("删除提醒");
				builder.setMessage("确定要删除该联系人吗?");
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(alertdialog.isShowing()){
							alertdialog.dismiss();
						}
						
					}
				});
				builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BmobQuery<Friend> query1=new BmobQuery<Friend>();
						query1.addWhereEqualTo("friendUser",userInfo.getUserId());
						BmobQuery<Friend> query2=new BmobQuery<Friend>();
						query2.addWhereEqualTo("myUser",(String)MyUser.getCurrentUser().getObjectId());
						List<BmobQuery<Friend>> andQuery1=new ArrayList<BmobQuery<Friend>>();
						andQuery1.add(query1);
						andQuery1.add(query2);
						BmobQuery<Friend> andQuery=new BmobQuery<Friend>();
						andQuery.and(andQuery1);     //第一个列表要满足相互与条件
					
						
						BmobQuery<Friend> query3=new BmobQuery<Friend>();
						query3.addWhereEqualTo("friendUser",(String)MyUser.getCurrentUser().getObjectId());
						BmobQuery<Friend> query4=new BmobQuery<Friend>();
						query4.addWhereEqualTo("myUser",userInfo.getUserId());
						List<BmobQuery<Friend>> andQuerys2=new ArrayList<BmobQuery<Friend>>();
						andQuerys2.add(query3);
						andQuerys2.add(query4);
						BmobQuery<Friend> andBmobQuery=new BmobQuery<Friend>();
						andBmobQuery.and(andQuerys2);
					
						List<BmobQuery<Friend>> mainList=new ArrayList<BmobQuery<Friend>>();
						mainList.add(andQuery);
						mainList.add(andBmobQuery);
						BmobQuery<Friend> mainQuery=new BmobQuery<Friend>();
						mainQuery.or(mainList);
						mainQuery.findObjects(new FindListener<Friend>() {
							
							@Override
						public void done(List<Friend> object, BmobException e) {
							if(e==null){
								List<BmobObject> friends=new ArrayList<BmobObject>();
								for (int i = 0; i <object.size(); i++) {
									   Friend friend=new Friend();
									   friend.setObjectId(object.get(i).getObjectId());
									   Friend friend2=new Friend();
									   friend2.setObjectId(object.get(i).getObjectId());
									   friends.add(friend);
									   friends.add(friend2);
									}
								
								if(popupWindow!=null&&popupWindow.isShowing())
									popupWindow.dismiss();
								
								UserModel.getInstance().deleteFriends(ChatActivity.this, friends,c,adapter);
								
								
								
								}else {
									Toast.makeText(ChatActivity.this,"删除失败,"+e.getMessage(), Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					}
					});
				builder.show();
				
				
				
			}
		});
    	buttonJuBao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
				   final AlertDialog dialog1=builder.create();
				     final String[] xuanzeweizhi={"发布暴力色情","严重骚扰","违反政治和法规","取消"};
				     builder.setTitle("请选择举报原因");
				     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 if(which!=3){
						     juBao(which);
							 }else if(which==3&&dialog1!=null&&dialog1.isShowing()){
								   dialog1.dismiss();
						   	}
						}});
				     builder.show();
				
			}
		});
    	buttonLaHei.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
				final AlertDialog alertdialog=builder.create();
				builder.setTitle("拉黑提醒");
				builder.setMessage("确定要拉黑对方吗?");
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(alertdialog.isShowing()){
							alertdialog.dismiss();
						}
						
					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						fragmentChat.converdb.clearUnReadNumById(c.getConversationId());
					    fragmentChat.converdb.updateIsFriendI(c.getConversationId(), 2);
						Black black=new Black();
						MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
						black.setMyUser(currentUser);
					    black.setBlackUser(myUser);
						black.save(new SaveListener<String>() {

							@Override
							public void done(String a, BmobException e) {
								if(e==null){
									if(popupWindow!=null&&popupWindow.isShowing())
										popupWindow.dismiss();
									finish();
								}else {
									Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
								}
								
							}
						});
                        fragmentChat.refreshNewFriend();
					    fragmentChat.refreshConversations(2,c.getConversationId());
					
					}

				
				});
				builder.show();
				
			}
		});
    }
    
    
    
    
    
    private void juBao(final int i){
      	     BmobQuery<Jubao> bmobQuery=new BmobQuery<Jubao>();
		 	 bmobQuery.addWhereEqualTo("myUser", myUser);
			 bmobQuery.findObjects(new FindListener<Jubao>() {

			@Override
			public void done(List<Jubao> list,BmobException e) {
	        if(e==null){
	        	Integer t=0;
			 if(list.size()>0){
				   switch (i) {
				case 0:
					t=list.get(0).getSex();
					break;
				case 1:
					t=list.get(0).getSaorao();
                    break;
				case 2:
					t=list.get(0).getAnticountry();
					break;
				default:
					break;
				}    
				   
				   if(t==null){
					   t=0;
				   }
				   
			  	     Integer sum=list.get(0).getSum();
			  	     
			  	     if(sum==null)
			  	    	 sum=0;
					 Jubao jubao=new Jubao();
					 
			     switch (i) {
						case 0:
							jubao.setSex(t+1);
							break;
						case 1:
							jubao.setSaorao(t+1);
		                    break;
						case 2:
							jubao.setAnticountry(t+1);
							break;
						default:
							break;
						}

					 jubao.setSum(sum+1);
					 jubao.update(list.get(0).getObjectId(),new UpdateListener() {
															
						@Override
					public void done(BmobException e) {
							if(e==null){
							 Toast.makeText(ChatActivity.this,"举报成功", Toast.LENGTH_SHORT).show();
							}else {
							 Toast.makeText(ChatActivity.this,"举报失败,"+e.getMessage(), Toast.LENGTH_SHORT).show();
								}
																
					}
								});
			}else {
						Jubao jubao=new Jubao();
						
						   switch (i) {
							case 0:
								jubao.setSex(1);
								break;
							case 1:
								jubao.setSaorao(1);
			                    break;
							case 2:
								jubao.setAnticountry(1);
								break;
							default:
								break;
							}

						jubao.setSum(1);
						jubao.setMyUser(myUser);
						jubao.save(new SaveListener<String>() {

							@Override
							public void done(String a,
									BmobException e) {
								if(e==null){
									 Toast.makeText(ChatActivity.this,"举报成功", Toast.LENGTH_SHORT).show();
								}else {
									 Toast.makeText(ChatActivity.this,"举报失败,"+e.getMessage(), Toast.LENGTH_SHORT).show();
							  	}
								
							}
							
							
							
						});
					}
												
			}else {
				 Toast.makeText(ChatActivity.this,"举报失败,"+e.getMessage(), Toast.LENGTH_SHORT).show();
		    	}
			}});
				
    }
    
    private void initEmoView() {
		emoPager = (ViewPager) findViewById(R.id.pager_emo);  //emoPager实例化
		emos = FaceTextUtils.faceTexts;   //包含有所有表情相应的字符定义

		List<View> views = new ArrayList<View>();
		for (int i = 0; i < 2; ++i) {
			views.add(getGridView(i));
		}
		emoPager.setAdapter(new EmoViewPagerAdapter(views));
	}
    
    private View getGridView(final int i) {
		View view = View.inflate(this, R.layout.include_emo_gridview, null);
		GridView gridview = (GridView) view.findViewById(R.id.gridview);
		List<FaceText> list = new ArrayList<FaceText>();
		if (i == 0) {
			list.addAll(emos.subList(0, 21));
		} else if (i == 1) {
			list.addAll(emos.subList(21, emos.size()));
		}
		final EmoteAdapter gridAdapter = new EmoteAdapter(ChatActivity.this,
				list);
		gridview.setAdapter(gridAdapter);  
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				FaceText name = (FaceText) gridAdapter.getItem(position);
				String key = name.text.toString();
				try {
					if (edit_msg != null && !TextUtils.isEmpty(key)) {
						int start = edit_msg.getSelectionStart();
						CharSequence content = edit_msg.getText()
								.insert(start, key);
						edit_msg.setText(content);
						// 定位光标位置
						CharSequence info = edit_msg.getText();
						if (info instanceof Spannable) {
							Spannable spanText = (Spannable) info;
							Selection.setSelection(spanText,
									start + key.length());
						}
					}
				} catch (Exception e) {

				}

			}
		});
		return view;
	}
    
    
    private void openTakePhoto(){    
    	 /**
    	 * 在启动拍照之前最好先判断一下sdcard是否可用
    	 */
    	 String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
    	 if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
    	  Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	  
    	  SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
    	  Date curDate=new Date(System.currentTimeMillis());
    	  String str=format.format(curDate);
    	  
    	  path=Environment.getExternalStorageDirectory().getAbsoluteFile()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/picture/"+str+".jpg";
    	  Uri uri=Uri.fromFile(new File(path));
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    	  startActivityForResult(intent,3);
    	 }else {
    	  Toast.makeText(ChatActivity.this,"sdcard不可用",Toast.LENGTH_SHORT).show();
    	 }
    	}
    private void initSwipeLayout(){
        sw_refresh.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this,c);
        rc_view.setAdapter(adapter);
        ll_chat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_chat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });
        //下拉加载
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });
        //设置RecyclerView的点击事件
        
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                 
            }

            @Override
            public boolean onItemLongClick(int position) {
                //这里省了个懒，直接长按就删除了该消息
            	  if(position==adapter.getCount()-1){
                 	 if(position==0){
                 		 fragmentChat.converdb.saveNewContentById(userInfo.getUserId(), "");
                 	 }else {
                 		 String content="";
 	  		             String id=c.getConversationId();
 						    if(adapter.getItem(position-1).getMsgType().equals(BmobIMMessageType.TEXT.getType()))
 							    content=adapter.getItem(position-1).getContent();
 							else if(adapter.getItem(position-1).getMsgType().equals(BmobIMMessageType.IMAGE.getType()))
 			                        content="[图片]";
 							else if(adapter.getItem(position-1).getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
 								content="[视频]";
 							else if(adapter.getItem(position-1).getMsgType().equals(BmobIMMessageType.VOICE.getType()))
 								content="[语音]";
 							fragmentChat.converdb.saveNewContentById(id, content);
 							fragmentChat.converdb.saveTimeById(id,adapter.getItem(position-1).getCreateTime());
 					    	fragmentChat.refreshConversations(1,c.getConversationId());
 					  }
                 }
            	 c.deleteMessage(adapter.getItem(position));
                
                adapter.remove(position);
                
                return true;
            }
        });
    }

    private void initBottomView(){
        edit_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_UP){
                    scrollToBottom();
                }
                return false;
            }
        });
        edit_msg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_add.setVisibility(View.GONE);
                } else {
                  
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                        btn_chat_add.setVisibility(View.VISIBLE);
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 初始化语音布局
     * @param
     * @return void
     */
    private void initVoiceView() {
        btn_speak.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }

    /**
     * 初始化语音动画资源
     * @Title: initVoiceAnimRes
     * @param
     * @return void
     */
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[] {
                getResources().getDrawable(R.drawable.chat_icon_voice2),
                getResources().getDrawable(R.drawable.chat_icon_voice3),
                getResources().getDrawable(R.drawable.chat_icon_voice4),
                getResources().getDrawable(R.drawable.chat_icon_voice5),
                getResources().getDrawable(R.drawable.chat_icon_voice6) };
    }

    private void initRecordManager(){
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

            @Override
            public void onVolumnChanged(int value) {
                iv_record.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                Log.i("voice", "已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    btn_speak.setPressed(false);
                    btn_speak.setClickable(false);
                    // 取消录音框
                    layout_record.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    if(fragmentChat.converdb.getIsFriend(c.getConversationId())!=3)
                    	 sendVoiceMessage(localPath, recordTime);
       			  else {
       				 Toast.makeText(ChatActivity.this,"对方拒绝接收", Toast.LENGTH_SHORT).show();
       			    }
       			
                   
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btn_speak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    /**
     * 长按说话
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    
    Toast toast;

    /**
     * 显示录音时间过短的Toast
     * @Title: showShortToast
     * @return void
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }


    /**
     * 根据是否点击笑脸来显示文本输入框的状态
     * @param  isEmo 用于区分文字和表情
     * @return void
     */
    private void showEditState(boolean isEmo) {
        edit_msg.setVisibility(View.VISIBLE);
        btn_chat_keyboard.setVisibility(View.GONE);
        btn_chat_voice.setVisibility(View.VISIBLE);
        btn_speak.setVisibility(View.GONE);
        edit_msg.requestFocus();
        if (isEmo) {
            layout_more.setVisibility(View.VISIBLE);
            layout_more.setVisibility(View.VISIBLE);
            layout_emo.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            layout_more.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(edit_msg, 0);
        }
    }

    /**
     * 发送文本消息
     */
    private void sendMessage(){
        String text=edit_msg.getText().toString();
        if(TextUtils.isEmpty(text.trim())){
            Toast.makeText(ChatActivity.this, "请输入内容",Toast.LENGTH_SHORT).show();
            return;
        }
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(text);
        //可设置额外信息
        
  //      Map<String,Object> map =new HashMap<String, Object>();
    //    map.put("level",talkpartername);//随意增加信息
      //  msg.setExtraMap(map);
        
        c.sendMessage(msg, listener);
        fragmentChat.converdb.saveNewContentById(c.getConversationId(),text);
        fragmentChat.converdb.saveTimeById(c.getConversationId(),msg.getCreateTime());
        
        if(isKeFu){
        	zidonghuifu(text);
        }
    }

    /**
     * 直接发送远程图片地址
     */
    public void sendRemoteImageMessage(){
        BmobIMImageMessage image =new BmobIMImageMessage();
        image.setRemoteUrl("http://img.lakalaec.com/ad/57ab6dc2-43f2-4087-81e2-b5ab5681642d.jpg");
        c.sendMessage(image, listener);
    }

    /**
     * 发送本地图片地址
     */
    public void sendLocalImageMessage(){
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        startActivityForResult(intent, 2);
        
    }

    @Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data) {
	     switch (requestCode) {
		case 2:          //这里2是取照片后返回
			if(resultCode==RESULT_OK){
				Uri uri=data.getData();
				String path=weather_info.getPath(ChatActivity.this, uri);
				BmobIMImageMessage image =new BmobIMImageMessage(path);
			    c.sendMessage(image, listener);
			    fragmentChat.converdb.saveNewContentById(c.getConversationId(),"[图片]");
			    fragmentChat.converdb.saveTimeById(c.getConversationId(),image.getCreateTime());
			}
			break;
		case 3:     //这里3是拍照后返回
			if(resultCode==RESULT_OK){
				File file=new File(path);
				if(file.exists()){
					 BmobIMImageMessage image =new BmobIMImageMessage(path);
				     c.sendMessage(image, listener);
				     
					 fragmentChat.converdb.saveNewContentById(c.getConversationId(),"[图片]");
					 fragmentChat.converdb.saveTimeById(c.getConversationId(),image.getCreateTime());
				}
			}
			break;
		case 4:      //这里4是拍摄视频和取本地视频后返回
			if(resultCode==RESULT_OK){
	
				Uri uri=data.getData();
				String path=weather_info.getPath(ChatActivity.this, uri);
				 File file=new File(path);
				if(file.exists()){
					BmobIMVideoMessage videoMessage=new BmobIMVideoMessage(file);
					c.sendMessage(videoMessage,listener);
					
				    fragmentChat.converdb.saveNewContentById(c.getConversationId(),"[视频]");
				    fragmentChat.converdb.saveTimeById(c.getConversationId(),videoMessage.getCreateTime());
				}
			}
			break;
		default:
			break;
		}
	}

	/**
     * 发送语音消息
     * @Title: sendVoiceMessage
     * @param  local
     * @param  length
     * @return void
     */
    private void sendVoiceMessage(String local, int length) {
        BmobIMAudioMessage audio =new BmobIMAudioMessage(local);
        //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
     //   Map<String,Object> map =new HashMap<String, Object>();
       // map.put("from", "优酷");
      //  audio.setExtraMap(map);
        //设置语音文件时长：可选
//        audio.setDuration(length);
        c.sendMessage(audio, listener);
	    fragmentChat.converdb.saveNewContentById(c.getConversationId(),"[语音]");
	    fragmentChat.converdb.saveTimeById(c.getConversationId(),audio.getCreateTime());
    }

    /**
     * 发送视频文件
     */

    /**
     * 发送地理位置
     */
//    public void sendLocationMessage(){
        //测试数据，真实数据需要从地图SDK中获取
  //      BmobIMLocationMessage location =new BmobIMLocationMessage("广州番禺区",23.5,112.0);
    //    Map<String,Object> map =new HashMap<String, Object>();
      //  map.put("from", "百度地图");
       // location.setExtraMap(map);
     //   c.sendMessage(location, listener);
   // }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener =new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Log.i("Main","onProgress："+value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            edit_msg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            edit_msg.setText("");
            scrollToBottom();
            if (e != null) {
                Toast.makeText(ChatActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }else {
            	Log.d("Main", "发送成功");
            }
        }
    };

    /**首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg){
        c.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                sw_refresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    Toast.makeText(ChatActivity.this, e.getMessage() + "(" + e.getErrorCode() + ")",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
    	
        Log.i("Main","聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i=0;i<list.size();i++){
           if(!(list.get(i).getMessage().getFromId()).equals(list.get(i).getMessage().getToId()))
               addMessage2Chat(list.get(i));
        }
    }

//    /**接收到聊天消息
//     * @param event
//     */
//    @Subscribe
//    public void onEventMainThread(MessageEvent event){
//        addMessage2Chat(event);
//    }
//
//    @Subscribe
//    public void onEventMainThread(OfflineMessageEvent event){
//        Map<String,List<MessageEvent>> map =event.getEventMap();
//        if(map!=null&&map.size()>0){
//            //只获取当前聊天对象的离线消息
//            List<MessageEvent> list = map.get(c.getConversationId());
//            if(list!=null && list.size()>0){
//                for (int i=0;i<list.size();i++){
//                    addMessage2Chat(list.get(i));
//                }
//            }
//        }
//    }

    /**添加消息到聊天界面中
     * @param event
     */
    private void addMessage2Chat(MessageEvent event){
        BmobIMMessage msg =event.getMessage();
        if(c!=null && event!=null && c.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()){//并且不为暂态消息
            if(adapter.findPosition(msg)<0){//如果未添加到界面中
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                c.updateReceiveStatus(msg);

            }
            scrollToBottom();
        }else{
            Log.i("Main","不是与当前聊天对象的消息");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layout_more.getVisibility() == View.VISIBLE) {
                layout_more.setVisibility(View.GONE);
                return false;
            }else if(popupWindow!=null&&popupWindow.isShowing()) {
            	popupWindow.dismiss();
                return false;	
              }else {
            	return super.onKeyDown(keyCode, event);
			}
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage(){
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if(cache.size()>0){
            int size =cache.size();
            for(int i=0;i<size;i++){
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
        if(recordManager!=null){
            recordManager.clear();
        }
        //更新此会话的所有消息为已读状态
        if(c!=null){
            c.updateLocalCache();
        }
        hideSoftInputView();
        
        if(NewRecordPlayClickListener.currentPlayListener!=null)
          NewRecordPlayClickListener.currentPlayListener.stopPlayRecord();
        
        fragmentChat.refreshConversations(1,c.getConversationId());
        
        super.onDestroy();
    }
    private void zidonghuifu(String text){
    	if(once1){
    		BmobIMDBManager dbManager=BmobIMDBManager.getInstance(weather_info.objectId);
    				
    		BmobIMMessage message=new BmobIMMessage();
    		message.setFromId("e5be088480");
    		message.setContent("在的，您好");
    		message.setCreateTime(System.currentTimeMillis());
    		message.setUpdateTime(System.currentTimeMillis());
    		message.setConversationId("e5be088480");
    		message.setIsTransient(false);
    		message.setMsgType(BmobIMMessageType.TEXT.getType());
    	    message.setSendStatus(4);
    	    message.setReceiveStatus(0);
    	    message.setToId(weather_info.objectId);
    	    message.setConversationType(0);
            message.setBmobIMConversation(c);
           
            dbManager.insertOrReplaceConversation(c);
    	    
    	    dbManager.insertOrUpdateMessage(message);
    		
    		 fragmentChat.converdb.saveNewContentById("e5be088480",message.getContent());
    	     fragmentChat.converdb.saveTimeById("e5be088480", message.getCreateTime());
    	      
    	     if(adapter.findPosition(message)<0){//如果未添加到界面中
                 adapter.addMessage(message);
                 //更新该会话下面的已读状态
                 c.updateReceiveStatus(message);

             }
    	     
             scrollToBottom();
    	   
             fragmentChat.refreshConversations(0,"e5be088480");
             
             once1=false;
    	}else if(text.contains("没人玩")&&once2){
    		BmobIMDBManager dbManager=BmobIMDBManager.getInstance(weather_info.objectId);
			
    		BmobIMMessage message=new BmobIMMessage();
    		message.setFromId("e5be088480");
    		message.setContent("现在确实刚推出来，玩的人少，后期更成熟的时候会大力宣传，玩的人越来越多，真是不好意思");
    		message.setCreateTime(System.currentTimeMillis());
    		message.setUpdateTime(System.currentTimeMillis());
    		message.setConversationId("e5be088480");
    		message.setIsTransient(false);
    		message.setMsgType(BmobIMMessageType.TEXT.getType());
    	    message.setSendStatus(4);
    	    message.setReceiveStatus(0);
    	    message.setToId(weather_info.objectId);
    	    message.setConversationType(0);
            message.setBmobIMConversation(c);
           
            dbManager.insertOrReplaceConversation(c);
    	    
    	    dbManager.insertOrUpdateMessage(message);
    		
    		 fragmentChat.converdb.saveNewContentById("e5be088480",message.getContent());
    	     fragmentChat.converdb.saveTimeById("e5be088480", message.getCreateTime());
    	      
    	     if(adapter.findPosition(message)<0){//如果未添加到界面中
                 adapter.addMessage(message);
                 //更新该会话下面的已读状态
                 c.updateReceiveStatus(message);

             }
    	     
             scrollToBottom();
    	   
             fragmentChat.refreshConversations(0,"e5be088480");
             
             BmobIMMessage message1=new BmobIMMessage();
             message1.setFromId("e5be088480");
     	 	message1.setContent("谢谢您的支持与理解");
     		message1.setCreateTime(System.currentTimeMillis());
     		message1.setUpdateTime(System.currentTimeMillis());
     		message1.setConversationId("e5be088480");
     		message1.setIsTransient(false);
     		message1.setMsgType(BmobIMMessageType.TEXT.getType());
     	    message1.setSendStatus(4);
     	    message1.setReceiveStatus(0);
     	    message1.setToId(weather_info.objectId);
     	    message1.setConversationType(0);
             message1.setBmobIMConversation(c);
            
             dbManager.insertOrReplaceConversation(c);
     	    
     	    dbManager.insertOrUpdateMessage(message1);
     		
     		 fragmentChat.converdb.saveNewContentById("e5be088480",message1.getContent());
     	     fragmentChat.converdb.saveTimeById("e5be088480", message1.getCreateTime());
     	      
     	     if(adapter.findPosition(message1)<0){//如果未添加到界面中
                  adapter.addMessage(message1);
                  //更新该会话下面的已读状态
                  c.updateReceiveStatus(message1);

              }
     	     
              scrollToBottom();
     	   
              fragmentChat.refreshConversations(0,"e5be088480");
             
              once2=false;
    	}
    }

}
