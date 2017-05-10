package model;



import android.app.Application;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;


import cn.bmob.newim.BmobIM;

/**
 * @author :smile
 * @project:BmobIMApplication
 * @date :2016-01-13-10:19
 */
public class BmobIMApplication extends Application{

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

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //��ʼ��Logger
        //ֻ�����������е�ʱ�����Ҫ��ʼ��
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im��ʼ��
            BmobIM.init(this);
            //ע����Ϣ������
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
        //uil��ʼ��
        UniversalImageLoader.initImageLoader(this);
    }

    /**
     * ��ȡ��ǰ���еĽ�����
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
