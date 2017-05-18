package message;

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
	}
     private void processMessage(MessageEvent event){
    	 newFriendManager=NewFriendManager.getInstance(mContext);
    	  BmobIMMessage bmobIMMessage=event.getMessage();
    	  if(bmobIMMessage.getMsgType().equals("add")){
    		  NewFriend newFriend=AddFriendMessage.convert(bmobIMMessage);
    		  long id=newFriendManager.insertOrUpdateNewFriend(newFriend);
    		  if(id>0)
    			  Log.d("Main","Ìí¼Ó³É¹¦");
    		  fragmentPart.newFriendImage.setVisibility(View.VISIBLE);
    		  fragmentPart.newFriendImage1.setVisibility(View.VISIBLE);
    	  }
    		  
     }
}
