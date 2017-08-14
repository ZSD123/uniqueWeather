package model;

import activity.MyUser;
import cn.bmob.v3.BmobObject;

public class Black extends BmobObject {
     private MyUser myUser;
     private MyUser blackUser;
	public MyUser getMyUser() {
		return myUser;
	}
	public void setMyUser(MyUser myUser) {
		this.myUser = myUser;
	}
	public MyUser getBlackUser() {
		return blackUser;
	}
	public void setBlackUser(MyUser blackUser) {
		this.blackUser = blackUser;
	}
     
}
