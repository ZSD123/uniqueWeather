package model;

import activity.MyUser;
import cn.bmob.v3.BmobObject;

public class Friend extends BmobObject {
    //”√ªß 
	private MyUser myUser;
    //∫√”—
	private MyUser friendUser;

	public MyUser getMyUser() {
		return myUser;
	}
	public void setMyUser(MyUser myUser) {
		this.myUser = myUser;
	}
	public MyUser getFriendUser() {
		return friendUser;
	}
	public void setFriendUser(MyUser friendUser) {
		this.friendUser = friendUser;
	}
}
