package model;

import activity.MyUser;
import android.R.integer;
import cn.bmob.v3.BmobObject;

public class Jubao extends BmobObject{
      private Integer sex;
      private Integer saorao;
      private MyUser myUser;
      private Integer anticountry;
      private Integer sum;
	public Integer getSum() {
		return sum;
	}
	public void setSum(Integer sum) {
		this.sum = sum;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public void setSaorao(Integer saorao) {
		this.saorao = saorao;
	}
	public void setAnticountry(Integer anticountry) {
		this.anticountry = anticountry;
	}
	public MyUser getMyUser() {
		return myUser;
	}
	public void setMyUser(MyUser myUser) {
		this.myUser = myUser;
	}
	public Integer getSex() {
		return sex;
	}
	public Integer getSaorao() {
		return saorao;
	}
	public Integer getAnticountry() {
		return anticountry;
	}

      
}
