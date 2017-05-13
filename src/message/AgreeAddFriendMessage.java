package message;



import android.text.TextUtils;
import android.util.Log;



import org.json.JSONObject;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**ͬ����Ӻ�������-����ֻ���ڷ���ͬ����Ӻ��ѵ���Ϣ
 * @author smile
 * @project AgreeAddFriendMessage
 * @date 2016-03-04-10:41
 * ���յ��Է����͵�ͬ������Լ�Ϊ���ѵ�����ʱ����Ҫ���������飺1���ڱ������ݿ����½�һ���Ự�������Ҫ����isTransientΪfalse,2����ӶԷ����Լ��ĺ��ѱ���
 */
public class AgreeAddFriendMessage extends BmobIMExtraMessage{

    //���¾��Ǵ�extra�������������ֶΣ������ȡ
    private String uid;//����ķ��ͷ�
    private Long time;
    private String msg;//����֪ͨ����ʾ������

    @Override
    public String getMsgType() {
        return "agree";
    }

    @Override
    public boolean isTransient() {
        //�����Ҫ�ڶԷ��ĻỰ��������һ�������͵���Ϣ��������Ϊfalse�������Ƿ���̬�Ự
        //�˴���ͬ����Ӻ��ѵ���������Ϊfalse��Ϊ����ʾ������Ự�����Ϣ��������һ������
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

    public AgreeAddFriendMessage(){}

    /**
     * �̳�BmobIMMessage������
     * @param msg
     */
    private AgreeAddFriendMessage(BmobIMMessage msg){
        super.parse(msg);
    }

    /**��BmobIMMessageת��AgreeAddFriendMessage
     * @param msg ��Ϣ
     * @return
     */
    public static AgreeAddFriendMessage convert(BmobIMMessage msg){
        AgreeAddFriendMessage agree =new AgreeAddFriendMessage(msg);
        try {
            String extra = msg.getExtra();
            if(!TextUtils.isEmpty(extra)){
                JSONObject json =new JSONObject(extra);
                Long time = json.getLong("time");
                String uid =json.getString("uid");
                String m =json.getString("msg");
                agree.setMsg(m);
                agree.setUid(uid);
                agree.setTime(time);
            }else{
                Log.i("Main","AgreeAddFriendMessage��extraΪ��");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agree;
    }
}

