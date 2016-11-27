package model;

public class County {
	     private String id;
	     private String countyName;
	     private String countyCode;
	     private String cityName;
	     public void setId(String p1)
	     {
	    	 this.id=p1;
	     }
	     public String getId()
	     {
	    	 return id;
	     }
	     public void setCountyName(String name)
	     {
	    	 this.countyName=name;
	     }
	     public String getCountyName()
	     {
	    	 return countyName;
	     }
	     public void setCountyCode(String code)
	     {
	    	 this.countyCode=code;
	     }
	     public String getCountyCode()
	     {
	    	 return countyCode;
	     }
	     public void setCityName(String cityName)
	     {
	    	 this.cityName=cityName;
	     }
	     public String getCityName()
	     {
	    	 return cityName;
	     }

}
