package model;

import java.net.URL;

import activity.MyUser;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class shareObject extends BmobObject {
      /**
	 * 
	 */
	  private MyUser myUser;     //����������
      private MyUser receiveUser;  //����������
   
	  private String title;      //����
      private String description;   //����
      private BmobGeoPoint objectPoint;   //����λ��
      private Boolean isComplete;    //�Ƿ����
      
      private Boolean isWorking;   //�Ƿ����ڽ���
      
      public Boolean getIsWorking() {
		return isWorking;
	}
	public void setIsWorking(Boolean isWorking) {
		this.isWorking = isWorking;
	}
	private String imageUrl;   //�����ͼƬ
      
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
