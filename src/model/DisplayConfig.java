package model;



import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @author :smile
 * @project:DisplayConfig
 * @date :2016-01-25-09:19
 * ע������PicassoԲ�Ǵ�����������������
 */
public class DisplayConfig {

    /**UILĬ�ϵ���ʾ����:Բ��
     * @param defaultRes
     * @return
     */
    public static DisplayImageOptions getDefaultOptions(boolean hasRounded,int defaultRes){
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���
                .cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����
                .considerExifParams(true)  //�Ƿ���JPEGͼ��EXIF��������ת����ת��
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ
                .bitmapConfig(Bitmap.Config.RGB_565)//����ͼƬ�Ľ�������:����ΪRGB565����Ĭ�ϵ�ARGB_8888Ҫ��ʡ�������ڴ�
//                .delayBeforeLoading(100)//����ͼƬǰ������ʱ����������廬����������
                .resetViewBeforeLoading(true);//����ͼƬ������ǰ�Ƿ����ã���λ
                if(hasRounded){
                    builder.displayer(new RoundedBitmapDisplayer(12));//�Ƿ�����ΪԲ�ǣ�����Ϊ����
                }
                if(defaultRes!=0){
                    builder.showImageForEmptyUri(defaultRes)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
//                            .showImageOnLoading(defaultRes) //����ͼƬ�������ڼ���ʾ��ͼƬ-->Ӧ��ȥ��-�����ListView��ͼƬ��˸
                            .showImageOnFail(defaultRes);  //����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
                }
        return builder.build();//�������
    }
}
