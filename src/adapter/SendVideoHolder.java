package adapter;


import Util.CommonUtils;
import activity.MyUser;
import activity.videoAct;
import activity.xiangxiDataAct;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface; 
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

import myCustomView.CircleImageView;

import com.sharefriend.app.R;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * 发送的视频类型---这是举个例子，并没有展示出视频缩略图等信息，开发者可自行实现
 */
public class SendVideoHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener {

	  protected CircleImageView iv_avatar;
      protected String path1;
	  protected ImageView iv_fail_resend;

	  protected TextView tv_time;

	  protected ImageView iv_picture;

	  protected TextView tv_send_status;

	  protected ProgressBar progress_load;

  BmobIMConversation c;

  public SendVideoHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener listener,View view) {
    super(context, root, listener,view);
    this.c =c;
    iv_picture=(ImageView)view.findViewById(R.id.iv_picture);
    iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
    iv_fail_resend=(ImageView)view.findViewById(R.id.iv_fail_resend);
    tv_time=(TextView)view.findViewById(R.id.tv_time);
    tv_send_status=(TextView)view.findViewById(R.id.tv_send_status);
    progress_load=(ProgressBar)view.findViewById(R.id.progress_load);
  }

  @Override
  public void bindData(Object o) {
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/"+(String)BmobUser.getObjectByKey("username")+"头像.png";
    File file=new File(path);
    if(file.exists()){
    	setTouXiangImage(file, iv_avatar); 
    }else {
    	setTouXiangWithResource(file, iv_avatar);

    }
    
    String time = dateFormat.format(message.getCreateTime());

    tv_time.setText(time);
     
    final BmobIMVideoMessage msg = BmobIMVideoMessage.buildFromDB(true,message);
		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile(".*(?=&[a-zA-z]+://[^\\s]*)");
		Matcher matcher=pattern.matcher(msg.getContent());
		 if (matcher.find()) {
		path1=matcher.group(0);
	} 
    
    int status =msg.getSendStatus();
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
     
    if(path1!=null){
		Bitmap bitmap=getVideoThumbnail(path1);
		iv_picture.setImageBitmap(bitmap);
    }
    iv_picture.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
	     Intent intent=new Intent(context,videoAct.class);
		 intent.putExtra("path", path1);
		 context.startActivity(intent);
		
         }
	});
    iv_picture.setOnLongClickListener(new OnLongClickListener() {
		
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
