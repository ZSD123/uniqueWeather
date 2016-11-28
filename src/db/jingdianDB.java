package db;

import java.util.ArrayList;
import java.util.List;

import model.jingdian;
import model.jingdiancity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class jingdianDB {
	private int VERSION=1;
	private String dB_name="jiandianDB";
	private jingdiandbHelper dbhHelper;
	private SQLiteDatabase db;
	private static jingdianDB jingdiandb;
	public jingdianDB(Context context)
	{
		dbhHelper=new jingdiandbHelper(context, dB_name, null, VERSION);
		db=dbhHelper.getWritableDatabase();
	}
    public synchronized static jingdianDB getInstance(Context context)
    {
    	if(jingdiandb==null)
    	{
    		jingdiandb=new jingdianDB(context);
    	}
    	return jingdiandb;
    }
    public void savejingdianCity(jingdiancity city)
    {
    	if(city!=null)
    	{
    		ContentValues values=new ContentValues();
    		values.put("jingdianCityId", city.getjingdiancityid());
    		values.put("jingdianCityName", city.getjingdiancityName());
    		db.insert("jingdianCity", null, values);
    	}
    }
    public jingdiancity loadjingdianCity(String name)     //找到相应的景点城市从而得到id
    {
    	jingdiancity city=new jingdiancity();
    	Cursor cursor=db.query("jingdianCity",null,"jingdianCityName=?", new String []{name}, null,null, null);
    	if(cursor.moveToFirst())
    	{
    		do{
    		
    			city.setjingdiancityid(cursor.getString(cursor.getColumnIndex("jingdianCityId")));
    			city.setjingdiancityName(cursor.getString(cursor.getColumnIndex("jingdianCityName")));
    			
    		}while(cursor.moveToNext());
    		
    	}
    	return city;
    }
    public void savejingdian(jingdian jing)
    {
    	if(jing!=null)
    	{
    		ContentValues values=new ContentValues();
    		values.put("jingdianTitle",jing.getjingdiantitle());
    		values.put("jingdianGrade", jing.getGrade());
    		values.put("price_min", jing.getPrice_min());
    		values.put("url", jing.geturlString());
    		values.put("cityid", jing.getCityId());
    		values.put("imageurl", jing.getImageUrl());
    		db.insert("jingdian", null, values);
    	}
    }
    public List<jingdian> loadjingdian(jingdiancity selectedJingdiancity)
    {
    	List<jingdian> datalist=new ArrayList<jingdian>();
    	Cursor cursor=db.query("jingdian",null,"cityid=?",new String []{selectedJingdiancity.getjingdiancityid()},null,null, null);
    	if(cursor.moveToFirst())
    	{
    		do{
    			jingdian jing=new jingdian();
    			jing.setCityId(cursor.getString(cursor.getColumnIndex("cityid")));
    			jing.setGrade(cursor.getString(cursor.getColumnIndex("jingdianGrade")));
    			jing.setImageUrl(cursor.getString(cursor.getColumnIndex("imageurl")));
    			jing.setjingdiantitle(cursor.getString(cursor.getColumnIndex("jingdianTitle")));
    			jing.setPrice_min(cursor.getString(cursor.getColumnIndex("price_min")));
    			jing.seturlString(cursor.getString(cursor.getColumnIndex("url")));
    			datalist.add(jing);
    		}while(cursor.moveToNext());
    		
    	}
    	return datalist;
    }
}
