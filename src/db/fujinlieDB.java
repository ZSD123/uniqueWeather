package db;

import java.util.ArrayList;
import java.util.List;

import activity.MyUser;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class fujinlieDB {    //这就是一个每个登录用户的数据库，里面有conversation数据表等等,0的时候是陌生人，1的时候是朋友，2的时候是黑名单,3的时候被对方拉黑
    private int VERSION=1;
    private static SQLiteDatabase db;
    public  static fujinlieDB fujinliedb;
    private static fujinliedbHelper dbHelper;
    String DB_name="fujinlieDB";
    String TABLE_name="fujinlie";
    
     public fujinlieDB(Context context){
   	 dbHelper=new fujinliedbHelper(context, DB_name, null, VERSION);
   	 db=dbHelper.getWritableDatabase();
    }
    public synchronized static fujinlieDB getInstance(Context context){
   	 if(fujinliedb==null){
   		 fujinliedb=new fujinlieDB(context);
   	 }
   	 return fujinliedb;
    }
    public void saveEverythingById(String id,String touXiang,String nickName,String sex,String distance){

    
     	    	ContentValues values=new ContentValues();
     	    	values.put("touXiang", touXiang);
     	    	values.put("nickName", nickName);
     	    	values.put("sex", sex);
     	    	values.put("distance", distance);
     	    	db.update(TABLE_name, values,"id=?",new String []{id});
     	    
        
    }
      public void saveLatAndLon(String id,String lat,String lon){
    	ContentValues values=new ContentValues();
    	values.put("id", id);
    	values.put("lat", lat);
    	values.put("lon", lon);
    	db.insert(TABLE_name, null, values);
      }
      public List<MyUser> getMyUsers(){
    	  List<MyUser> list=new ArrayList<MyUser>();
    	  Cursor cursor=db.query(TABLE_name,null,null, null,null, null, null);
    	  if(cursor.moveToFirst()){
    		  do {
				    MyUser myUser=new MyUser();
				    myUser.setObjectId(cursor.getString(cursor.getColumnIndex("id")));
				    myUser.setTouXiangUrl(cursor.getString(cursor.getColumnIndex("touXiang")));
				    myUser.setNick(cursor.getString(cursor.getColumnIndex("nickName")));
				    myUser.setSex(cursor.getString(cursor.getColumnIndex("sex")));
				    list.add(myUser);
				    
			} while (cursor.moveToNext());
    	  }
    	  return list;
      }
      public int getDistanceById(String id){
    	  Cursor cursor=db.query(TABLE_name,null,"id=?",new String[]{id},null, null, null);
    	  int distance=0;
    	  if(cursor.moveToFirst()){
    		  do {
				    distance=Integer.parseInt(cursor.getString(cursor.getColumnIndex("distance")));
			} while (cursor.moveToNext());
    	  }
    	  return distance;
      }
      public double [] getLatbyId(String objectId){
    	  double lat = 0;
    	  double lon= 0;
    	  Cursor cursor=db.query(TABLE_name,null, "id=?",new String[]{objectId},null, null, null);
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
      public void deleteAll(){
			db.delete(TABLE_name,null,null);
      
      }
    
}
