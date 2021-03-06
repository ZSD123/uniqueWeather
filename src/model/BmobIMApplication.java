package model;



import android.app.Activity;
import android.app.Application;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import message.myMessageHandler;


import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * @author :smile
 * @project:BmobIMApplication
 * @date :2016-01-13-10:19
 */
public class BmobIMApplication extends Application{
    private List<Activity> activities=new LinkedList<Activity>();
    private static BmobIMApplication INSTANCE;
    public static BmobIMApplication INSTANCE(){
        return INSTANCE;
    }
    
    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }
    public void add(Activity activity){
    	activities.add(activity);
    }
    public void remove(Activity activity){
    	activities.remove(activity);
    }
    public void delete(){
    	for (Activity activity:activities) {
			activity.finish();
		}
    	if(activities.size()==0)
    		activities.clear();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //初始化Logger
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            Bmob.initialize(this,"f3065817051f7c298d2e49d9329a2a6b", "bmob");
            
            BmobIM.init(this);

            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new myMessageHandler(getApplicationContext()));
            
            
        }
        //uil初始化
        UniversalImageLoader.initImageLoader(this);
        
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
