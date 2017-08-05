package db;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;

import com.amap.api.services.a.v;

import activity.MyUser;
import activity.newFriendActivity;
import activity.weather_info;
import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class conversationDB {    //这就是一个每个登录用户的数据库，里面有conversation数据表等等
     private int VERSION=1;
     private static SQLiteDatabase db;
     public  static conversationDB condb;
     private static conversationdbHelper dbHelper;
     String DB_name="conversationDB";
     
     
     private conversationDB(Context context){
    	 dbHelper=new conversationdbHelper(context, DB_name, null, VERSION);
    	 db=dbHelper.getWritableDatabase();
     }
     public synchronized static conversationDB getInstance(Context context){
    	 if(condb==null){
    		 condb=new conversationDB(context);
    	 }
    	 return condb;
     }
     public void saveId(String id){
    	 if(!id.equals(weather_info.objectId)){
    	 ContentValues values=new ContentValues();
    	 values.put("id", id);
    	 values.put("fromId", weather_info.objectId);
    	 int i= db.update("conversation",values, "id=? AND fromId=?",new String[]{id,weather_info.objectId});//这里就是为了判断是否有之前的，这里用update的原因在于用insert会提示错误的，当影响的行数等于0的时候表示之前没有就直接插入
    	  if(i==0){
        	values.put("unReadNum", 0);
    	 	db.insert("conversation", null, values);
    	 	
     	  }
    	 }
     }
     
   
     public void saveNickById(String id,String nick){              
    	 ContentValues values=new ContentValues();                 
    	 values.put("nickName",nick); 
    	 db.update("conversation", values,"id=? AND fromId=?",new String[]{id,weather_info.objectId});
         
     }
     public String getNickById(String id){
    	 String nickName="";
    	 Cursor cursor= db.query("conversation",new String[]{"nickName"},"id=? AND fromId=?",new String[]{id,weather_info.objectId}, null, null, null);
    	 if(cursor.moveToFirst()){
    		 do {
				nickName=cursor.getString(cursor.getColumnIndex("nickName"));
			} while (cursor.moveToNext());
    	 }
    	 return nickName;
     }
     public void addUnReadNumById(String id){
    	  int i=getUnReadNumById(id);
    	  ContentValues values=new ContentValues();
    	  values.put("unReadNum",i+1);
    	  db.update("conversation", values,"id=? AND fromId=?", new String[]{id,weather_info.objectId});
     }
     public int getUnReadNumById(String id){
    	 int i=0;
    	 Cursor cursor=db.query("conversation", new String []{"unReadNum"},"id=? AND fromId=?",new String[]{id,weather_info.objectId}, null, null, null);
    	 if(cursor.moveToFirst()){
    		 do {
				i=cursor.getInt(cursor.getColumnIndex("unReadNum"));
			} while (cursor.moveToNext());
    	 }
    	 return i;
     }
     public void clearUnReadNumById(String Id){
    	 ContentValues values=new ContentValues();
    	 values.put("unReadNum", 0);
   	     db.update("conversation", values,"id=? AND fromId=?", new String[]{Id,weather_info.objectId});
     }
     public void saveTimeById(String id,long time){
    	 ContentValues values=new ContentValues();
    	 values.put("newTime", time);
    	 db.update("conversation", values, "id=? AND fromId=?", new String[]{id,weather_info.objectId});
     }
     public List<BmobIMConversation> getConverByTime(){
    	 List<BmobIMConversation> list=new ArrayList<BmobIMConversation>();
    	 Cursor cursor=db.rawQuery("select * from conversation where fromId=? order by newTime DESC ", new String []{weather_info.objectId});
    	 if(cursor.moveToFirst()){
    		 do {
				BmobIMConversation conversation=new BmobIMConversation();
				conversation.setConversationId(cursor.getString(cursor.getColumnIndex("id")));
				conversation.setConversationTitle(cursor.getString(cursor.getColumnIndex("nickName")));
				conversation.setUpdateTime(cursor.getLong(cursor.getColumnIndex("newTime")));
				conversation.setConversationIcon(cursor.getString(cursor.getColumnIndex("touXiang")));
				list.add(conversation);
			} while (cursor.moveToNext());
    	 }
    	 return list;
     }
     public void saveTouXiangById(String id,String touXiang){
    	 ContentValues values=new ContentValues();
    	 values.put("touXiang", touXiang);
    	 db.update("conversation", values, "id=? AND fromId=?", new String[]{id,weather_info.objectId});
     }
     public String getTouXiangById(String id){
    	 String touXiang="0";
    	 Cursor cursor=db.query("conversation", new String []{"touXiang"},"id=? AND fromId=?",new String[]{id,weather_info.objectId}, null, null, null);
    	 if(cursor.moveToFirst()){
    		 do {
				touXiang=cursor.getString(cursor.getColumnIndex("touXiang"));
			} while (cursor.moveToNext());
    	 }
    	 return touXiang;
     }
     public void saveNewContentById(String id,String content){
    	 Log.d("Main", "saveContent:id="+id+"objectId="+weather_info.objectId+"content="+content);
    	 ContentValues values=new ContentValues();
    	 values.put("newContent", content);
    	 db.update("conversation", values, "id=? AND fromId=?", new String[]{id,weather_info.objectId});
     }
     public String getNewContentById(String id){
    	 String newContent="";
    	 Cursor cursor=db.query("conversation", new String []{"newContent"},"id=? AND fromId=?",new String[]{id,weather_info.objectId}, null, null, null);
    	 if(cursor.moveToFirst()){
    		 do {
				newContent=cursor.getString(cursor.getColumnIndex("newContent"));
			} while (cursor.moveToNext());
    	 }
    	 return newContent;
     }
     public void deleteCoversationById(String id){
    	 db.delete("conversation", "id=? AND fromId=?", new String[]{id,weather_info.objectId});
     }
     public void deleteAll(){
    	 db.delete("conversation", null, null);
     }
     
}
