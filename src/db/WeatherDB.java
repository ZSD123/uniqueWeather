package db;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDB {
	public static final String DB_name="unique_weather";
	public static final int VERSION=3;
	public static WeatherDB weatherDB;
	public SQLiteDatabase db;
	private WeatherDB(Context context)
	{ 
		weatherdbHelper dbhelper=new weatherdbHelper(context,DB_name,null,VERSION);
		db=dbhelper.getWritableDatabase();
	}
	public synchronized static WeatherDB getInstance(Context context)
	{
		if(weatherDB==null)
		{
			weatherDB=new WeatherDB(context);
		}
		return weatherDB;
	}
	public void saveProvince(Province province)
	{
		if(province!=null)
		{
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("province", null, values);
		}
	}
	public List<Province> loadProvince()
	{
		List<Province> list=new ArrayList<Province> ();
		Cursor cursor=db.query("province", null, null, null,null, null,null);
		if(cursor.moveToFirst())
		{
			do{
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getInt(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		return list;
	}
	public void saveCity(City city)
	{
		if(city!=null)
		{
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_name", city.getProvinceName());
			db.insert("city", null, values);
		}
	}
	public List<City> loadCity(String provinceName)
	{
		List<City> list=new ArrayList<City> ();
		Cursor cursor=db.query("city", null,"province_name=?", new String[]{provinceName},null, null,null);
		if(cursor.moveToFirst())
		{
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
	}
	public void saveCounty(County county)
	{
		if(county!=null)
		{
			ContentValues values=new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_name", county.getCityName());
			db.insert("county", null, values);
		}
	}
	public List<County> loadCounty(String cityName)
	{
		List<County> list=new ArrayList<County> ();
		Cursor cursor=db.query("county", null,"city_name=?" ,new String[]{cityName},null, null,null);
		if(cursor.moveToFirst())
		{
			do{
				County county=new County();
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				list.add(county);
			}while(cursor.moveToNext());
		}
		return list;
	}

}
