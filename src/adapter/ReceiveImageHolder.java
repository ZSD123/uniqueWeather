package adapter;


import activity.MyUser;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.uniqueweather.app.R;

import java.io.File;
import java.text.SimpleDateFormat;

import myCustomView.CircleImageView;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的文本类型
 */
public class ReceiveImageHolder extends BaseViewHolder {


  protected CircleImageView iv_avatar;


  protected TextView tv_time;


  protected ImageView iv_picture;

  protected ProgressBar progress_load;

  public ReceiveImageHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener,View view) {
     super(context, root,onRecyclerViewListener,view);
     iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
     tv_time=(TextView)view.findViewById(R.id.tv_time);
     iv_picture=(ImageView)view.findViewById(R.id.iv_picture);
     progress_load=(ProgressBar)view.findViewById(R.id.progress_load);
  }

  @Override
  public void bindData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
    final BmobIMUserInfo info = msg.getBmobIMUserInfo();

    
    String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+msg.getFromId()+".jpg_";
    File file=new File(path);
    if(file.exists()){
		BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;      
      try {
         Bitmap bmp = BitmapFactory.decodeFile(path, opts);
         iv_avatar.setImageBitmap(bmp);
      } catch (OutOfMemoryError err) {
    	  err.printStackTrace();
     }
    }else {
    	
    	iv_avatar.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.head));
    }
    
    
    
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(msg.getCreateTime());
    tv_time.setText(time);
    //可使用buildFromDB方法转化为指定类型的消息
    final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false,msg);
    //显示图片
  
    ImageLoaderFactory.getLoader().load(iv_picture,message.getRemoteUrl(),  R.drawable.ic_launcher,new ImageLoadingListener(){

    @Override
      public void onLoadingStarted(String s, View view) {
        progress_load.setVisibility(View.VISIBLE);
      }

      @Override
      public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        progress_load.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onLoadingCancelled(String s, View view) {
        progress_load.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onLoadingFailed(String s, View view, FailReason failReason) {
        progress_load.setVisibility(View.INVISIBLE);
      }
    });

    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击" + info.getName() + "的头像");
      }
    });

    iv_picture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击图片:"+message.getRemoteUrl()+"");
        if(onRecyclerViewListener!=null){
          onRecyclerViewListener.onItemClick(getPosition());
        }
      }
    });

    iv_picture.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (onRecyclerViewListener != null) {
          onRecyclerViewListener.onItemLongClick(getPosition());
        }
        return true;
      }
    });

  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}