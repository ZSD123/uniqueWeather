package model;



import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * �����ͼƬ���ؽӿ�
 * @author smile
 */
public interface ILoader {

    /**
     * ����Բ��ͷ��
     * @param iv
     * @param url
     * @param defaultRes
     */
    void loadAvator(ImageView iv, String url, int defaultRes);

    /**
     * ����ͼƬ����Ӽ�����
     * @param iv
     * @param url
     * @param defaultRes
     * @param listener
     */
    void load(ImageView iv,String url,int defaultRes,ImageLoadingListener listener);

}
