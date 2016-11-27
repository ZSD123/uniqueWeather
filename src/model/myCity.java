package model;

import android.widget.ImageView;
import android.widget.TextView;

public class myCity {
     public String myCityName;
     public String myCityWeather;
     public String myCityTemp;
     public String myCityPic;
     public  myCity(String name,String weather,String temp,String pic)
     {
    	 this.myCityName=name;
    	 this.myCityWeather=weather;
    	 this.myCityTemp=temp;
    	 this.myCityPic=pic;

     }
     public  String getMyCityName()
     {
    	 return myCityName;
     }
     public String getMyCityWeather()
     {
    	 return myCityWeather;
     }
     public String getMyCityTemp()
     {
    	 return myCityTemp;
     }
     public String getMyCityPic()
     {
    	 return myCityPic;
     }

}
