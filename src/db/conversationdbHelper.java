package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class conversationdbHelper extends SQLiteOpenHelper {
    public static final String CREATE_CONVERSATION="create table conversation("
    		+ "id Text UNIQUE,"
    		+ "nickName Text,"
    		+ "unReadNum Integer)";
	public conversationdbHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CONVERSATION);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists conversation");
		onCreate(db);

	}

}
