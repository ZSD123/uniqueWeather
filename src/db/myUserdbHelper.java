package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class myUserdbHelper extends SQLiteOpenHelper {
    
	public static String CREATE_MYUSER="create table myuser("
			+ "id integer primary key autoincrement,"
			+ "account Text,"
			+ "username Text,"
			+ "userPic Text,"
			+ "chenhuOnce integer)";
			
	public myUserdbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MYUSER);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
