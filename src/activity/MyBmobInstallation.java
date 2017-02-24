package activity;

import android.content.Context;
import cn.bmob.v3.BmobInstallation;

public class MyBmobInstallation extends BmobInstallation {
     private String uid;
     public MyBmobInstallation(){
    	 super();
    	 this.setTableName("_Installation");
     }
     public String getUid(){
    	 return uid;
     }
     public void setUid(String uid){
    	 this.uid=uid;
    	 
     }
     
}
