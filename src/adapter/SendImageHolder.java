package adapter;


import Util.download;
import activity.MyUser;
import activity.baseFragmentActivity;
import activity.xiangxiDataAct;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import myCustomView.CircleImageView;
import myCustomView.TouchImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sharefriend.app.R;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
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
  ViewGroup root;
  public SendImageHolder(Context context, ViewGroup root,BmobIMConversation c,OnRecyclerViewListener onRecyclerViewListener,View view) {
    super(context, root,onRecyclerViewListener,view);
    this.c =c;
    this.root=root;
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

    
     String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/"+(String)BmobUser.getObjectByKey("username")+"头像.png";
    File file=new File(path);
    if(file.exists()){
    	
    	setTouXiangImage(file, iv_avatar); 
    }else {
    	setTouXiangWithResource(file, iv_avatar);
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
    ImageLoaderFactory.getLoader().load(iv_picture,TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(),R.drawable.no404,null);

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
    iv_picture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
    	  final PopupWindow popupWindow=new PopupWindow(context);
          View view=((baseFragmentActivity)context).getLayoutInflater().inflate(R.layout.pictureview, null);
          
          popupWindow.setContentView(view);
          popupWindow.setWidth(LayoutParams.MATCH_PARENT);
          popupWindow.setHeight(LayoutParams.MATCH_PARENT);
          popupWindow.setFocusable(true);
          view.setOnKeyListener(new OnKeyListener() {
 			
 			@Override
 			public boolean onKey(View v, int keyCode, KeyEvent event) {
 				  if (keyCode == KeyEvent.KEYCODE_BACK) { 
 					  if(popupWindow.isShowing()){
 			            popupWindow.dismiss(); 
 			            return true;  
 					  }
 			        }  
 				return false;
 			}
 		});
          TouchImageView imageView=(TouchImageView) view.findViewById(R.id.image);
          RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.picturerela);
          relativeLayout.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				if(popupWindow.isShowing())
 					popupWindow.dismiss();
 				
 			}
 		});
          ImageLoaderFactory.getLoader().load(imageView,TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(), R.drawable.ic_launcher, new ImageLoadingListener() {
 			
 			@Override
 			public void onLoadingStarted(String arg0, View arg1) {
 		     
 				
 			}
 			
 			@Override
 			public void onLoadingFailed(String arg0, View arg1, FailReason reason) {
 				toast("加载失败,"+reason.toString());
 				
 				
 			}
 			
 			@Override
 			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
 				
 				
 			}
 			
 			@Override
 			public void onLoadingCancelled(String arg0, View arg1) {
 				// TODO Auto-generated method stub
 				
 			}
 		});
          imageView.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				if(popupWindow.isShowing())
 					popupWindow.dismiss();
 				
 			}
 		});
          ImageButton button=(ImageButton)view.findViewById(R.id.download);
          button.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 			
 					String path1 = null;
 					java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("[a-zA-z]+://[^\\s]*");
 					Matcher matcher=pattern.matcher(message.getContent());
 					 if (matcher.find()) {
						path1=matcher.group(0);
					}
			    if(path1!=null){
 				 SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		    	  Date curDate=new Date(System.currentTimeMillis());
		    	  String str=format.format(curDate);  
				
				  BmobFile bmobFile=new BmobFile(str+".png",null,path1);//确定文件名字（头像.png）和网络地址
				  download.downloadFile1(bmobFile,context);//进行下载操作
			    }
 			}
 		});
          popupWindow.showAtLocation(root,Gravity.CENTER,0 , 0);
          
      }
    });

    iv_picture.setOnLongClickListener(new View.OnLongClickListener() {
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
