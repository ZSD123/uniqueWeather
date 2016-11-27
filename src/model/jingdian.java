package model;

public class jingdian {
	public String jingdiantitle;
	public String grade;
	public String price_min;
	public String urlString;
	public String cityId;
	public String imageurl;
	public String getjingdiantitle()
	{
		return jingdiantitle;
	}
	public String getGrade()
	{
		return grade;
	}
	public String getPrice_min()
	{
		return price_min;
	}
	public String geturlString()
	{
		return urlString;
	}
	public String getImageUrl()
	{
		return imageurl;
	}
	public void setjingdiantitle(String title)
	{
		this.jingdiantitle=title;
	}
	public void setGrade(String grade)
	{
		this.grade=grade;
	}
	public void setPrice_min(String price)
	{
		this.price_min=price;
	}
	public void seturlString(String urlString)
	{
		this.urlString=urlString;
	}
	public void setImageUrl(String imageurl)
	{
		this.imageurl=imageurl;
	}
	public void setCityId(String cityId)
	{
		this.cityId=cityId;
	}
	public String getCityId()
	{
		return cityId;
	}
}
