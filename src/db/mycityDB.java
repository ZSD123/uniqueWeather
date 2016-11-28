package db;

import java.util.ArrayList;
import java.util.List;

import model.myCity;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class mycityDB {
	public int VERSION=1;
	public String db_name="mycityDB";
	public SQLiteDatabase db;
	public mycitydbHelper dbhelper;
	public static mycityDB mycitydb;
	public mycityDB(Context context)
	{
		dbhelper=new mycitydbHelper(context, db_name,null, VERSION);
		db=dbhelper.getWritableDatabase();
	}
	public static synchronized mycityDB getInstance(Context context)
	{
		if (mycitydb==null) 
		{
			mycitydb=new mycityDB(context);
		}
		return mycitydb;
	}
	public void saveMyCity(myCity mycity)
	{
		if(mycity!=null)
		{
			ContentValues values=new ContentValues();
			values.put("myCityName",mycity.getMyCityName());
			values.put("myCityWeather",mycity.getMyCityWeather());
			values.put("myCityTemp", mycity.getMyCityTemp());
			values.put("myCityPic",mycity.getMyCityPic());
			db.insert("mycity", null, values);
		}
	}
	public List<myCity> loadMyCity()
	{
		List<myCity> datalist=new ArrayList<myCity>();
		Cursor cursor=db.query("mycity",null, null, null,null, null, null);
		if(cursor.moveToFirst())
		{
			do{
				myCity mycity=new myCity(cursor.getString(cursor.getColumnIndex("myCityName")),cursor.getString(cursor.getColumnIndex("myCityWeather")),cursor.getString(cursor.getColumnIndex("myCityTemp")), cursor.getString(cursor.getColumnIndex("myCityPic")));
				datalist.add(mycity);
			}while(cursor.moveToNext());
		}
		return datalist;
	}
	public int loadmycityId()
	{
		int i = 0;
		Cursor cursor=db.query("mycity", null, null,null,null,null,null);
		if(cursor.moveToFirst())
		{
			do{
				i=cursor.getInt(cursor.getColumnIndex("id"));
			}while(cursor.moveToNext());
		}
		return i;
	}
	public String loadmycityPic(String name)
	{
		String pic = null;
		Cursor cursor=db.query("mycity", null,"myCityName=?", new String[]{name},null,null, null);
		if(cursor.moveToFirst())
		{
			do{
				 pic=cursor.getString(cursor.getColumnIndex("myCityPic"));
			}while(cursor.moveToNext());
			
		}
		return pic;
		
	}
	public String[] loadmycityName()
	{   int i=0;
		String[] myCityName = null;
		Cursor cursor=db.query("mycity",null ,null, null, null,null, null);
		if(cursor.moveToFirst())
		{
			do{
				myCityName[i]=cursor.getString(cursor.getColumnIndex("myCityName"));
				i++;
			}while(cursor.moveToNext());
		}
		return myCityName;
	}

}
