package db;

import com.amap.api.services.a.v;

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

public class conversationDB {
     private int VERSION=2;   //���ﵽʱ��ǵ��޸Ļ���
     private static SQLiteDatabase db;
     private static conversationDB condb;
     private static conversationdbHelper dbHelper;
     private conversationDB(Context context){
    	 dbHelper=new conversationdbHelper(context, "conversationDB", null, VERSION);
    	 db=dbHelper.getWritableDatabase();
     }
     public synchronized static conversationDB getInstance(Context context){
    	 if(condb==null){
    		 condb=new conversationDB(context);
    	 }
    	 return condb;
     }
     public void saveId(String id){
    	 ContentValues values=new ContentValues();
    	 values.put("id", id);
    	 int i= db.update("conversation",values, "id=?",new String[]{id});//�������Ϊ���ж��Ƿ���֮ǰ�ģ�������update��ԭ��������insert����ʾ����ģ���Ӱ�����������0��ʱ���ʾ֮ǰû�о�ֱ�Ӳ���
    	 if(i==0){
        	values.put("unReadNum", 0);
    	 	db.insert("conversation", null, values);
     	}
     }
     
   
     public void saveNickById(String id,String nick){
    	 ContentValues values=new ContentValues();
    	 values.put("nickName",nick);
    	 db.update("conversation", values,"id=?",new String[]{id});
              
     }
     public String getNickById(String id){
    	 String nickName="";
    	 Cursor cursor= db.query("conversation",new String[]{"nickName"},"id=?",new String[]{id}, null, null, null);
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
    	  db.update("conversation", values,"id=?", new String[]{id});
     }
     public int getUnReadNumById(String id){
    	 int i=0;
    	 Cursor cursor=db.query("conversation", new String []{"unReadNum"},"id=?",new String[]{id}, null, null, null);
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
   	     db.update("conversation", values,"id=?", new String[]{Id});
     }
     
}
