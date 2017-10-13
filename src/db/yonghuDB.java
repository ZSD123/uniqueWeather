package db;

import java.net.ContentHandler;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class yonghuDB {
      private int VERSION=1;
      private String db_name="yonghu";
      private SQLiteDatabase db;
      private yonghudbHelper dbHelper;
      private static yonghuDB yongDb;
      private yonghuDB(Context context){
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
      public void updateData(String objectId,String lat,String lon){   //当相应用户存在在数据表的时候，就可以直接更新它的地理位置信息
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
      public  void updateJiaZai1(String objectId){     //根据objectId来更新jiazai为1,这是用在用户未设置头像的时候下加载完就更新
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
      public int checkJiaZai(String objectId){      //通过objectId查询相应的jiazai位，这是用在没有设置头像的用户的时候
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
      public void saveData(String objectId,String nickName,String sex,String age,String zhiye,String username,boolean canCall){
    		ContentValues values=new ContentValues();
	    	values.put("nickName", nickName);
	    	values.put("sex", sex);
	    	values.put("age", age);
	    	values.put("zhiye", zhiye);
	    	values.put("userName", username);
	    	if(canCall){
	         	values.put("canCall", 1);
	
	    	}else {
	    		values.put("canCall", 0);
	   
	    	}
	    	db.update("yonghu", values,"objectId=?",new String[]{objectId});
      }
       public Bundle loadData(String objectId){
    	   Bundle bundle=new Bundle();
    	   Cursor cursor=db.query("yonghu", new String []{"nickName","sex","age","zhiye","touxiangUrl","userName","canCall"}, "objectId=?",new String[]{objectId}, null,null, null);
    	   if(cursor.moveToFirst()){
    		   do {
				  String nickName=cursor.getString(cursor.getColumnIndex("nickName"));
				  String sex=cursor.getString(cursor.getColumnIndex("sex"));
				  String age=cursor.getString(cursor.getColumnIndex("age"));
				  String zhiye=cursor.getString(cursor.getColumnIndex("zhiye"));
				  String touXiang=cursor.getString(cursor.getColumnIndex("touxiangUrl"));
				  String userName=cursor.getString(cursor.getColumnIndex("userName"));
				  int canCall=cursor.getInt(cursor.getColumnIndex("canCall"));
                  bundle.putString("nickName", nickName);
                  bundle.putString("sex", sex);
                  bundle.putString("age", age);
                  bundle.putString("zhiye", zhiye);
                  bundle.putString("touxiangUrl",touXiang);
                  bundle.putString("userName", userName);
                  if(canCall==1){
              
                	  bundle.putBoolean("canCall", true);
                  }else {
            
                	  bundle.putBoolean("canCall",false);
				  }
			} while (cursor.moveToNext());
    	   }
    	   return bundle;
       }
}
