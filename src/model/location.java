package model;

import activity.MyUser;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class location extends BmobObject {
      private MyUser myUser;
      private BmobGeoPoint myLocation;
	public MyUser getMyUser() {
		return myUser;
	}
	public void setMyUser(MyUser myUser) {
		this.myUser = myUser;
	}
	public BmobGeoPoint getMyLocation() {
		return myLocation;
	}
	public void setMyLocation(BmobGeoPoint myLocation) {
		this.myLocation = myLocation;
	}
      
}
