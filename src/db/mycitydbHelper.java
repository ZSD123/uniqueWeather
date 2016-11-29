package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class mycitydbHelper extends SQLiteOpenHelper {
    
	public static String CREATE_MYCITY="create table mycity("
			+ "id integer primary key autoincrement,"
			+ "myCityName Text,"
			+ "myCityWeatherWeb Text,"    //网络地址
			+ "myCityWeatherLocal Text,"   //本地地址
			+ "myCityTemp Text,"
			+ "myCityPicWeb Text,"
			+ "myCityPicLocal Text)";
	
	
	public mycitydbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MYCITY);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
	

	}

}
