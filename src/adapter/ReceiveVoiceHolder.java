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
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * 接收到的文本类型
 */
public class ReceiveVoiceHolder extends BaseViewHolder {


  protected CircleImageView iv_avatar;


  protected TextView tv_time;

  protected TextView tv_voice_length;

  protected ImageView iv_voice;

  protected LinearLayout voice_layout;
  protected ProgressBar progress_load;

  private String currentUid="";

  public ReceiveVoiceHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener,View view) {
    super(context, root,onRecyclerViewListener,view);
    try {
      currentUid = BmobUser.getCurrentUser().getObjectId();
    } catch (Exception e) {
      e.printStackTrace();
    }
    iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
    tv_time=(TextView)view.findViewById(R.id.tv_time);
    tv_voice_length=(TextView)view.findViewById(R.id.tv_voice_length);
    iv_voice=(ImageView)view.findViewById(R.id.iv_voice);
    voice_layout=(LinearLayout)view.findViewById(R.id.layout_voice);
    progress_load=(ProgressBar)view.findViewById(R.id.progress_load);
  }

  @Override
  public void bindData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'

 
      
    String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+msg.getFromId()+".jpg_";
    File file=new File(path);
    if(file.exists()){
    	setTouXiangImage(file, iv_avatar); 
    }else {
    	setTouXiangWithResource(file, iv_avatar);

    }
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(msg.getCreateTime());
    tv_time.setText(time);
    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
    	     Bundle bundle=new Bundle();
    	     bundle.putSerializable("myUser", myUser);
    	     Intent intent=new Intent(context,xiangxiDataAct.class);
    	     intent.putExtra("bundle", bundle);
    	     context.startActivity(intent);
    	   
      }
    });
    //显示特有属性
    final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(false, msg);
    boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
    if(!isExists){//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
        BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(),msg,new FileDownloadListener() {

          @Override
          public void onStart() {
              progress_load.setVisibility(View.VISIBLE);
              tv_voice_length.setVisibility(View.GONE);
              iv_voice.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
          }

          @Override
          public void done(BmobException e) {
            if(e==null){
                progress_load.setVisibility(View.GONE);
                tv_voice_length.setVisibility(View.VISIBLE);
                tv_voice_length.setText(message.getDuration()+"\''");
                iv_voice.setVisibility(View.VISIBLE);
            }else{
                progress_load.setVisibility(View.GONE);
                tv_voice_length.setVisibility(View.GONE);
                iv_voice.setVisibility(View.INVISIBLE);
            }
          }
        });
        downloadTask.execute(message.getContent());
    }else{
        tv_voice_length.setVisibility(View.VISIBLE);
        tv_voice_length.setText(message.getDuration() + "\''");
    }
    voice_layout.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, iv_voice));

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

  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}