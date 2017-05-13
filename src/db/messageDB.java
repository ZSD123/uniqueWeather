package db;



import android.R.integer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class messageDB {
     private int VERSION=1;
     private static messageDB messagedb;
     private String db_name="message";
     private SQLiteDatabase db;
     private messagedbHelper dbHelper;
     public messageDB(Context context){
    	 dbHelper=new messagedbHelper(context, db_name, null, VERSION);
    	 db=dbHelper.getWritableDatabase();
     }
     public static synchronized messageDB getInstance(Context context){
    	 if(messagedb==null){
    		 messagedb=new messageDB(context);
    	 }
    	 return messagedb;
     }
}
