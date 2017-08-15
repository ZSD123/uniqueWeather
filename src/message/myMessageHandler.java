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
	    Log.i("Main","������Ϣ����" + map.size() + "���û�");
	        //���������������Ϣ�������û�����Ϣ�Ƿ���Ҫ����
	        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
	            List<MessageEvent> list = entry.getValue();
	            int size = list.size();
	            for (int i = 0; i < size; i++) {
	                processMessage(list.get(i),1);  //������Ϊ1��ʱ���ʾ������Ϣ����¼��Ҫsave�����ݿ�
	            }
	        }
		
	}
     private void processMessage(final MessageEvent event,int i){
    	 newFriendManager=NewFriendManager.getInstance(mContext);
    	  BmobIMMessage bmobIMMessage=event.getMessage();
    	  if(bmobIMMessage.getMsgType().equals("add")){
    		  if(checkCunZai(newFriendManager.getAllNewFriend(), bmobIMMessage.getFromId())!=1){//����1��ʾ��������ڣ���=1��ʾ�����󲻴���
    		  NewFriend newFriend=AddFriendMessage.convert(bmobIMMessage);
    		  newFriendManager.insertOrUpdateNewFriend(newFriend);
    		  
    		  fragmentChat.newFriendImage.setVisibility(View.VISIBLE);
    		  fragmentChat.newFriendImage1.setVisibility(View.VISIBLE);
    		  }
    	  } else if (bmobIMMessage.getMsgType().equals("agree")) {//���յ��ĶԷ�ͬ������Լ�Ϊ����,��ʱ��Ҫ�������飺1����ӶԷ�Ϊ���ѣ�2����ʾ֪ͨ
              AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(bmobIMMessage);
              addFriend(agree.getFromId());//�����Ϣ�ķ��ͷ�Ϊ����
              
              fragmentChat.converdb.saveId(event.getConversation().getConversationId(), 1);//�ȴ�Id

              fragmentChat.converdb.saveNickById(event.getConversation().getConversationId(), event.getFromUserInfo().getName());//��NickName
              fragmentChat.converdb.saveNewContentById(event.getConversation().getConversationId(),"���Ѿ�ͨ������ĺ�������һ���������");//��content
              fragmentChat.converdb.saveTouXiangById(event.getConversation().getConversationId(), event.getConversation().getConversationIcon());
              fragmentChat.converdb.saveTimeById(event.getConversation().getConversationId(), event.getMessage().getCreateTime());
              
              fragmentChat.refreshConversations(0,event.getConversation().getConversationId());
              
              //����Ӧ��Ҳ��Ҫ����У��--��������Ƿ��Ѿ�ͬ����ú�������������ʡ����
          }else if(bmobIMMessage.getMsgType().equals("decline")){  //���յ��ܾ�����Ϣ
        	  NewFriend newFriend=declineFriendMessage.convert(bmobIMMessage);
        	  long id=newFriendManager.insertOrUpdateNewFriend(newFriend);
        	  if(id>0){
        		  Toast.makeText(mContext, bmobIMMessage.getBmobIMUserInfo().getName()+"�ܾ����������",Toast.LENGTH_SHORT).show();
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
			                        content="[ͼƬ]";
							else if(event.getMessage().getMsgType().equals(BmobIMMessageType.VIDEO.getType()))
								content="[��Ƶ]";
							else if(event.getMessage().getMsgType().equals(BmobIMMessageType.VOICE.getType()))
								content="[����]";
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
 			if(friends.get(i).getStatus()==0)      //statusΪ0��ֻ��һ���Ϳ�����
 		     if(friends.get(i).getUid().equals(objectId)){
 		    	 return 1;
 		     }
 		}
 		return 2;
 	}
     private void showAddNotify(NewFriend friend) {
         Intent pendingIntent = new Intent(mContext, newFriendActivity.class);
         pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
         //���������Ӧ��ͼ�꣬Ҳ���Խ�����ͷ��ת��bitmap
         Bitmap largetIcon = BitmapFactory.decodeResource(mContext.getResources(), com.sharefriend.app.R.drawable.ic_launcher);
         BmobNotificationManager.getInstance(mContext).showNotification(largetIcon,
                 friend.getName(), friend.getMsg(), friend.getName() + "���������Ϊ����", pendingIntent);
     }

     /**
      * ��ʾ�Է�ͬ������Լ�Ϊ���ѵ�֪ͨ
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
