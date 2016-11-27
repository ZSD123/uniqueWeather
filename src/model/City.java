package model;

public class City {
	     private int id;
	     private String cityName;
	     private String cityCode;
	     private String provinceName;
	     public void setId(int id)
	     {
	    	 this.id=id;
	     }
	     public int getId()
	     {
	    	 return id;
	     }
	     public void setCityName(String name)
	     {
	    	 this.cityName=name;
	     }
	     public String getCityName()
	     {
	    	 return cityName;
	     }
	     public void setCityCode(String code)
	     {
	    	 this.cityCode=code;
	     }
	     public String getCityCode()
	     {
	    	 return cityCode;
	     }
	     public void setProvinceName(String provinceName)
	     {
	    	 this.provinceName=provinceName;
	     }
	     public String getProvinceName()
	     {
	    	 return provinceName;
	     }

}
