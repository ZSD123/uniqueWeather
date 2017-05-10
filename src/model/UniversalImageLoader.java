package model;



import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * ʹ��UILͼƬ��ܼ���ͼƬ������������չ����ͼƬ��ܣ�����glide��fresco
 * Created by Administrator on 2016/5/24.
 */
public class UniversalImageLoader implements ILoader{

    public UniversalImageLoader(){}

    @Override
    public void loadAvator(ImageView iv, String url, int defaultRes) {
        if(!TextUtils.isEmpty(url)){
            display(iv,url,true,defaultRes,null);
        } else {
            iv.setImageResource(defaultRes);
        }
    }

    @Override
    public void load(ImageView iv, String url, int defaultRes,ImageLoadingListener listener) {
        if(!TextUtils.isEmpty(url)){
            display(iv,url.trim(),false,defaultRes,listener);
        } else {
            iv.setImageResource(defaultRes);
        }
    }

    /**
     * չʾͼƬ
     * @param iv
     * @param url
     * @param defaultRes
     * @param listener
     */
    private void display(ImageView iv,String url,boolean isCircle,int defaultRes,ImageLoadingListener listener){
        if(!url.equals(iv.getTag())){//����tag��ǣ�����UIL��display����
            iv.setTag(url);
            //��ֱ��display imageview��ΪImageAware�����ListView����ʱ�ظ�����ͼƬ
            ImageAware imageAware = new ImageViewAware(iv, false);
            ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfig.getDefaultOptions(isCircle,defaultRes),listener);
        }
    }

    /**
     * ��ʼ��ImageLoader
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);
        config.memoryCache(new WeakMemoryCache());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
