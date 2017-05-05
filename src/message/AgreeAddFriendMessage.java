package message;

import android.util.Log;
import cn.bmob.newim.bean.BmobIMExtraMessage;

public class AgreeAddFriendMessage extends BmobIMExtraMessage {
      private String uid;   //最初的发送方
      private Long time;
      private String msg;    //用于通知栏显示的内容
	@Override
	public String getMsgType() {
		// TODO Auto-generated method stub
		return "agree";
	}
	@Override
	public boolean isTransient() {
		// TODO Auto-generated method stub
		return false;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
      
}
