package message;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

public class myMessageHandler extends BmobIMMessageHandler {

	@Override
	public void onMessageReceive(MessageEvent event) {
		super.onMessageReceive(event);
	}

	@Override
	public void onOfflineReceive(OfflineMessageEvent event) {
		super.onOfflineReceive(event);
	}
        
}
