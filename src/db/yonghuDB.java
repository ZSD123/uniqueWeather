package db;

import java.net.ContentHandler;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.bmob.push.a.in;

public class yonghuDB {
      private int VERSION=1;
      private String db_name="yonghu";
      private SQLiteDatabase db;
      private yonghudbHelper dbHelper;
      private static yonghuDB yongDb;
      public yonghuDB(Context context){
    	  dbHelper=new yonghudbHelper(context,db_name,null,VERSION);
    	  db=dbHelper.getWritableDatabase();
      }
      public synchronized static yonghuDB getInstance(Context context){
    	  if(yongDb==null){
    		  yongDb=new yonghuDB(context);
    	  }
    	  return yongDb;
      }
      public void saveObjectIdandlatlon(String objectId,String lat,String lon){
    	    if(objectId!=null){
    	    	ContentValues values=new ContentValues();
    	    	values.put("objectId", objectId);
    	    	values.put("lat", lat);
    	    	values.put("lon", lon);
    	    	db.insert("yonghu", null, values);
    	    }
      }
      public List<String> loadObjectId(){
    	  List<String> list=new ArrayList<String>();
    	  Cursor cursor=db.query("yonghu",null,null, null,null, null, null);
    	  if(cursor.moveToFirst()){
    		  do {
				    list.add(cursor.getString(cursor.getColumnIndex("objectId")));
				    
			} while (cursor.moveToNext());
    	  }
    	  return list;
      }
      public void saveUserNameandTouxiangUrl(String objectId,String userName,String touxiangUrl){
    	  if(userName!=null){
    		   ContentValues values=new ContentValues();
    		   values.put("userName", userName);
    		   values.put("touxiangUrl", touxiangUrl);
    		   db.update("yonghu", values, "objectId=?",new String[]{objectId});
    	  }
      }
    
      public String loadUserTouxiangUrl(String objectId){
    	  String touxiangUrl = null; 
    	  Cursor cursor=db.query("yonghu", null,"objectId=?", new String []{objectId}, null,null, null);
    	   if(cursor.moveToNext()){
    		   do{
    			   touxiangUrl=cursor.getString(cursor.getColumnIndex("touxiangUrl"));
    		   }while(cursor.moveToNext());
    	   }
    	   return touxiangUrl;
      }
}
