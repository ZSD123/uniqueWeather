package db;

import org.apache.http.impl.io.ChunkedInputStream;

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
import android.text.style.UpdateAppearance;

public class myUserDB {
	private int VERSION=1;
	private String dB_name="myUserDB";
	private myUserdbHelper dbhHelper;
	private SQLiteDatabase db;
	private static myUserDB myUserdb;
	public myUserDB(Context context)
	{
		dbhHelper=new myUserdbHelper(context, dB_name, null, VERSION);
		db=dbhHelper.getWritableDatabase();
	}
    public synchronized static myUserDB getInstance(Context context)
    {
    	if(myUserdb==null)
    	{
    		myUserdb=new myUserDB(context);
    	}
    	return myUserdb;
    }
    public void saveUserName(String account,String username,int chenhu){
    	if(username!=null&&account!=null){
    		ContentValues values=new ContentValues();
    		values.put("account", account);
    		values.put("username", username);
    		values.put("chenhuOnce",chenhu);
            db.insert("myuser", null, values);
    	}
    }
    public void updateUserName(String account,String username){
    	if(username!=null&&account!=null){
    		ContentValues values=new ContentValues();
    		values.put("username", username);
            db.update("myuser", values,"account=?", new String []{account});
    	}
    }
    public String loadUserName(String account){
    	String username = "";
    	Cursor cursor=db.query("myuser", new String []{"username"},"account=?", new String[]{account}, null, null, null);
    	if(cursor.moveToFirst()){
    		do {
    			 username=cursor.getString(cursor.getColumnIndex("username"));
			} while (cursor.moveToNext());
    		
    	}
    	return username;
    }
    public void saveUserPic(String account,String userPic){
    	if(account!=null&&userPic!=null){
    		ContentValues values=new ContentValues();
    		values.put("userPic",userPic);
    		db.update("myuser", values, "account=?", new String []{account});
    	}
    }
    public String loadUserPic(String account){
    	String userPic="";
    	Cursor cursor=db.query("myuser",new String []{"userPic"}, "account=?", new String []{account}, null, null, null);
    	if(cursor.moveToFirst()){
    		do {
				userPic=cursor.getString(cursor.getColumnIndex("userPic"));
			} while (cursor.moveToNext());
    	}
    	return userPic;
    }
    public int loadChenhuOnce(String account){
    	int chenhu=0;
    	Cursor cursor=db.query("myuser",new String []{"chenhuOnce"}, "account=?", new String []{account}, null, null, null);
    	if(cursor.moveToFirst()){
    		do {
				chenhu=cursor.getInt(cursor.getColumnIndex("chenhuOnce"));
			} while (cursor.moveToNext());
    	}
    	return chenhu;
    }
    public void updateUserPic(String account,String userPic){
    	if(userPic!=null&&account!=null){
    		ContentValues values=new ContentValues();
    		values.put("userPic", userPic);
            db.update("myuser", values,"account=?", new String []{account});
    	}
    }
    public void updateChenhu(String account){
    	ContentValues values=new ContentValues();
    	values.put("chenhuOnce", 1);
    	db.update("myuser", values,"account=?", new String[]{account});
    }
    public boolean checkCunZai(String account){
    	String acc=null;
    	Cursor cursor=db.query("myuser",null ,"account=?",new String[]{account}, null,null, null);
    	if(cursor.moveToFirst()){
    		do {
				acc=cursor.getString(cursor.getColumnIndex("account"));
			} while (cursor.moveToNext());
    	}
    	if(acc!=null){
    		return true;
    	}else {
			return false;
		}
    }
    public void checkandSaveUpdateN(String account,String username){
    	  if(checkCunZai(account)){
    		  updateUserName(account, username);
    	  }else {
			saveUserName(account, username,0);
		  }
    }
    public void checkandSaveUpdateP(String account,String userPic){
    	  if(checkCunZai(account)){
    		  updateUserPic(account, userPic);
    	  }else {
			saveUserPic(account, userPic);
		  }
    }
    public void checkandSaveUpdateC(String account,String username,int chenhu){
    	 if(checkCunZai(account)){
    		 updateChenhu(account);
    	 }else {
			  saveUserName(account, username,chenhu);
		   }
    }
}
