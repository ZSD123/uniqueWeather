package adapter;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import myCustomView.CircleImageView;

import com.amap.api.services.a.o;
import com.uniqueweather.app.R;

import butterknife.ButterKnife;
import activity.baseActivity;
import activity.baseFragmentActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

  OnRecyclerViewListener onRecyclerViewListener;
  protected Context context;

  public BaseViewHolder(Context context, ViewGroup root,OnRecyclerViewListener listener,View view) {
	super(view);
    this.context=context;
    ButterKnife.bind(this, itemView);
    this.onRecyclerViewListener =listener;
    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(this);
  }

  public Context getContext() {
    return itemView.getContext();
  }

  public abstract void bindData(T t);

  private Toast toast;
  public void toast(final Object obj) {
    try {
      ((baseFragmentActivity)context).runOnUiThread(new Runnable() {

        @Override
        public void run() {
          if (toast == null)
            toast = Toast.makeText(context,"", Toast.LENGTH_SHORT);
          toast.setText(obj.toString());
          toast.show();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClick(View v) {
    if(onRecyclerViewListener!=null){
      onRecyclerViewListener.onItemClick(getPosition());
    }
  }

  @Override
  public boolean onLongClick(View v) {
    if(onRecyclerViewListener!=null){
      onRecyclerViewListener.onItemLongClick(getPosition());
    }
    return true;
  }
  public Bitmap createVideoThumbnail(String url, int width, int height) {
      Bitmap bitmap = null;
      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      int kind = MediaStore.Video.Thumbnails.MINI_KIND;
      try {
          if (Build.VERSION.SDK_INT >= 14) {
              retriever.setDataSource(url, new HashMap<String, String>());
          } else {
              retriever.setDataSource(url);
          }
          bitmap = retriever.getFrameAtTime();
      } catch (IllegalArgumentException ex) {
          // Assume this is a corrupt video file
      } catch (RuntimeException ex) {
          // Assume this is a corrupt video file.
      } finally {
          try {
              retriever.release();
          } catch (RuntimeException ex) {
              // Ignore failures while cleaning up.
          }
      }
      if (kind == Images.Thumbnails.MICRO_KIND && bitmap != null) {
          bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                  ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
      }
      return bitmap;
  }
  public Bitmap getVideoThumbnail(String videoPath) {
	  MediaMetadataRetriever media =new MediaMetadataRetriever();
	  media.setDataSource(videoPath);
	  Bitmap bitmap = media.getFrameAtTime();
	  return bitmap;
	}
  protected void setTouXiangImage(File file,CircleImageView iv_avatar){

	      try {
			InputStream is=new FileInputStream(file);
			
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
			
			opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
			opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
			
			opts.inSampleSize=2;
			opts.inInputShareable=true;//设置解码位图的尺寸信息
			
			Bitmap bitmap2=BitmapFactory.decodeStream(is, null, opts);
			iv_avatar.setImageBitmap(bitmap2);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

  }
  protected void setTouXiangWithResource(File file,CircleImageView iv_avatar){
	  try {
			InputStream is=new FileInputStream(file);
			
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
			
			opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
			opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
			
			opts.inSampleSize=2;
			opts.inInputShareable=true;//设置解码位图的尺寸信息
			
			Bitmap bitmap2=BitmapFactory.decodeResource(context.getResources(),R.drawable.userpicture, opts);
			iv_avatar.setImageBitmap(bitmap2);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

  }

}