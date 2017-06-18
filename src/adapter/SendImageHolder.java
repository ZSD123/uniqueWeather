package adapter;


import activity.MyUser;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;

import myCustomView.CircleImageView;

import com.uniqueweather.app.R;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 发送的文本类型
 */
public class SendImageHolder extends BaseViewHolder {

  protected CircleImageView iv_avatar;

  protected ImageView iv_fail_resend;

  protected TextView tv_time;

  protected ImageView iv_picture;

  protected TextView tv_send_status;

  protected ProgressBar progress_load;
  BmobIMConversation c;

  public SendImageHolder(Context context, ViewGroup root,BmobIMConversation c,OnRecyclerViewListener onRecyclerViewListener,View view) {
    super(context, root,onRecyclerViewListener,view);
    this.c =c;
    iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
    iv_fail_resend=(ImageView)view.findViewById(R.id.iv_fail_resend);
    tv_time=(TextView)view.findViewById(R.id.tv_time);
    iv_picture=(ImageView)view.findViewById(R.id.iv_picture);
    tv_send_status=(TextView)view.findViewById(R.id.tv_send_status);
    progress_load=(ProgressBar)view.findViewById(R.id.progress_load);
  }

  @Override
  public void bindData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
    final BmobIMUserInfo info = msg.getBmobIMUserInfo();

    
    String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/"+"头像.png";
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
    //
    final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(true, msg);
    int status =message.getSendStatus();
    if (status == BmobIMSendStatus.SENDFAILED.getStatus() ||status == BmobIMSendStatus.UPLOADAILED.getStatus()) {
        iv_fail_resend.setVisibility(View.VISIBLE);
        progress_load.setVisibility(View.GONE);
        tv_send_status.setVisibility(View.INVISIBLE);
    } else if (status== BmobIMSendStatus.SENDING.getStatus()) {
        progress_load.setVisibility(View.VISIBLE);
        iv_fail_resend.setVisibility(View.GONE);
        tv_send_status.setVisibility(View.INVISIBLE);
    } else {
        tv_send_status.setVisibility(View.VISIBLE);
        tv_send_status.setText("已发送");
        iv_fail_resend.setVisibility(View.GONE);
        progress_load.setVisibility(View.GONE);
    }

    //发送的不是远程图片地址，则取本地地址
    ImageLoaderFactory.getLoader().load(iv_picture,TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(),R.drawable.ic_launcher,null);
//  ViewUtil.setPicture(TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteU,R.drawable.ic_launcher,null);
//    ViewUtil.setPicture(TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(), R.mipmap.ic_launcher, iv_picture,null);

    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击" + info.getName() + "的头像");
      }
    });
    iv_picture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击图片:"+(TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl())+"");
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

    //重发
    iv_fail_resend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        c.resendMessage(message, new MessageSendListener() {
          @Override
          public void onStart(BmobIMMessage msg) {
            progress_load.setVisibility(View.VISIBLE);
            iv_fail_resend.setVisibility(View.GONE);
            tv_send_status.setVisibility(View.INVISIBLE);
          }

          @Override
          public void done(BmobIMMessage msg, BmobException e) {
            if (e == null) {
              tv_send_status.setVisibility(View.VISIBLE);
              tv_send_status.setText("已发送");
              iv_fail_resend.setVisibility(View.GONE);
              progress_load.setVisibility(View.GONE);
            } else {
              iv_fail_resend.setVisibility(View.VISIBLE);
              progress_load.setVisibility(View.GONE);
              tv_send_status.setVisibility(View.INVISIBLE);
            }
          }
        });
      }
    });
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}
