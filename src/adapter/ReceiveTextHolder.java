package adapter;


import activity.MyUser;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myCustomView.CircleImageView;

import com.uniqueweather.app.R;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * ���յ����ı�����
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
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy��MM��dd�� HH:mm");
    String time = dateFormat.format(message.getCreateTime());
    
    
    String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/head/"+message.getFromId()+".jpg_";
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
    
    
    
    tv_time.setText(time);
    String content =  message.getContent();
    tv_message.setText(replace(content));
    
    final String info = message.getFromId();
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
    
    
    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
    	  try {
    		  toast("���" +myUser.getNick() + "��ͷ��");
		} catch (Exception e) {
			e.printStackTrace();
		}
        
      }
    });
    tv_message.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          toast("���"+message.getContent());
          if(onRecyclerViewListener!=null){
            onRecyclerViewListener.onItemClick(getPosition());
          }
        }
    });

    tv_message.setOnLongClickListener(new View.OnLongClickListener() {
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