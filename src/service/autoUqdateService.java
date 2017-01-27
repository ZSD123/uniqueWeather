package service;

import receiver.uqdateReceive;
import Util.Http;
import Util.HttpCallbackListener;
import Util.Utility;
import activity.weather_info;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

public class autoUqdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int  onStartCommand(Intent intent,int startId,int flags)
	{
		new Thread(new Runnable(){
		@Override
		public void run(){
			uqdateWeather();
		}}).start();
		AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
		int anHour=8*60*60*1000;
	    long triggerTime=SystemClock.elapsedRealtime()+anHour;
	    Intent i=new Intent(this,uqdateReceive.class);
	    PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
	    manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime,pi );
	    return super.onStartCommand(intent,startId,flags);
		
	}
	public void uqdateWeather()
	{
		SharedPreferences pre=PreferenceManager.getDefaultSharedPreferences(this);
		String coName=pre.getString("locDistrict", "");
		Http.sendWeatherRequest(coName,weather_info.address3,new HttpCallbackListener()
		{
			@Override
			public void onFinish(String response)
			{
				Utility.handleWeather(response,autoUqdateService.this);
				 
			}
		});
	}

}
