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
     public void saveTitle(String title){
    	 ContentValues values=new ContentValues();
    	 values.put("conversationTitle", title);
    	 int i= db.update("conversation",values, "conversationTitle=?",new String[]{title});//�������Ϊ���ж��Ƿ���֮ǰ�ģ�������update��ԭ��������insert����ʾ����ģ���Ӱ�����������0��ʱ���ʾ֮ǰû�о�ֱ�Ӳ���
    	 if(i==0){
        	values.put("unReadNum", 0);
    	 	db.insert("conversation", null, values);
     	}
     }
     
   
     public void saveNickByTitle(String title,String nick){
    	 ContentValues values=new ContentValues();
    	 values.put("nickName",nick);
    	 db.update("conversation", values,"conversationTitle=?",new String[]{title});
              
     }
     public String getNickByTitle(String title){
    	 String nickName="";
    	 Cursor cursor= db.query("conversation",new String[]{"nickName"},"conversationTitle=?",new String[]{title}, null, null, null);
    	 if(cursor.moveToFirst()){
    		 do {
				nickName=cursor.getString(cursor.getColumnIndex("nickName"));
			} while (cursor.moveToNext());
    	 }
    	 return nickName;
     }
     public void addUnReadNumByTitle(String title){
    	  int i=getUnReadNumByTitle(title);
    	  ContentValues values=new ContentValues();
    	  values.put("unReadNum",i+1);
    	  db.update("conversation", values,"conversationTitle=?", new String[]{title});
     }
     public int getUnReadNumByTitle(String title){
    	 int i=0;
    	 Cursor cursor=db.query("conversation", new String []{"unReadNum"},"conversationTitle=?",new String[]{title}, null, null, null);
    	 if(cursor.moveToFirst()){
    		 do {
				i=cursor.getInt(cursor.getColumnIndex("unReadNum"));
			} while (cursor.moveToNext());
    	 }
    	 return i;
     }
     public void clearUnReadNumByTitle(String title){
    	 ContentValues values=new ContentValues();
    	 values.put("unReadNum", 0);
   	     db.update("conversation", values,"conversationTitle=?", new String[]{title});
     }
     
}
