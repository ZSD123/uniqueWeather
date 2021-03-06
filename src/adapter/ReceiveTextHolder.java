package adapter;


import activity.MyUser;
import activity.chooseAreaActivity;
import activity.myAccountAct;
import activity.xiangxiDataAct;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myCustomView.CircleImageView;

import com.amap.api.services.a.bu;
import com.amap.api.services.a.m;
import com.sharefriend.app.R;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * 接收到的文本类型
 */
public class ReceiveTextHolder extends BaseViewHolder {


  protected CircleImageView iv_avatar;

 
  protected TextView tv_time;


  protected TextView tv_message;
  public ReceiveTextHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener,View view) {
    super(context, root,onRecyclerViewListener,view);
    iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
    tv_time=(TextView)view.findViewById(R.id.tv_time);
    tv_message=(TextView)view.findViewById(R.id.tv_message);
  }


  @Override
  public void bindData(Object o) {
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(message.getCreateTime());
    
    
    String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+message.getFromId()+".jpg_";
    File file=new File(path);
    if(file.exists()){
    	setTouXiangImage(file, iv_avatar); 
    }else {
    	setTouXiangWithResource(file, iv_avatar);

    }
    
    
    
    tv_time.setText(time);
    String content =  message.getContent();
    tv_message.setText(replace(content));

    
  
    
    
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
    

    tv_message.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
        
          AlertDialog.Builder builder=new AlertDialog.Builder(context);
		     final String[] xuanzeweizhi={"复制到剪贴板","删除"};
		     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					  if(which==0){
						  ClipboardManager cManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
						  cManager.setText(tv_message.getText());
						  Toast.makeText(context,"已经复制到剪贴板", Toast.LENGTH_SHORT).show();
					  }else if(which==1){
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
  private  Pattern buildPattern() {
		return Pattern.compile("\\\\ue[a-z0-9]{3}", Pattern.CASE_INSENSITIVE);
	}

	private  CharSequence replace(String text) {
		try {
			SpannableString spannableString = new SpannableString(text);
			int start = 0;
			Pattern pattern = buildPattern();
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				String faceText = matcher.group();
				String key = faceText.substring(1);
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
						getContext().getResources().getIdentifier(key, "drawable", getContext().getPackageName()), options);
				ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
				int startIndex = text.indexOf(faceText, start);
				int endIndex = startIndex + faceText.length();
				if (startIndex >= 0)
					spannableString.setSpan(imageSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				start = (endIndex - 1);
			}
			return spannableString;
		} catch (Exception e) {
			return text;
		}
	}
}