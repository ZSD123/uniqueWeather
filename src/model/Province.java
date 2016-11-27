package model;

public class Province {
     private int id;
     private String provinceName;
     private int provinceCode;
     public void setId(int id)
     {
    	 this.id=id;
     }
     public int getId()
     {
    	 return id;
     }
     public void setProvinceName(String name)
     {
    	 this.provinceName=name;
     }
     public String getProvinceName()
     {
    	 return provinceName;
     }
     public void setProvinceCode(int i)
     {
    	 this.provinceCode=i;
     }
     public int getProvinceCode()
     {
    	 return provinceCode;
     }

}
