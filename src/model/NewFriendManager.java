package model;



import activity.MyUser;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import cn.bmob.v3.BmobUser;


/**
 * Created by Administrator on 2016/4/27.
 */
public class NewFriendManager {

    private DaoMaster.DevOpenHelper openHelper;
    Context mContecxt;
    String uid=null;
    private static HashMap<String, NewFriendManager> daoMap = new HashMap<String,NewFriendManager>();

    /**��ȡDBʵ��
     * @param context
     * @return
     */
    public static NewFriendManager getInstance(Context context) {
        MyUser user = BmobUser.getCurrentUser( MyUser.class);
        String loginId=user.getObjectId();
        if(TextUtils.isEmpty(loginId)){
            throw new RuntimeException("you must login.");
        }
        NewFriendManager dao = daoMap.get(loginId);
        if (dao == null) {
            dao = new NewFriendManager(context,loginId);
            daoMap.put(loginId, dao);
        }
        
        return dao;
    }

    private NewFriendManager(Context context, String uId){
        clear();
        this.mContecxt =context.getApplicationContext();
        this.uid=uId;
        String DBName = uId+".demodb";
        this.openHelper = new DaoMaster.DevOpenHelper(mContecxt, DBName, null);
    }

    /**
     * �����Դ
     */
    public void clear() {
        if(openHelper !=null) {
            openHelper.close();
            openHelper = null;
            mContecxt=null;
            uid =null;
        }
    }

    private DaoSession openReadableDb() {
        checkInit();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private DaoSession openWritableDb(){
        checkInit();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private void checkInit(){
        if(openHelper ==null){
            throw new RuntimeException("���ʼ��db");
        }
    }

    //-------------------------------------------------------------

    /**��ȡ�������е�������Ϣ
     * @return
     */
    public List<NewFriend> getAllNewFriend(){
        NewFriendDao dao = openReadableDb().getNewFriendDao();
        return dao.queryBuilder().orderDesc(NewFriendDao.Properties.Time).list();
    }

    /**�����������������Ϣ
     * @param info
     * @return long:���ز�����޸ĵ�id
     */
    public long insertOrUpdateNewFriend(NewFriend info){
        NewFriendDao dao =  openWritableDb().getNewFriendDao();
        NewFriend local = getNewFriend(info.getUid(), info.getTime());
        
        if(local==null){
            return dao.insertOrReplace(info);
        }else{
            return -1;
        }
    }

    /**
     * ��ȡ���صĺ�������
     * @param uid
     * @param time
     * @return
     */
    private NewFriend getNewFriend(String uid,Long time){
    	Log.d("Main", "uid="+uid);
    	Log.d("Main", "time="+time);
        NewFriendDao dao =  openReadableDb().getNewFriendDao();
        return dao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(uid))
                .where(NewFriendDao.Properties.Time.eq(time)).build().unique();
    }

    /**
     * �Ƿ����µĺ�������
     * @return
     */
    public boolean hasNewFriendInvitation(){
        List<NewFriend> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * ��ȡδ���ĺ�������
     * @return
     */
    public int getNewInvitationCount(){
        List<NewFriend> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            return infos.size();
        }else{
            return 0;
        }
    }
    /**
     * ��ȡ����δ��δ��֤�ĺ�������
     * @return
     */
    private List<NewFriend> getNoVerifyNewFriend(){
        NewFriendDao dao =  openReadableDb().getNewFriendDao();
        return dao.queryBuilder().where(NewFriendDao.Properties.Status.eq(message.Config.STATUS_VERIFY_NONE))
                .build().list();
    }

    /**
     * ��������δ��δ��֤��״̬Ϊ�Ѷ�
     */
    public void updateBatchStatus(){
        List<NewFriend> infos =getNoVerifyNewFriend();
        if(infos!=null && infos.size()>0){
            int size =infos.size();
            List<NewFriend> all =new ArrayList<NewFriend>();
            for (int i = 0; i < size; i++) {
                NewFriend msg =infos.get(i);
                msg.setStatus(message.Config.STATUS_VERIFY_READED);
                all.add(msg);
            }
            insertBatchMessages(infos);
        }
    }

    /**����������Ϣ
     * @param msgs
     */
    public  void insertBatchMessages(List<NewFriend> msgs){
        NewFriendDao dao =openWritableDb().getNewFriendDao();
        dao.insertOrReplaceInTx(msgs);
    }

    /**
     * �޸�ָ�����������״̬
     * @param friend
     * @param status
     * @return
     */
    public long updateNewFriend(NewFriend friend,int status){
        NewFriendDao dao = openWritableDb().getNewFriendDao();
        friend.setStatus(status);
        return dao.insertOrReplace(friend);
    }

    /**
     * ɾ��ָ�����������
     * @param friend
     */
    public void deleteNewFriend(NewFriend friend){
        NewFriendDao dao =openWritableDb().getNewFriendDao();
        dao.delete(friend);
    }

}
