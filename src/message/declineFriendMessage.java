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
        //�����Ҫ�ڶԷ��ĻỰ��������һ�������͵���Ϣ��������Ϊfalse�������Ƿ���̬�Ự
        //�˴���ͬ����Ӻ��ѵ���������Ϊfalse��Ϊ����ʾ������Ự�����Ϣ��������һ������
        return false;
    }

  

    public declineFriendMessage(){}

    /**
     * �̳�BmobIMMessage������
     * @param msg
     */
 
    /**��BmobIMMessageת��AgreeAddFriendMessage
     * @param msg ��Ϣ
     * @return
     */
    public static NewFriend convert(BmobIMMessage msg){
        NewFriend add =new NewFriend();
        add.setMsg("�Է��Ѿܾ����ĺ�������");
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
                Log.i("Main","AddFriendMessage��extraΪ��");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }
    public static NewFriend Iconvert(BmobIMUserInfo bmobIMUserInfo,NewFriend newFriend){  //�����ǵ���ܾ�֮����ʾ�ڱ�����Ϣ��ɡ����Ѿ��ܾ��Է��ĺ�������
    	NewFriend add=new NewFriend();
    	add.setMsg(newFriend.getMsg());
    	add.setStatus(Config.STATUS_VERIFY_IREFUSE);
    	add.setName(bmobIMUserInfo.getName());
    	add.setAvatar(bmobIMUserInfo.getAvatar());
    	add.setUid(bmobIMUserInfo.getUserId());
    	return add;
    }
}


