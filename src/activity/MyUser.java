package activity;

import android.R.integer;
import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	private String sex;
	private String nick;
	private int age;
	private String constellation;
	private String shengri;
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
	public int getAge(){
		return this.age;
	}
	public void setAge(int age){
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
}
