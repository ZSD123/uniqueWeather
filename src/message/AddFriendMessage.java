package message;

import cn.bmob.newim.bean.BmobIMExtraMessage;

public class AddFriendMessage extends BmobIMExtraMessage {
      public AddFriendMessage(){}

	@Override
	public String getMsgType() {
		return "add";
	}

	@Override
	public boolean isTransient() {
		return true;   //表示暂态消息
	}
      
}
