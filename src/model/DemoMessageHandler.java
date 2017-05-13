package model;



import activity.MyUser;
import activity.weather_info;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;


import message.AddFriendMessage;
import message.AgreeAddFriendMessage;

import org.greenrobot.eventbus.EventBus;


import java.util.List;
import java.util.Map;


import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * ��Ϣ������
 *
 * @author smile
 * @project DemoMessageHandler
 * @date 2016-03-08-17:37
 */
public class DemoMessageHandler extends BmobIMMessageHandler {

    private Context context;

    public DemoMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //�����յ���������������Ϣʱ���˷���������
        Log.i("Main",event.getConversation().getConversationTitle() + "," + event.getMessage().getMsgType() + "," + event.getMessage().getContent());
        excuteMessage(event);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //ÿ�ε���connect����ʱ���ѯһ��������Ϣ������У��˷����ᱻ����
        Map<String, List<MessageEvent>> map = event.getEventMap();
        Log.i("Main","������Ϣ����" + map.size() + "���û�");
        //���������������Ϣ�������û�����Ϣ�Ƿ���Ҫ����
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                excuteMessage(list.get(i));
            }
        }
    }

    /**
     * ������Ϣ
     *
     * @param event
     */
    private void excuteMessage(final MessageEvent event) {
        //����û���Ϣ�Ƿ���Ҫ����
        UserModel.getInstance().updateUserInfo(event, new UpdateCacheListener() {
            @Override
            public void done(BmobException e) {
                BmobIMMessage msg = event.getMessage();
                if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//�û��Զ������Ϣ���ͣ�������ֵ��Ϊ0
                    processCustomMessage(msg, event.getFromUserInfo());
                } else {//SDK�ڲ��ڲ�֧�ֵ���Ϣ����
                    if (BmobNotificationManager.getInstance(context).isShowNotification()) {//�����Ҫ��ʾ֪ͨ����SDK�ṩ����������ʾ��ʽ��
                        Intent pendingIntent = new Intent(context, weather_info.class);
                        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //1������û��Ķ�����Ϣ�ϲ���һ��֪ͨ����XX����ϵ�˷�����XX����Ϣ
                        BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
                        //2���Զ���֪ͨ��Ϣ��ʼ��ֻ��һ��֪ͨ������Ϣ���Ǿ���Ϣ
//                        BmobIMUserInfo info =event.getFromUserInfo();
//                        //���������Ӧ��ͼ�꣬Ҳ���Խ�����ͷ��ת��bitmap
//                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
//                                info.getName(),msg.getContent(),"����һ������Ϣ",pendingIntent);
                    } else {//ֱ�ӷ�����Ϣ�¼�
                        Log.i("Main","��ǰ����Ӧ���ڣ�����event");
                        EventBus.getDefault().post(event);
                    }
                }
            }
        });
    }

    /**
     * �����Զ�����Ϣ����
     *
     * @param msg
     */
    private void processCustomMessage(BmobIMMessage msg, BmobIMUserInfo info) {
        //���д����Զ�����Ϣ����
        Log.i("Main",msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra());
        String type = msg.getMsgType();
        //����ҳ��ˢ�µĹ㲥
        EventBus.getDefault().post(new RefreshEvent());
        //������Ϣ
        if (type.equals("add")) {//���յ�����Ӻ��ѵ�����
            NewFriend friend = AddFriendMessage.convert(msg);
            //���غ������������У�飬����û�еĲ�������ʾ֪ͨ��--�п���������Ϣ����Щ�ظ�
            long id = NewFriendManager.getInstance(context).insertOrUpdateNewFriend(friend);
            if (id > 0) {
                showAddNotify(friend);
            }
        } else if (type.equals("agree")) {//���յ��ĶԷ�ͬ������Լ�Ϊ����,��ʱ��Ҫ�������飺1����ӶԷ�Ϊ���ѣ�2����ʾ֪ͨ
            AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(msg);
            addFriend(agree.getFromId());//�����Ϣ�ķ��ͷ�Ϊ����
            //����Ӧ��Ҳ��Ҫ����У��--��������Ƿ��Ѿ�ͬ����ú�������������ʡ����
            showAgreeNotify(info, agree);
        } else {
            Toast.makeText(context, "���յ����Զ�����Ϣ��" + msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ��ʾ�Է�����Լ�Ϊ���ѵ�֪ͨ
     *
     * @param friend
     */
    private void showAddNotify(NewFriend friend) {
        Intent pendingIntent = new Intent(context, weather_info.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //���������Ӧ��ͼ�꣬Ҳ���Խ�����ͷ��ת��bitmap
        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), com.uniqueweather.app.R.drawable.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
                friend.getName(), friend.getMsg(), friend.getName() + "���������Ϊ����", pendingIntent);
    }

    /**
     * ��ʾ�Է�ͬ������Լ�Ϊ���ѵ�֪ͨ
     *
     * @param info
     * @param agree
     */
    private void showAgreeNotify(BmobIMUserInfo info, AgreeAddFriendMessage agree) {
        Intent pendingIntent = new Intent(context, weather_info.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), com.uniqueweather.app.R.drawable.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(largetIcon, info.getName(), agree.getMsg(), agree.getMsg(), pendingIntent);
    }

    /**
     * ��ӶԷ�Ϊ�Լ��ĺ���
     *
     * @param uid
     */
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
