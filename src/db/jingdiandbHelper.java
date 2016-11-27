package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class jingdiandbHelper extends SQLiteOpenHelper {
    public  static final String CREATE_JINGDIANCITY="create table jingdianCity("
    		+ "id integer primary key autoincrement,"
    		+ "jingdianCityId Text,"
    		+ "jingdianCityName Text)";
    public static final String CREATE_JINGDIAN="create table jingdian("
    		+ "id integer primary key autoincrement,"
    		+ "jingdianTitle Text,"
    		+ "jingdianGrade Text,"
    		+ "price_min Text,"
    		+ "url Text,"
    		+ "cityid Text,"
    		+ "imageurl Text)";
	public jingdiandbHelper(Context context,String name,CursorFactory factory,int version)
	{
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(CREATE_JINGDIANCITY);
		db.execSQL(CREATE_JINGDIAN);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}

}
