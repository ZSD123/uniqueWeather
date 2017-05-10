package model;



import java.util.List;

import activity.MyUser;
import android.text.TextUtils;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author :smile
 * @project:UserModel
 * @date :2016-01-22-18:09
 */
public class UserModel extends BaseModel {

    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    private UserModel() {
    }

    /**
     * ��¼
     *
     * @param username
     * @param password
     * @param listener
     */
    public void login(String username, String password, final LogInListener listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new BmobException(CODE_NULL, "����д�û���"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(CODE_NULL, "����д����"));
            return;
        }
        final MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    listener.done(MyUser.getCurrentUser(), null);
                } else {
                    listener.done(user, e);
                }
            }
        });


    }

    /**
     * �˳���¼
     */
    public void logout() {
        BmobUser.logOut();
    }

    public MyUser getCurrentUser() {
        return BmobUser.getCurrentUser(MyUser.class);
    }

    /**
     * @param username
     * @param password
     * @param pwdagain
     * @param listener
     */
    public void register(String username, String password, String pwdagain, final LogInListener listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new BmobException(CODE_NULL, "����д�û���"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(CODE_NULL, "����д����"));
            return;
        }
        if (TextUtils.isEmpty(pwdagain)) {
            listener.done(null, new BmobException(CODE_NULL, "����дȷ������"));
            return;
        }
        if (!password.equals(pwdagain)) {
            listener.done(null, new BmobException(CODE_NULL, "������������벻һ�£�����������"));
            return;
        }
        final MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    listener.done(null, null);
                } else {
                    listener.done(null, e);
                }
            }
        });


    }

    /**
     * ��ѯ�û�
     *
     * @param username
     * @param limit
     * @param listener
     */
    public void queryUsers(String username, final int limit, final FindListener<MyUser> listener) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        //ȥ����ǰ�û�
        try {
            BmobUser user = BmobUser.getCurrentUser();
            query.addWhereNotEqualTo("username", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(CODE_NULL, "���޴���"));
                    }
                } else {
                    listener.done(list, e);
                }
            }

			
        });


    }

    /**
     * ��ѯ�û���Ϣ
     *
     * @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(
                new FindListener<MyUser>() {
                    @Override
                    public void done(List<MyUser> list, BmobException e) {
                        if (e == null) {

                            if (list != null && list.size() > 0) {
                                listener.done(list.get(0), null);
                            } else {
                                listener.done(null, new BmobException(000, "���޴���"));
                            }
                        } else {
                            listener.done(null, e);
                        }
                    }
                });


    }

    /**
     * �����û����ϺͻỰ����
     *
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String title = conversation.getConversationTitle();
        //sdk�ڲ������»Ự�ĻỰ������objectId��ʾ�������Ҫ�ȶ��û����ͻỰ����--���ģ���������ݻỰ���ͽ����ж�
        if (!username.equals(title)) {
            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(MyUser s, BmobException e) {
                    if (e == null) {
                        String name = s.getUsername();
                        String avatar = s.getTouXiangUrl();
                  //      Logger.i("query success��" + name + "," + avatar);
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //�����û�����
                        BmobIM.getInstance().updateUserInfo(info);
                        //���»Ự����-�����Ϣ����̬��Ϣ���򲻸��»Ự����
                        if (!msg.isTransient()) {
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    } else {
                       Log.d("Main", e.toString());
                    }
                    listener.done(null);
                }
            });
        } else {
            listener.done(null);
        }
    }

    /**
     * ͬ����Ӻ��ѣ�1������ͬ����ӵ�����2����ӶԷ����Լ��ĺ����б���
     */
    public void agreeAddFriend(MyUser friend, SaveListener<String> listener) {
        Friend f = new Friend();
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        f.setMyUser(user);
        f.setFriendUser(friend);
        f.save(listener);
    }

    /**
     * ��ѯ����
     *
     * @param listener
     */
    public void queryFriends(final FindListener<Friend> listener) {
        BmobQuery<Friend> query = new BmobQuery<Friend>();
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        query.addWhereEqualTo("myUser", user);
        query.include("friendUser");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(0, "������ϵ��"));
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });


    }

    /**
     * ɾ������
     *
     * @param f
     * @param listener
     */
    public void deleteFriend(Friend f, UpdateListener listener) {
        Friend friend = new Friend();
        friend.delete(f.getObjectId(), listener);
    }
}
