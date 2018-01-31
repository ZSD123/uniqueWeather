package model;

import java.net.URL;

import activity.MyUser;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class shareObject extends BmobObject {
      /**
	 * 
	 */
	  private MyUser myUser;     //持有者名字
      private MyUser receiveUser;  //接收者名字
   
	  private String title;      //名字
      private String description;   //描述
      private BmobGeoPoint objectPoint;   //地理位置
      private Boolean isComplete;    //是否完成
      
      private Boolean isWorking;   //是否正在进行
      
      public Boolean getIsWorking() {
		return isWorking;
	}
	public void setIsWorking(Boolean isWorking) {
		this.isWorking = isWorking;
	}
	private String imageUrl;   //共享的图片
      
      public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public MyUser getReceiveUser() {
  		return receiveUser;
  	  }
     	public void setReceiveUser(MyUser receiveUser) {
  		this.receiveUser = receiveUser;
  	   }
      
	public Boolean getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
	public MyUser getMyUser() {
		return myUser;
	}
	public void setMyUser(MyUser myUser) {
		this.myUser = myUser;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BmobGeoPoint getObjectionPoint() {
		return objectPoint;
	}
	public void setObjectionPoint(BmobGeoPoint objectionPoint) {
		this.objectPoint = objectionPoint;
	}
      
}
