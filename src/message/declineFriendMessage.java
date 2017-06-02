package message;

import model.NewFriend;

import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

public class declineFriendMessage extends BmobIMExtraMessage {

    @Override
    public String getMsgType() {
        return "decline";
    }

    @Override
    public boolean isTransient() {
        //如果需要在对方的会话表中新增一条该类型的消息，则设置为false，表明是非暂态会话
        //此处将同意添加好友的请求设置为false，为了演示怎样向会话表和消息表中新增一个类型
        return false;
    }

  

    public declineFriendMessage(){}

    /**
     * 继承BmobIMMessage的属性
     * @param msg
     */
 
    /**将BmobIMMessage转成AgreeAddFriendMessage
     * @param msg 消息
     * @return
     */
    public static NewFriend convert(BmobIMMessage msg){
        NewFriend add =new NewFriend();
        add.setMsg("对方已拒绝您的好友请求");
        add.setTime(msg.getCreateTime());
        add.setStatus(Config.STATUS_VERIFY_REFUSE);
        try {
            String extra = msg.getExtra();
            if(!TextUtils.isEmpty(extra)){
                JSONObject json =new JSONObject(extra);
                String name = json.getString("name");
                add.setName(name);
                String avatar = json.getString("avatar");
                add.setAvatar(avatar);
                add.setUid(json.getString("uid"));
            }else{
                Log.i("Main","AddFriendMessage的extra为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }
    public static NewFriend Iconvert(BmobIMUserInfo bmobIMUserInfo,NewFriend newFriend){  //这里是点击拒绝之后显示在本地消息变成“您已经拒绝对方的好友请求”
    	NewFriend add=new NewFriend();
    	add.setMsg(newFriend.getMsg());
    	add.setStatus(Config.STATUS_VERIFY_IREFUSE);
    	add.setName(bmobIMUserInfo.getName());
    	add.setAvatar(bmobIMUserInfo.getAvatar());
    	add.setUid(bmobIMUserInfo.getUserId());
    	return add;
    }
}


