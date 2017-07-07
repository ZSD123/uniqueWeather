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

import java.io.File;
import java.text.SimpleDateFormat;

import myCustomView.CircleImageView;

import com.uniqueweather.app.R;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 发送的语音类型
 */
public class SendVoiceHolder extends BaseViewHolder {

  @Bind(R.id.iv_avatar)
  protected CircleImageView iv_avatar;

  @Bind(R.id.iv_fail_resend)
  protected ImageView iv_fail_resend;

  @Bind(R.id.tv_time)
  protected TextView tv_time;

  protected TextView tv_voice_length;
  protected ImageView iv_voice;

  @Bind(R.id.tv_send_status)
  protected TextView tv_send_status;

  @Bind(R.id.progress_load)
  protected ProgressBar progress_load;

  BmobIMConversation c;
  public SendVoiceHolder(Context context, ViewGroup root,BmobIMConversation c,OnRecyclerViewListener onRecyclerViewListener,View view) {
    super(context, root,onRecyclerViewListener,view);
    this.c =c;
    iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
    iv_fail_resend=(ImageView)view.findViewById(R.id.iv_fail_resend);
    tv_time=(TextView)view.findViewById(R.id.tv_time);
    tv_voice_length=(TextView)view.findViewById(R.id.tv_voice_length);
    iv_voice=(ImageView)view.findViewById(R.id.iv_voice);
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
    	iv_avatar.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.userpicture));
    }
    
    
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(msg.getCreateTime());
    tv_time.setText(time);
    //使用buildFromDB方法转化成指定类型的消息
    final BmobIMAudioMessage  message = BmobIMAudioMessage.buildFromDB(true,msg);
    tv_voice_length.setText(message.getDuration()+"\''");

    int status =message.getSendStatus();
    if (status == BmobIMSendStatus.SENDFAILED.getStatus()||status == BmobIMSendStatus.UPLOADAILED.getStatus()) {//发送失败/上传失败
        iv_fail_resend.setVisibility(View.VISIBLE);
        progress_load.setVisibility(View.GONE);
        tv_send_status.setVisibility(View.INVISIBLE);
        tv_voice_length.setVisibility(View.INVISIBLE);
    } else if (status== BmobIMSendStatus.SENDING.getStatus()) {
        progress_load.setVisibility(View.VISIBLE);
        iv_fail_resend.setVisibility(View.GONE);
        tv_send_status.setVisibility(View.INVISIBLE);
        tv_voice_length.setVisibility(View.INVISIBLE);
    } else {//发送成功
        iv_fail_resend.setVisibility(View.GONE);
        progress_load.setVisibility(View.GONE);
        tv_send_status.setVisibility(View.GONE);
        tv_voice_length.setVisibility(View.VISIBLE);
    }

    iv_voice.setOnClickListener(new NewRecordPlayClickListener(getContext(),message,iv_voice));

    iv_voice.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
          if (onRecyclerViewListener != null) {
              onRecyclerViewListener.onItemLongClick(getOldPosition());
          }
          return true;
      }
    });

    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击" + info.getName() + "的头像");
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
            if(e==null){
                tv_send_status.setVisibility(View.VISIBLE);
                tv_send_status.setText("已发送");
                iv_fail_resend.setVisibility(View.GONE);
                progress_load.setVisibility(View.GONE);
            }else{
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
