package adapter;


import Util.download;
import activity.MyUser;
import activity.baseFragmentActivity;
import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.Pools.Pool;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.uniqueweather.app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import myCustomView.CircleImageView;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * 接收到的文本类型
 */
public class ReceiveImageHolder extends BaseViewHolder {


  protected CircleImageView iv_avatar;


  protected TextView tv_time;


  protected ImageView iv_picture;

  protected ProgressBar progress_load;
  protected ViewGroup root;
  public ReceiveImageHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener,View view) {
     super(context, root,onRecyclerViewListener,view);
     iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
     tv_time=(TextView)view.findViewById(R.id.tv_time);
     iv_picture=(ImageView)view.findViewById(R.id.iv_picture);
     progress_load=(ProgressBar)view.findViewById(R.id.progress_load);
     this.root=root;
  }

  @Override
  public void bindData(Object o) {
	 
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
    final String info = msg.getFromId();
    final MyUser myUser=new MyUser();
    
    BmobQuery<MyUser> query=new BmobQuery<MyUser>();
    query.getObject(info, new QueryListener<MyUser>() {
		
		@Override
		public void done(MyUser user, BmobException e ) {
		      if(e==null){
		    	  myUser.setNick(user.getNick());
		      }else{
		    	  toast(e.getMessage());
		      }
			
		}
	});
  
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
         ImageView imageView=(ImageView) view.findViewById(R.id.image);
         RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.picturerela);
         relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(popupWindow.isShowing())
					popupWindow.dismiss();
				
			}
		});
         ImageLoaderFactory.getLoader().load(imageView, message.getRemoteUrl(), R.drawable.ic_launcher, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
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
				
				 SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		    	  Date curDate=new Date(System.currentTimeMillis());
		    	  String str=format.format(curDate);  
				
				  BmobFile bmobFile=new BmobFile(str+".png",null,message.getRemoteUrl());//确定文件名字（头像.png）和网络地址
				  download.downloadFile1(bmobFile,context);//进行下载操作
				
			}
		});
         popupWindow.showAtLocation(root,Gravity.CENTER,0 , 0);
         
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