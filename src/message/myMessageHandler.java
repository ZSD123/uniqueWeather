package message;

import java.util.List;
import java.util.Map;

import model.NewFriend;
import model.NewFriendManager;
import model.UserModel;
import activity.MyUser;
import activity.fragmentPart;
import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.exception.BmobException;
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
		Log.d("Main", event.getMessage().toString());
		processMessage(event);
	}

	@Override
	public void onOfflineReceive(OfflineMessageEvent event) {
		super.onOfflineReceive(event);
		Log.d("Main", event.toString());
		Map<String, List<MessageEvent>> map = event.getEventMap();
	    Log.i("Main","������Ϣ����" + map.size() + "���û�");
	        //���������������Ϣ�������û�����Ϣ�Ƿ���Ҫ����
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
    			  Log.d("Main","��ӳɹ�");
    		  fragmentPart.newFriendImage.setVisibility(View.VISIBLE);
    		  fragmentPart.newFriendImage1.setVisibility(View.VISIBLE);
    	  } else if (bmobIMMessage.getMsgType().equals("agree")) {//���յ��ĶԷ�ͬ������Լ�Ϊ����,��ʱ��Ҫ�������飺1����ӶԷ�Ϊ���ѣ�2����ʾ֪ͨ
              AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(bmobIMMessage);
              addFriend(agree.getFromId());//�����Ϣ�ķ��ͷ�Ϊ����
              //����Ӧ��Ҳ��Ҫ����У��--��������Ƿ��Ѿ�ͬ����ú�������������ʡ����
          } else {
              Toast.makeText(mContext, "���յ����Զ�����Ϣ��" + bmobIMMessage.getMsgType() + "," + bmobIMMessage.getContent() + "," + bmobIMMessage.getExtra(), Toast.LENGTH_SHORT).show();
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
                         } else {
                             Log.e("Main",e.getMessage());
                         }
                     }
                 });
     }
}
