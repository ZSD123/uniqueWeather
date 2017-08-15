package message;

import java.util.List;
import java.util.Map;

import model.Friend;
import model.NewFriend;
import model.NewFriendManager;
import model.UserModel;
import activity.MyUser;
import activity.fragmentChat;
import activity.newFriendActivity;
import activity.weather_info;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class myMessageHandler extends BmobIMMessageHandler {
    private NewFriendManager newFriendManager;
    private Context mContext;
    public myMessageHandler(Context context){
    	mContext=context;
    }
	@Override
	public void onMessageReceive(MessageEvent event) {
		super.onMessageReceive(event);
		if(MyUser.getCurrentUser()!=null)
		     processMessage(event,0);
	}

	@Override
	public void onOfflineReceive(OfflineMessageEvent event) {
		super.onOfflineReceive(event);
		Map<String, List<MessageEvent>> map = event.getEventMap();
	    Log.i("Main","离线消息属于" + map.size() + "个用户");
	        //挨个检测下离线消息所属的用户的信息是否需要更新
	        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
	            List<MessageEvent> list = entry.getValue();
	            int size = list.size();
	            for (int i = 0; i < size; i++) {
	                processMessage(list.get(i),1);  //当后者为1的时候表示离线信息，登录后要save到数据库
	            }
	        }
		
	}
     private void processMessage(final MessageEvent event,int i){
    	 newFriendManager=NewFriendManager.getInstance(mContext);
    	  BmobIMMessage bmobIMMessage=event.getMessage();
    	  if(bmobIMMessage.getMsgType().equals("add")){
    		  if(checkCunZai(newFriendManager.getAllNewFriend(), bmobIMMessage.getFromId())!=1){//等于1表示该请求存在，！=1表示该请求不存在
    		  NewFriend newFriend=AddFriendMessage.convert(bmobIMMessage);
    		  newFriendManager.insertOrUpdateNewFriend(newFriend);
    		  
    		  fragmentChat.newFriendImage.setVisibility(View.VISIBLE);
    		  fragmentChat.newFriendImage1.setVisibility(View.VISIBLE);
    		  }
    	  } else if (bmobIMMessage.getMsgType().equals("agree")) {//接收到的对方同意添加自己为好友,此时需要做的事情：1、添加对方为好友，2、显示通知
              AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(bmobIMMessage);
              addFriend(agree.getFromId());//添加消息的发送方为好友
              
              fragmentChat.converdb.saveId(event.getConversation().getConversationId(), 1);//先存Id

              fragmentChat.converdb.saveNickById(event.getConversation().getConversationId(), event.getFromUserInfo().getName());//存NickName
              fragmentChat.converdb.saveNewContentById(event.getConversation().getConversationId(),"我已经通过了你的好友请求，一起来聊天吧");//存content
              fragmentChat.converdb.saveTouXiangById(event.getConversation().getConversationId(), event.getConversation().getConversationIcon());
              fragmentChat.converdb.saveTimeById(event.getConversation().getConversationId(), event.getMessage().getCreateTime());
              
              fragmentChat.refreshConversations(0,event.getConversation().getConversationId());
              
              //这里应该也需要做下校验--来检测下是否已经同意过该好友请求，我这里省略了
          }else if(bmobIMMessage.getMsgType().equals("decline")){  //接收到拒绝的消息
        	  NewFriend newFriend=declineFriendMessage.convert(bmobIMMessage);
        	  long id=newFriendManager.insertOrUpdateNewFriend(newFriend);
        	  if(id>0){
        		  Toast.makeText(mContext, bmobIMMessage.getBmobIMUserInfo().getName()+"拒绝了您的添加",Toast.LENGTH_SHORT).show();
        	  }
        	  newFriendActivity.bAdapter.notifyDataSetChanged();
        	  fragmentChat.newFriendImage.setVisibility(View.VISIBLE);
    		  fragmentChat.newFriendImage1.setVisibility(View.VISIBLE);
    		} else {
    			if(i==1&&fragmentChat.converdb.getIsFriend(event.getConversation().getConversationId())!=2){
    				
    				fragmentChat.converdb.saveId(event.getConversation().getConversationId(),0);
    				String content="";
    				        if(event.getMessage().getMsgType().equals(BmobIMMessageType.TEXT.getType()))
						    	content=event.getMessage().getContent();
							else if(event.getMessage().getMsgType().equals(BmobIMMessageType.IMAGE.getType()))
			                        content="[图片]";
							else if(event.getMessage().getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
								content="[视频]";
							else if(event.getMessage().getMsgType().equals(BmobIMMessageType.VOICE.getType()))
								content="[语音]";
    		      if(fragmentChat.converdb.getNickById(event.getConversation().getConversationId())==null||fragmentChat.converdb.getNickById(event.getConversation().getConversationId()).equals(""))
    			      fragmentChat.converdb.saveNickById(event.getConversation().getConversationId(),event.getFromUserInfo().getName()) ;
    			  fragmentChat.converdb.saveNewContentById(event.getMessage().getConversationId(),content);
    			  fragmentChat.converdb.saveTimeById(event.getMessage().getConversationId(), event.getMessage().getCreateTime());
                  fragmentChat.refreshConversations(0,event.getConversation().getConversationId());
    			}
			
			}
    		  
     }
     private void addFriend(String uid) {
         MyUser user = new MyUser();
         user.setObjectId(uid);
         UserModel.getInstance()
                 .agreeAddFriend(user, new SaveListener<String>() {
                     @Override
                     public void done(String s, BmobException e) {
                         if (e == null) {
                             Log.e("Main","success");
                             fragmentChat.refreshNewFriend();
                         } else {
                             Log.e("Main",e.getMessage());
                             Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
         
         
     }
     private int checkCunZai(List<NewFriend> friends,String objectId){
 		for (int i = 0; i < friends.size(); i++) {
 			if(friends.get(i).getStatus()==0)      //status为0的只有一条就可以了
 		     if(friends.get(i).getUid().equals(objectId)){
 		    	 return 1;
 		     }
 		}
 		return 2;
 	}
     private void showAddNotify(NewFriend friend) {
         Intent pendingIntent = new Intent(mContext, newFriendActivity.class);
         pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
         //这里可以是应用图标，也可以将聊天头像转成bitmap
         Bitmap largetIcon = BitmapFactory.decodeResource(mContext.getResources(), com.sharefriend.app.R.drawable.ic_launcher);
         BmobNotificationManager.getInstance(mContext).showNotification(largetIcon,
                 friend.getName(), friend.getMsg(), friend.getName() + "请求添加你为朋友", pendingIntent);
     }

     /**
      * 显示对方同意添加自己为好友的通知
      *
      * @param info
      * @param agree
      */
     private void showAgreeNotify(BmobIMUserInfo info, AgreeAddFriendMessage agree) {
         Intent pendingIntent = new Intent(mContext, weather_info.class);
         pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
         Bitmap largetIcon = BitmapFactory.decodeResource(mContext.getResources(), com.sharefriend.app.R.drawable.ic_launcher);
         BmobNotificationManager.getInstance(mContext).showNotification(largetIcon, info.getName(), agree.getMsg(), agree.getMsg(), pendingIntent);
     }

}
