package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class yonghudbHelper extends SQLiteOpenHelper {
    public static final String CREATE_YONGHU="create table yonghu("
    		+ "id integer primary key autoincrement, "
    		+ "objectId Text,"
    		+ "userName Text,"
    		+ "touxiangUrl Text,"
    		+ "lat Text,"
    		+ "lon Text)";
	public yonghudbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	     db.execSQL(CREATE_YONGHU);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}

}
