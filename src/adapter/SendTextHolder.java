package adapter;



import activity.MyUser;
import activity.xiangxiDataAct;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myCustomView.CircleImageView;

import com.uniqueweather.app.R;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * ���͵��ı�����
 */
public class SendTextHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener {
	 @Bind(R.id.iv_avatar)
	  protected CircleImageView iv_avatar;

	  @Bind(R.id.iv_fail_resend)
	  protected ImageView iv_fail_resend;

	  @Bind(R.id.tv_time)
	  protected TextView tv_time;

	  @Bind(R.id.tv_message)
	  protected TextView tv_message;
	  @Bind(R.id.tv_send_status)
	  protected TextView tv_send_status;

	  @Bind(R.id.progress_load)
	  protected ProgressBar progress_load;
      BmobIMConversation c;

  public SendTextHolder(Context context, ViewGroup root,BmobIMConversation c,OnRecyclerViewListener listener,View view) {
    super(context, root, listener,view);
    this.c =c;
    iv_avatar=(CircleImageView)view.findViewById(R.id.iv_avatar);
    iv_fail_resend=(ImageView)view.findViewById(R.id.iv_fail_resend);
    tv_time=(TextView)view.findViewById(R.id.tv_time);
    tv_message=(TextView)view.findViewById(R.id.tv_message);
    tv_send_status=(TextView)view.findViewById(R.id.tv_send_status);
    progress_load=(ProgressBar)view.findViewById(R.id.progress_load);
    
  }

  @Override
  public void bindData(Object o) {
	     
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    //���ط����ı����Լ���ͷ��
    
    String path=Environment.getExternalStorageDirectory()+"/EndRain/"+(String)MyUser.getObjectByKey("username")+"/"+"ͷ��.png";
    File file=new File(path);
    if(file.exists()){
    	setTouXiangImage(file, iv_avatar);  
    }else {
  	    setTouXiangWithResource(file, iv_avatar);
    }
    
    
    String time = dateFormat.format(message.getCreateTime());
    String content = message.getContent();
    
    
    tv_message.setText(replace(content));
    tv_time.setText(time);

    int status =message.getSendStatus();
    if (status == BmobIMSendStatus.SENDFAILED.getStatus()) {
      iv_fail_resend.setVisibility(View.VISIBLE);
      progress_load.setVisibility(View.GONE);
    } else if (status== BmobIMSendStatus.SENDING.getStatus()) {
      iv_fail_resend.setVisibility(View.GONE);
      progress_load.setVisibility(View.VISIBLE);
    } else {
      iv_fail_resend.setVisibility(View.GONE);
      progress_load.setVisibility(View.GONE);
    }

   

    tv_message.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
    	  AlertDialog.Builder builder=new AlertDialog.Builder(context);
		     final String[] xuanzeweizhi={"���Ƶ�������","ɾ��"};
		     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					  if(which==0){
						  ClipboardManager cManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
						  cManager.setText(tv_message.getText());
						  Toast.makeText(context,"�Ѿ����Ƶ�������", Toast.LENGTH_SHORT).show();
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

    //�ط�
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
              tv_send_status.setText("�ѷ���");
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
