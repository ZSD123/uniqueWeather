package db;

import activity.MyUser;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class conversationdbHelper extends SQLiteOpenHelper {
	
    public static final String CREATE_CONVERSATION="create table conversation ("
    		+ "id Text ,"
    		+ "fromId Text,"  //���˻�
    		+ "nickName Text,"
    		+ "touXiang Text,"
    		+ "unReadNum Integer,"
    		+ "newTime Integer,"
    		+ "newContent Text,"
    		+ "isFriend Integer)";   //0��ʱ����İ���ˣ�1��ʱ�������ѣ�2��ʱ���Ǻ�����
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
	
	}

}
