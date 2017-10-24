package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class fujinliedbHelper extends SQLiteOpenHelper {
	
    private  final String CREATE_FUJINLIE="create table fujinlie ("
    		+ "id Text ,"
    		+ "nickName Text,"
    		+ "touXiang Text,"
    		+ "sex Text,"
    		+ "distance Text,"
    		+ "city Text,"
    		+ "lat Text,"
    		+ "lon Text)";
	public fujinliedbHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_FUJINLIE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
	}

}
