package adapter;


import activity.MyUser;
import activity.xiangxiDataAct;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import cn.bmob.v3.BmobUser;
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
  protected LinearLayout voice_layout;
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
    voice_layout=(LinearLayout)view.findViewById(R.id.layout_voice);
  }

  @Override
  public void bindData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
   
    String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/"+"头像.png";
    File file=new File(path);
    if(file.exists()){
    	setTouXiangImage(file, iv_avatar); 
   
    }else {
    	setTouXiangWithResource(file, iv_avatar);
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

    voice_layout.setOnClickListener(new NewRecordPlayClickListener(getContext(),message,iv_voice));

    voice_layout.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
    	  AlertDialog.Builder builder=new AlertDialog.Builder(context);
		     final String[] xuanzeweizhi={"删除"};
		     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					 if(which==0){
						  if (onRecyclerViewListener != null) {
					            onRecyclerViewListener.onItemLongClick(getPosition());
					         }
					  }
				}
			});
		     builder.show();
          return true;
      }
    });

    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
    	  Bundle bundle=new Bundle();
     	 MyUser currentUser=new MyUser();
     	 currentUser.setAge(BmobUser.getCurrentUser(MyUser.class).getAge());
     	 currentUser.setConstellation(BmobUser.getCurrentUser(MyUser.class).getConstellation());
     	 currentUser.setGuxiang(BmobUser.getCurrentUser(MyUser.class).getGuxiang());
     	 currentUser.setNick(BmobUser.getCurrentUser(MyUser.class).getNick());
     	 currentUser.setSchool(BmobUser.getCurrentUser(MyUser.class).getSchool());
     	 currentUser.setSex(BmobUser.getCurrentUser(MyUser.class).getSex());
     	 currentUser.setShengri(BmobUser.getCurrentUser(MyUser.class).getShengri());
     	 currentUser.setSuozaidi(BmobUser.getCurrentUser(MyUser.class).getSuozaidi());
     	 currentUser.setTouXiangUrl(BmobUser.getCurrentUser(MyUser.class).getTouXiangUrl());
     	 currentUser.setZhiye(BmobUser.getCurrentUser(MyUser.class).getZhiye());
  	     bundle.putSerializable("myUser", currentUser);
  	     Intent intent=new Intent(context,xiangxiDataAct.class);
  	     intent.putExtra("bundle", bundle);
  	     context.startActivity(intent);
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
