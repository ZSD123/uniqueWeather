package db;

import java.net.ContentHandler;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
    	    	values.put("jiazai", 0);
    	    	values.put("touxiangUrl","0");
    	    	db.insert("yonghu", null, values);
    	    }
      }
      public void updateData(String objectId,String lat,String lon){   //����Ӧ�û����������ݱ���ʱ�򣬾Ϳ���ֱ�Ӹ������ĵ���λ����Ϣ
    	  if(objectId!=null){
  	    	ContentValues values=new ContentValues();
  	    	values.put("lat", lat);
  	    	values.put("lon", lon);
  	    	db.update("yonghu", values,"objectId=?",new String []{objectId});
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
    	  Cursor cursor=db.query("yonghu",new String []{"touxiangUrl"},"objectId=?", new String []{objectId}, null,null, null);
    	   if(cursor.moveToNext()){
    		   do{
    			   touxiangUrl=cursor.getString(cursor.getColumnIndex("touxiangUrl"));
    		   }while(cursor.moveToNext());
    	   }
    	   return touxiangUrl;
      }
      public List<String> loadobjectIdWhereUrlemp(){
      	List<String> list=new ArrayList<String>();
      	Cursor cursor=db.query("yonghu",new String []{"objectId"}, "touxiangUrl=?",new String []{"0"},null, null, null);
      	if(cursor.moveToFirst()){
      		do {
				list.add(cursor.getString(cursor.getColumnIndex("objectId")));
			} while (cursor.moveToNext());
      	}
      	return list;
      }
      public List<String> loadUrl(){
    	  List<String> list=new ArrayList<String>();
    	  Cursor cursor=db.query("yonghu",null,"touxiangUrl!=?", new String []{"0"}, null, null,null);
    	  if(cursor.moveToFirst()){
    		  do {
				  list.add(cursor.getString(cursor.getColumnIndex("touxiangUrl")));
			} while (cursor.moveToNext());
    	  }
    	  return list;
      }
      public  void updateJiaZai0(String url){   
    	  ContentValues values=new ContentValues();
    	  values.put("jiazai", 0);
    	  db.update("yonghu", values,"touxiangUrl=?", new String[]{url});
      }
      public  void updateJiaZai1(String objectId){     //����objectId������jiazaiΪ1,���������û�δ����ͷ���ʱ���¼�����͸���
    	  ContentValues values=new ContentValues();
    	  values.put("jiazai", 1);
    	  db.update("yonghu", values,"objectId=?", new String[]{objectId});
      }
      public double [] loadLatbyId(String objectId){
    	  double lat = 0;
    	  double lon=0;
    	  Cursor cursor=db.query("yonghu",null, "objectId=?",new String[]{objectId},null, null, null);
    	  if(cursor.moveToFirst()){
    		  do {
				lat=Double.parseDouble(cursor.getString(cursor.getColumnIndex("lat")));
                lon=Double.parseDouble(cursor.getString(cursor.getColumnIndex("lon")));			
     		  } while (cursor.moveToNext());
    	  }
    	  double [] latlon=new double [2];
    	  latlon[0]=lat;
    	  latlon[1]=lon;
    	  return latlon;
      }
      public int checkJiaZai(String objectId){      //ͨ��objectId��ѯ��Ӧ��jiazaiλ����������û������ͷ����û���ʱ��
    	  int jiazai = 2;
    	  Cursor cursor=db.query("yonghu",new String []{"jiazai"},"objectId=?",new String[]{objectId}, null, null, null);
    	  if(cursor.moveToFirst()){
    		  do {
    			  jiazai=cursor.getInt(cursor.getColumnIndex("jiazai"));
			} while (cursor.moveToNext());
    	  }
    	  return jiazai;
      }
      public void deleteAll(){
				db.delete("yonghu",null,null);
	  }
   
       
}