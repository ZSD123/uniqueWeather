package adapter;


import Util.CommonUtils;
import Util.download;
import activity.MyUser;
import activity.baseFragmentActivity;
import activity.loginAct;
import activity.videoAct;
import activity.xiangxiDataAct;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import myCustomView.CircleImageView;

import com.amap.api.mapcore2d.p;
import com.amap.api.services.a.bi;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sharefriend.app.R;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * 接收到的视频类型--这是举个例子，并没有展示出视频缩略图等信息，开发者可自行设置
 */
public class ReceiveVideoHolder extends BaseViewHolder {

	  protected CircleImageView iv_avatar;


	  protected TextView tv_time;


	  protected ImageView iv_picture;

	  protected ProgressBar progress_load;
	  protected ViewGroup root;
  public ReceiveVideoHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener,View view) {
    super(context, root,onRecyclerViewListener,view);
    iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
    tv_time=(TextView)view.findViewById(R.id.tv_time);
    iv_picture=(ImageView)view.findViewById(R.id.iv_picture);
    progress_load=(ProgressBar)view.findViewById(R.id.progress_load);
    this.root=root;
  }

  @Override
  public void bindData(Object o) {
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(message.getCreateTime());
    tv_time.setText(time);
    

    
    
    String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+message.getFromId()+".jpg_";
    File file=new File(path);
    if(file.exists()){
    	setTouXiangImage(file, iv_avatar); 
    }else {
    	setTouXiangWithResource(file, iv_avatar);
      
    }
    
    final BmobIMVideoMessage msg = BmobIMVideoMessage.buildFromDB(false,message);
    //显示图片
    
     
    Bitmap bitmap=createVideoThumbnail(msg.getRemoteUrl(),150, 150);

    
  if(bitmap!=null){
    	iv_picture.setImageBitmap(bitmap);
    	progress_load.setVisibility(View.GONE);
    	
    }else {
    	iv_picture.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.no404));
    	progress_load.setVisibility(View.GONE);
	}
    iv_picture.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 if(CommonUtils.isWifi(context)){
			
				 Intent intent=new Intent(context,videoAct.class);
				 intent.putExtra("path", msg.getRemoteUrl());
				 context.startActivity(intent);
			}else {

				AlertDialog.Builder builder=new AlertDialog.Builder(context);
				final AlertDialog alertDialog=builder.create();
				builder.setTitle("非wifi环境提醒");
				builder.setMessage("当前非wifi环境，确定播放吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						 Intent intent=new Intent(context,videoAct.class);
						 intent.putExtra("path", msg.getRemoteUrl());
						 context.startActivity(intent);
						
						  
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(alertDialog.isShowing()){
							alertDialog.dismiss();
						}
					}
				});
				
				builder.show();
				
			  
			  }
		
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

   

   
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
   

}