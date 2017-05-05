package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class messagedbHelper extends SQLiteOpenHelper {
    
	public static final String CREATE_MESSAGE="create table message("
			+ "id integer primary key autoincrement,"
			+ "nickName0 Text,"
			+ "objectId0 Text,"
			+ "nickName1 Text,"
			+ "objectId1 Text,"
			+ "date Text,"
			+ "senoracc integer,"
			+ "ourText Text,"
			+ "weidu Integer)";
	public messagedbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MESSAGE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
