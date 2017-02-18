package activity;

import android.R.integer;
import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	private String sex;
	private String nick;
	private String age;
	private String constellation;
	private String shengri;
	private int zhiye;
	private String school;
	private String suozaidi;
	private String guxiang;
	public String getSex(){
		return this.sex;
	}
	public void setSex(String sex){
		this.sex=sex;
	}
	public String getNick(){
		return this.nick;
	}
	public void setNick(String nick){
		this.nick=nick;
	}
	public String getAge(){
		return this.age;
	}
	public void setAge(String age){
		this.age=age;
	}
	public String getConstellation(){
		return this.constellation;
	}
	public void setConstellation(String constellation){
		this.constellation=constellation;
	}
	public String getShengri(){
		return this.shengri;
	}
	public void setShengri(String shengri){
		this.shengri=shengri;
	}
	public int getZhiye(){
		return this.zhiye;
	}
	public void setZhiye(int i){
		this.zhiye=i;
	}
	public String getSchool(){
		return this.school;
	}
	public void setSchool(String school){
		this.school=school;
	}
	public String getSuozaidi(){
		return this.suozaidi;
		
	}
	public void setSuozaidi(String suozaidi){
		this.suozaidi=suozaidi;
	}
	public String getGuxiang(){
		return this.guxiang;
	}
	public void setGuxiang(String guxiang){
		this.guxiang=guxiang;
	}
}
