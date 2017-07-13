package db;

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
     private int VERSION=1;
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
    	 int i= db.update("conversation",values, "conversationTitle=?",new String[]{title});
    	 if(i==0){
    	 	db.insert("conversation", null, values);
     	}
     }
     
   
     public void saveNickByTitle(String title,String nick){
    	 ContentValues values=new ContentValues();
    	 values.put("nickName",nick);
    	int row= db.update("conversation", values,"conversationTitle=?",new String[]{title});

     }
     public String loadNickByTitle(String title){
    	 String nickName="";
    	 Cursor cursor= db.query("conversation",new String[]{"nickName"},"conversationTitle=?",new String[]{title}, null, null, null);
    	 if(cursor.moveToFirst()){
    		 do {
				nickName=cursor.getString(cursor.getColumnIndex("nickName"));
			} while (cursor.moveToNext());
    	 }
    	 return nickName;
     }
   
}
