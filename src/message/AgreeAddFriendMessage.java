package message;

import android.util.Log;
import cn.bmob.newim.bean.BmobIMExtraMessage;

public class AgreeAddFriendMessage extends BmobIMExtraMessage {
      private String uid;   //����ķ��ͷ�
      private Long time;
      private String msg;    //����֪ͨ����ʾ������
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
