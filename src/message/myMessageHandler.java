package message;

import java.util.List;
import java.util.Map;

import model.NewFriend;
import model.NewFriendManager;
import activity.fragmentPart;
import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

public class myMessageHandler extends BmobIMMessageHandler {
    private NewFriendManager newFriendManager;
    private Context mContext;
    public myMessageHandler(Context context){
    	mContext=context;
    }
	@Override
	public void onMessageReceive(MessageEvent event) {
		super.onMessageReceive(event);
		Log.d("Main", event.getMessage().toString());
		processMessage(event);
	}

	@Override
	public void onOfflineReceive(OfflineMessageEvent event) {
		super.onOfflineReceive(event);
		Log.d("Main", event.toString());
		Map<String, List<MessageEvent>> map = event.getEventMap();
	    Log.i("Main","离线消息属于" + map.size() + "个用户");
	        //挨个检测下离线消息所属的用户的信息是否需要更新
	        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
	            List<MessageEvent> list = entry.getValue();
	            int size = list.size();
	            for (int i = 0; i < size; i++) {
	                processMessage(list.get(i));
	            }
	        }
		
	}
     private void processMessage(MessageEvent event){
    	 newFriendManager=NewFriendManager.getInstance(mContext);
    	  BmobIMMessage bmobIMMessage=event.getMessage();
    	  if(bmobIMMessage.getMsgType().equals("add")){
    		  NewFriend newFriend=AddFriendMessage.convert(bmobIMMessage);
    		  long id=newFriendManager.insertOrUpdateNewFriend(newFriend);
    		  if(id>0)
    			  Log.d("Main","添加成功");
    		  fragmentPart.newFriendImage.setVisibility(View.VISIBLE);
    		  fragmentPart.newFriendImage1.setVisibility(View.VISIBLE);
    	  }
    		  
     }
}
