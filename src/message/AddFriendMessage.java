package message;

import cn.bmob.newim.bean.BmobIMExtraMessage;



import android.text.TextUtils;
import android.util.Log;

import model.NewFriend;

import org.json.JSONObject;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;


/**��Ӻ�������
 * @author :smile
 * @project:AddFriendMessage
 * @date :2016-01-30-17:28
 */
public class AddFriendMessage extends BmobIMExtraMessage{

    public AddFriendMessage(){};

    /**��BmobIMMessageת��NewFriend
     * @param msg ��Ϣ
     * @return
     */
    public static NewFriend convert(BmobIMMessage msg){  //�ⲿ���ǽ����ߵ�������Ϣ��ʱ�����
        NewFriend add =new NewFriend();
        String content = msg.getContent();
        add.setMsg(content);
        add.setTime(msg.getCreateTime());
        add.setStatus(Config.STATUS_VERIFY_NONE);
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


    @Override
    public String getMsgType() {
        return "add";
    }

    @Override
    public boolean isTransient() {
        //����Ϊtrue,����Ϊ��̬��Ϣ����ô������Ϣ�����ᱣ�浽����db�У�SDKֻ�����ͳ�ȥ
        //����Ϊfalse,��ᱣ�浽ָ���Ự�����ݿ���
        return true;
    }

}

