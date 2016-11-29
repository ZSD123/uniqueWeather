package model;

import android.widget.ImageView;
import android.widget.TextView;

public class myCity {
     public String myCityName;
     public String myCityWeatherWeb;
     public String myCityWeatherLocal;
     public String myCityTemp;
     public String myCityPicWeb;
     public String myCityPicLocal;
     public  myCity(String name,String weatherWeb,String weatherLocal,String temp,String picWeb,String picLocal)
     {
    	 this.myCityName=name;
    	 this.myCityWeatherWeb=weatherWeb;
    	 this.myCityWeatherLocal=weatherLocal;
    	 this.myCityTemp=temp;
    	 this.myCityPicWeb=picWeb;
    	 this.myCityPicLocal=picLocal;

     }
     public  String getMyCityName()
     {
    	 return myCityName;
     }
     public String getMyCityWeatherWeb()
     {
    	 return myCityWeatherWeb;
     }
     public String getMyCityWeatherLocal()
     {
    	 return myCityWeatherLocal;
     }
     public String getMyCityTemp()
     {
    	 return myCityTemp;
     }
     public String getMyCityPicWeb()
     {
    	 return myCityPicWeb;
     }
     public String getMyCityPicLocal()
     {
    	 return myCityPicLocal;
     }

}
